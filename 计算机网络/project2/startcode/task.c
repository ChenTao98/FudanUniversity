#include <malloc.h>
#include <string.h>
#include <assert.h>
#include <sys/time.h>
#include "udpPacket.h"
#include "task.h"
#include "chunk.h"
#include "transfer.h"
#include "timer.h"

//初始化上传池
void init_up_pool(up_pool_t *pool, int max_conn) {
    pool->max_conn = max_conn;
    pool->conn = 0;
    pool->conns = malloc(max_conn * sizeof(up_conn_t *));
    memset(pool->conns, 0, max_conn * sizeof(up_conn_t *));
}

//根据传入对等方地址和数据包数组的初始化上传连接
up_conn_t *make_up_conn(bt_peer_t *peer, data_packet_t **pkts) {
    assert(pkts != NULL);
    up_conn_t *conn = malloc(sizeof(up_conn_t));
    memset(conn, 0, sizeof(up_conn_t));

    conn->up_id = ++up_conn_id;
    conn->receive_ack_during_avoid = 0;
    conn->ssthresh = 64;
    conn->is_slow_start = 1;
    conn->expect_ack = 1;
    conn->last_sent = 0;
    conn->receiver = peer;
    conn->cwnd = 1;
    conn->pkts = pkts;
    conn->duplicate = 0;
    conn->available = conn->expect_ack + conn->cwnd - 1;
    return conn;
}

//根据对等方信息从上传池获取上传连接
up_conn_t *get_up_conn(up_pool_t *pool, bt_peer_t *peer) {
    up_conn_t **conns = pool->conns;
    for (int i = 0; i < pool->max_conn; i++) {
        //获取上传池非空且id与对等方相等的连接
        if (conns[i] != NULL && conns[i]->receiver->id == peer->id) {
            return conns[i];
        }
    }
    return NULL;
}


//根据传入对等方地址和数据包数组的初始化上传连接并加入上传池
up_conn_t *add_to_up_pool(up_pool_t *pool, bt_peer_t *peer, data_packet_t **pkts) {
    up_conn_t *conn = make_up_conn(peer, pkts);
    //遍历上传池,找到第一个为空的位置,加入连接
    for (int i = 0; i < pool->max_conn; i++) {
        if (pool->conns[i] == NULL) {
            pool->conns[i] = conn;
            break;
        }
    }
    pool->conn++;
    return conn;
}

//根据对等方信息将上传连接从上传池移除
void remove_from_up_pool(up_pool_t *pool, bt_peer_t *peer) {
    up_conn_t **conns = pool->conns;
    for (int i = 0; i < pool->max_conn; i++) {
        //找到id与对等方相同的项,移除
        if (conns[i] != NULL && conns[i]->receiver->id == peer->id) {
            for (int j = 0; j < 512; j++) {
                free(conns[i]->pkts[j]);
            }
            free(conns[i]->pkts);
            free(conns[i]);
            //将该位置还原为null
            conns[i] = NULL;
            pool->conn--;
            break;
        }
    }
}

//初始化下载池
void init_down_pool(down_pool_t *pool, int max_conn) {
    pool->max_conn = max_conn;
    pool->conn = 0;
    pool->conns = malloc(max_conn * sizeof(down_conn_t *));
    memset(pool->conns, 0, max_conn * sizeof(down_conn_t *));
}

//根据传入对等方地址和chunk的初始化下载连接
down_conn_t *make_down_conn(bt_peer_t *peer, chunk_t *chunk) {
    assert(chunk != NULL);
    down_conn_t *conn = malloc(sizeof(down_conn_t));
    conn->expect_seq = 1;
    conn->pos = 0;
    conn->chunk = chunk;
    conn->sender = peer;
    return conn;
}

//根据传入对等方找到对应的下载连接
down_conn_t *get_down_conn(down_pool_t *pool, bt_peer_t *peer) {
    down_conn_t **conns = pool->conns;
    //遍历下载池,找到与下载方id相同的下载连接
    for (int i = 0; i < pool->max_conn; i++) {
        if (conns[i] != NULL && conns[i]->sender->id == peer->id) {
            return conns[i];
        }
    }
    return NULL;
}

//根据传入对等方地址和数据包数组的初始化下载连接并加入下载池
down_conn_t *add_to_down_pool(down_pool_t *pool, bt_peer_t *peer, chunk_t *chunk) {
    down_conn_t *conn = make_down_conn(peer, chunk);
    //遍历下载池,找到第一个为空位置,加入下载连接
    for (int i = 0; i < pool->max_conn; i++) {
        if (pool->conns[i] == NULL) {
            pool->conns[i] = conn;
            break;
        }
    }
    pool->conn++;
    return conn;
}

