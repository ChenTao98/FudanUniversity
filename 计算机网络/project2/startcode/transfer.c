#include <stdio.h>
#include <malloc.h>
#include "chunk.h"
#include "udpPacket.h"
#include "transfer.h"

extern bt_config_t config;
extern queue *has_chunks;

extern void handle_time_out_thread(void *arg);

//读取chunkfile,将内容转化为二进制哈希值的队列
//此处chunkfile为getchunkfile和haschunkfile,
queue *chunk_file_to_queue(const char *chunk_file) {
    //获取文件句柄
    FILE *fp = fopen(chunk_file, "r");
    //初始化队列
    queue *chunks = make_queue();
    char read_buf[64];
    char sha_buf[2 * SHA1_HASH_SIZE];
    uint8_t *chunk;
    int i;
    //读取文件内容,并转换
    //fgets每次读取一行
    while (fgets(read_buf, 64, fp) != NULL) {
        sscanf(read_buf, "%d %s", &i, sha_buf);
        chunk = malloc(SHA1_HASH_SIZE);
        hex2binary(sha_buf, 2 * SHA1_HASH_SIZE, chunk);
        enqueue(chunks, chunk);
    }
    fclose(fp);
    return chunks;
}

//将haschunkfile内容转为队列
void init_has_chunks(const char *has_chunk_file) {
    has_chunks = chunk_file_to_queue(has_chunk_file);
}

//根据传入的whohas队列,寻找本地拥有其中的某些项,并返回
//用于处理收到whohas包时找到本地拥有的chunk
queue *which_i_have(queue *wanted) {
    //如果haschunk未初始化,先初始化
    if (has_chunks == NULL) {
        init_has_chunks(config.has_chunk_file);
    }
    if (has_chunks->n == 0) {
        return NULL;
    }
    //初始化返回队列
    queue *result = make_queue();
    uint8_t *chunk;
    //遍历对等方需要的chunk,为每个chunk寻找本地对应的chunk
    while ((chunk = dequeue(wanted)) != NULL) {
        node *current = has_chunks->head;
        uint8_t *hash_value;
        int isHave = 0;
        //遍历本地拥有的chunk,以寻找
        while (current != NULL) {
            hash_value = current->data;
            //两者的哈希值相同,说明本地拥有块,加入返回队列
            if (memcmp(chunk, hash_value, SHA1_HASH_SIZE) == 0) {
                enqueue(result, chunk);
                isHave = 1;
                break;
            }
            current = current->next;
        }
        if (!isHave) {
            free(chunk);
        }
    }
    if (!result->n) {
        free_queue(result, 0);
        result = NULL;
    }
    return result;
}

//根据传入的哈希值队列,生成对应的包队列
//生成的包类型为whohas和ihave类型,根据传入的参数决定哪种类型
queue *init_chunk_packet_queue(queue *chunks, data_packet_t *(*make_chunk_packet)(short, void *)) {
    //初始化返回队列
    queue *pkts = make_queue();
    uint8_t *chunk;
    int i;
    //遍历取出队列里面的哈希值,生成对应类型的包
    while (chunks->n) {
        //由于udp数据包大小有限,导致一次udp包包含的哈希值最多为74个
        //如果哈希值超过74个,就分成多个包传送
        size_t pkt_num = (size_t) (chunks->n < MAX_CHANK_NUM ? chunks->n : MAX_CHANK_NUM);
        //该数据包大小,加4是由于whohas和ihave包前有4字节空间
        size_t data_len = pkt_num * SHA1_HASH_SIZE + 4;
        char *data_t = malloc(data_len);
        //在数据前面写入本包包含的哈希值个数
        *data_t = (char) pkt_num;
        i = 0;
        //循环往这个包里面写入pkt_nun个哈希值
        while (i < pkt_num) {
            chunk = dequeue(chunks);
            memcpy(data_t + 4 + i * SHA1_HASH_SIZE, chunk, SHA1_HASH_SIZE);
            free(chunk);
            i++;
        }
        //生成whohas或ihave包
        data_packet_t *pkt = make_chunk_packet((short) (data_len + HEADERLEN), data_t);
        enqueue(pkts, pkt);
    }
    return pkts;
}

//根据传入的get_chunk_file生成需要发送的whohas包队列
queue *init_whohas_packet_queue(const char *get_chunk_file) {
    //根据文件生成哈希值队列
    queue *whohas_chunks = chunk_file_to_queue(get_chunk_file);
    //根据哈希值队列生成whohas包队列
    queue *ret = init_chunk_packet_queue(whohas_chunks, make_whohas_packet);
    free_queue(whohas_chunks, 0);
    return ret;
}

//根据传入的哈希值队列生成ihave包队列
queue *init_ihave_packet_queue(queue *chunks) {
    return init_chunk_packet_queue(chunks, make_ihave_packet);
}

