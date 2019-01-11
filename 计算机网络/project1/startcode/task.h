#ifndef START_TASK_H
#define START_TASK_H

#include <stdint.h>
#include "sha.h"
#include "bt_parse.h"
#include "queue.h"
#include "udpPacket.h"

#define WINDOW_SIZE 8

//chunk结构,用于存储需要下载的chunk的状态
typedef struct chunk_s {
    int id;
    uint8_t sha1[SHA1_HASH_SIZE];//chunk的哈希值
    int flag;//是否下载完成
    int inuse;//是否在下载
    int num;//该chunk可以从多少个peer获取
    char *data;//存储chunk的数据
    queue *peers;//拥有该chunk的peer
} chunk_t;

//下载连接的结构体
typedef struct down_conn_s {
    uint expect_seq;//下一个需要收到的数据包的序列号
    chunk_t *chunk;//该连接对应下载chunk
    int pos;//当前数据包下载的位置指针
    bt_peer_t *sender;//下载源
} down_conn_t;

//上传连接的结构体
typedef struct up_conn_s {
    int expect_ack;//下一个期待的ack
    int last_sent;//上一个发送的包的序列号
    int available;//可以发送的包的最大序列号
    int rwnd;//接收方窗口大小
    data_packet_t **pkts;//需要上传的数据包数组
    bt_peer_t *receiver;//接收数据方
} up_conn_t;

//下载池,保存了当前的下载连接
typedef struct down_pool_s {
    down_conn_t **conns;//正在使用的下载连接数组
    int conn;//当前下载池数量
    int max_conn;//最大下载数量
} down_pool_t;

//上传池
typedef struct up_pool_s {
    up_conn_t **conns;//正在使用的上传连接数组
    int conn;//当前上传池数量
    int max_conn;//最大上传数量
} up_pool_t;

//当前对等方下载任务
typedef struct task_s {
    int chunk_num;//已经下载的chunk数量
    int need_num;//需要下载的chunk数量
    queue *chunks;//需要下载的chunk队列
    char output_file[BT_FILENAME_LEN];//下载的数据存储到该路径
} task_t;

//根据传入的chunk哈希和id用于初始化chunk
chunk_t *make_chunk(int id, const uint8_t *sha1);

//释放chunk
void free_chunk(chunk_t *chunk);

//初始化任务池
task_t *init_task(const char *, const char *, int);

//根据outputfile和getchunkfile初始化任务
int check_task(task_t *task);

//检测到当前任务未完成,继续下载任务
void continue_task(task_t *task, down_pool_t *pool, int sock);

//结束下载任务
task_t *finish_task(task_t *task);

//初始化上传池
void init_up_pool(up_pool_t *pool, int max_conn);

//根据对等方信息从上传池获取上传连接
up_conn_t *get_up_conn(up_pool_t *, bt_peer_t *);

//根据传入对等方地址和数据包数组的初始化上传连接并加入上传池
up_conn_t *add_to_up_pool(up_pool_t *pool, bt_peer_t *peer, data_packet_t **pkts);

//根据对等方信息将上传连接从上传池移除
void remove_from_up_pool(up_pool_t *pool, bt_peer_t *peer);

//根据传入对等方地址和数据包数组的初始化上传连接
up_conn_t *make_up_conn(bt_peer_t *peer, data_packet_t **pkts);

//初始化下载池
void init_down_pool(down_pool_t *pool, int max_conn);

//根据传入对等方找到对应的下载连接
down_conn_t *get_down_conn(down_pool_t *, bt_peer_t *);

//根据传入对等方地址和数据包数组的初始化下载连接并加入下载池
down_conn_t *add_to_down_pool(down_pool_t *pool, bt_peer_t *peer, chunk_t *chunk);

//根据对等方信息移除相应的下载连接
void remove_from_down_pool(down_pool_t *pool, bt_peer_t *peer);

//根据传入对等方地址和chunk的初始化下载连接
down_conn_t *make_down_conn(bt_peer_t *peer, chunk_t *chunk);

//根据传入的哈希值找到任务中对应的chunk,将传入的对等方加入其下载源,以更新可用对等方
void available_peer(task_t *task, uint8_t *sha1, bt_peer_t *peer);

//根据传入的ihave的哈希值,选择一个chunk进行下载
chunk_t *choose_chunk(task_t *task, queue *chunks, bt_peer_t *peer);

//寻找peer队列中是否存在这个peer
int check_peers(queue *peers, bt_peer_t *peer);

#endif
