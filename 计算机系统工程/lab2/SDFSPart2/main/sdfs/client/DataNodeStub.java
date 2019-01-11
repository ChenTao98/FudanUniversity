package sdfs.client;

import sdfs.datanode.DataNode;
import sdfs.datanode.IDataNode;
import sdfs.utils.JudgeException;
import sdfs.socketUtil.SocketClientMessage;
import sdfs.socketUtil.SocketUtils;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class DataNodeStub implements IDataNode, Serializable {
    private Socket socket;
    private String className = DataNode.class.getName();
    private String methodName;
    private Class<?>[] parameterType;
    private Object[] parameter;
    private int port;

    //    初始化获取stub的时候，会获取dataNode的可用端口，用于创立连接
    DataNodeStub() {
        this.port = DataNode.port;
    }

    //    调用服务器端DataNode同名函数的步骤：
//    将要调用的函数名称，函数参数类型，函数参数以及被调用类的数据封装到SocketClientMessage的对象中
//    通过socket发送该对象
//    服务器端接收，获取对象数据，利用反射机制调用对应函数
//    服务器端包装返回消息被发送回来，客户端接收并解析消息
//    如果存在调用异常就抛出异常，没有异常就接收返回值
    @Override
    public byte[] read(UUID fileUuid, int blockNumber, int offset, int size) throws IndexOutOfBoundsException {
        methodName = "read";
        parameterType = new Class<?>[]{UUID.class, int.class, int.class, int.class};
        parameter = new Object[]{fileUuid, blockNumber, offset, size};
        sendMessage();
        Object object = null;
        try {
            object = receiveMessage();
        } catch (Throwable throwable) {
            if (!JudgeException.isIndexOutOfBoundsException(throwable)) {
                throwable.printStackTrace();
            }
            ;
        }
        return (byte[]) object;
    }

    @Override
    public void write(UUID fileUuid, int blockNumber, int offset, byte[] b) throws IndexOutOfBoundsException {
        methodName = "write";
        parameterType = new Class<?>[]{UUID.class, int.class, int.class, byte[].class};
        parameter = new Object[]{fileUuid, blockNumber, offset, b};
        sendMessage();
        try {
            receiveMessage();
        } catch (Throwable throwable) {
            if (!JudgeException.isIndexOutOfBoundsException(throwable)) {
                throwable.printStackTrace();
            }
        }
    }

    //    发送消息到服务端
    private void sendMessage() {
//        根据变量新建客户端消息的实例
        SocketClientMessage socketClientMessage = new SocketClientMessage(className, methodName, parameterType, parameter);
//        创立连接
        try {
            socket = new Socket("127.0.0.1", port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SocketUtils.clientSend(socket, socketClientMessage);
    }

    //    接收服务器端返回的消息，即调用函数的返回值，并且有可能抛出被调用函数的错误
    private Object receiveMessage() throws Throwable {
        return SocketUtils.clientReceive(socket);
    }
}
