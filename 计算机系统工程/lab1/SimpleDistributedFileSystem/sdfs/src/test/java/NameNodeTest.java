import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sdfs.client.SDFSFileChannel;
import sdfs.datanode.DataNode;
import sdfs.namenode.NameNode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;

import static org.junit.jupiter.api.Assertions.*;

class NameNodeTest {

    @BeforeAll
    static void setup() throws IOException {
        System.setProperty("sdfs.namenode.dir", Files.createTempDirectory("sdfs.namenode.data").toAbsolutePath().toString());
        System.setProperty("sdfs.datanode.dir", Files.createTempDirectory("sdfs.datanode.data").toAbsolutePath().toString());
    }

    @Test
    void testMkdir() {
        NameNode nameNode = NameNode.getInstance();

        try {
            // Make a directory
            nameNode.mkdir("/a/");

            // Make a directory without a ending forward slash
            try {
                nameNode.mkdir("/b");
                fail("Directory without ending slash.");
            } catch (InvalidPathException ignore) {
            }

            // Make a directory without a starting forward slash
            try {
                nameNode.mkdir("c/");
                fail("Directory without starting slash");
            } catch (InvalidPathException ignore) {
            }

            nameNode.mkdir("/foo/bar/");
            // Duplicated directory
            nameNode.mkdir("/foo/");
            fail("Directory already exists.");
        } catch (FileAlreadyExistsException ignore) {
        }
    }

    @Test
    void testCreate() {
        NameNode nameNode = NameNode.getInstance();

        try {
            // Create a file
            nameNode.create("/a.txt");

            // Create a file with a ending forward slash
            try {
                nameNode.create("/b.txt/");
                fail("File with ending slash.");
            } catch (InvalidPathException ignore) {
            }

            // Create a file without a starting forward slash
            try {
                nameNode.create("c.txt");
                fail("File without starting slash");
            } catch (InvalidPathException ignore) {
            }

            // Create a file with parent directory does not exist
            nameNode.create("/foo/bar/a.txt");

            // Create a file with the same name to a directory
            nameNode.create("/foo/bar");
            fail("File with the same name to a directory.");
        } catch (FileAlreadyExistsException ignore) {
        }
    }

    @Test
    void testOpenAndClose() {
        NameNode nameNode = NameNode.getInstance();

        String aUri = "/foo/bar/a.txt";
        String bUri = "/foo/bar/b.txt";

        // Create two files
        try {
            SDFSFileChannel afcrw = nameNode.create(aUri);
            SDFSFileChannel bfcrw = nameNode.create(bUri);

            // Close them first
            afcrw.close();
            bfcrw.close();
        } catch (FileAlreadyExistsException ignore) {
        }

        try {
            // Open a.txt as readonly
            SDFSFileChannel afcro1 = nameNode.openReadonly(aUri);
            assertTrue(afcro1.isReadOnly());
            // Open a.txt as readonly again
            SDFSFileChannel afcro2 = nameNode.openReadonly(aUri);
            assertTrue(afcro2.isReadOnly());

            // Open b.txt as readwrite
            SDFSFileChannel bfcrw1 = nameNode.openReadwrite(bUri);
            assertTrue(!bfcrw1.isReadOnly());

            // Open b.txt as readonly
            SDFSFileChannel bfcro1 = nameNode.openReadonly(bUri);
            assertTrue(bfcro1.isReadOnly());
            // Open b.txt as readwrite again
            try {
                nameNode.openReadwrite(bUri);
                fail("Open one file as readwrite twice.");
            } catch (IllegalStateException ignore) {
            }

            // Close the readwrite channel to b.txt
            bfcrw1.close();

            // Open b.txt as readwrite again
            try {
                SDFSFileChannel bfcrw2 = nameNode.openReadwrite(bUri);
                assertTrue(!bfcrw2.isReadOnly());
            } catch (IllegalStateException ignore) {
                fail("Open a closed file as readwrite.");
            }

        } catch (FileNotFoundException ignore) {
        }
    }

