/*
 * peer.c
 * 
 * Author: Zhigang Wu <16212010025@fudan.edu.cn>,
 *
 * Modified from CMU 15-441,
 * Original Authors: Ed Bardsley <ebardsle+441@andrew.cmu.edu>,
 *                   Dave Andersen
 * 
 * Class: Computer Network (Autumn 2017)
 *
 */

#include <sys/types.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include "debug.h"
#include "spiffy.h"
#include "bt_parse.h"
#include "input_buffer.h"
#include "udpPacket.h"
#include "transfer.h"
#include "chunk.h"
#include "timer.h"

typedef struct time_out_arg {
    int seq_num;
    int id;
} out_args;
static struct timeval timer_peer_strart;//进程启动时间变量，记录进程启动的时间
pthread_mutex_t mutex;//进程锁变量
bt_config_t config; // 存储本地用户的一些信息
int sock; // 当前进程绑定的sock
task_t *task;//当前进程的下载任务
queue *has_chunks;//has_chunk_file里数据初始化存储队列
up_pool_t up_pool;//上传任务池
down_pool_t down_pool;//下载任务池
//处理接收到的各个包的类型对应的函数声明
//传入参数为接收到的数据包以及数据发送方
void handle_whohas(data_packet_t *pkt, bt_peer_t *peer);

void handle_ihave(data_packet_t *pkt, bt_peer_t *peer);

void handle_get(data_packet_t *pkt, bt_peer_t *peer);

void handle_data(data_packet_t *pkt, bt_peer_t *peer);

void handle_ack(data_packet_t *pkt, bt_peer_t *peer);

//根据地址获取对应的对等方
bt_peer_t *get_peer(struct sockaddr_in addr);

//用于记录窗口改变大小的函数，根据传入的参数记录
void log_window(int conn_id, int window, int time, int ssthresh, int isslowstart);

//处理time_out的线程函数，创建后sleep 1秒，期间可被停止，1秒后超时，启动处理过程
void handle_time_out_thread(void *arg);

//用于处理超时线程的死锁
void handle_dead_lock(void *d);

void peer_run(bt_config_t *config);

//开始计时函数，创建计时线程，启动time out计时
void begin_time_out(up_conn_t *up_conn);

int main(int argc, char **argv) {

    bt_init(&config, argc, argv);

    DPRINTF(DEBUG_INIT, "peer.c main beginning\n");

#ifdef TESTING
    config.identity = 1; // your student number here
  strcpy(config.chunk_file, "chunkfile");
  strcpy(config.has_chunk_file, "haschunks");
#endif

    bt_parse_command_line(&config);
    //初始化任务池
    init_up_pool(&up_pool, config.max_conn);
    init_down_pool(&down_pool, config.max_conn);

#ifdef DEBUG
    if (debug & DEBUG_INIT) {
    bt_dump_config(&config);
  }
#endif
    //设置开始时间，用于改变cwnd的时间计算时间差
    timer_start(&timer_peer_strart);
    FILE *fp = fopen("problem2-peer.txt", "w");
    fprintf(fp, "up_link:n\ttime\tcwnd\tsst\tisSlowStart\n\n");
    fclose(fp);
    peer_run(&config);
    //任务结束,释放内存
    free(up_pool.conns);
    free(down_pool.conns);
    return 0;
}

//接收udp包的处理函数
void process_inbound_udp(int sock) {
    struct sockaddr_in from;//存储udp发送方地址
    socklen_t fromlen;
    char buf[BUFLEN];//接收udp的数据缓冲
    data_packet_t *pkt;//存储接收到的数据包
    fromlen = sizeof(from);
    if (spiffy_recvfrom(sock, buf, BUFLEN, 0, (struct sockaddr *) &from, &fromlen) != -1) {
        pkt = (data_packet_t *) buf;//将数据转为数据包类型
        net2host(pkt); //网络端数据转化为本地端数据
        int packet_type = packet_is_legal(pkt);//获取数据包类型
        bt_peer_t *peer = get_peer(from);
        //判断数据包类型,并调用对应的处理函数
        switch (packet_type) {
            case PKT_WHOHAS:
                handle_whohas(pkt, peer);
                break;
            case PKT_IHAVE:
                handle_ihave(pkt, peer);
                break;
            case PKT_GET:
                handle_get(pkt, peer);
                break;
            case PKT_DATA:
                handle_data(pkt, peer);
                break;
            case PKT_ACK:
                handle_ack(pkt, peer);
                break;
            default:
                break;
        }
    }
}

