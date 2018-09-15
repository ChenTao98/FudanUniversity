/*
 * mm-naive.c - The fastest, least memory-efficient malloc package.
 * 
 * In this naive approach, a block is allocated by simply incrementing
 * the brk pointer.  A block is pure payload. There are no headers or
 * footers.  Blocks are never coalesced or reused. Realloc is
 * implemented directly using mm_malloc and mm_free.
 *
 * NOTE TO STUDENTS: Replace this header comment with your own header
 * comment that gives a high level description of your solution.
 */
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <unistd.h>
#include <string.h>

#include "mm.h"
#include "memlib.h"

/*********************************************************
 * NOTE TO STUDENTS: Before you do anything else, please
 * provide your team information in the following struct.
 ********************************************************/
team_t team = {
    /* Team name */
    "ateam",
    /* First member's full name */
    "Dinh Dat Thanh",
    /* First member's email address */
    "ddthanh@apcs.edu.vn",
    /* Second member's full name (leave blank if none) */
    "Vo Tran Thanh Luong",
    /* Second member's email address (leave blank if none) */
    "vttluong@apcs.edu.vn"
};

/* alignment  must be equal to integer size */
#define ALIGNMENT 8

/* rounds up to the nearest multiple of ALIGNMENT */
#define ALIGN(size) (((size) + (ALIGNMENT-1)) & ~0x7)


#define SIZE_T_SIZE (ALIGN(sizeof(size_t)))

// My additional Macros
#define WSIZE     4          // word and header/footer size (bytes)
#define DSIZE     8          // double word size (bytes)
#define INITCHUNKSIZE (1<<6)
#define CHUNKSIZE (1<<12)//+(1<<7) 

#define LIST    20      
#define REALLOC_BUFFER  (1<<7)    

#define MAX(x, y) ((x) > (y) ? (x) : (y)) 
#define MIN(x, y) ((x) < (y) ? (x) : (y)) 

// Pack a size and allocated bit into a word
#define PACK(size, alloc) ((size) | (alloc))

// Read and write a word at address p 
#define GET(p)            (*(unsigned int *)(p))
#define PUT(p, val)       (*(unsigned int *)(p) = (val) | GET_TAG(p))
#define PUT_NOTAG(p, val) (*(unsigned int *)(p) = (val))

// Store predecessor or successor pointer for free blocks 
#define SET_PTR(p, ptr) (*(unsigned int *)(p) = (unsigned int)(ptr))

// Read the size and allocation bit from address p 
#define GET_SIZE(p)  (GET(p) & ~0x7)
#define GET_ALLOC(p) (GET(p) & 0x1)
#define GET_TAG(p)   (GET(p) & 0x2)
#define SET_RATAG(p)   (GET(p) |= 0x2)
#define REMOVE_RATAG(p) (GET(p) &= ~0x2)

// Address of block's header and footer 
#define HDRP(ptr) ((char *)(ptr) - WSIZE)
#define FTRP(ptr) ((char *)(ptr) + GET_SIZE(HDRP(ptr)) - DSIZE)

// Address of  next and previous blocks 
#define NEXT_BLKP(ptr) ((char *)(ptr) + GET_SIZE((char *)(ptr) - WSIZE))
#define PREV_BLKP(ptr) ((char *)(ptr) - GET_SIZE((char *)(ptr) - DSIZE))

// Address of free block's predecessor and successor entries 
#define PRED_PTR(ptr) ((char *)(ptr))
#define SUCC_PTR(ptr) ((char *)(ptr) + WSIZE)

// Address of free block's predecessor and successor on the segregated list 
#define PRED(ptr) (*(char **)(ptr))
#define SUCC(ptr) (*(char **)(SUCC_PTR(ptr)))

// End of my additional macros


// Global var
void *free_lists[LIST]; 


// Helper Functions
static void *extend_heap(size_t size);
static void *coalesce(void *ptr);
static void *place(void *ptr, size_t asize);
static void insert_node(void *ptr, size_t size);
static void delete_node(void *ptr);


