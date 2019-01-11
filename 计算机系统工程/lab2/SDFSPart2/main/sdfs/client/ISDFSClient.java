package sdfs.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

public interface ISDFSClient {
    /**
     * Open a readonly file that is already exist.
     *
     * @param fileUri The file uri to be open. The fileUri should look like /foo/bar.data which is a request to sdfs://[ip]:[port]/foo/bar.data
     * @return File channel of this file
     * @throws FileNotFoundException if the file is not exist
     */
    SDFSFileChannel openReadonly(String fileUri) throws IOException;

    /**
     * Open a read write file that is already exist.
     *
     * @param fileUri The file uri to be create. The fileUri should look like /foo/bar.data which is a request to sdfs://[ip]:[port]/foo/bar.data
     * @return file channel of this file
     * @throws FileNotFoundException if the file is not exist
     */
    SDFSFileChannel openReadWrite(String fileUri) throws IOException;

    /**
     * Create a empty file and return the output stream to this file.
     *
     * @param fileUri The file uri to be create. The fileUri should look like /foo/bar.data which is a request to sdfs://[ip]:[port]/foo/bar.data
     * @return File channel of this file
     * @throws FileAlreadyExistsException if the file is already exist
     */
    SDFSFileChannel create(String fileUri) throws IOException;

    /**
     * Make a directory on given file uri.
     *
     * @param fileUri the directory path
     * @throws FileAlreadyExistsException if directory or file is already exist
     */
    void mkdir(String fileUri) throws IOException;
}
