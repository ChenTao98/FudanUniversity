package sdfs.socketUtil;

import java.io.Serializable;

//服务器端发送消息的消息封装类
public class SocketServerMessage implements Serializable {
    private Throwable throwable;//被调用函数抛出的异常，没有则为空
    private Object object;//被调用函数的返回值，如果抛出异常则为空

    public SocketServerMessage(Throwable e, Object object) {
        this.throwable = e;
        this.object = object;
    }

    public Throwable getException() {
        return throwable;
    }

    public Object getObject() {
        return object;
    }
}
