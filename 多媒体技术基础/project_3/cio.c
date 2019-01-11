/** 
 * @file cio.c
 * @brief memory manager and operations for compressing JPEG IO.
 */

#include <string.h>
#include "cjpeg.h"
#include "cio.h"


/*
 * flush input and output of compress IO.
 */

bool
flush_cin_buffer(void *cio) {
    mem_mgr *in = ((compress_io *) cio)->in;
    size_t len = in->end - in->set;
    memset(in->set, 0, len);
    if (fread(in->set, sizeof(UINT8), len, in->fp) != len)
        return false;
    in->pos = in->set;
    return true;
}

bool
flush_cout_buffer(void *cio) {
    mem_mgr *out = ((compress_io *) cio)->out;
    size_t len = out->pos - out->set;
    if (fwrite(out->set, sizeof(UINT8), len, out->fp) != len)
        return false;
    memset(out->set, 0, len);
    out->pos = out->set;
    return true;
}


/*
 * init memory manager.
 */

void
init_mem(compress_io *cio,
         FILE *in_fp, int in_size, FILE *out_fp, int out_size) {
    cio->in = (mem_mgr *) malloc(sizeof(mem_mgr));
    if (!cio->in)
        err_exit(BUFFER_ALLOC_ERR);
    cio->in->set = (UINT8 *) malloc(sizeof(UINT8) * in_size);
    if (!cio->in->set)
        err_exit(BUFFER_ALLOC_ERR);
    cio->in->pos = cio->in->set;
    cio->in->end = cio->in->set + in_size;
    cio->in->flush_buffer = flush_cin_buffer;
    cio->in->fp = in_fp;

    cio->out = (mem_mgr *) malloc(sizeof(mem_mgr));
    if (!cio->out)
        err_exit(BUFFER_ALLOC_ERR);
    cio->out->set = (UINT8 *) malloc(sizeof(UINT8) * out_size);
    if (!cio->out->set)
        err_exit(BUFFER_ALLOC_ERR);
    cio->out->pos = cio->out->set;
    cio->out->end = cio->out->set + out_size;
    cio->out->flush_buffer = flush_cout_buffer;
    cio->out->fp = out_fp;

    cio->temp_bits.len = 0;
    cio->temp_bits.val = 0;
}

void
free_mem(compress_io *cio) {
    fflush(cio->out->fp);
    free(cio->in->set);
    free(cio->out->set);
    free(cio->in);
    free(cio->out);
}


/*
 * write operations.
 */

void
write_byte(compress_io *cio, UINT8 val) {
    mem_mgr *out = cio->out;
    *(out->pos)++ = val & 0xFF;
    if (out->pos == out->end) {
        if (!(out->flush_buffer)(cio))
            err_exit(BUFFER_WRITE_ERR);
    }
}

void
write_word(compress_io *cio, UINT16 val) {
    write_byte(cio, (val >> 8) & 0xFF);
    write_byte(cio, val & 0xFF);
}

void
write_marker(compress_io *cio, JPEG_MARKER mark) {
    write_byte(cio, 0xFF);
    write_byte(cio, (int) mark);
}


static UINT32 huffman_code = 0;
static UINT8 length = 0;
const UINT8 SHORT_LENGTH = 16;
const UINT8 INT_LENGTH = 32;
const UINT8 BYTE_LENGTH = 8;

void
write_bits(compress_io *cio, BITS bits) {
    /******************************************************/
    /*                                                    */
    /*            finish the missing codes                */
    /*                                                    */
    /******************************************************/
    //write_bits思路：
    //一个32位的Huffman_code，高16位的低位用于存储上次写入之后剩下的数据，低16位的高位用于存储这次需要写入的数据
    //上次的剩下的数据和新的数据都靠近第16位，两者在16，17位相连，例如xxxxxooonnnxxxx(x为0，o为旧数据，n为新数据)
    //每次连接后，如果长度大于8，写入数据到剩余数据长度不为8，等待下一次写数据

    //获取输入数据的长度和编码
    UINT8 input_length = bits.len;
    UINT16 input_huffman_code = bits.val;
    //将新数据左移到最高位为16，与旧数据相连
    huffman_code += (input_huffman_code << (SHORT_LENGTH - input_length));
    //记录当前数据的最高位是哪一位
    UINT8 high_bit = length + SHORT_LENGTH;
    //更新长度
    length += input_length;
    //循环判断长度是否大于8，是的话就截取数据写入
    while (length >= BYTE_LENGTH) {
        //获取要写入的字节
        UINT8 byte = (UINT8) (huffman_code >> (high_bit - BYTE_LENGTH));
        //写入字节，并判断字节是否为0xff，如果是，再写入0x00
        write_byte(cio, byte);
        if (byte == 0xff) {
            write_byte(cio, 0x00);
        }
        //更新length，更新最高位
        length -= BYTE_LENGTH;
        high_bit -= BYTE_LENGTH;
        //把刚刚写入的数据为变为0
        huffman_code = ((huffman_code << (INT_LENGTH - high_bit)) >> (INT_LENGTH - high_bit));
    }
    //剩余不足8位，将数据移到高16位的低位
    huffman_code = (huffman_code << (SHORT_LENGTH - high_bit + length));
}

void
write_align_bits(compress_io *cio) {
    /******************************************************/
    /*                                                    */
    /*            finish the missing codes                */
    /*                                                    */
    /******************************************************/
    //如果剩余的数据长度是0，直接返回
    if (length == (UINT8) 0) {
        return;
    }
    //将数据移到最低位
    huffman_code = huffman_code >> SHORT_LENGTH;
    while (length < BYTE_LENGTH) {
        //每次左移加1，就是在末尾补1，直到满足一个字节为止
        huffman_code = (huffman_code << 1) + 1;
        length++;
    }
    //写入字节
    write_byte(cio, (UINT8) huffman_code);
    if((UINT8) huffman_code==0xff){
        write_byte(cio,0x00);
    }
}

