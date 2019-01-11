package sdfs.filetree;

import java.io.Serializable;

public abstract class Node implements Serializable {
    // TODO your code here
    int type;// 0 为DIR, 1 为File
    int nodeId;
    String name;

    public Node() {
        this(0, 0, "name");
    }

    public Node(int type, int nodeId, String name) {
        this.type = type;
        this.nodeId = nodeId;
        this.name = name;
    }

    public abstract void serialize(String fileUri);

    public abstract Node deserialize(String fileUri);

    public int getNodeId() {
        return nodeId;
    }

    public int getType() {
        return type;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }
}