package sdfs.filetree;

import sdfs.namenode.NameNode;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static sdfs.utils.sdfsUtils.getPathArray;

public class DirNode extends Node implements Iterable<Entry> {
    public DirNode(){
        this.type=0;
    }

    private Set<Entry> entries = new HashSet<>();

    @Override
    public Iterator<Entry> iterator() {
        return entries.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DirNode that = (DirNode) o;

        return entries.equals(that.entries);
    }

    @Override
    public int hashCode() {
        return entries.hashCode();
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

    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    //    根据传入的路径寻找对应的node
    public Node findNode(String fileUri) {
//        判断是否是根目录
        if (fileUri.equals("/")) {
            return NameNode.getInstance().getRoot();
        }
        DirNode tempRoot = NameNode.getInstance().getRoot();
//        获取分割后的路径
        String[] path = getPathArray(fileUri);
//        先获取需要寻找的node的父节点
        for (int i = 0; i < path.length - 1; i++) {
            if (null == tempRoot) {
                return null;
            } else {
                Node node = tempRoot.getNode(path[i]);

//                if (node == null || node.getType() == 1) {
//                    return null;
//                } else {
//                    tempRoot = (DirNode) node;
//                }
//              如果寻找不到或者对应节点为文件，则表明无法找到该节点
                if (null == node) {
                    return null;
                } else if (node.getType() == 1) {
                    return new DirNode();
                } else {
//                    找到该文件夹，继续寻找
                    tempRoot = (DirNode) node;
                }
            }
        }
//        获取父节点下的同名文件
        if (tempRoot != null) {
            return tempRoot.getNode(path[path.length - 1]);
        }
        return null;
    }

    //根据传入的名字，在当前目录下寻找对应文件
    private Node getNode(String name) {
        for (Entry entry : entries) {
            if (entry.getName().equals(name)) {
                return deserialize(NameNode.NAMENODE_DATA_DIR + entry.getNode().getNodeId() + ".node");
            }
        }
        return null;
    }

    //序列化自定义
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(type);
        out.writeInt(nodeId);
        out.writeObject(name);
        Entry[] entriesToWrite = new Entry[entries.size()];
        entries.toArray(entriesToWrite);
        out.writeObject(entriesToWrite);
    }

    //反序列化自定义
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        type = in.readInt();
        nodeId = in.readInt();
        name = (String) in.readObject();
        Entry[] entriesToRead = (Entry[]) in.readObject();
        entries = new HashSet<>();
        entries.addAll(Arrays.asList(entriesToRead));
    }
}
