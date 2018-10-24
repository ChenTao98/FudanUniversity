import java.io.*;
import java.util.*;

public class Compress {
    private static long fileInitialLength=0;
    private static long fileCompressLength=0;
    //判断压缩方式，选择对应压缩
    public static long compressMain(File file) throws IOException {
        fileInitialLength=0;
        fileCompressLength=0;
        if (file.isDirectory()) return compressDirectory(file);
        else return compressSingle(file, 1);
    }

    //压缩单个文件的第一步骤
    public static long compressSingle(File file, int flush) throws IOException {
        // 获取时间
        long startTime = System.currentTimeMillis();
        fileInitialLength=file.length();
        //判断是否空文件
        if (file.length() != 0) {
            String path = file.getAbsolutePath();
            path += ".huff";
            File output = new File(path);
            if (!output.exists()) {
                try {
                    output.createNewFile();
                } catch (IOException e) {
                }
            }
//            构建输出流，压缩文件
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(output));
            compressSingleFile(file, outputStream);
            if (flush == 1) outputStream.close();
            fileCompressLength=output.length();
        } else {
//            空文件直接压缩，写入0，用于解压判断。
            String path = file.getAbsolutePath();
            path += ".huff";
            File output = new File(path);
            if (!output.exists()) {
                try {
                    output.createNewFile();
                } catch (IOException e) {
                }
            }

            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(output));
            outputStream.write(0);
            outputStream.close();
            fileCompressLength=output.length();
        }