static void *extend_heap(size_t size)
{
    size_t tempsize=size;
    void * ptr= mem_sbrk(tempsize);

    //not enough space
    if(ptr == (void * ) -1)
        return NULL;

    //set header and footer infomation
    //header
    PUT_NOTAG(HDRP(ptr),PACK(tempsize,0));
    //footer
    PUT_NOTAG(FTRP(ptr),PACK(tempsize,0));
    PUT_NOTAG(HDRP(NEXT_BLKP(ptr)),PACK(0,1));
    //insert free node
    insert_node(ptr,tempsize);

    return coalesce(ptr);
}
//insert the node to the segregated free list
static void insert_node(void * ptr, size_t size)
{
    int index;
    void *next = ptr;
    void *before = NULL;

    for(index=0;index < LIST -1; index++ )
    {
        if(size > 1)
        {
            size = size >> 1;
        }
        else break;
    }
    next = free_lists[index];
    //traverse the free list to find a position to input the node
    while( next !=NULL && size < GET_SIZE(HDRP(next)))
    {
        before = next;
        next = PRED(next);
    }
    if(next != NULL)
    {
        //insert between the list
        if(before!= NULL)
        {
            SET_PTR(PRED_PTR(ptr),next);
            SET_PTR(SUCC_PTR(next), ptr);
            SET_PTR(PRED_PTR(before), ptr);
            SET_PTR(SUCC_PTR(ptr), before);
        }
        //insert at the begining of the list
        else
        {
            SET_PTR(PRED_PTR(ptr), next);
            SET_PTR(SUCC_PTR(next), ptr);
            SET_PTR(SUCC_PTR(ptr), NULL);
            //update the root of the free list
            free_lists[index]= ptr;
        }
    }
    //at the end of the list
    else
    {
        //at the end of the list
        if(before!=NULL)
        {
            SET_PTR(PRED_PTR(ptr),NULL);
            SET_PTR(SUCC_PTR(ptr), before);
            SET_PTR(PRED_PTR(before),ptr);
        }
        //the list is empty initially at that index
        else
        {
            SET_PTR(PRED_PTR(ptr),NULL);
            SET_PTR(SUCC_PTR(ptr),NULL);
            //update the root of free list at the index
            free_lists[index]=ptr;
        }
    }
    return;
    
}
//delete the node in the segregated free list to input ( if the pointer is in the list 2, after delete and insert again in the 5th list)
static void delete_node(void * ptr)
{
    int index;
    int size= GET_SIZE(HDRP(ptr));

    //select segregated list
    while((index < LIST-1  ) && (size >1) )
    {
        size = size >>1;
        index++;
    }

    //the pointer is not the head of the doubly linked list
    if(PRED(ptr) != NULL)
    {
        //the pointer is not at the end of the doubly linked list
        if(SUCC(ptr) != NULL)
        {
            //link the successor and predessor of the pointer
            SET_PTR(SUCC_PTR(PRED(ptr)) , SUCC(ptr));
            SET_PTR(PRED_PTR(SUCC(ptr)) , PRED(ptr));
        }
        //the pointer is at the end
        else
        {
            SET_PTR(SUCC_PTR(PRED(ptr)), NULL);
            free_lists[index] = PRED(ptr);
        }
    }
    //the pointer at the beginning
    else
    {
        //the list has 2 nodes
        if(SUCC(ptr) !=NULL)
        {
            SET_PTR(PRED_PTR(SUCC(ptr)),NULL);
        }
        else
        {
            free_lists[index]=NULL;
        }
    }
    return;
}

