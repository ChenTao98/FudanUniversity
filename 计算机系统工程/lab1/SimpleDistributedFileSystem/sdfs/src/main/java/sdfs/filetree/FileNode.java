package sdfs.filetree;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public void serialize(String fileUri) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileUri));
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Node deserialize(String fileUri) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileUri));
            return (Node) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //序列化自定义
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(type);
        out.writeInt(nodeId);
        out.writeObject(name);
        out.writeLong(fileSize);
        BlockInfo[] blockInfoToWrite = new BlockInfo[blockInfoList.size()];
        blockInfoList.toArray(blockInfoToWrite);
        out.writeObject(blockInfoToWrite);
    }

    //反序列化自定义
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        type = in.readInt();
        nodeId = in.readInt();
        name = (String) in.readObject();
        fileSize = in.readLong();
        BlockInfo[] blockInfoToRead = (BlockInfo[]) in.readObject();
        blockInfoList = new ArrayList<>();
        blockInfoList.addAll(Arrays.asList(blockInfoToRead));
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

