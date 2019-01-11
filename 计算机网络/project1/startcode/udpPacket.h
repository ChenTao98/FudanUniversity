#ifndef START_UDPPACKET_H_
#define START_UDPPACKET_H_

#include <assert.h>
#include <sys/socket.h>
#include <string.h>
#include <stdio.h>
//定义必要的长度数据
#define BUFLEN 1500
#define PACKETLEN 1500
#define HEADERLEN 16
#define SHA1_HASH_SIZE 20
#define MAX_CHANK_NUM 74
#define DATALEN PACKETLEN-HEADERLEN
#define SEND_PACKET_DATA_LEN 1024
//定义num与version
#define MAGICNUM 15441
#define VERSION 1
//定义包的类型
#define PKT_WHOHAS        0
#define PKT_IHAVE        1
#define PKT_GET            2
#define PKT_DATA        3
#define PKT_ACK        4
//数据包头结构
typedef struct header_s {
    short magicnum;//magicnum
    char version;//版本号
    char packet_type;//数据包类型
    short header_len;//包头部长度
    short packet_len;//包数据长度
    u_int seq_num;//序列号
    u_int ack_num;//确认号
} header_t;
//数据包结构,包括头部与数据
typedef struct data_packet {
    header_t header;//数据包头部
    char data[DATALEN];//包所包含的数据
} data_packet_t;

//根据传入数据初始化数据包
void init_packet(data_packet_t *, char, short, u_int, u_int, char *);

//根据传入数据创建数据包,初始化数据包
data_packet_t *make_packet(char, short, u_int, u_int, char *);

void free_packet(data_packet_t *);

data_packet_t *make_whohas_packet(short, void *);

data_packet_t *make_ihave_packet(short, void *);

data_packet_t *make_get_packet(short, char *);

data_packet_t *make_data_packet(short, uint, uint, char *);

data_packet_t *make_ack_packet(uint, uint);

//将数据包头部转为网络格式
void host2net(data_packet_t *);

//将数据包头部转为本地格式
void net2host(data_packet_t *);

//判断数据包是否合法,不合法返回-1,合法返回数据包类型
int packet_is_legal(void *);

#endif