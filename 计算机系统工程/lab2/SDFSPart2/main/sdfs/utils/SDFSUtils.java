package sdfs.utils;

import sdfs.filetree.DirNode;
import sdfs.namenode.NameNode;

import java.nio.file.InvalidPathException;

public class SDFSUtils {
    //    判断是否是合法的文件夹
    public static boolean isValidDir(String fileUri) {
        if (fileUri.equals("/")) {
            return true;
        }
        if ((!fileUri.matches("^/.+/$"))) {
            throw new InvalidPathException(fileUri, "路径不合法");
        }
        return true;
    }

    //    判断是否是合法的文件路径
    public static boolean isValidFile(String fileUri) {
        if ((null == fileUri) || (!(fileUri.matches("^/.+") && (!fileUri.matches("^.+/$"))))) {
            throw new InvalidPathException(fileUri, "路径不合法");
        }
        return true;
    }

    //    获取分割的路径数组
    public static String[] getPathArray(String fileUri) {
        return fileUri.substring(1).split("/");
    }

    //    获取最后一个文件名
    public static String getLastName(String fileUri) {
        String[] path = getPathArray(fileUri);
        return path[path.length - 1];
    }

    public static DirNode getRoot() {
        DirNode root = new DirNode();
        root = (DirNode) root.deserialize(NameNode.NAMENODE_DATA_DIR + "0.node");
        return root;
    }
}