import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sdfs.client.SDFSClient;
import sdfs.client.SDFSFileChannel;
import sdfs.datanode.DataNode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SDFSClientTest {

    @BeforeAll
    static void setup() throws IOException {
        System.setProperty("sdfs.namenode.dir", Files.createTempDirectory("sdfs.namenode.data").toAbsolutePath().toString());
        System.setProperty("sdfs.datanode.dir", Files.createTempDirectory("sdfs.datanode.data").toAbsolutePath().toString());
    }

    @Test
    void testSDFSFileChannel() {
        SDFSClient client = new SDFSClient();
        try {
            SDFSFileChannel newFc = client.create("/foo/bar.txt");
            assertTrue(newFc.isOpen());
            assertTrue(!newFc.isReadOnly());
            assertEquals(0, newFc.getNumBlocks());
            assertEquals(0, newFc.size());
            assertEquals(0, newFc.position());

            // Write 2 * BLOCK_SIZE + 10 random bytes to this file
            int size = 2 * DataNode.BLOCK_SIZE + 10;
            byte[] data = new byte[size];
            new Random().nextBytes(data);
            newFc.write(ByteBuffer.wrap(data));

            assertEquals(3, newFc.getNumBlocks());
            assertEquals(size, newFc.size());
            assertEquals(size, newFc.position());

            newFc.position(20);
            assertEquals(size, newFc.size());
            assertEquals(20, newFc.position());

            // Truncate to 1 * BLOCK_SIZE + 20
            int newSize = DataNode.BLOCK_SIZE + 20;
            newFc.truncate(newSize);
            assertEquals(2, newFc.getNumBlocks());
            assertEquals(newSize, newFc.size());

            // Read all
            newFc.position(0);
            ByteBuffer readData = ByteBuffer.wrap(new byte[newSize]);
            int bytesRead = newFc.read(readData);
            assertEquals(newSize, bytesRead);
            assertArrayEquals(Arrays.copyOfRange(data, 0, newSize), readData.array());
            assertEquals(newSize, newFc.position());

            int moreRead = newFc.read(ByteBuffer.wrap(new byte[1]));
            assertEquals(-1, moreRead);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
