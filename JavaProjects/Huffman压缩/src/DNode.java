import java.util.*;

public class DNode {
    //构建文件夹树的节点，用于压缩与解压文件夹
//    存储该节点代表的文件路径
    private String path;
//    0 表示文件，1 表示文件夹
    private int isDirectory;
//    存储该目录下子目录的数量
    private int childNumber;
//    存储该目录下子目录的ArrayList;
    private ArrayList<DNode> children = new ArrayList<>();
//    存储该文件的大小
    private int fileLength;

    DNode(int isDirectory, String path) {
        this.isDirectory = isDirectory;
        this.path = path;
    }

    public int getIsDirectory() {
        return isDirectory;
    }

    public void setIsDirectory(int isDirectory) {
        this.isDirectory = isDirectory;
    }

    public int getChildNumber() {
        return childNumber;
    }

    public void setChildNumber(int childNumber) {
        this.childNumber = childNumber;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<DNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<DNode> children) {
        this.children = children;
    }

    public void setFileLength(int fileLength) {
        this.fileLength = fileLength;
    }

    public int getFileLength() {
        return fileLength;
    }

}
