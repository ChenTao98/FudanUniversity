package sdfs.socketUtil;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

//socket的工具类
public class SocketUtils {
    //    客户端发送消息函数
    public static void clientSend(Socket socket, SocketClientMessage socketClientMessage) {
        try {
//            获取socket的输出流
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
//            写入客户端发送的消息
            objectOutputStream.writeObject(socketClientMessage);
            socket.shutdownOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    客户端接收服务器消息函数
    public static Object clientReceive(Socket socket) throws Throwable {
        SocketServerMessage socketServerMessage = new SocketServerMessage(null, null);
        try {
//            获取服务端传回的的消息
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            socketServerMessage = (SocketServerMessage) objectInputStream.readObject();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
//        判断是否有异常，没有异常获取返回值，有异常就抛出异常
        Object object = null;
        if (socketServerMessage.getException() == null) {
            object = socketServerMessage.getObject();
        } else {
            throw socketServerMessage.getException();
        }
        return object;
    }

    //    服务端接收消息
    private static SocketServerMessage serverReceive(SocketClientMessage socketClientMessage) {
        Object object;
        SocketServerMessage serverMessage = null;
        try {
//            获取客户端消息中的参数
            Class<?> callClass = Class.forName(socketClientMessage.getClassName());
//            利用反射机制获取要调用的函数，并调用
            Method method = callClass.getMethod(socketClientMessage.getMethodName(), socketClientMessage.getParameterType());
            object = method.invoke(callClass.newInstance(), socketClientMessage.getParameter());
            serverMessage = new SocketServerMessage(null, object);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
//            如果存在调用异常，就返回异常的服务端消息封装类
            serverMessage = new SocketServerMessage(e.getTargetException(), null);
        }
        return serverMessage;
    }

    //    服务端发送消息
    private static void serverSend(Socket socket, SocketServerMessage socketServerMessage) {
        try {
//            获取输出流，发送消息
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(socketServerMessage);
            objectOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    服务端监听函数
//    启动服务监听，不断死循环
//    等待客户端的消息，接收到客户端消息后就处理消息
//    并返回消息给对应客户端
    public static void listen(int port) {
        try {
//            启动服务监听
            ServerSocket server = new ServerSocket(port);
            while (true) {
//                阻塞等待客户端消息
                Socket socket = server.accept();
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
//                获取客户端消息，处理消息并返回
                SocketClientMessage socketClientMessage = (SocketClientMessage) objectInputStream.readObject();
                SocketServerMessage socketServerMessage = SocketUtils.serverReceive(socketClientMessage);
                SocketUtils.serverSend(socket, socketServerMessage);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
