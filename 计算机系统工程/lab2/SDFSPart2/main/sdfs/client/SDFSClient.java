package sdfs.client;

import sdfs.namenode.NameNode;

import java.io.IOException;

public class SDFSClient implements ISDFSClient {
    /**
     * Since in the first lab our file system is pseudo-distributional,
     * you could communicate with NameNode and DataNode by directly call the methods.
     */
    NameNodeStub nameNodeStub;

    public SDFSClient() {
        nameNodeStub = new NameNodeStub();
    }

    @Override
    public SDFSFileChannel openReadonly(String fileUri) throws IOException {
        return nameNodeStub.openReadonly(fileUri);
    }

    @Override
    public SDFSFileChannel create(String fileUri) throws IOException {
        return nameNodeStub.create(fileUri);
    }

    @Override
    public SDFSFileChannel openReadWrite(String fileUri) throws IOException {
        return nameNodeStub.openReadwrite(fileUri);
    }

    @Override
    public void mkdir(String fileUri) throws IOException {
        nameNodeStub.mkdir(fileUri);
    }
}
