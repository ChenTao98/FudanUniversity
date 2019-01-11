package sdfs.filetree;

import java.io.*;

public abstract class Node implements Serializable {
    // TODO your code here
    int type;// 0 为DIR, 1 为File
    private int nodeId;

    Node() {
        this(0, 0);
    }

    private Node(int type, int nodeId) {
        this.type = type;
        this.nodeId = nodeId;
    }

    public void serialize(String fileUri) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileUri));
            oos.writeObject(this);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Node deserialize(String fileUri) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileUri));
            Node node = (Node) ois.readObject();
            ois.close();
            return node;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getNodeId() {
        return nodeId;
    }

    public int getType() {
        return type;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }
}