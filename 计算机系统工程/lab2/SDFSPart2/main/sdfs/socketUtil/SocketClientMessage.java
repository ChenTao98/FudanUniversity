package sdfs.socketUtil;

import java.io.Serializable;

//客户端发送的消息的封装类
public class SocketClientMessage implements Serializable {
    private String className;//要调用的函数所在类
    private String methodName;//要调用的函数名称
    private Class<?>[] parameterType;//要调用的函数参数类型
    private Object[] parameter;//要调用的函数的传入参数

    public SocketClientMessage(String className, String methodName, Class<?>[] parameterType, Object[] parameter) {
        this.className = className;
        this.methodName = methodName;
        this.parameterType = parameterType;
        this.parameter = parameter;

    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameterType() {
        return parameterType;
    }

    public Object[] getParameter() {
        return parameter;
    }

}
