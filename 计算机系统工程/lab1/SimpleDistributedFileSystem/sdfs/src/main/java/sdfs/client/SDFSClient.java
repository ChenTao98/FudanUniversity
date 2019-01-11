package sdfs.client;

import sdfs.namenode.NameNode;

import java.io.IOException;

public class SDFSClient implements ISDFSClient {
    /**
     * Since in the first lab our file system is pseudo-distributional,
     * you could communicate with NameNode and DataNode by directly call the methods.
     */

    public SDFSClient() {
    }

    @Override
    public SDFSFileChannel openReadonly(String fileUri) throws IOException {
        NameNode nameNode = NameNode.getInstance();
        return nameNode.openReadonly(fileUri);
    }

    @Override
    public SDFSFileChannel create(String fileUri) throws IOException {
        NameNode nameNode = NameNode.getInstance();
        return nameNode.create(fileUri);
    }

    @Override
    public SDFSFileChannel openReadWrite(String fileUri) throws IOException {
        NameNode nameNode = NameNode.getInstance();
        return nameNode.openReadwrite(fileUri);
    }

    @Override
    public void mkdir(String fileUri) throws IOException {
        NameNode nameNode = NameNode.getInstance();
        nameNode.mkdir(fileUri);
    }
}