//根据对等方信息移除相应的下载连接
void remove_from_down_pool(down_pool_t *pool, bt_peer_t *peer) {
    down_conn_t **conns = pool->conns;
    //遍历下载池,找到id相同的项,移除
    for (int i = 0; i < pool->max_conn; i++) {
        if (conns[i] != NULL && conns[i]->sender->id == peer->id) {
            free(conns[i]);
            //将该位置还原为null
            conns[i] = NULL;
            pool->conn--;
            break;
        }
    }
}

//根据哈希值和id初始化chunk
chunk_t *make_chunk(int id, const uint8_t *sha1) {
    chunk_t *chunk = malloc(sizeof(chunk_t));
    chunk->num = 0;
    chunk->flag = 0;
    chunk->inuse = 0;
    chunk->id = id;
    memcpy(chunk->sha1, sha1, SHA1_HASH_SIZE);
    chunk->data = malloc(BT_CHUNK_SIZE);
    chunk->peers = make_queue();
    return chunk;
}

//释放chunk
void free_chunk(chunk_t *chunk) {
    free_queue(chunk->peers, 0);
    free(chunk->data);
    free(chunk);
}

//根据outputfile和getchunkfile初始化任务
task_t *init_task(const char *output_file, const char *get_chunk_file, int max_conn) {
    task_t *task = malloc(sizeof(task_t));
    //将文件输出路径保存
    strcpy(task->output_file, output_file);
    task->chunks = make_queue();
    task->max_conn = max_conn;
    //读取getchunkfile
    FILE *fp = fopen(get_chunk_file, "r");
    char read_buf[64];
    char sha_buf[2 * SHA1_HASH_SIZE];
    int i = -1;
    chunk_t *chunk;
    uint8_t sha1[SHA1_HASH_SIZE];
    //读取getchunkfile,在chunk队列里加入哈希值
    while (fgets(read_buf, 64, fp) != NULL) {
        sscanf(read_buf, "%d %s", &i, sha_buf);
        hex2binary(sha_buf, 2 * SHA1_HASH_SIZE, sha1);
        chunk = make_chunk(i, sha1);
        enqueue(task->chunks, chunk);
    }
    //初始化需要下载的数目和已经下载数目
    task->chunk_num = 0;
    task->need_num = i + 1;
    fclose(fp);
    return task;
}

//根据传入的哈希值找到任务中对应的chunk,将传入的对等方加入其下载源,以更新可用对等方
void available_peer(task_t *task, uint8_t *sha1, bt_peer_t *peer) {
    assert(task != NULL);
    chunk_t *chunk;
    //遍历任务的所有chunk
    for (node *current = task->chunks->head; current != NULL; current = current->next) {
        chunk = (chunk_t *) current->data;
        //判断传入哈希值是否与chunk哈希值相同,相同说明该对等方拥有这个chunk,将其加入下载源
        if (memcmp(sha1, chunk->sha1, SHA1_HASH_SIZE) == 0) {
            if (chunk->peers == NULL) {
                chunk->peers = make_queue();
            }
            if (!check_peers(chunk->peers, peer)) {
                chunk->num += 1;
                enqueue(chunk->peers, peer);
            }
            return;
        }
    }
}

//结束下载任务
task_t *finish_task(task_t *task) {
    chunk_t *chunk;
    //打开写入文件
    FILE *fp = fopen(task->output_file, "wb+");
    //遍历所有下载任务
    for (uint j = 0; j < task->need_num; j++) {
        //将每个下载任务的数据全部写入下载文件
        chunk = dequeue(task->chunks);
        fwrite(chunk->data, BT_CHUNK_KSIZE, SEND_PACKET_DATA_LEN, fp);
        free_chunk(chunk);
    }
    fclose(fp);
    free_queue(task->chunks, 0);
    free(task);
    return NULL;
}

//判断任务的下载是否全部完成
int check_task(task_t *task) {
    int flag = 1;
    chunk_t *chunk;
    uint8_t sha1[SHA1_HASH_SIZE];
    //遍历每个需要下载的chunk
    for (node *cur = task->chunks->head; cur != NULL; cur = cur->next) {
        chunk = cur->data;
        //根据已经下载的数据计算哈希值
        shahash((uint8_t *) chunk->data, BT_CHUNK_SIZE, sha1);
        //判断预期哈希值和下载的哈希值是否相同
        //相同则说明下载完成,不相同则没有下载完成
        if (memcmp(sha1, chunk->sha1, SHA1_HASH_SIZE) != 0) {
            flag = 0;
            chunk->flag = 0;
            chunk->inuse = 0;
        }
    }
    return flag;
}