//get命令处理函数,处理用户输入的get命令
void process_get(char *chunkfile, char *outputfile) {
    printf("PROCESS GET SKELETON CODE CALLED.  Fill me in!  (%s, %s)\n", chunkfile, outputfile);
    //将outputfile文件名存储
    stpcpy(config.output_file, outputfile);
    //将get_chunk_file转whohas的包的队列
    //该函数调用里先将chunkfile的十六进制哈希值转化为二进制值队列,再将二进制值队列转化为包队列
    queue *who_has_queue = init_whohas_packet_queue(chunkfile);
    //初始化下载任务
    task = init_task(outputfile, chunkfile, config.max_conn);
    //向所有对等方发送whohas请求
    send_whohas(sock, who_has_queue);
    free_queue(who_has_queue, 0);
    timer_start(&(task->timer));
}

//处理用户输入函数
void handle_user_input(char *line, void *cbdata) {
    char chunkf[128], outf[128];

    bzero(chunkf, sizeof(chunkf));
    bzero(outf, sizeof(outf));

    if (sscanf(line, "GET %120s %120s", chunkf, outf)) { // 接受用户手动输入参数
        if (strlen(outf) > 0) {
            process_get(chunkf, outf); // 处理get请求
        }
    }
}

//peer执行函数
void peer_run(bt_config_t *config) {
    pthread_mutex_init(&mutex, NULL);
    struct sockaddr_in myaddr;
    fd_set readfds;//描述符集合,用于后面的阻塞等待
    struct user_iobuf *userbuf;

    if ((userbuf = create_userbuf()) == NULL) {
        perror("peer_run could not allocate userbuf");
        exit(-1);
    }

    if ((sock = socket(AF_INET, SOCK_DGRAM, IPPROTO_IP)) == -1) {
        perror("peer_run could not create socket");
        exit(-1);
    }

    bzero(&myaddr, sizeof(myaddr));
    myaddr.sin_family = AF_INET;
    myaddr.sin_addr.s_addr = htonl(INADDR_ANY);
    myaddr.sin_port = htons(config->myport);

    if (bind(sock, (struct sockaddr *) &myaddr, sizeof(myaddr)) == -1) {
        perror("peer_run could not bind socket");
        exit(-1);
    }

    spiffy_init(config->identity, (struct sockaddr *) &myaddr, sizeof(myaddr));

    while (1) {
        int nfds;
        struct timeval timer;
        timer.tv_sec = 2;//每次阻塞等待的时间
        timer.tv_usec = 0;
        FD_ZERO(&readfds);//初始化描述符集合
        FD_SET(STDIN_FILENO, &readfds);//将标准输入加入集合
        FD_SET(sock, &readfds);//将绑定的sock输入集合

        nfds = select(sock + 1, &readfds, NULL, NULL, &timer);
        //判断是否有输入,如果有就处理输入
        if (nfds > 0) {
            if (FD_ISSET(sock, &readfds)) {
                process_inbound_udp(sock);
            }

            if (FD_ISSET(STDIN_FILENO, &readfds)) {
                process_user_input(STDIN_FILENO, userbuf, handle_user_input, "Currently unused");
            }
        }
        if (task != NULL) { // 说明task 但task完成时不进入该分支
            int f = remove_stalled_chunks(&down_pool);
            if (f) {
                continue_task(task, &down_pool, sock);
            }
        }
        remove_unack_peers(&up_pool, sock);
    }
}

