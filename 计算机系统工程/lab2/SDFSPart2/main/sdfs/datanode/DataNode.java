package sdfs.datanode;


import sdfs.socketUtil.SocketUtils;

import java.io.*;
import java.util.UUID;

public class DataNode implements IDataNode {
    private static String suffixBlock = ".block";
    public static int port = 8000;//监听端口号

    public DataNode() {
        // TODO, your code here
    }

    /* listening requests from client */
    public void listenRequest() {
//        每调用一次监听函数，端口就会加一，并且创立监听连接，
//        端口加一是为了防止端口重用的问题
        ++port;
        SocketUtils.listen(port);
    }

    @Override
    public byte[] read(UUID fileUuid, int blockNumber, int offset, int size) throws IndexOutOfBoundsException, FileNotFoundException {
        // TODO your code here
//        当偏移位置小于0或者读取末尾超过块大小时，抛出异常
        if ((offset < 0) || (offset + size > BLOCK_SIZE)) {
            throw new IndexOutOfBoundsException();
        }
//        文件不存在时，抛出异常
        File file = new File(DATANODE_DATA_DIR + blockNumber + suffixBlock);
        if ((!file.exists()) || (!file.isFile())) {
            throw new FileNotFoundException("文件" + blockNumber + suffixBlock + "不存在");
        }
//        读取内容
        byte[] bytes = new byte[size];
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        try {
            bufferedInputStream.read(bytes, offset, size);
            bufferedInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    @Override
    public void write(UUID fileUuid, int blockNumber, int offset, byte[] b) throws IndexOutOfBoundsException {
        // TODO your code here
//        当偏移位置小于0或者写入末尾超过块大小时，抛出异常
        if ((offset < 0) || (offset + b.length > BLOCK_SIZE)) {
            throw new IndexOutOfBoundsException();
        }
//        写入文件
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(DATANODE_DATA_DIR + blockNumber + suffixBlock)));
            bufferedOutputStream.write(b, offset, b.length);
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
