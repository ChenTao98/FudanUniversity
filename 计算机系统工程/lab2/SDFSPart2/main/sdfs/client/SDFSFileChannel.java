package sdfs.client;

import sdfs.filetree.BlockInfo;
import sdfs.filetree.FileNode;
import sdfs.filetree.LocatedBlock;
import sdfs.namenode.NameNode;

import java.io.Flushable;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.SeekableByteChannel;
import java.util.Iterator;
import java.util.UUID;

public class SDFSFileChannel implements SeekableByteChannel, Flushable,Serializable {

    private final UUID uuid; // File uuid

    private  FileNode fileNode;
    private final boolean isReadOnly;
    private boolean isOpen;
    private long position;
    private DataNodeStub dataNodeStub=new DataNodeStub();
    private NameNodeStub nameNodeStub=new NameNodeStub();
    public SDFSFileChannel(UUID uuid, FileNode fileNode, boolean isReadOnly) {
        this.uuid = uuid;
        this.fileNode = fileNode;
        this.isReadOnly = isReadOnly;
        // TODO your code here
        isOpen = true;
        position = 0;
    }

    public int read(ByteBuffer dst) throws IOException {
        // TODO your code here
//        读取之前，判断是否是打开的的或者是否可读
        if (!isOpen) {
            throw new IOException("通道未打开,无法读取文件");
        }
        if ((null == dst) || (fileNode.getFileSize() <= position)) {
            return -1;
        }
//        初始化一些变量
        Iterator<BlockInfo> iteratorBlockInfo = fileNode.iterator();
        int bufferSize = dst.limit() - dst.position();//缓存流存储的大小
        int remainingTotalSize = (int) (fileNode.getFileSize() - position);//文件剩下的大小
        int readSize = remainingTotalSize < bufferSize ? remainingTotalSize : bufferSize;//判断读取的大小，缓存流与文件剩余取小者
        int leftToRead = readSize;//剩下的需要读取的大小，初始等于需要读取的总数，每次读取之后减小
        int blockIndex = (int) (position / dataNodeStub.BLOCK_SIZE);//本次读取的第一个块的下标
        int index = 0;//文件的index个块
//        开始读取操作
        while (iteratorBlockInfo.hasNext()) {
//            获取文件的块
            Iterator<LocatedBlock> iteratorLocatedBlock = iteratorBlockInfo.next().iterator();
            LocatedBlock locatedBlock = iteratorLocatedBlock.next();
//            判断当前获取块是否与需要读取的块相同，相同则进入读取
            if (blockIndex == index) {
                int offset = (int) (position % dataNodeStub.BLOCK_SIZE);//当前块的偏移量
                int blockRemainSize = dataNodeStub.BLOCK_SIZE - offset;//当前块剩余可读字节
                if (blockRemainSize < leftToRead) {
//                    当前剩余可读字节小于剩余需要读取的字节时，读取当前块剩余全部字节
                    byte[] bytes = dataNodeStub.read(uuid, locatedBlock.getBlockNumber(), offset, blockRemainSize);
                    dst.put(bytes);
//                    更新position，更新剩余需要读取字节，
                    position += blockRemainSize;
                    leftToRead -= blockRemainSize;
//                    更新需要读取的块下标
                    blockIndex++;
                } else {
//                    当剩余可读字节大于需要读取的字节时，读取字节数为需要读取的字节
                    byte[] bytes = dataNodeStub.read(uuid, locatedBlock.getBlockNumber(), offset, leftToRead);
                    dst.put(bytes);
                    position += leftToRead;
//                    由于需要读取字节读取完毕，直接跳出循环
                    break;
                }
            }
            if (leftToRead <= 0) {
                break;
            }
            index++;
        }
        return readSize;
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        // TODO your code here
//        判断是否是符合写入规范
        if (isReadOnly) {
            throw new IOException("该通道只读，不能写入");
        }
        if (null == src) {
            return -1;
        }
//        初始化变量
        int leftToWrite = src.limit() - src.position();//剩余需要写入的字节数
        int writeSize = leftToWrite;//写入的字节数，返回值
        int fileSize = (int) (position + leftToWrite);//写入完成之后的大小
//        判断写入完成之后文件需要的block数量，并移除或添加block
        int needBlockNumber = fileSize % dataNodeStub.BLOCK_SIZE == 0 ? fileSize / dataNodeStub.BLOCK_SIZE : fileSize / dataNodeStub.BLOCK_SIZE + 1;
        if (needBlockNumber > fileNode.getNumBlocks()) {
            nameNodeStub.addBlocks(uuid, needBlockNumber - fileNode.getNumBlocks());
        } else if (needBlockNumber < fileNode.getNumBlocks()) {
            nameNodeStub.removeLastBlocks(uuid, fileNode.getNumBlocks() - needBlockNumber);
        }
        this.fileNode=nameNodeStub.getReadwriteFile(uuid).getFileNode();
        Iterator<BlockInfo> iteratorBlockInfo = fileNode.iterator();
        int blockIndex = (int) (position / dataNodeStub.BLOCK_SIZE);//本次写入第一次在的块下标
        int index = 0;
        while (iteratorBlockInfo.hasNext()) {
//            获取当前块
            Iterator<LocatedBlock> iteratorLocatedBlock = iteratorBlockInfo.next().iterator();
            LocatedBlock locatedBlock = iteratorLocatedBlock.next();
//            判断当前块是否是需要写入的块
            if (blockIndex == index) {
                int offset = (int) (position % dataNodeStub.BLOCK_SIZE);//当前块写入位置偏移
                int blockRemainSize = dataNodeStub.BLOCK_SIZE - offset;//当前块剩余可写入的大小
                if (blockRemainSize < leftToWrite) {
//                    当前块剩余可写入大小 小于 剩余需要写入的字节数时，将当前块写满
                    byte[] bytes = new byte[blockRemainSize];
                    src.get(bytes);
                    dataNodeStub.write(uuid, locatedBlock.getBlockNumber(), offset, bytes);
//                    更新位置的变量
                    position += blockRemainSize;
                    leftToWrite -= blockRemainSize;
                    blockIndex++;
                } else {
//                    当前块剩余可写入大小 大于 剩余需要写入的字节数时，将剩余需要写入数据全部写入
                    byte[] bytes = new byte[leftToWrite];
                    src.get(bytes);
                    dataNodeStub.write(uuid, locatedBlock.getBlockNumber(), offset, bytes);
                    position += leftToWrite;
//                    写入完成，退出
                    break;
                }
            }
            if (leftToWrite <= 0) {
                break;
            }
            index++;
        }
//        更新文件大小
        this.fileNode=nameNodeStub.getReadwriteFile(uuid).getFileNode();
        setFileSize(fileSize);
        nameNodeStub.updateChannel(fileNode,position,uuid);
        return writeSize;
    }

