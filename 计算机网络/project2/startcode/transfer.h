#ifndef START_TRANSFER_H
#define START_TRANSFER_H

#include <netinet/in.h>
#include "queue.h"
#include "task.h"
#include "spiffy.h"

//根据传入的get_chunk_file生成需要发送的whohas包队列
queue *init_whohas_packet_queue(const char *);

//根据传入的哈希值队列生成ihave包队列
queue *init_ihave_packet_queue(queue *);

//根据传入的哈希值寻找对应的chunk,并根据chunk生成data包
//由于每个chunk有512k,数据包只能有1500字节,所以每次读取1024字节生成一个data包,总共生成512包
data_packet_t **init_data_packet_array(uint8_t *sha);

//读取chunkfile,将内容转化为二进制哈希值的队列
//此处chunkfile为getchunkfile和haschunkfile,
queue *chunk_file_to_queue(const char *chunk_file);

//将haschunkfile内容转为队列
void init_has_chunks(const char *);

//根据传入的whohas队列,寻找本地拥有其中的某些项,并返回
//用于处理收到whohas包时找到本地拥有的chunk
queue *which_i_have(queue *);

//根据传入的数据生成哈希值队列
//用于处理whohas包和ihave包的数据
queue *data2chunks_queue(void *);

//根据传入的哈希值队列,生成对应的包队列
//生成的包类型为whohas和ihave类型,根据传入的参数决定哪种类型
queue *init_chunk_packet_queue(queue *chunks, data_packet_t *(*make_chunk_packet)(short, void *));

//向特定的对等方发送ihava包,将队列的包全部发送
void send_pkts(int, struct sockaddr *, queue *);

//向所有对等方发送队列中whohas包,用于处理get命令时使用发送whohas包
void send_whohas(int sock, queue *whohas_pkts);

//向特定对等方发送数据包
void send_data_packets(up_conn_t *, int, struct sockaddr *);

//发送包的接口,发送所以包统一调用该接口,该接口调用spiffy_send发送数据
void send_packet(int, data_packet_t *, struct sockaddr *);


#endif
