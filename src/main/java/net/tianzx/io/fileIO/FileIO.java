package net.tianzx.io.fileIO;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.tianzx.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Author: tianzx
 * Date: 2018-12-24  20:33
 */
public class FileIO {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileIO.class);
    private FileChannel fc;
    private static String path;

    public void read() {
    }

    public void write() throws IOException {
        String test = "hello 中国\r\n";
        fc.write(ByteBuffer.wrap(test.getBytes()));
    }

    private void init() throws FileNotFoundException {
        fc = new RandomAccessFile(path, "rw").getChannel();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("WriteIO-%d")
                .setDaemon(true)
                .build();
        final ExecutorService service = Executors.newFixedThreadPool(64, threadFactory);
        path = Utils.class.getClassLoader().getResource("test.txt").getPath();
        FileIO file = new FileIO();
        file.init();
        file.start();
        for (int i = 0; i < 1024; i++) {
            service.submit(() -> {
                try {
                    file.write();
                } catch (IOException e) {
                    LOGGER.error("write failed");
                }
            });
        }
//        file.read();
        while (true) {
            Thread.sleep(1000);
        }
    }

    private void start() {

    }
}
