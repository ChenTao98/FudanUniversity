/*
 * Copyright (c) Jipzingking 2016.
 */

package sdfs.client;

import sdfs.filetree.FileNode;
import sdfs.filetree.LocatedBlock;
import sdfs.namenode.INameNode;
import sdfs.namenode.NameNode;
import sdfs.utils.JudgeException;
import sdfs.socketUtil.SocketClientMessage;
import sdfs.socketUtil.SocketUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.InvalidPathException;
import java.util.List;
import java.util.UUID;

public class NameNodeStub implements INameNode, Serializable {
    private Socket socket;
    private String className = NameNode.class.getName();
    private String methodName;
    private Class<?>[] parameterType;
    private Object[] parameter;
    private int port;

    //    初始化时，获取nameNode的可用端口，用于创建连接
    NameNodeStub() {
        port = NameNode.port;
    }

    //    调用服务器端NameNode同名函数的步骤：
//    将要调用的函数名称，函数参数类型，函数参数以及被调用类的数据封装到SocketClientMessage的对象中
//    通过socket发送该对象
//    服务器端接收，获取对象数据，利用反射机制调用对应函数
//    服务器端包装返回消息被发送回来，客户端接收并解析消息
//    如果存在调用异常就抛出异常，没有异常就接收返回值
//    本类所有对应函数均以此方式调用NameNode的对应同名函数
    @Override
    public SDFSFileChannel openReadonly(String fileUri) throws FileNotFoundException, InvalidPathException {
        methodName = "openReadonly";
        parameterType = new Class<?>[]{String.class};
        parameter = new Object[]{fileUri};
        sendMessage();
        Object object = null;
        try {
            object = receiveMessage();
        } catch (Throwable throwable) {
            boolean isException = JudgeException.isFileNotFoundException(throwable) && JudgeException.isInvalidPathException(throwable, fileUri);
            if (!isException) {
                throwable.printStackTrace();
            }
        }
        return (SDFSFileChannel) object;
    }

    @Override
    public SDFSFileChannel openReadwrite(String fileUri) throws IndexOutOfBoundsException, IllegalStateException {
        methodName = "openReadwrite";
        parameterType = new Class<?>[]{String.class};
        parameter = new Object[]{fileUri};
        sendMessage();
        Object object = null;
        try {
            object = receiveMessage();
        } catch (Throwable throwable) {
            boolean isException = JudgeException.isIndexOutOfBoundsException(throwable) && JudgeException.isIllegalStateException(throwable);
            if (!isException) {
                throwable.printStackTrace();
            }
        }
        return (SDFSFileChannel) object;
    }

    @Override
    public SDFSFileChannel create(String fileUri) throws IllegalStateException {
        methodName = "create";
        parameterType = new Class<?>[]{String.class};
        parameter = new Object[]{fileUri};
        sendMessage();
        Object object = null;
        try {
            object = receiveMessage();
        } catch (Throwable throwable) {
            boolean isException = JudgeException.isIllegalStateException(throwable);
            if (!isException) {
                throwable.printStackTrace();
            }
        }
        return (SDFSFileChannel) object;
    }

    @Override
    public void closeReadonlyFile(UUID fileUuid) throws IllegalStateException {
        methodName = "closeReadonlyFile";
        parameterType = new Class<?>[]{UUID.class};
        parameter = new Object[]{fileUuid};
        sendMessage();
        try {
            receiveMessage();
        } catch (Throwable throwable) {
            boolean isException = JudgeException.isIllegalStateException(throwable);
            if (!isException) {
                throwable.printStackTrace();
            }
        }
    }

    @Override
    public void closeReadwriteFile(UUID fileUuid, int newFileSize) throws IllegalStateException, IllegalArgumentException, IOException {
        methodName = "closeReadwriteFile";
        parameterType = new Class<?>[]{UUID.class, int.class};
        parameter = new Object[]{fileUuid, newFileSize};
        sendMessage();
        try {
            receiveMessage();
        } catch (Throwable throwable) {
            boolean isException = JudgeException.isIllegalStateException(throwable) && JudgeException.isIllegalArgumentException(throwable) && JudgeException.isIOException(throwable);
            if (!isException) {
                throwable.printStackTrace();
            }
        }
    }

    @Override
    public void mkdir(String fileUri) throws InvalidPathException, FileAlreadyExistsException {
        methodName = "mkdir";
        parameterType = new Class<?>[]{String.class};
        parameter = new Object[]{fileUri};
        sendMessage();
        try {
            receiveMessage();
        } catch (Throwable throwable) {
            boolean isException = JudgeException.isInvalidPathException(throwable, fileUri) && JudgeException.isFileAlreadyExistsException(throwable);
            if (!isException) {
                throwable.printStackTrace();
            }
        }
    }


    @Override
    public List<LocatedBlock> addBlocks(UUID fileUuid, int blockAmount) {
        methodName = "addBlocks";
        parameterType = new Class<?>[]{UUID.class, int.class};
        parameter = new Object[]{fileUuid, blockAmount};
        sendMessage();
        Object object = null;
        try {
            object = receiveMessage();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return (List<LocatedBlock>) object;
    }

    @Override
    public void removeLastBlocks(UUID fileUuid, int blockAmount) throws IllegalStateException {
        methodName = "removeLastBlocks";
        parameterType = new Class<?>[]{UUID.class, int.class};
        parameter = new Object[]{fileUuid, blockAmount};
        sendMessage();
        try {
            receiveMessage();
        } catch (Throwable throwable) {
            if (!JudgeException.isIllegalStateException(throwable)) {
                throwable.printStackTrace();
            }
        }
    }

    public SDFSFileChannel getReadonlyFile(UUID fileUuid) throws IllegalStateException {
        methodName = "getReadonlyFile";
        parameterType = new Class<?>[]{UUID.class};
        parameter = new Object[]{fileUuid};
        sendMessage();
        Object object = null;
        try {
            object = receiveMessage();
        } catch (Throwable throwable) {
            if (!JudgeException.isIllegalStateException(throwable)) {
                throwable.printStackTrace();
            }
        }
        return (SDFSFileChannel) object;
    }

    public SDFSFileChannel getReadwriteFile(UUID fileUuid) throws IllegalStateException {
        methodName = "getReadwriteFile";
        parameterType = new Class<?>[]{UUID.class};
        parameter = new Object[]{fileUuid};
        sendMessage();
        Object object = null;
        try {
            object = receiveMessage();
        } catch (Throwable throwable) {
            if (!JudgeException.isIllegalStateException(throwable)) {
                throwable.printStackTrace();
            }
        }
        return (SDFSFileChannel) object;
    }

    void updateChannel(FileNode fileNode, long position, UUID uuid) {
        methodName = "updateChannel";
        parameterType = new Class<?>[]{FileNode.class, long.class, UUID.class};
        parameter = new Object[]{fileNode, position, uuid};
        sendMessage();
        try {
            receiveMessage();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    //    发送消息到服务器端
    private void sendMessage() {
//        根据数据封装发送消息
        SocketClientMessage socketClientMessage = new SocketClientMessage(className, methodName, parameterType, parameter);
//        创立连接
        try {
            socket = new Socket("127.0.0.1", port);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        发送消息
        SocketUtils.clientSend(socket, socketClientMessage);
    }

    //    接收消息，消息包含返回值或者被调用函数抛出的异常
    private Object receiveMessage() throws Throwable {
        return SocketUtils.clientReceive(socket);
    }

}
