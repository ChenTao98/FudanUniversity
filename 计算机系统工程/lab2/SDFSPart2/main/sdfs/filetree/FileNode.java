package sdfs.filetree;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileNode extends Node implements Iterable<BlockInfo>, Serializable {
    public FileNode(){
        this.type=1;
    }

    private List<BlockInfo> blockInfoList = new ArrayList<>();

    private long fileSize; // file size should be checked when closing the file.

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getNumBlocks() {
        return blockInfoList.size();
    }

    @Override
    public Iterator<BlockInfo> iterator() {
        return blockInfoList.listIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileNode that = (FileNode) o;

        return blockInfoList.equals(that.blockInfoList);
    }

    @Override
    public int hashCode() {
        return blockInfoList.hashCode();
    }

    //    移除最后的几个block
    public void removeLastBlock(int amount) {
        for (int i = 0; i < amount; i++) {
            blockInfoList.remove(blockInfoList.size() - 1);
        }
    }

    public void addBlockInfo(BlockInfo blockInfo) {
        blockInfoList.add(blockInfo);
    }
}

