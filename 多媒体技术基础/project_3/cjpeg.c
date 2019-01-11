/** 
 * @file cjpeg.c
 * @brief main file, convert BMP to JPEG image.
 */

#include <string.h>
#include "cjpeg.h"
#include "cio.h"

/* YCbCr to RGB transformation */

/*
 * precalculated tables for a faster YCbCr->RGB transformation.
 * use a INT32 table because we'll scale values by 2^16 and
 * work with integers.
 */

ycbcr_tables ycc_tables;

void
init_ycbcr_tables() {
    UINT16 i;
    for (i = 0; i < 256; i++) {
        ycc_tables.r2y[i] = (INT32) (65536 * 0.299 + 0.5) * i;
        ycc_tables.r2cb[i] = (INT32) (65536 * -0.16874 + 0.5) * i;
        ycc_tables.r2cr[i] = (INT32) (32768) * i;
        ycc_tables.g2y[i] = (INT32) (65536 * 0.587 + 0.5) * i;
        ycc_tables.g2cb[i] = (INT32) (65536 * -0.33126 + 0.5) * i;
        ycc_tables.g2cr[i] = (INT32) (65536 * -0.41869 + 0.5) * i;
        ycc_tables.b2y[i] = (INT32) (65536 * 0.114 + 0.5) * i;
        ycc_tables.b2cb[i] = (INT32) (32768) * i;
        ycc_tables.b2cr[i] = (INT32) (65536 * -0.08131 + 0.5) * i;
    }
}

void
rgb_to_ycbcr(UINT8 *rgb_unit, ycbcr_unit *ycc_unit, int x, int w) {
    ycbcr_tables *tbl = &ycc_tables;
    UINT8 r, g, b;
    //由于实现rgb存储单元实现有点不同，所以注释了两句
    //个人的 rgb 是8*8分块存储在单元里面，所以只需要按顺序转换即可
//    int src_pos = x * 3;
    int src_pos = 0;
    int dst_pos = 0;
    int i, j;
    for (j = 0; j < DCTSIZE; j++) {
        for (i = 0; i < DCTSIZE; i++) {
            b = rgb_unit[src_pos];
            g = rgb_unit[src_pos + 1];
            r = rgb_unit[src_pos + 2];
            ycc_unit->y[dst_pos] = (INT8) ((UINT8)
                                                   ((tbl->r2y[r] + tbl->g2y[g] + tbl->b2y[b]) >> 16) - 128);
            ycc_unit->cb[dst_pos] = (INT8) ((UINT8)
                    ((tbl->r2cb[r] + tbl->g2cb[g] + tbl->b2cb[b]) >> 16));
            ycc_unit->cr[dst_pos] = (INT8) ((UINT8)
                    ((tbl->r2cr[r] + tbl->g2cr[g] + tbl->b2cr[b]) >> 16));
            src_pos += 3;
            dst_pos++;
        }
//        src_pos += (w - DCTSIZE) * 3;
    }
}


/* quantization */

quant_tables q_tables;

void
init_quant_tables(UINT32 scale_factor) {
    quant_tables *tbl = &q_tables;
    int temp1, temp2;
    int i;
    for (i = 0; i < DCTSIZE2; i++) {
        temp1 = ((UINT32) STD_LU_QTABLE[i] * scale_factor + 50) / 100;
        if (temp1 < 1)
            temp1 = 1;
        if (temp1 > 255)
            temp1 = 255;
        tbl->lu[ZIGZAG[i]] = (UINT8) temp1;

        temp2 = ((UINT32) STD_CH_QTABLE[i] * scale_factor + 50) / 100;
        if (temp2 < 1)
            temp2 = 1;
        if (temp2 > 255)
            temp2 = 255;
        tbl->ch[ZIGZAG[i]] = (UINT8) temp2;
    }
}

void
jpeg_quant(ycbcr_unit *ycc_unit, quant_unit *q_unit) {
    quant_tables *tbl = &q_tables;
    float q_lu, q_ch;
    int x, y, i = 0;
    for (x = 0; x < DCTSIZE; x++) {
        for (y = 0; y < DCTSIZE; y++) {
            q_lu = 1.0 / ((double) tbl->lu[ZIGZAG[i]] * \
                    AAN_SCALE_FACTOR[x] * AAN_SCALE_FACTOR[y] * 8.0);
            q_ch = 1.0 / ((double) tbl->ch[ZIGZAG[i]] * \
                    AAN_SCALE_FACTOR[x] * AAN_SCALE_FACTOR[y] * 8.0);

            q_unit->y[i] = (INT16) (ycc_unit->y[i] * q_lu + 16384.5) - 16384;
            q_unit->cb[i] = (INT16) (ycc_unit->cb[i] * q_ch + 16384.5) - 16384;
            q_unit->cr[i] = (INT16) (ycc_unit->cr[i] * q_ch + 16384.5) - 16384;

            i++;
        }
    }
}