    @Test
    void testGetOpenedFile() {
        NameNode nameNode = NameNode.getInstance();

        try {
            String fileUri = "/foo/bar/c.txt";
            SDFSFileChannel sdfsFileChannel = nameNode.create(fileUri);
            sdfsFileChannel.close();

            SDFSFileChannel rwfc = nameNode.openReadwrite(fileUri);
            SDFSFileChannel rofc = nameNode.openReadonly(fileUri);

            // Get readwrite file with readwrite file uuid
            nameNode.getReadwriteFile(rwfc.getUuid());

            // Get readonly file with readonly file uuid
            nameNode.getReadonlyFile(rofc.getUuid());

            // Get readwrite file with readonly file uuid
            try {
                nameNode.getReadwriteFile(rofc.getUuid());
                fail("Get readwrite file with readonly file uuid.");
            } catch (IllegalStateException ignore) {
            }

            // Get readonly file with readwrite file uuid
            try {
                nameNode.getReadonlyFile(rwfc.getUuid());
                fail("Get readonly file with readwrite file uuid.");
            } catch (IllegalStateException ignore) {
            }


        } catch (FileAlreadyExistsException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testAddBlocksAndRemoveBlocks() {
        NameNode nameNode = NameNode.getInstance();

        try {
            String fileUri = "/foo/bar/d.txt";
            SDFSFileChannel sdfsFileChannel = nameNode.create(fileUri);
            sdfsFileChannel.close();

            SDFSFileChannel rwfc = nameNode.openReadwrite(fileUri);
            SDFSFileChannel rofc = nameNode.openReadonly(fileUri);

            assertEquals(rwfc.getNumBlocks(), rofc.getNumBlocks());

            // Add 2 blocks through rwfc
            nameNode.addBlocks(rwfc.getUuid(), 2);

            // Add 1 block through rofc
            try {
                nameNode.addBlocks(rofc.getUuid(), 1);
                fail("Add blocks through readonly channel.");
            } catch (IllegalStateException ignore) {
            }

            // Update rwfc and rofc
            rwfc = nameNode.getReadwriteFile(rwfc.getUuid());
            rofc = nameNode.getReadonlyFile(rofc.getUuid());

            // Not equal because rwfc has not been closed
            assertNotEquals(rwfc.getNumBlocks(), rofc.getNumBlocks());

            // Get another readonly channel
            SDFSFileChannel rofc2 = nameNode.openReadonly(fileUri);

            // Not equal because rwfc has not been closed
            assertNotEquals(rwfc.getNumBlocks(), rofc2.getNumBlocks());

            // Close rwfc
            rwfc.setFileSize(rwfc.getNumBlocks() * DataNode.BLOCK_SIZE - 1);
            rwfc.close();

            // Now get another readonly channel
            SDFSFileChannel rofc3 = nameNode.openReadonly(fileUri);

            // Now newly opened channel reveal the change
            assertEquals(rwfc.getNumBlocks(), rofc3.getNumBlocks());
            // Update the rofc
            rofc = nameNode.getReadonlyFile(rofc.getUuid());
            // rofc is still the same
            assertNotEquals(rofc.getNumBlocks(), rofc3.getNumBlocks());

            // Open another readwrite channel
            SDFSFileChannel rwfc2 = nameNode.openReadwrite(fileUri);
            // Remove 1 block through rofc
            try {
                nameNode.removeLastBlocks(rofc3.getUuid(), 1);
                fail("Remove blocks through readonly channel.");
            } catch (IllegalStateException ignore) {
            }

            // Remove 10 blocks
            try {
                nameNode.removeLastBlocks(rwfc2.getUuid(), 10);
                fail("No enough blocks to remove.");
            } catch (IllegalArgumentException ignore) {
            }

            // Remove 1 block
            nameNode.removeLastBlocks(rwfc2.getUuid(), 1);

            // Update rwfc2 and rofc3
            rwfc2 = nameNode.getReadwriteFile(rwfc2.getUuid());
            rofc3 = nameNode.getReadonlyFile(rofc3.getUuid());

            assertNotEquals(rwfc2.getNumBlocks(), rofc3.getNumBlocks());

            // Close rwfc2
            rwfc2.setFileSize(rwfc2.getNumBlocks() * DataNode.BLOCK_SIZE - 1);
            rwfc2.close();

            // Now get another readonly channel
            SDFSFileChannel rofc4 = nameNode.openReadonly(fileUri);
            // Now newly opened channel reveal the change
            assertEquals(rwfc2.getNumBlocks(), rofc4.getNumBlocks());

        } catch (FileAlreadyExistsException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