//处理whohas数据包
void handle_whohas(data_packet_t *pkt, bt_peer_t *peer) {
    //将接收到的数据转化为队列,队列的每个元素都对应一个chunk的二进制值
    //该队列是对方需要的包的值
    queue *chunks = data2chunks_queue(pkt->data);
    //根据whohas的队列查找has_chunk,找到对应的所拥有的值
    queue *ihave = which_i_have(chunks);
    if (ihave != NULL) {
        //拥有对方所需要的chunk,初始化i_have类型数据包
        queue *pkts = init_ihave_packet_queue(ihave);
        //向该对等方发送i_hava数据包
        send_pkts(sock, (struct sockaddr *) &peer->addr, pkts);
        free_queue(pkts, 0);
        free_queue(ihave, 0);
    }
    free_queue(chunks, 1);
}

//处理i_have数据包
void handle_ihave(data_packet_t *pkt, bt_peer_t *peer) {
    if (get_down_conn(&down_pool, peer) != NULL) {
        //如果不为null,则表示已经在下载该对等方的数据,返回
        return;
    } else {
        //将接收到的数据转化为队列,队列的每个元素都对应一个chunk的二进制值
        //该队列是对方拥有的包的值
        queue *chunks = data2chunks_queue(pkt->data);
        //将该对方方加入task中对应的chunk中,表示该chunk可以从该对等方处下载
        for (node *n = chunks->head; n != NULL; n = n->next) {
            uint8_t *sha1 = n->data;
            available_peer(task, sha1, peer);
        }
        //如果下载池已满,输出错误
        if (down_pool.conn == down_pool.max_conn) {
            fprintf(stderr, "Full pool!");
        } else {
            //下载池未满,从任务中选择一个与该对等方对应的chunk进行下载
            chunk_t *chunk = choose_chunk(task, chunks, peer);
            //将与该对等方的链接加入下载池
            down_conn_t *down_conn = add_to_down_pool(&down_pool, peer, chunk);
            timer_start(&down_conn->timer);
            chunk->inuse = 1;
            //向该对等发送get包,表名需要下载一个chunk
            data_packet_t *get = make_get_packet(HEADERLEN + SHA1_HASH_SIZE, (char *) chunk->sha1);
            send_packet(sock, get, (struct sockaddr *) &peer->addr);
            free_packet(get);
        }
        free_queue(chunks, 1);
    }
}

//处理get数据包
void handle_get(data_packet_t *pkt, bt_peer_t *peer) {
    up_conn_t *up_conn = get_up_conn(&up_pool, peer);
    if (up_conn != NULL && (up_conn->last_sent == 512 || up_conn->expect_ack >= 513)) {
        //如果上传池中与该对等方有连接,且上传已经完成,将该对等方清除
        remove_from_up_pool(&up_pool, peer);
    }
    up_conn = get_up_conn(&up_pool, peer);
    if (up_conn != NULL) {
        remove_from_up_pool(&up_pool, peer);
        up_conn = NULL;
    }
//    if (up_conn == NULL) {
    if (up_pool.conn >= up_pool.max_conn) { // 已满 拒绝请求
        data_packet_t *denied_pkt = make_denied_packet();
        send_packet(sock, denied_pkt, (struct sockaddr *) &peer->addr);
        free_packet(denied_pkt);
    } else {
        //上传池有空间,根据对方需要下载的chunk初始化数据包队列
        data_packet_t **pkts = init_data_packet_array((uint8_t *) pkt->data);
        up_conn = add_to_up_pool(&up_pool, peer, pkts);
        // 启动定时器
        if (up_conn != NULL) {
            timer_start(&up_conn->timer);
            //发送数据包,实现了窗口控制
            send_data_packets(up_conn, sock, (struct sockaddr *) &peer->addr);
            //启动time out计时
            begin_time_out(up_conn);
        }
    }
//    } else {
//        fprintf(stderr, "already in pool!\n");
//    }
}


