package sdfs.namenode;

import sdfs.client.SDFSFileChannel;
import sdfs.filetree.LocatedBlock;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.InvalidPathException;
import java.util.List;
import java.util.UUID;

public interface INameNode {

    /**
     * Location of the node file.
     */
    String NAMENODE_DATA_DIR = System.getProperty("sdfs.namenode.dir") + "/";

    /**
     * Open a readonly file that is already exist.
     * Allow multi readonly access to the same file.
     * Also, if the file is currently being writing by other client, it is also LEGAL to open the same file.
     * However, only after the write instance is closed could other client to read the new data.
     *
     * @param fileUri The file uri to be open
     * @return The SDFSFileChannel represent the file
     * @throws FileNotFoundException if the file is not exist
     * @throws InvalidPathException  fileUri is not valid
     */
    SDFSFileChannel openReadonly(String fileUri) throws FileNotFoundException, InvalidPathException;

    /**
     * Open a readwrite file that is already exist.
     * At most one UUID with readwrite permission could exist on the same file at the same time.
     *
     * @param fileUri The file uri to be open
     * @return The SDFSFileChannel represent the file
     * @throws FileNotFoundException if the file is not exist
     * @throws InvalidPathException  fileUri is not valid
     * @throws IllegalStateException if the file is already opened readwrite
     */
    SDFSFileChannel openReadwrite(String fileUri) throws FileNotFoundException, InvalidPathException, IllegalStateException;

    /**
     * Create a empty file. It should maintain a readwrite file on the memory and return the uuid to client.
     *
     * @param fileUri The file uri to be create
     * @return The SDFSFileChannel represent the file.
     * @throws FileAlreadyExistsException if the file is already exist
     * @throws InvalidPathException       if the uri of the file is invalid
     */
    SDFSFileChannel create(String fileUri) throws FileAlreadyExistsException, InvalidPathException;

    /**
     * Close a readonly file.
     *
     * @param fileUuid file to be closed
     * @throws IllegalStateException if uuid is illegal
     */
    void closeReadonlyFile(UUID fileUuid) throws IllegalStateException;

    /**
     * Close a readwrite file. If file metadata has been changed, store them on the disk.
     *
     * @param fileUuid    file to be closed
     * @param newFileSize The new file size after modify
     * @throws IllegalArgumentException if new file size not in (blockAmount * BLOCK_SIZE, (blockAmount + 1) * BLOCK_SIZE]
     * @throws IllegalStateException    if uuid is illegal
     */
    void closeReadwriteFile(UUID fileUuid, int newFileSize) throws IllegalStateException, IllegalArgumentException, IOException;

    /**
     * Make a directory on given file uri.
     * URI must start and end with a '/'.
     *
     * @param fileUri the directory path
     * @throws FileAlreadyExistsException if directory or file is already exist
     * @throws InvalidPathException       if fileUri is invalid
     */
    void mkdir(String fileUri) throws InvalidPathException, FileAlreadyExistsException;

    /**
     * Request a special amount of free blocks for a file
     * No metadata should be written to disk until it is correctly close
     *
     * @param fileUuid    the file uuid with readwrite state
     * @param blockAmount the request block amount
     * @return a special amount of blocks that is free and could be used by client
     * @throws IllegalStateException if file is readonly or uuid is illegal
     */
    List<LocatedBlock> addBlocks(UUID fileUuid, int blockAmount) throws IllegalStateException;

    /**
     * Delete the last blocks for a file
     * No metadata should be written to disk until it is correctly close
     *
     * @param fileUuid    the file uuid with readwrite state
     * @param blockAmount the blocks amount to be removed
     * @throws IllegalStateException    if the file is readonly or uuid is illegal
     * @throws IllegalArgumentException if there is no enough block in this file
     */
    void removeLastBlocks(UUID fileUuid, int blockAmount) throws IllegalStateException;

    /**
     * Get a opened readonly file with uuid.
     *
     * @param fileUuid the file uuid with readonly state
     * @return The SDFSFileChannel represent the file
     * @throws IllegalStateException if uuid is illegal
     */
    SDFSFileChannel getReadonlyFile(UUID fileUuid) throws IllegalStateException;

    /**
     * Get a opened readwrite file with uuid.
     *
     * @param fileUuid the file uuid with readwrite state
     * @return The SDFSFileChannel represent the file
     * @throws IllegalStateException if uuid is illegal
     */
    SDFSFileChannel getReadwriteFile(UUID fileUuid) throws IllegalStateException;
}