/* huffman compression */

huff_tables h_tables;

void
set_huff_table(UINT8 *nrcodes, UINT8 *values, BITS *h_table) {
    int i, j, k;
    j = 0;
    UINT16 value = 0;
    for (i = 1; i <= 16; i++) {
        for (k = 0; k < nrcodes[i]; k++) {
            h_table[values[j]].len = i;
            h_table[values[j]].val = value;
            j++;
            value++;
        }
        value <<= 1;
    }
}

void
init_huff_tables() {
    huff_tables *tbl = &h_tables;
    set_huff_table(STD_LU_DC_NRCODES, STD_LU_DC_VALUES, tbl->lu_dc);
    set_huff_table(STD_LU_AC_NRCODES, STD_LU_AC_VALUES, tbl->lu_ac);
    set_huff_table(STD_CH_DC_NRCODES, STD_CH_DC_VALUES, tbl->ch_dc);
    set_huff_table(STD_CH_AC_NRCODES, STD_CH_AC_VALUES, tbl->ch_ac);
}

void
set_bits(BITS *bits, INT16 data) {
    /******************************************************/
    /*                                                    */
    /*             finish the missing codes               */
    /*                                                    */
    /******************************************************/
    //获取data的绝对值并存储
    INT16 abs_data = data < 0 ? (~(data) + (INT16) 1) : data;
    UINT8 length = 0;
    //获取绝对值的位长度
    //temp_data每次右移一位，判断是否大于0，当不大于0时即已到最高位
    while (abs_data > (INT16) 0) {
        length = length + (UINT8) 1;
        abs_data = abs_data >> 1;
    }
    //获取data调整后的值
    UINT16 byte = (UINT16) (data < 0 ? ((data + (((INT16) 1) << length) - (INT16) 1)) : data);

    bits->len = length;
    bits->val = byte;
}

#ifdef DEBUG
void
print_bits(BITS bits)
{
    printf("%hu %hu\t", bits.len, bits.val);
}
#endif

/*
 * compress JPEG
 */
void
jpeg_compress(compress_io *cio,
              INT16 *data, INT16 *dc, BITS *dc_htable, BITS *ac_htable) {
    INT16 zigzag_data[DCTSIZE2];
    BITS bits;
    INT16 diff;
    int i, j;
    int zero_num;
    int mark;

    /* zigzag encode */
    for (i = 0; i < DCTSIZE2; i++)
        zigzag_data[ZIGZAG[i]] = data[i];

    /* write DC */
    diff = zigzag_data[0] - *dc;
    *dc = zigzag_data[0];

    if (diff == 0)
        write_bits(cio, dc_htable[0]);
    else {
        set_bits(&bits, diff);
        write_bits(cio, dc_htable[bits.len]);
        write_bits(cio, bits);
    }

    /* write AC */
    int end = DCTSIZE2 - 1;
    while (zigzag_data[end] == 0 && end > 0)
        end--;
    for (i = 1; i <= end; i++) {
        j = i;
        while (zigzag_data[j] == 0 && j <= end)
            j++;
        zero_num = j - i;
        for (mark = 0; mark < zero_num / 16; mark++)
            write_bits(cio, ac_htable[0xF0]);
        zero_num = zero_num % 16;
        set_bits(&bits, zigzag_data[j]);
        write_bits(cio, ac_htable[zero_num * 16 + bits.len]);
        write_bits(cio, bits);
        i = j;
    }

    /* write end of unit */
    if (end != DCTSIZE2 - 1)
        write_bits(cio, ac_htable[0]);
}
//哭了，后面发现Huffman表已经计算好了，难受
////自己额外加的函数，用于生成Huffman表
////huffman码表生成原理是按照ppt上来的
//BITS *get_huffman_table(const UINT8 *nrcode, const UINT8 *values, int size) {
//    //根据size初始化空间
//    BITS *bits = malloc(sizeof(BITS) * size);
//    memset(bits, 0, sizeof(BITS) * size);
//    int index = 0;
//    UINT16 huffman_code = 0;//huffman编码
//    int isFirst = 1;//判断是否是第一个编码
//    UINT8 length = 0;//当前Huffman编码的长度
//    for (int i = 1; i <= 16; i++) {
//        int cnt = nrcode[i];
//        for (int j = 0; j < cnt; ++j) {
//            if (isFirst == 1) {
//                isFirst = 0;
//                length = (UINT8) i;
//                bits[values[index]].len = length;
//                bits[values[index]].val = huffman_code;
//                index++;
//                continue;
//            }
//            if (length != i) {
//                UINT16 temp_code = (UINT16) (1 << (length));
//                if (temp_code - huffman_code == 1) {
//                    length = length + (UINT8) 1;
//                }
//                huffman_code = huffman_code + (UINT16) 1;
//                if (length != i) {
//                    huffman_code = huffman_code << (i - length);
//                    length = (UINT8) i;
//                }
//            } else {
//                huffman_code = huffman_code + (UINT16) 1;
//            }
//            bits[values[index]].len = length;
//            bits[values[index]].val = huffman_code;
//            index++;
//        }
//    }
//    return bits;
//}