//expand the free block and input to the segregated free list
static void * coalesce(void * ptr)
{
    //check if the prevrious block is allocated
    size_t prev_all =GET_ALLOC(HDRP(PREV_BLKP(ptr)));
    //check if the next block is allocated
    size_t next_all =GET_ALLOC(HDRP(NEXT_BLKP(ptr)));
    size_t size =GET_SIZE(HDRP(ptr));

    //if the previous is reallocated, do not coalesce
    if(GET_TAG(HDRP(PREV_BLKP(ptr))) == 1)
        prev_all = 1;

    //cannot coalesce with previous and the next block
    if(prev_all == 1 && next_all ==1)
        return ptr;

    //can coalesce with the next block
    if(prev_all == 1 && next_all == 0)
    {
        delete_node(ptr);
        delete_node(NEXT_BLKP(ptr));
        //the new size of the coalesce free block
        size += GET_SIZE(HDRP(NEXT_BLKP(ptr)));
        //update the info at the header and the footer of the new free block at the pointer
        PUT(HDRP(ptr), PACK(size,0));
        PUT(FTRP(ptr), PACK(size,0));
    }
    //coalesce with the previous block
    else if(prev_all == 0 && next_all == 1)
    {
        delete_node(ptr);
        delete_node(PREV_BLKP(ptr));
        size+= GET_SIZE(HDRP(PREV_BLKP(ptr)));
        ptr= PREV_BLKP(ptr);
        PUT(HDRP(ptr), PACK(size,0));
        PUT(FTRP(ptr), PACK(size,0));
    }
    //coalesce with both previous and next block
    else if (prev_all ==0 && next_all ==0)
    {
        delete_node(ptr);
        delete_node(PREV_BLKP(ptr));
        delete_node(NEXT_BLKP(ptr));

        size+= GET_SIZE(HDRP(PREV_BLKP(ptr))) + GET_SIZE(HDRP(NEXT_BLKP(ptr)));

        ptr = PREV_BLKP(ptr);
        PUT(HDRP(ptr), PACK(size,0));
        PUT(FTRP(ptr), PACK(size,0));   
    }
    //insert the new free list to the segregated free list
    insert_node(ptr,size);
    return ptr;
}


//place the size in the appropriate block in the free list and decide to whether it is needed to split the block
//return the pointer to the new allocated block
static void * place(void * ptr, size_t asize)
{
    size_t size = GET_SIZE(HDRP(ptr));
    size_t remain = size - asize;

    delete_node(ptr);

    if(remain <= DSIZE*2)
    {
        //do not split
        PUT(HDRP(ptr), PACK(size,1));
        PUT(FTRP(ptr), PACK(size,1));
    }

    else if(asize >= 100)
    {
        //split block
        PUT(HDRP(ptr), PACK(remain,0));
        PUT(FTRP(ptr), PACK(remain,0));
        //put the allocated block at the end of the free block
        PUT_NOTAG(HDRP(NEXT_BLKP(ptr)), PACK(asize,1));
        PUT_NOTAG(FTRP(NEXT_BLKP(ptr)), PACK(asize,1));
        //insert the remainder free block to segregated free list
        insert_node(ptr,remain);
        return NEXT_BLKP(ptr);
    }
    //put the allocated block at the beginning of the free block
    else
    {
        //split block
        PUT(HDRP(ptr), PACK(asize,1));
        PUT(FTRP(ptr), PACK(asize,1));
        PUT_NOTAG(HDRP(NEXT_BLKP(ptr)), PACK(remain,0));
        PUT_NOTAG(FTRP(NEXT_BLKP(ptr)), PACK(remain,0));
        insert_node(NEXT_BLKP(ptr),remain);
    }
    return ptr;
}
/* 
 * mm_init - initialize the malloc package.
 */
int mm_init(void)
{
    int index;
    //initialize the segregated free list NULL
    for(index=0;index< LIST; index++)
        free_lists[index] = NULL;

    char * heap;
    //cannot allocated the heap
    if((long)(heap = mem_sbrk(4 * WSIZE)) == -1)
        return  -1;
    //padding
    PUT_NOTAG(heap, 0);
    //input the prologue header
    PUT_NOTAG(heap + 1* WSIZE, PACK(DSIZE,1));
    //prologue footer
    PUT_NOTAG(heap + 2* WSIZE, PACK(DSIZE,1));
    //epilogue header
    PUT_NOTAG(heap + 3* WSIZE, PACK(0,1));

    if(extend_heap(INITCHUNKSIZE)==NULL)
        return -1;
    return 0;
}

/* 
 * mm_malloc - Allocate a block by incrementing the brk pointer.
 *     Always allocate a block whose size is a multiple of the alignment.
 */
