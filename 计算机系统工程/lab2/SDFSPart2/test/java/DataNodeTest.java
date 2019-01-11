import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sdfs.datanode.DataNode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.fail;

class DataNodeTest {

    @BeforeAll
    static void setup() throws IOException {
        System.setProperty("sdfs.namenode.dir", Files.createTempDirectory("sdfs.namenode.data").toAbsolutePath().toString());
        System.setProperty("sdfs.datanode.dir", Files.createTempDirectory("sdfs.datanode.data").toAbsolutePath().toString());
    }

    @Test
    void testWriteAndRead() {
        DataNode dataNode = new DataNode();

        // Write offset is negative
        try {
            dataNode.write(null, 0, -1, new byte[10]);
            fail("Offset is negative.");
        } catch (IndexOutOfBoundsException ignore) {
        }

        // Write size exceed block size
        try {
            dataNode.write(null, 0, 0, new byte[DataNode.BLOCK_SIZE + 1]);
            fail("Size exceed block size.");
        } catch (IndexOutOfBoundsException ignore) {
        }
        try {
            dataNode.write(null, 0, 0, new byte[DataNode.BLOCK_SIZE]);
        } catch (IndexOutOfBoundsException ignore) {
        }

        // Read offset is negative
        try {
            dataNode.read(null, 0, -1, 10);
            fail("Offset is negative.");
        } catch (IndexOutOfBoundsException | IOException ignore) {
        }
        // Read size exceed block size
        try {
            dataNode.read(null, 0, 0, DataNode.BLOCK_SIZE + 1);
            fail("Size exceed block size.");
        } catch (IndexOutOfBoundsException | IOException ignore) {
        }
        try {
            dataNode.read(null, 0, 0, 10);

            // Read from a free block
            dataNode.read(null, 1, 0, 10);
            fail("Read from a free block.");
        } catch (IOException ignore) {

        }


    }
}