/*
 * main JPEG encoding
 */
void
jpeg_encode(compress_io *cio, bmp_info *binfo) {
    /* init tables */
    UINT32 scale = 50;
    init_ycbcr_tables();
    init_quant_tables(scale);
    init_huff_tables();

    /* write info */
    write_file_header(cio);
    write_frame_header(cio, binfo);
    write_scan_header(cio);

    /* encode */
    mem_mgr *in = cio->in;
    ycbcr_unit ycc_unit;
    quant_unit q_unit;
    INT16 dc_y = 0,
            dc_cb = 0,
            dc_cr = 0;
    int x, y;

    int offset = binfo->offset;
    fseek(in->fp, offset, SEEK_SET);


    /******************************************************/
    /*                                                    */
    /*             finish the missing codes               */
    /*                                                    */
    /******************************************************/

    int bmp_width = binfo->width;//bmp的宽
    int bmp_height = binfo->height;//bmp的高
    //由于要8*8分块，判断bmp的长和宽，如果不是8的倍数，补成8的倍数
    int jpeg_width = (bmp_width % 8 == 0) ? bmp_width : (bmp_width / 8 + 1) * 8;
    int jpeg_height = (bmp_height % 8 == 0) ? bmp_height : (bmp_height / 8 + 1) * 8;

    int column = jpeg_width / 8;//分块的列数
    int row = jpeg_height / 8;//分块的行数
    UINT8 *rgb_array[column * row];//存储rgb的二维数组
    ycbcr_unit *ycbcr_array[column * row];//存储ycbcr的数组，个元素是一个块的ycbcr_unit.代表一个块的ycbcr
    quant_unit *quant_array[column * row];//存储量化后数据的数组，个元素是一个块的quant_unit，代表一个块量化后的数据
    size_t rgb_block_size = sizeof(UINT8) * 3 * DCTSIZE2;
    for (int i = 0; i < row; i++) {
        for (int j = 0; j < column; j++) {
            int index = i * column + j;
            //为rgb数组分配内存，每个元素是8*8×3数组，用于存储一个块的rgb
            rgb_array[index] = malloc(rgb_block_size);
            memset(rgb_array[index], 0, rgb_block_size);
            //为ycbcr数组分配内存并初始化，每个元素是ycbcr_unit，代表一个块的数据
            ycbcr_array[index] = malloc(sizeof(ycbcr_unit));
            memset(ycbcr_array[index], 0, sizeof(ycc_unit));
            //为量化后的数据分配内存并初始化，每个元素是个quant_unit，代表一个块量化后的数据
            quant_array[index] = malloc(sizeof(quant_unit));
            memset(quant_array[index], 0, sizeof(quant_unit));
        }
    }

    UINT8 *rgb_unit;

//    BITS *lu_dc_table = get_huffman_table(STD_LU_DC_NRCODES, STD_LU_DC_VALUES, 16);
//    BITS *lu_ac_table = get_huffman_table(STD_LU_AC_NRCODES, STD_LU_AC_VALUES, 256);
//    BITS *ch_dc_table = get_huffman_table(STD_CH_DC_NRCODES, STD_CH_DC_VALUES, 16);
//    BITS *ch_ac_table = get_huffman_table(STD_CH_AC_NRCODES, STD_CH_AC_VALUES, 256);

    //读取rgb数据并存储
    for (int i = 0; i < bmp_height; ++i) {
        //判断rgb数据所在的块的行数与偏移量
        int temp_row = (bmp_height - i - 1) / DCTSIZE;
        int temp_y = (bmp_height - i - 1) % DCTSIZE;
        for (int j = 0; j < bmp_width; ++j) {
            //判断rgb数据所在的列数与偏移量
            int temp_column = j / DCTSIZE;
            int temp_x = j % DCTSIZE;
            int index = (temp_y * DCTSIZE + temp_x) * 3;
            //获取相应的块并读取存储数据
            rgb_unit = rgb_array[temp_row * column + temp_column];
            fread(&rgb_unit[index], sizeof(UINT8), 3, in->fp);
        }
    }
    //转换、DCT、量化、压缩存储
    for (int i = 0; i < row; ++i) {
        for (int j = 0; j < column; ++j) {
            //计算当前的块的下标
            int index = i * column + j;
            //rgb转ycbcr
            rgb_to_ycbcr(rgb_array[index], ycbcr_array[index], 0, 0);
            //离散余弦变换
            jpeg_fdct(ycbcr_array[index]->y);
            jpeg_fdct(ycbcr_array[index]->cb);
            jpeg_fdct(ycbcr_array[index]->cr);
            //量化
            jpeg_quant(ycbcr_array[index], quant_array[index]);
//            jpeg_compress(cio, quant_array[index]->y, &dc_y, lu_dc_table, lu_ac_table);
//            jpeg_compress(cio, quant_array[index]->cb, &dc_cb, ch_dc_table, ch_ac_table);
//            jpeg_compress(cio, quant_array[index]->cr, &dc_cr, ch_dc_table, ch_ac_table);

            //压缩写入数据
            jpeg_compress(cio, quant_array[index]->y, &dc_y, h_tables.lu_dc, h_tables.lu_ac);
            jpeg_compress(cio, quant_array[index]->cb, &dc_cb, h_tables.ch_dc, h_tables.ch_ac);
            jpeg_compress(cio, quant_array[index]->cr, &dc_cr, h_tables.ch_dc, h_tables.ch_ac);
            //释放内存
            free(rgb_array[index]);
            free(ycbcr_array[index]);
            free(quant_array[index]);
        }
    }
    write_align_bits(cio);

    /* write file end */
    write_file_trailer(cio);
}