//根据传入的数据生成哈希值队列
//用于处理whohas包和ihave包的数据
queue *data2chunks_queue(void *data) {
    queue *chunks = make_queue();
    //获取该包中哈希值的数目
    int num = *(char *) data;
    char *ptr = (char *) data + 4;
    //循环将哈希值存入队列
    for (int i = 0; i < num; i++) {
        char *chunk = malloc(SHA1_HASH_SIZE);
        memcpy(chunk, ptr + i * SHA1_HASH_SIZE, SHA1_HASH_SIZE);
        enqueue(chunks, chunk);
    }
    return chunks;
}

//根据传入的哈希值寻找对应的chunk,并根据chunk生成data包
//由于每个chunk有512k,数据包只能有1500字节,所以每次读取1024字节生成一个data包,总共生成512包
data_packet_t **init_data_packet_array(uint8_t *sha) {
    //获取chunkfile句柄
    FILE *fp = fopen(config.chunk_file, "r");
    if (fp == NULL) {
        fprintf(stderr, "Error!: file %s doesn't exist!", config.chunk_file);
        return NULL;
    }
    //用于存储master_chunk_file的第一行
    char buf[BT_FILENAME_LEN + 10] = {0};
    //用于存储获取chunk的文件路径
    char file_name[BT_FILENAME_LEN];
    char sha_buf[2 * SHA1_HASH_SIZE];
    //读取masterfile的第一行,并从第一行获取路径保存,
    //第一行格式为FIle: path
    fgets(buf, BT_FILENAME_LEN, fp);
    sscanf(buf, "File: %s\n", file_name);
    //跳过Chunk:这一行
    fgets(buf, BT_FILENAME_LEN, fp);
    int i = -1;
    uint8_t sha_binary_buf[SHA1_HASH_SIZE];
    char buf2[64];
    //读取master_chunk_file,获取与传入哈希值相同的想的id,该id用于定位文件位置
    while (fgets(buf2, 64, fp) != NULL) {
        sscanf(buf2, "%d %s", &i, sha_buf);
        hex2binary(sha_buf, 2 * SHA1_HASH_SIZE, sha_binary_buf);
        if (memcmp(sha, sha_binary_buf, SHA1_HASH_SIZE) == 0) {
            break;
        }
    }
    fclose(fp);
    //判断是否找到对应的chunk的id
    if (i == -1) {
        fprintf(stderr, "Error!: No such chunk: %s", sha_buf);
        return NULL;
    }
    //打开存储chunk数据文件
    fp = fopen(file_name, "r");
    //定位该chunk的起始位置
    fseek(fp, i * BT_CHUNK_SIZE, SEEK_SET);
    //存储发送的数据buf
    char data[SEND_PACKET_DATA_LEN];
    data_packet_t *pkt;
    data_packet_t **data_pkts = malloc(BT_CHUNK_KSIZE * sizeof(data_packet_t *));
    //读取512次数据,将该chunk的所以数据读取并创建对应的数据包
    for (uint j = 0; j < BT_CHUNK_KSIZE; j++) {
        fread(data, SEND_PACKET_DATA_LEN, 1, fp);
        //创建数据包,seqnum为自增的
        pkt = make_data_packet(HEADERLEN + SEND_PACKET_DATA_LEN, 0, j + 1, data);
        data_pkts[j] = pkt;
    }
    fclose(fp);
    return data_pkts;
}

//向所有对等方发送队列中whohas包,用于处理get命令时使用
void send_whohas(int sock, queue *whohas_pkts) {
    data_packet_t *pkt;
    //每次取出队列的一个包
    while ((pkt = dequeue(whohas_pkts)) != NULL) {
        bt_peer_t *current = config.peers;
        //遍历所以对等方,向他们发送whohas包
        while (current != NULL) {
            if (current->id != config.identity) {
                send_packet(sock, pkt, (struct sockaddr *) &current->addr);
            }
            current = current->next;
        }
        free_packet(pkt);
    }
}

//向特定的对等方发送ihava包,将队列的包全部发送
void send_pkts(int sock, struct sockaddr *dst, queue *ihave_pkts) {
    data_packet_t *pkt;
    //遍历队列,发送所以包
    while ((pkt = dequeue(ihave_pkts)) != NULL) {
        fflush(stdout);
        send_packet(sock, pkt, dst);
        free_packet(pkt);
    }
}

//向特定对等方发送数据包
void send_data_packets(up_conn_t *conn, int sock, struct sockaddr *dst) {
    //判断发送包是否在窗口内,且发送数目小于520个
    while (conn->last_sent < conn->available && conn->last_sent < BT_CHUNK_KSIZE) {
        send_packet(sock, conn->pkts[conn->last_sent], dst);
        conn->last_sent++;
    }
}

//发送包的接口,发送所以包统一调用该接口,该接口调用spiffy_send发送数据
void send_packet(int sock, data_packet_t *pkt, struct sockaddr *to) {
    size_t len = (size_t) pkt->header.packet_len;
    host2net(pkt);
    spiffy_sendto(sock, pkt, len, 0, to, sizeof(*to));
    net2host(pkt);
}