//计算时间
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    //压缩单个文件第二步骤
    public static void compressSingleFile(File file, BufferedOutputStream outputStream) throws IOException {
//        读取文件，计算字符，获取权值
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
        int[] frequency = new int[256];
        String[] huffmanCodeTable = new String[256];
        try {
            int tmp;
            while ((tmp = input.read()) != -1) {
                frequency[tmp]++;
            }
            input.close();
        } catch (IOException e) {
        }
//        构建Huffman树以及Huffman编码
        Node root = HuffmanTree.createHuffmanTree(frequency);
        HuffmanTree.createHuffmanCode(root, "", huffmanCodeTable);
//        压缩文件
        writeFileHead(outputStream, frequency);
        rewriteFile(file, outputStream, huffmanCodeTable);
    }

    //写入文件头
    public static void writeFileHead(BufferedOutputStream outputStream, int[] frequency) throws IOException {
//        写入字符种类数
        int count = 0;
        int length = frequency.length;
        for (int i = 0; i < length; i++) {
            if (frequency[i] != 0)
                count++;
        }
        outputStream.write(count - 1);
//        写入字符权值
        for (int i = 0; i < length; i++) {
            int tmp = frequency[i];
            if (tmp != 0) {
                outputStream.write(i);
//                移位操作，由于权值较大，需要分成4个byte写入
                outputStream.write((tmp >> 24) & 0xff);
                outputStream.write((tmp >> 16) & 0xff);
                outputStream.write((tmp >> 8) & 0xff);
                outputStream.write((tmp) & 0xff);
            }
        }
    }

    //写入正文
    public static void rewriteFile(File file, BufferedOutputStream outputStream, String[] huffmanCodeTable) throws IOException {
        StringBuilder compressedFile = new StringBuilder("");
        BufferedInputStream input0 = new BufferedInputStream(new FileInputStream(file));
        int tmpByte;
        String tmpString;
//        一个一个读取byte，转为Huffman编码
        while ((tmpByte = input0.read()) != -1) {
//            添加Huffman编码至字符串
            compressedFile.append(huffmanCodeTable[tmpByte]);
//            判断编码是否长度大于8，每次截取前前8个编码转为一个byte写入。
            if (compressedFile.length() > 8) {
                int i;
                for (i = 0; i < compressedFile.length() - 8; i += 8) {
                    outputStream.write(Integer.parseInt(compressedFile.substring(i, i + 8), 2));
                }
                compressedFile.delete(0, i);
            }
        }
//        处理最后的byte，如果不满8位，补0。
        tmpString = compressedFile.substring(0);
        int len = tmpString.length();
        for (int i = 0; i < 8 - len; i++) {
            compressedFile.append('0');
        }
        outputStream.write(Integer.parseInt(compressedFile.toString(), 2));
//        关闭输入流
        input0.close();
    }

    //压缩文件夹第一步骤
    public static long compressDirectory(File file) throws IOException {
        long startTime = System.currentTimeMillis();
//        判断是否空文件夹
        if (file.listFiles().length != 0) {
            String path = file.getAbsolutePath();
            path += ".huff";

            File output = new File(path);
            if (!output.exists()) {
                try {
                    output.createNewFile();
                } catch (IOException e) {
                }
            }
//            压缩文件夹
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(output));
            compressDirectoryFile(file, outputStream);
            fileCompressLength=output.length();
        } else {
//            空文件夹，直接压缩
            String path = file.getAbsolutePath();
            path += ".huff";
            fileInitialLength=0;
            File output = new File(path);
            if (!output.exists()) {
                try {
                    output.createNewFile();
                } catch (IOException e) {
                }
            }
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    //压缩文件夹第二步骤
    public static void compressDirectoryFile(File file, BufferedOutputStream outputStream) throws IOException {
//        构建文件夹节点
        DNode root = createDirectoryTree(file);
//        写入文件头
        writeDirectoryFileHead(outputStream, root);
//        压缩文件夹
        writeDirectoryFile(file, outputStream);
        outputStream.close();
    }

    //写入文件夹头
    public static void writeDirectoryFileHead(BufferedOutputStream outputStream, DNode root) throws IOException {
//        文件信息，判断为文件夹压缩
        outputStream.write(1);
//        写入该文件夹下目录数量以及路径
        outputStream.write(root.getChildren().size());
        outputStream.write(root.getPath().getBytes().length);
        outputStream.write(root.getPath().getBytes());
        ArrayList<DNode> children = root.getChildren();
//        循环递归写入文件头信息
        int length = children.size();
        for (int i = 0; i < length; i++) {
            DNode tmpNode = children.get(i);
            if (tmpNode.getIsDirectory() == 1) {
                writeDirectoryFileHead(outputStream, tmpNode);
            } else {
                outputStream.write(0);
                outputStream.write(tmpNode.getPath().getBytes().length);
                outputStream.write(tmpNode.getPath().getBytes());
            }
        }
    }

    //压缩文件夹正文
    public static void writeDirectoryFile(File file, BufferedOutputStream outputStream) throws IOException {
//        判断是否为文件夹，如果是，递归读取文件夹下的文件
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                writeDirectoryFile(listFiles[i], outputStream);
            }
        } else {
//            如果为文件，写入文件长度，并压缩文件正文
            int fileLength = (int) file.length();
            fileInitialLength+=file.length();
            outputStream.write((fileLength >> 24) & 0xff);
            outputStream.write((fileLength >> 16) & 0xff);
            outputStream.write((fileLength >> 8) & 0xff);
            outputStream.write((fileLength) & 0xff);
            compressSingleFile(file, outputStream);
        }
    }

    // 构建文件夹节点
    public static DNode createDirectoryTree(File file) {
//        构建根节点。并获取文件夹节点的树
        String[] filePath=(file.getAbsolutePath()).split("\\\\");
        DNode root = new DNode(1,filePath[filePath.length-1]);
        createDirectory(file, root);
        return root;
    }

    //构建文件节点树
    public static void createDirectory(File file, DNode curNode) {
        File[] files = file.listFiles();
        DNode tmpDNode;
        int length = files.length;
        for (int i = 0; i < length; i++) {
            if (files[i].isDirectory()) {
                String[] filePath=(files[i].getAbsolutePath()).split("\\\\");
                tmpDNode = new DNode(1, filePath[filePath.length-1]);
                curNode.getChildren().add(tmpDNode);
                createDirectory(files[i], tmpDNode);
            } else {
                String[] filePath=(files[i].getAbsolutePath()).split("\\\\");
                tmpDNode = new DNode(0, filePath[filePath.length-1]);
                curNode.getChildren().add(tmpDNode);
            }
        }
    }
    public long getFileInitialLength(){
        return fileInitialLength;
    }
    public long getFileCompressLength(){
        return fileCompressLength;
    }
}
