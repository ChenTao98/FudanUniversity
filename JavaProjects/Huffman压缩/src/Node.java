public class Node implements Comparable<Node> {
    //创建Huffman树的节点
//    存储该节点代表的byte的字节编码
    private int name = 0;
//    存储权值
    private int frequency = 0;
//    关联节点
    private Node parent, left, right;
//    存储该节点代表的byte的 Huffman 编码
    private String binaryCode;

    public Node(int name) {
        this.name = name;
        frequency++;
    }

    public Node(int name, int frequency) {
        this.name = name;
        this.frequency = frequency;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public String getBinaryCode() {
        return binaryCode;
    }

    public void setBinaryCode(String binaryCode) {
        this.binaryCode = binaryCode;
    }

    public boolean isLeaf() {
        return (left == null && right == null);
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeftChild() {
        return (!isRoot() && this == parent.getLeft());
    }

    public boolean isRightChild() {
        return (!isRoot() && this == parent.getRight());
    }

    public int compareTo(Node node) {
        if (this.getFrequency() > node.getFrequency()) return 1;
        else if (this.getFrequency() < node.getFrequency()) return -1;
        else return 0;
    }
}

