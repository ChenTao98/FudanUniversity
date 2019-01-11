package sdfs.datanode;

import java.io.FileNotFoundException;
import java.util.UUID;

public interface IDataNode {
    /**
     * The block size may be changed during test.
     * So please use this constant.
     */
    int BLOCK_SIZE = 64 * 1024;

    /**
     * Location of the block file.
     */
    String DATANODE_DATA_DIR = System.getProperty("sdfs.datanode.dir") + "/";

    /**
     * Read data from a block.
     * It should be redirect to [blockNumber].block file
     *
     * @param fileUuid    the file uuid to check whether have permission to read or not. (not used in lab 1)
     * @param blockNumber the block number to be read
     * @param offset      the offset on the block file
     * @param size        the total size to be read
     * @return the total number of bytes read into the buffer
     * @throws IndexOutOfBoundsException if offset less than zero, or offset+size larger than block size.
     * @throws FileNotFoundException     if the block is free (block file not exist)
     * @throws IllegalStateException     if uuid is illegal or has no permission on this file (not used in lab 1)
     */
    byte[] read(UUID fileUuid, int blockNumber, int offset, int size) throws IllegalStateException, IndexOutOfBoundsException, FileNotFoundException;

    /**
     * Write data to a block.
     * It should be redirect to [blockNumber].block file
     *
     * @param fileUuid    the file uuid to check whether have permission to write or not. (not used in lab 1)
     * @param blockNumber the block number to be written
     * @param offset      the offset on the block file
     * @param b           the buffer that stores the data
     * @throws IndexOutOfBoundsException if offset less than zero, or offset+size larger than block size.
     * @throws IllegalStateException     if uuid is illegal or has no permission on this file (not used in lab 1)
     */
    void write(UUID fileUuid, int blockNumber, int offset, byte b[]) throws IllegalStateException, IndexOutOfBoundsException;
}