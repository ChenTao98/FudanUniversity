package sdfs.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.InvalidPathException;

public class JudgeException {
    //    工具类
//    每个函数都用于判断传入的参数是否是对应异常的实例
//    如果是就抛出对应异常
//    如果不是，就返回false
    public static boolean isFileNotFoundException(Throwable throwable) throws FileNotFoundException {
        if (throwable instanceof FileNotFoundException) {
            throw new FileNotFoundException();
        }
        return false;
    }

    public static boolean isInvalidPathException(Throwable throwable, String fileUri) throws InvalidPathException {
        if (throwable instanceof InvalidPathException) {
            throw new InvalidPathException(fileUri, "路径不合法");
        }
        return false;
    }

    public static boolean isIndexOutOfBoundsException(Throwable throwable) throws IndexOutOfBoundsException {
        if (throwable instanceof IndexOutOfBoundsException) {
            throw new IndexOutOfBoundsException();
        }
        return false;
    }

    public static boolean isIllegalStateException(Throwable throwable) throws IllegalStateException {
        if (throwable instanceof IllegalStateException) {
            throw new IllegalStateException();
        }
        return false;
    }

    public static boolean isIllegalArgumentException(Throwable throwable) throws IllegalArgumentException {
        if (throwable instanceof IllegalArgumentException) {
            throw new IllegalArgumentException();
        }
        return false;
    }

    public static boolean isIOException(Throwable throwable) throws IOException {
        if (throwable instanceof IOException) {
            throw new IOException();
        }
        return false;
    }

    public static boolean isFileAlreadyExistsException(Throwable throwable) throws FileAlreadyExistsException {
        if (throwable instanceof FileAlreadyExistsException) {
            throw new FileAlreadyExistsException("文件已存在");
        }
        return false;
    }
}
