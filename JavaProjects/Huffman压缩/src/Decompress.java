import java.io.*;
import java.util.ArrayList;

public class Decompress {
    //判断解压方式，选择对应解压
    public static long decompressMain(File file) throws IOException {
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        if (inputStream.read() != 1) return decompressSingle(file);
        else return decompressDirectory(file);
    }

    //解压单个文件的第一步骤
    public static long decompressSingle(File file) throws IOException {
//        获取时间
        long startTime = System.currentTimeMillis();
//        获取文件的路径
        String path = file.getAbsolutePath();
        path = path.substring(0, path.length() - 5);
        BufferedInputStream inputStream2 = new BufferedInputStream(new FileInputStream(file));
//        判断是否为空文件
        File output = new File(path);
        if (inputStream2.read() == 0) {
            if (!output.exists()) {
                try {
                    output.createNewFile();
                } catch (IOException e) {
                }
            }
        } else {
//            判断是否为空文件夹
            if (file.length() == 0) {
                output.mkdirs();
            }

            if (!output.exists()) {
                try {
                    output.createNewFile();
                } catch (IOException e) {
                }
            }
//            解压文件
            if (file.length() > 4) {
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                decompressSingleFile(inputStream, output);
                inputStream.close();
            }
        }
//        计算并返回时间
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    //解压当个文件的第二步骤
    public static void decompressSingleFile(BufferedInputStream inputStream, File file) throws IOException {
//        读取文件头，构建Huffman树
        int[] frequency = readFileHead(inputStream);
        Node root = HuffmanTree.createHuffmanTree(frequency);
//        解压正文
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
        readAllFile(root, inputStream, outputStream);
    }

    //读取文件头
    public static int[] readFileHead(BufferedInputStream inputStream) throws IOException {

        int[] frequency = new int[256];
        int length = inputStream.read() + 1;
        int tmpName, len1, len2, len3, len4, len;
//        获取byte编码，以及权值
        for (int i = 0; i < length; i++) {
            tmpName = inputStream.read();
            len1 = inputStream.read();
            len2 = inputStream.read();
            len3 = inputStream.read();
            len4 = inputStream.read();
            len = (len1 << 24) + (len2 << 16) + (len3 << 8) + len4;
            frequency[tmpName] = len;
        }
        return frequency;
    }

    //读取正文
    public static void readAllFile(Node root, BufferedInputStream inputStream, BufferedOutputStream outputStream) throws IOException {
        int tmpByte;
        String tmpString;
        Node tmpNode = root;
//        按byte读取正文
        while ((tmpByte = inputStream.read()) != -1) {
//            将byte转为二进制字符串
            tmpString = Integer.toBinaryString(tmpByte);
            int stringLength = tmpString.length();
//            由于转为的二进制字符串未必长度为8，前面补0，还原二进制编码
            for (int i = 0; i < 8 - stringLength; i++) {
                tmpString = "0" + tmpString;
            }
//            按编码写入解压文件
            for (int i = 0; i < 8; i++) {
                if (tmpString.charAt(i) == '0') {
                    tmpNode = tmpNode.getLeft();
                } else tmpNode = tmpNode.getRight();
                if (tmpNode.isLeaf()) {
                    outputStream.write(tmpNode.getName());
                    tmpNode = root;
                }
            }
        }
        outputStream.close();
    }

    //解压文件夹第一步骤
    public static long decompressDirectory(File file) throws IOException {
        long startTime = System.currentTimeMillis();
//        获取路径构建文件夹
        String path = file.getAbsolutePath();
        path = path.substring(0, path.length() - 5);
        File output = new File(path);
        output.mkdirs();
//        解压文件夹
        if (file.length() > 0) {
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            decompressDirectoryFile(inputStream, output);
            inputStream.close();
        }

        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    //解压文件夹第二步骤
    public static void decompressDirectoryFile(BufferedInputStream inputStream, File output) throws IOException {
//        获取文件夹节点，构建文件夹树
        DNode root = createDirectoryTreeRoot(inputStream,output);
        createDirectoryTree(inputStream, root,output.getAbsolutePath());

//        构建文件夹文件
        createDirectory(root);
//        解压文件
        rewriteFile(inputStream, output);
        inputStream.close();

    }

    //获取文件夹一级节点
    public static DNode createDirectoryTreeRoot(BufferedInputStream inputStream,File file) throws IOException {
//        获取文件一级节点信息
        int isDirectory = inputStream.read();
        int childNumber = inputStream.read();
        int pathLength = inputStream.read();
        byte[] strPath = new byte[pathLength];
        inputStream.read(strPath);
//        构建节点
        DNode root = new DNode(isDirectory, file.getAbsolutePath());
        root.setChildNumber(childNumber);
        return root;
    }

    //获取二级以及更深的目录构建文件夹树
    public static void createDirectoryTree(BufferedInputStream inputStream, DNode root,String path) throws IOException {
        int childNum = 0;
        int tmpType;
        DNode newTmpNode;
        int pathLength;
//        获取当前文件夹子目录数量
        int child = root.getChildNumber();
//        循环读取子目录
        while (childNum < child) {
            tmpType = inputStream.read();
            if (tmpType == 0) {
                pathLength = inputStream.read();
                byte[] strPath = new byte[pathLength];
                inputStream.read(strPath);
                newTmpNode = new DNode(0, path+"\\\\"+new String(strPath));
                root.getChildren().add(newTmpNode);
            } else {
//                若该文件为文件夹，读取信息，并递归读取该文件夹子目录
                int childNumber = inputStream.read();
                pathLength = inputStream.read();
                byte[] strPath = new byte[pathLength];
                inputStream.read(strPath);
                newTmpNode = new DNode(1, path+"\\\\"+new String(strPath));
                newTmpNode.setChildNumber(childNumber);
                root.getChildren().add(newTmpNode);
                createDirectoryTree(inputStream, newTmpNode,path+"\\\\"+new String(strPath));
            }
            childNum++;
        }
    }

    //根据文件夹树构建文件夹目录
    public static void createDirectory(DNode tmpNode) throws IOException {
        int isDirectory = tmpNode.getIsDirectory();
        String path = tmpNode.getPath();
        File newFile = new File(path);
//        判断是否文件夹，如果是，创建文件夹，并递归创建子目录；如果不是文件夹，创建文件
        if (isDirectory == 1) {
            newFile.mkdirs();
            ArrayList<DNode> children = tmpNode.getChildren();
            for (int i = 0; i < children.size(); i++) {
                DNode newTmpNode = children.get(i);
                createDirectory(newTmpNode);
            }
        } else {
            newFile.createNewFile();
        }
    }

    //解压文件夹正文
    public static void rewriteFile(BufferedInputStream inputStream, File file) throws IOException {
//        递归读取并解压文件
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            int length = files.length;
            for (int i = 0; i < length; i++) {
                rewriteFile(inputStream, files[i]);
            }
        } else {
//            解压文件夹单个文件
            decompressDirectorySingleFile(inputStream, file);
        }
    }

    //解压文件夹单个文件第一步骤
    public static void decompressDirectorySingleFile(BufferedInputStream inputStream, File file) throws IOException {
//        读取文件长度
        int len1, len2, len3, len4, fileLength;
        len1 = inputStream.read();
        len2 = inputStream.read();
        len3 = inputStream.read();
        len4 = inputStream.read();
        fileLength = (len1 << 24) + (len2 << 16) + (len3 << 8) + len4;
//        读取文件头，构建Huffman树
        int[] frequency = readFileHead(inputStream);
        Node root = HuffmanTree.createHuffmanTree(frequency);
//        解压该文件正文
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
        readDirectoryAllFile(root, inputStream, outputStream, fileLength);
    }

    //解压文件夹第二步骤
    public static void readDirectoryAllFile(Node root, BufferedInputStream inputStream, BufferedOutputStream outputStream, int fileLength) throws IOException {
        int tmpByte;
        String tmpString;
        Node tmpNode = root;
        int count = 0;
//        判断是否写入文件的长度是否与文件原长度相同，或者是否读完压缩文件
        while ((count < fileLength) && ((tmpByte = inputStream.read()) != -1)) {
//            获取byte转为二进制字符串
            tmpString = Integer.toBinaryString(tmpByte);
            int stringLength = tmpString.length();
            for (int i = 0; i < 8 - stringLength; i++) {
                tmpString = "0" + tmpString;
            }
            for (int i = 0; i < 8; i++) {
                if (tmpString.charAt(i) == '0') {
                    tmpNode = tmpNode.getLeft();
                } else tmpNode = tmpNode.getRight();
                if (tmpNode.isLeaf()) {
//                    写入一个byte，count加1
                    outputStream.write(tmpNode.getName());
                    tmpNode = root;
                    count++;
                }
            }
        }
        outputStream.close();
    }
}