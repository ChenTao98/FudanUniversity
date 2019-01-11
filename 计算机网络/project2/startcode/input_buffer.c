#include <stdio.h>
#include <string.h>
#include <assert.h>
#include "input_buffer.h"

struct user_iobuf *create_userbuf() {
    struct user_iobuf *b;
    b = malloc(sizeof(struct user_iobuf));
    if (!b) return NULL;

    b->buf = malloc(USERBUF_SIZE + 1);
    if (!b->buf) {
        free(b); // 果然buf创建失败则b也无效 释放b
        return NULL;
    }

    b->cur = 0; // 游标位置 0
    bzero(b->buf, USERBUF_SIZE + 1); // 将所有数据置0
    return b;
}

void process_user_input(int fd, struct user_iobuf *userbuf,
                        void (*handle_line)(char *, void *), void *cbdata) {
    int nread;
    char *ret;

    assert(userbuf != NULL);
    assert(userbuf->buf != NULL);

    /* A real program would propagate this error back to the select loop or
     * implement some other form of error handling */

    if (userbuf->cur >= (USERBUF_SIZE - 1)) {
        fprintf(stderr, "process_user_input error:  buffer full;  line too long!\n"); // 不能填满buf
        exit(-1);
    }

    nread = read(fd, userbuf->buf + userbuf->cur,
                 (USERBUF_SIZE - userbuf->cur));

    if (nread > 0) {
        userbuf->cur += nread;
    }

    while ((ret = strchr(userbuf->buf, '\n')) != NULL) {
        *ret = '\0';
        handle_line(userbuf->buf, cbdata);
        /* Shift the remaining contents of the buffer forward */
        memmove(userbuf->buf, ret + 1,
                USERBUF_SIZE - (ret - userbuf->buf)); // 可以填充的区域大小为 ret - userbuf->buf 的大小 ，则需要移动的buf宽度为bufsize-区域宽度
        userbuf->cur -= (ret - userbuf->buf + 1); // cur 右边左移
    }

}
