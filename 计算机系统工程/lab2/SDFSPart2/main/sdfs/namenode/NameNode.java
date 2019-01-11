package sdfs.namenode;

import sdfs.client.SDFSFileChannel;
import sdfs.filetree.*;
import sdfs.socketUtil.SocketUtils;
import sdfs.utils.SDFSUtils;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NameNode implements INameNode {
    public static int port = 6000;//监听端口号
    private DirNode root;
    private static int nodeNumber = 0;//node的数量
    private static int blockNumber = 0;//block的数量
    private static String suffixNode = ".node";
    private static ArrayList<SDFSFileChannel> channelArrayList = new ArrayList<>();//用于存储打开的通道

    public NameNode() {
        // TODO, your code here
        File file = new File(NAMENODE_DATA_DIR + "0.node");
        if (file.exists()) {
            root = SDFSUtils.getRoot();
        } else {
            root = new DirNode();
            root.serialize(NAMENODE_DATA_DIR + "0.node");
        }
    }

    /* listening requests from client */
    public void listenRequest() {
//        每调用一次监听函数，端口就会加一，并且创立监听连接，
//        端口加一是为了防止端口重用的问题
        ++port;
        SocketUtils.listen(port);
    }


    @Override
    public SDFSFileChannel openReadonly(String fileUri) throws InvalidPathException, FileNotFoundException {
        // TODO your code here
//        判断是否是合法文件路径
        if (!SDFSUtils.isValidFile(fileUri)) {
            throw new InvalidPathException(fileUri, "路径不合法");
        }
//        判断是否可以寻找到文件
        Node node = root.findNode(fileUri);
        if ((null == node) || (node.getType() == 0)) {
            throw new FileNotFoundException("文件不存在");
        }
//        打开通道,存储并返回
        FileNode fileNode = (FileNode) node;
        SDFSFileChannel sdfsFileChannel = new SDFSFileChannel(UUID.randomUUID(), fileNode, true);
        channelArrayList.add(sdfsFileChannel);
        return sdfsFileChannel;
    }

    @Override
    public SDFSFileChannel openReadwrite(String fileUri) throws InvalidPathException, FileNotFoundException, IllegalStateException {
        // TODO your code here
//        判断是否是合法路径
        if (!SDFSUtils.isValidFile(fileUri)) {
            throw new InvalidPathException(fileUri, "路径不合法");
        }
//        判断文件是否存在
        Node node = root.findNode(fileUri);
        if ((null == node) || (node.getType() == 0)) {
            throw new FileNotFoundException("文件不存在");
        }
//        判断是否已经以读写方式打开
        FileNode fileNode = (FileNode) node;
        for (SDFSFileChannel sdfsFileChannel : channelArrayList) {
            if ((fileNode.getNodeId() == sdfsFileChannel.getFileNode().getNodeId()) && (!sdfsFileChannel.isReadOnly())) {
                throw new IllegalStateException("打开读写文件两次");
            }
        }
//        创建通道并返回
        SDFSFileChannel sdfsFileChannel = new SDFSFileChannel(UUID.randomUUID(), fileNode, false);
        channelArrayList.add(sdfsFileChannel);
        return sdfsFileChannel;
    }

    @Override
    public SDFSFileChannel create(String fileUri) throws FileAlreadyExistsException, InvalidPathException {
        // TODO your code here
//        判断是否是合法路径
        if (!SDFSUtils.isValidFile(fileUri)) {
            throw new InvalidPathException(fileUri, "路径不合法");
        }
//        判断文件是否存在
        if (root.findNode(fileUri) != null) {
            throw new FileAlreadyExistsException("文件已存在");
        }
        String nodeName = SDFSUtils.getLastName(fileUri);//需要创建的文件的node名
        String parentPath = fileUri.substring(0, fileUri.lastIndexOf("/") + 1);//获取父级路径
//        获取父节点
        DirNode parentNode = (DirNode) root.findNode(parentPath);
        if (parentNode == null) {
//            如果父节点不存在，那么就创建父文件夹
            mkdir(parentPath);
            parentNode = (DirNode) root.findNode(parentPath);
        }
//        创建节点，并加入父节点的链表
        FileNode fileNode = new FileNode();
        fileNode.setNodeId(++nodeNumber);
//        写入文件树
        fileNode.serialize(NAMENODE_DATA_DIR + fileNode.getNodeId() + suffixNode);
        parentNode.addEntry(new Entry(nodeName, fileNode));
        parentNode.serialize(NAMENODE_DATA_DIR + parentNode.getNodeId() + suffixNode);
//        创建并返回通道
        SDFSFileChannel sdfsFileChannel = new SDFSFileChannel(UUID.randomUUID(), fileNode, false);
        channelArrayList.add(sdfsFileChannel);
        return sdfsFileChannel;
    }

    @Override
    public SDFSFileChannel getReadonlyFile(UUID fileUuid) throws IllegalStateException {
        // TODO your code here
//        获取通道，判断异常
        SDFSFileChannel sdfsFileChannel = getChannelByUUID(fileUuid);
        if ((null == sdfsFileChannel) || (!sdfsFileChannel.isReadOnly())) {
            throw new IllegalStateException();
        }
        return sdfsFileChannel;
    }

    @Override
    public SDFSFileChannel getReadwriteFile(UUID fileUuid) throws IllegalStateException {
        // TODO your code here
//        获取通道，判断异常
        SDFSFileChannel sdfsFileChannel = getChannelByUUID(fileUuid);
        if ((null == sdfsFileChannel) || (sdfsFileChannel.isReadOnly())) {
            throw new IllegalStateException();
        }
        return sdfsFileChannel;
    }

    @Override
    public void closeReadonlyFile(UUID fileUuid) throws IllegalStateException {
        // TODO your code here
//        获取通道，判断异常
        SDFSFileChannel sdfsFileChannel = getChannelByUUID(fileUuid);
        if ((null == sdfsFileChannel) || (!sdfsFileChannel.isReadOnly())) {
            throw new IllegalStateException();
        }
        channelArrayList.remove(sdfsFileChannel);
    }

    @Override
    public void closeReadwriteFile(UUID fileUuid, int newFileSize) throws IllegalStateException, IllegalArgumentException {
        // TODO your code here
//        获取通道，判断异常
        SDFSFileChannel sdfsFileChannel = getChannelByUUID(fileUuid);
        if ((null == sdfsFileChannel) || (sdfsFileChannel.isReadOnly())) {
            throw new IllegalStateException();
        }
//        更改文件树信息
        FileNode fileNode = sdfsFileChannel.getFileNode();
        fileNode.serialize(NAMENODE_DATA_DIR + fileNode.getNodeId() + suffixNode);
        channelArrayList.remove(sdfsFileChannel);
    }

    @Override
    public void mkdir(String fileUri) throws InvalidPathException, FileAlreadyExistsException {
        // TODO your code here
//        判断是否路径合法
        if (!SDFSUtils.isValidDir(fileUri)) {
            throw new InvalidPathException(fileUri, "路径不合法");
        }
//        判断是否已存在
        if (root.findNode(fileUri) != null) {
            throw new FileAlreadyExistsException("文件夹已存在");
        }
        String nodeName = SDFSUtils.getLastName(fileUri);//所要创建的节点名
        String temp = fileUri.substring(0, fileUri.length() - 1);
        String parentPath = fileUri.substring(0, temp.lastIndexOf("/") + 1);//父目录路径
        DirNode parentNode = (DirNode) root.findNode(parentPath);//获取父节点
        if (null == parentNode) {
//            如果父节点不存在，创建父目录
            mkdir(parentPath);
            parentNode = (DirNode) root.findNode(parentPath);
        }
//        创建目录，加入父节点链表
        DirNode dirNode = new DirNode();
        dirNode.setNodeId(++nodeNumber);
//        更新文件树
        dirNode.serialize(NAMENODE_DATA_DIR + dirNode.getNodeId() + suffixNode);
        parentNode.addEntry(new Entry(nodeName, dirNode));
        parentNode.serialize(NAMENODE_DATA_DIR + parentNode.getNodeId() + suffixNode);
    }

    @Override
    public List<LocatedBlock> addBlocks(UUID fileUuid, int blockAmount) throws IllegalStateException {
        // TODO your code here
//        获取通道，判断是否可写
        SDFSFileChannel sdfsFileChannel = getChannelByUUID(fileUuid);
        if (null == sdfsFileChannel || sdfsFileChannel.isReadOnly()) {
            throw new IllegalStateException();
        }
        FileNode fileNode = sdfsFileChannel.getFileNode();
        List<LocatedBlock> locatedBlockList = new ArrayList<>();
        for (int i = 0; i < blockAmount; i++) {
//            创建块，加入文件树
            LocatedBlock locatedBlock = new LocatedBlock(blockNumber++);
            locatedBlockList.add(locatedBlock);
            BlockInfo blockInfo = new BlockInfo();
            blockInfo.addLocatedBlock(locatedBlock);
            fileNode.addBlockInfo(blockInfo);
        }
        return locatedBlockList;
    }

    @Override
    public void removeLastBlocks(UUID fileUuid, int blockAmount) throws IllegalStateException, IllegalArgumentException {
        // TODO your code here
//        获取通道，判断是否可写
        SDFSFileChannel sdfsFileChannel = getChannelByUUID(fileUuid);
        if (null == sdfsFileChannel || sdfsFileChannel.isReadOnly()) {
            throw new IllegalStateException();
        }
//        判断移除block的数量是否合法
        FileNode fileNode = sdfsFileChannel.getFileNode();
        if ((blockAmount < 0) || (blockAmount > fileNode.getNumBlocks())) {
            throw new IllegalArgumentException();
        }
//        移除块
        fileNode.removeLastBlock(blockAmount);
    }


    //    根据uuid获取通道
    private SDFSFileChannel getChannelByUUID(UUID uuid) {
        for (SDFSFileChannel sdfsFileChannel : channelArrayList) {
            if (sdfsFileChannel.getUuid().equals(uuid)) {
                return sdfsFileChannel;
            }
        }
        return null;
    }

    public void updateChannel(FileNode fileNode, long position, UUID uuid) {
        for (SDFSFileChannel temp : channelArrayList) {
            if (temp.getUuid().equals(uuid) && (!temp.isReadOnly())) {
                try {
                    temp.position(position);
                    temp.setFileSize(fileNode.getFileSize());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

}