//处理data数据包
void handle_data(data_packet_t *pkt, bt_peer_t *peer) {
    //获取下载池中的下载连接
    down_conn_t *down_conn = get_down_conn(&down_pool, peer);
    if (down_conn == NULL) return;//如果下载连接不存在,说明不是我所需要的包,忽视
    timer_start(&down_conn->timer);
    //下载连接存在,是我所需要的包
    uint seq = pkt->header.seq_num;
    int data_len = pkt->header.packet_len - HEADERLEN;
    data_packet_t *ack_packet = NULL;
    if (seq == down_conn->expect_seq) {
        //序列号与我需要的序列号相同,表明是正确的包
        //将数据保存
        memcpy(down_conn->chunk->data + down_conn->pos, pkt->data, (size_t) data_len);
        //更改位置,将需要的ack加一
        down_conn->pos += data_len;
        down_conn->expect_seq += 1;
        ack_packet = make_ack_packet(seq, 0);
    } else {
        ack_packet = make_ack_packet(down_conn->expect_seq - 1, 0);
    }
    //发送ack包
    send_packet(sock, ack_packet, (struct sockaddr *) &peer->addr);
    timer_start(&down_conn->timer);
    free_packet(ack_packet);
    //判断下载是否完成
    if (down_conn->pos == BT_CHUNK_SIZE) {
        //下载字节数达到预期
        down_conn->chunk->flag = 1;//完成标准置1
        task->chunk_num++;//完成下载任务数加一
        remove_from_down_pool(&down_pool, peer);
        //如果下载任务完成,结束下载任务,否则继续下载
        if (task->chunk_num == task->need_num && check_task(task)) {
            task = finish_task(task);
        } else {
            continue_task(task, &down_pool, sock);
        }
    }
}

//处理ack数据包
void handle_ack(data_packet_t *pkt, bt_peer_t *peer) {
    //判断是否有给该对等方上传,没有则忽视该ack
    up_conn_t *up_conn = get_up_conn(&up_pool, peer);
    if (up_conn == NULL) return;
    timer_start(&up_conn->timer);
    pthread_mutex_lock(&mutex);
    //ack是确定发给我的
    if (pkt->header.ack_num >= BT_CHUNK_KSIZE) {
        //ack达到最大值,结束上传任务
        if (!pthread_cancel(up_conn->pid)) {
            pthread_join(up_conn->pid, NULL);
        }
        up_conn->expect_ack = 513;
        remove_from_up_pool(&up_pool, peer);
    } else if (pkt->header.ack_num >= up_conn->expect_ack) {
        //累计确认,ack大于等于我所需要的ack,改变可用窗口,继续发送数据
        if (!pthread_cancel(up_conn->pid)) {
            pthread_join(up_conn->pid, NULL);
        }
        if (up_conn->is_slow_start) {
            //是slowStart状态，改变cwnd
            up_conn->cwnd++;
            //由于slowStart状态，所以拥塞避免期间的收到ack数量改为0
            up_conn->receive_ack_during_avoid = 0;
            //记录窗口状态
            log_window(up_conn->up_id, up_conn->cwnd, timer_diff_now(&timer_peer_strart), up_conn->ssthresh,
                       up_conn->is_slow_start);
            //判断是否达到阈值，如果达到，就进入拥塞避免状态
            if (up_conn->cwnd >= up_conn->ssthresh) {
                up_conn->is_slow_start = 0;
            }
        } else {
            //拥塞避免状态，记录拥塞避免过程收到的ack的数量（差）
            up_conn->receive_ack_during_avoid += (pkt->header.ack_num - up_conn->expect_ack + 1);
            //判断收到ack是否大于当前窗口大小，如果是，说明经过了一个RTT，窗口加1
            if (up_conn->receive_ack_during_avoid >= up_conn->cwnd) {
                up_conn->cwnd++;
                up_conn->receive_ack_during_avoid = 0;
                log_window(up_conn->up_id, up_conn->cwnd, timer_diff_now(&timer_peer_strart), up_conn->ssthresh,
                           up_conn->is_slow_start);
            }
        }
        //更新对应数据
        up_conn->expect_ack = pkt->header.ack_num + 1;
        up_conn->duplicate = 0;
        up_conn->available = up_conn->expect_ack + up_conn->cwnd - 1;
        send_data_packets(up_conn, sock, (struct sockaddr *) &peer->addr);
        //time out计时重启
        begin_time_out(up_conn);
    } else if (pkt->header.ack_num == up_conn->expect_ack - 1) {  // 重复ack
        up_conn->duplicate++;
        if (up_conn->duplicate >= 3) {
            //重复ack超过3次
            if (!pthread_cancel(up_conn->pid)) {
                pthread_join(up_conn->pid, NULL);
            }
            up_conn->last_sent = pkt->header.ack_num; // send回退
            up_conn->duplicate = 0; // 更新duplicate
            //改变阈值、窗口
            up_conn->ssthresh = (up_conn->cwnd / 2 > 2) ? (up_conn->cwnd / 2) : 2;
            up_conn->cwnd = 1;
            up_conn->is_slow_start = 1;
            up_conn->available = up_conn->expect_ack + up_conn->cwnd - 1;
            //记录窗口大小变化
            log_window(up_conn->up_id, up_conn->cwnd, timer_diff_now(&timer_peer_strart), up_conn->ssthresh,
                       up_conn->is_slow_start);
            send_data_packets(up_conn, sock, (struct sockaddr *) &peer->addr);
            //time out计时重启
            begin_time_out(up_conn);
        }
    }
    pthread_mutex_unlock(&mutex);
}

