#include <malloc.h>
#include <netinet/in.h>
#include "udpPacket.h"
#include "spiffy.h"

//根据传入数据初始化数据包
void init_packet(data_packet_t *pkt, char type, short len, u_int seq, u_int ack, char *data) {
    pkt->header.magicnum = 15441;
    pkt->header.version = 1;
    pkt->header.packet_type = type;
    pkt->header.header_len = HEADERLEN;
    pkt->header.packet_len = len;
    pkt->header.seq_num = seq;
    pkt->header.ack_num = ack;
    if (pkt->data != NULL) {
        memcpy(pkt->data, data, len - HEADERLEN);
    }
}

//根据传入数据创建数据包,初始化数据包
data_packet_t *make_packet(char type, short len, u_int seq, u_int ack, char *data) {
    data_packet_t *pkt = malloc(sizeof(data_packet_t));
    init_packet(pkt, type, len, seq, ack, data);
    return pkt;
}

//清空数据包
void free_packet(data_packet_t *pkt) {
    free(pkt);
}

//创建各个类型的数据包
data_packet_t *make_whohas_packet(short len, void *data) {
    return make_packet(PKT_WHOHAS, len, 0, 0, data);
}

data_packet_t *make_ihave_packet(short len, void *data) {
    return make_packet(PKT_IHAVE, len, 0, 0, data);
}

data_packet_t *make_get_packet(short len, char *data) {
    return make_packet(PKT_GET, len, 0, 0, data);
}

data_packet_t *make_data_packet(short len, uint ack_num, uint seq_num, char *data) {
    return make_packet(PKT_DATA, len, seq_num, ack_num, data);
}

data_packet_t *make_ack_packet(uint ack_num, uint seq_num) {
    return make_packet(PKT_ACK, HEADERLEN, seq_num, ack_num, NULL);
}

data_packet_t *make_denied_packet() {
    return make_packet(PKT_DENIED, HEADERLEN, 0, 0, NULL);
}

//判断数据包是否合法,并返回数据包类型
//数据包是否合法包括判断magicnum是否正确,version是否正确,数据包类型是否符合要求
//不合法返回-1,合法返回对应类型
int packet_is_legal(void *packet) {
    data_packet_t *pkt = (data_packet_t *) packet;
    header_t header = pkt->header;
    if (header.magicnum != MAGICNUM) {
        return -1;
    }
    if (header.version != VERSION)
        return -1;
    if (header.packet_type < PKT_WHOHAS || header.packet_type > PKT_ACK)
        return -1;
    return header.packet_type;
}

//将数据包头部转为网络格式
void host2net(data_packet_t *pkt) {
    pkt->header.magicnum = htons(pkt->header.magicnum);
    pkt->header.header_len = htons(pkt->header.header_len);
    pkt->header.packet_len = htons(pkt->header.packet_len);
    pkt->header.seq_num = htonl(pkt->header.seq_num);
    pkt->header.ack_num = htonl(pkt->header.ack_num);
}

//将数据包头部转为本地格式
void net2host(data_packet_t *pkt) {
    pkt->header.magicnum = ntohs(pkt->header.magicnum);
    pkt->header.header_len = ntohs(pkt->header.header_len);
    pkt->header.packet_len = ntohs(pkt->header.packet_len);
    pkt->header.seq_num = ntohl(pkt->header.seq_num);
    pkt->header.ack_num = ntohl(pkt->header.ack_num);
}