void *mm_malloc(size_t size)
{
    if(size==0)
        return NULL;

    size_t asize ; //adjust size
    size_t extend; //extend heap if neccessary
    void * ptr = NULL;

    //align block size
    if( size <= DSIZE)
    {
        asize = 2*DSIZE;
    }
    else
    {
        asize =ALIGN(size + DSIZE);
    }

    int index=0;
    size_t search =asize;
    //traverse the segregated free list
    while(index < LIST)
    {
        //find the appropriate free list
        if((index == LIST -1) || (search <= 1 && free_lists[index] != NULL))
        {
            ptr = free_lists[index];
            //ignore the block with reallcation bit and find the smallest different size block
            while(ptr !=NULL  && ((asize > GET_SIZE(HDRP(ptr)) || GET_TAG(ptr))))
            {
                ptr = PRED(ptr);
            }
            //can find the free block
            if(ptr != NULL)
                break;
        }
        search = search >>1;
        index ++;
    }
    //expand the heap to allocate
    if(ptr == NULL)
    {
        extend = MAX(asize,CHUNKSIZE);
        //cannot extend the heap
        ptr = extend_heap(extend);
        if(ptr == NULL)
            return NULL;
    }
    //place and divide block to the memory
    ptr = place(ptr,asize);

    //return pointer to the allocated block
    return ptr;
}

/*
 * mm_free - Freeing a block does nothing.
 */
void mm_free(void *ptr)
{
    size_t size= GET_SIZE(HDRP(ptr));
    //remove the reallcoted tag
    REMOVE_RATAG(HDRP(NEXT_BLKP(ptr)));
    //input the new info in to the block
    PUT(HDRP(ptr), PACK(size,0));
    PUT(FTRP(ptr), PACK(size,0));
    //insert the node to the segregated free list
    insert_node(ptr,size);
    //expand the free block if possible
    coalesce(ptr);

}

/*
 * mm_realloc - Implemented simply in terms of mm_malloc and mm_free
 */
void *mm_realloc(void *ptr, size_t size)
{
    if(size == 0 )
        return NULL;
    void * oldptr = ptr;
    size_t newsize =size;   //size of the new block
    int remain; //the remain size after allocation
    int extend; //size of heap extension 
    int blockbuff;

    //align block size
    if( size <= DSIZE)
    {
        newsize = 2*DSIZE;
    }
    else
    {
        newsize =ALIGN(size + DSIZE);
    }
    //add overhead requirment for block size
    newsize += REALLOC_BUFFER;
    //calculate the block buffer
    blockbuff = GET_SIZE(HDRP(ptr)) - newsize;
    //not enough space
    if(blockbuff < 0)
    {
        //check if the next block is free or the epilogue block
        if(GET_ALLOC(HDRP(NEXT_BLKP(ptr)))==0 || GET_SIZE(HDRP(NEXT_BLKP(ptr))) == 0)
        {
            //calculate the space missing
            remain = GET_SIZE(HDRP(ptr)) + GET_SIZE(HDRP(NEXT_BLKP(ptr))) - newsize;
            //not enough space
            if(remain < 0)
            {
                extend = MAX ( -remain, CHUNKSIZE);
                //can not extend the heap
                if(extend_heap(extend) == NULL)
                    return NULL;
                remain += extend;
            }

            delete_node(NEXT_BLKP(ptr));

            //do not split block, update the info of the current block
            PUT_NOTAG(HDRP(ptr), PACK(newsize + remain,1));
            PUT_NOTAG(FTRP(ptr), PACK(newsize + remain,1));

        }
        else
        {
            //enough space to allocated into the block
            oldptr = mm_malloc(newsize -DSIZE);
            memcpy( oldptr, ptr, MIN(size,newsize));
            mm_free(ptr);
        }
        blockbuff = GET_SIZE(HDRP(oldptr)) - newsize;
    }

    if(blockbuff < 2 * REALLOC_BUFFER)
    {
        SET_RATAG(HDRP(NEXT_BLKP(oldptr)));
    }
    //return the reallocation block
    return oldptr;
}
