#ifndef START_QUEUE_H
#define START_QUEUE_H
//队列中的节点
typedef struct node {
    void *data;//节点数据
    struct node *next;//下一个节点
} node;
//队列,用于存储过程中的各类链表数据
typedef struct queue {
    node *head;//队列头部
    node *tail;//队列尾部
    int n;//队列节点数量
} queue;

//获取队列空间,并初始化队列
queue *make_queue();

//初始化队列
void init_queue(queue *);

//清空队列
void free_queue(queue *, int);

//将数据加入队列
void enqueue(queue *, void *);

//将数据提取出队列
void *dequeue(queue *);


#endif