bool
is_bmp(FILE *fp) {
    UINT8 marker[3];
    if (fread(marker, sizeof(UINT16), 2, fp) != 2)
        err_exit(FILE_READ_ERR);
    if (marker[0] != 0x42 || marker[1] != 0x4D)
        return false;
    rewind(fp);
    return true;
}

void
err_exit(const char *error_string, int exit_num) {
    printf(error_string);
    exit(exit_num);
}


void
print_help() {
    printf("compress BMP file into JPEG file.\n");
    printf("Usage:\n");
    printf("    cjpeg {BMP} {JPEG}\n");
    printf("\n");
    printf("Author: Yu, Le <yeolar@gmail.com>\n");
}


int
main(int argc, char *argv[]) {
    if (argc == 3) {
        /* open bmp file */
        FILE *bmp_fp = fopen(argv[1], "rb");
        if (!bmp_fp)
            err_exit(FILE_OPEN_ERR);
        if (!is_bmp(bmp_fp))
            err_exit(FILE_TYPE_ERR);

        /* open jpeg file */
        FILE *jpeg_fp = fopen(argv[2], "wb");
        if (!jpeg_fp)
            err_exit(FILE_OPEN_ERR);

        /* get bmp info */
        bmp_info binfo;
        read_bmp(bmp_fp, &binfo);

        /* init memory for input and output */
        compress_io cio;
        int in_size = (binfo.width * 3 + 3) / 4 * 4 * DCTSIZE;
        int out_size = MEM_OUT_SIZE;
        init_mem(&cio, bmp_fp, in_size, jpeg_fp, out_size);

        /* main encode process */
        jpeg_encode(&cio, &binfo);

        /* flush and free memory, close files */
        if (!(cio.out->flush_buffer)(&cio))
            err_exit(BUFFER_WRITE_ERR);
        free_mem(&cio);
        fclose(bmp_fp);
        fclose(jpeg_fp);
    } else
        print_help();
    exit(0);
}