//根据传入的ihave的哈希值,选择一个chunk进行下载
chunk_t *choose_chunk(task_t *task, queue *chunks, bt_peer_t *peer) {
    uint8_t *sha1;
    chunk_t *chunk;
    chunk_t *ret = NULL;
    //找到第一个可下载的任务,返回
    while ((sha1 = dequeue(chunks)) != NULL) {
        for (node *cur = task->chunks->head; cur != NULL; cur = cur->next) {
            chunk = cur->data;
            if (!chunk->inuse && !chunk->flag && memcmp(sha1, chunk->sha1, SHA1_HASH_SIZE) == 0) {
                return chunk;
            }
        }
    }
    return ret;
}

//当前任务未完成,继续下载任务
void continue_task(task_t *task, down_pool_t *pool, int sock) {
    //初始化需要下载的任务队列
    queue *q = make_queue();
    chunk_t *chunk = NULL;
    bt_peer_t *peer = NULL;
    int flag = 0;
    //遍历任务的chunk,找到没有开始下载的任务存入队列
    for (node *cur = task->chunks->head; cur != NULL; cur = cur->next) {
        chunk = cur->data;
        if (chunk->flag || chunk->inuse) {
            continue;
        } else {
            enqueue(q, chunk);
        }
    }
    //找到第一个可以下载,且对等方不在下载池中的任务,跳出循环
    while ((chunk = dequeue(q)) != NULL) {
        for (node *cur = chunk->peers->head; cur != NULL; cur = cur->next) {
            peer = cur->data;
            if (get_down_conn(pool, peer) == NULL) {
                flag = 1;
                break;
            }
        }
        if (flag == 1) {
            break;
        }
    }
    //如果标志置1,说明需要下载,马上发送get请求
    if (flag) {
        add_to_down_pool(pool, peer, chunk);
        chunk->inuse = 1;
        data_packet_t *pkt = make_get_packet(SHA1_HASH_SIZE + HEADERLEN, (char *) chunk->sha1);
        send_packet(sock, pkt, (struct sockaddr *) &peer->addr);
        free_packet(pkt);
    }
    //剩下队列发送whohas请求
    if (q->n) {
        queue *ret = init_chunk_packet_queue(q, make_whohas_packet);
        send_whohas(sock, ret);
        free_queue(ret, 0);
    }
    free_queue(q, 0);
}

//寻找peer队列中是否存在这个peer
int check_peers(queue *peers, bt_peer_t *peer) {
    node *current = peers->head;
    while (current != NULL) {
        bt_peer_t *cur_peer = (bt_peer_t *) current->data;
        if (cur_peer->id == peer->id) {
            return 1;
        }
        current = current->next;
    }
    return 0;
}

//清空失效节点
int remove_stalled_chunks(down_pool_t *pool) { // 检查所有chunk是否处于停滞状态
    struct timeval now;
    gettimeofday(&now, NULL); // 获得时间
    chunk_t *chunk;
    int ret = 0;
    for (int i = 0; i < pool->max_conn; i++) {
        if (pool->conns[i] != NULL) {
            chunk = pool->conns[i]->chunk;
            if (chunk->inuse && timer_diff(&pool->conns[i]->timer, &now) > 30000) {
                chunk->inuse = 0;
                remove_from_down_pool(pool, pool->conns[i]->sender);
                ret = 1;
            }
        }
    }
    return ret;
}

//未回应ack对等方的处理
void remove_unack_peers(up_pool_t *pool, int sock) {
    struct timeval now;
    gettimeofday(&now, NULL); // 获得时间
    for (int i = 0; i < pool->max_conn; i++) {
        if (pool->conns[i] != NULL) {
            if (timer_diff(&pool->conns[i]->timer, &now) > 30000) {
                remove_from_up_pool(pool, pool->conns[i]->receiver);
            } else if (timer_diff(&pool->conns[i]->timer, &now) > 2000) {
                if (pool->conns[i]->expect_ack < 1) {
                    pool->conns[i]->expect_ack = 1;
                }
                pool->conns[i]->last_sent = pool->conns[i]->expect_ack - 1;
                send_data_packets(pool->conns[i], sock, (struct sockaddr *) &pool->conns[i]->receiver->addr);
            }
        }
    }
}