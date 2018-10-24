import java.util.*;

public class HuffmanTree {
    //构建Huffman树，并返回root
    public static Node createHuffmanTree(int[] frequency) {
//        创建优先队列
        PriorityQueue<Node> nodeList = new PriorityQueue<>(256);
        int length = frequency.length;
//        获取权值不为0的字符创建节点，并加入优先队列
        for (int i = 0; i < length; i++) {
            if (frequency[i] != 0) {
                Node node = new Node(i, frequency[i]);
                nodeList.add(node);
            }
        }
//        当只有一个字符时直接返回
        if (nodeList.size() == 1) {
            Node root = new Node(-1, frequency[0]);
            Node onlyNode = nodeList.poll();
            root.setLeft(onlyNode);
            onlyNode.setParent(root);
            return root;
        }
//        构建Huffman树
        Node node1, node2, tmpNode;
        int frequency1, frequency2, tmpFrequency;
        while (nodeList.size() > 1) {
            node1 = nodeList.poll();
            node2 = nodeList.poll();
            frequency1 = node1.getFrequency();
            frequency2 = node2.getFrequency();
            tmpFrequency = frequency1 + frequency2;
            tmpNode = new Node(-1, tmpFrequency);
            tmpNode.setLeft(node1);
            tmpNode.setRight(node2);
            node1.setParent(tmpNode);
            node2.setParent(tmpNode);
            nodeList.add(tmpNode);
        }
        return nodeList.poll();
    }

    //创建Huffman编码
    public static void createHuffmanCode(Node tmp, String huffmanCode, String[] huffmanCodeTable) {
        if (tmp == null) return;
        if (tmp.isLeaf()) {
            tmp.setBinaryCode(huffmanCode);
            huffmanCodeTable[tmp.getName()] = huffmanCode;
            return;
        }
        createHuffmanCode(tmp.getLeft(), huffmanCode + "0", huffmanCodeTable);
        createHuffmanCode(tmp.getRight(), huffmanCode + "1", huffmanCodeTable);
    }
}