//time out计时重启函数
void begin_time_out(up_conn_t *up_conn) {
    //终止上一个计时线程
    if (!pthread_cancel(up_conn->pid)) {
        pthread_join(up_conn->pid, NULL);
    }
    //生成参数结构
    out_args *outArgs = malloc(sizeof(out_args));
    outArgs->id = up_conn->up_id;
    outArgs->seq_num = up_conn->expect_ack - 1;
    //新建计时线程
    pthread_create(&(up_conn->pid), NULL, (void *) handle_time_out_thread, outArgs);
}

//根据地址从对等方列表中获取对等方
bt_peer_t *get_peer(struct sockaddr_in addr) {
    bt_peer_t *p;
    for (p = config.peers; p != NULL; p = p->next) {
        if (p->addr.sin_port == addr.sin_port) {
            return p;
        }
    }
    return NULL;
}

//记录窗口大小函数
void log_window(int conn_id, int window, int time, int ssthresh, int isslowstart) {
    FILE *fp = fopen("problem2-peer.txt", "a+");
    fprintf(fp, "up_link:%d\t%d\t%d\t%d\t%d\n", conn_id, time, window, ssthresh, isslowstart);
    fclose(fp);
}

//处理time_out的线程函数，创建后sleep
void handle_time_out_thread(void *arg) {
    //获取传入参数
    out_args *outArgs = (out_args *) arg;
    int up_id = outArgs->id;
    int seq_num = outArgs->seq_num;
    free(outArgs);
    sleep(1);//sleep 1秒
//    pthread_setcancelstate(PTHREAD_CANCEL_DISABLE, NULL);
    up_conn_t *up_conn = NULL;
    up_conn_t **conns = up_pool.conns;
    for (int i = 0; i < up_pool.conn; ++i) {
        if (conns[i] != NULL && conns[i]->up_id == up_id) {
            up_conn = conns[i];
        }
    }
    pthread_cleanup_push(handle_dead_lock, NULL) ;
            pthread_mutex_lock(&mutex);
            //处理超时，与处理重复ack一样
            if (up_conn != NULL) {
                up_conn->last_sent = seq_num; // send回退
                up_conn->duplicate = 0; // 更新duplicate
                up_conn->ssthresh = (up_conn->cwnd / 2 > 2) ? (up_conn->cwnd / 2) : 2;
                up_conn->cwnd = 1;
                up_conn->is_slow_start = 1;
                up_conn->available = up_conn->expect_ack + up_conn->cwnd - 1;
                log_window(up_conn->up_id, up_conn->cwnd, timer_diff_now(&timer_peer_strart),
                           up_conn->ssthresh, up_conn->is_slow_start);
                send_data_packets(up_conn, sock, (struct sockaddr *) &(up_conn->receiver)->addr);
                begin_time_out(up_conn);
            }
            pthread_mutex_unlock(&mutex);
    pthread_cleanup_pop(0);
};

void handle_dead_lock(void *d) {
    pthread_mutex_unlock(&mutex);
};