    @Override
    public long position() throws IOException {
        // TODO your code here
//        不是打开通道时，抛出异常
        if (!isOpen) {
            throw new ClosedChannelException();
        }
        return position;
    }

    @Override
    public SeekableByteChannel position(long newPosition) throws IOException {
        // TODO your code here
//        判断是否符合规范
        if (!isOpen) {
            throw new ClosedChannelException();
        }
        if (newPosition < 0) {
            throw new IllegalArgumentException("position不能小于0");
        }
        position = newPosition;
        return this;
    }

    @Override
    public long size() throws IOException {
        // TODO your code here
        if (!isOpen) {
            throw new ClosedChannelException();
        }
        return fileNode.getFileSize();
    }

    @Override
    public SeekableByteChannel truncate(long size) throws IOException {
        // TODO your code here
//        处理不符合规范情况
        if (!isOpen) {
            throw new ClosedChannelException();
        }
        if (isReadOnly) {
            throw new NonWritableChannelException();
        }
        if (size < 0) {
            throw new IllegalArgumentException();
        }
//        当文件大小大于截断大小时截断，反之不必要处理
        if (fileNode.getFileSize() > size) {
            int needBlockNumber = size % dataNodeStub.BLOCK_SIZE == 0 ? (int) (size / dataNodeStub.BLOCK_SIZE) : (int) (size / dataNodeStub.BLOCK_SIZE + 1);
            nameNodeStub.removeLastBlocks(uuid, fileNode.getNumBlocks() - needBlockNumber);
            this.fileNode=nameNodeStub.getReadwriteFile(uuid).getFileNode();
            setFileSize(size);
            nameNodeStub.updateChannel(fileNode,position,uuid);
        }

//        更新位置
        if (position > size) {
            position = size;
            nameNodeStub.updateChannel(fileNode,position,uuid);
        }
        return this;
    }

    @Override
    public boolean isOpen() {
        // TODO your code here
        return isOpen;
    }

    @Override
    public void close() {
        // TODO your code here
        if (isOpen) {
            isOpen = false;
            flush();
//            关闭时判断类型，选择合适的关闭
            if (isReadOnly) {
                nameNodeStub.closeReadonlyFile(uuid);
            } else {
                try {
                    nameNodeStub.closeReadwriteFile(uuid, (int) position);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void flush() {
        // TODO your code here
        fileNode.serialize(NameNode.NAMENODE_DATA_DIR + fileNode.getNodeId() + ".node");
    }
    /**
     * Is the file channel read-only?
     *
     * @return true if it is
     */
    public boolean isReadOnly() {
        return isReadOnly;
    }

    /**
     * Get UUID of the file channel
     *
     * @return uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Get the amount of blocks in this file
     *
     * @return the amount of the blocks
     */
    public int getNumBlocks() {
        return fileNode.getNumBlocks();
    }

    /**
     * Set the size of this file channel
     *
     * @param fileSize new size
     */
    public void setFileSize(long fileSize) {
        this.fileNode.setFileSize(fileSize);
    }
    public FileNode getFileNode() {
        return fileNode;
    }
}
