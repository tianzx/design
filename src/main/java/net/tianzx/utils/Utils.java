package net.tianzx.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Properties;

/**
 * Author: tianzx
 * Date: 2018-12-03  20:38
 */
public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    private static Properties prop;

    private static void init() throws IOException {
        String path = Utils.class.getClassLoader().getResource("config.properties").getPath();
        prop = new Properties();
        LOGGER.info(path);
        FileInputStream file = new FileInputStream(path);
        prop.load(file);
    }

    public static String getConfig(String key) throws IOException {
        if (null == prop) {
            init();
        }
        LOGGER.info(prop.getProperty(key));
        return prop.getProperty(key);
    }

    public static byte[] readFile(String path) {
        String name = Utils.class.getClassLoader().getResource(path).getPath();

        RandomAccessFile file = null;
        FileChannel inChannel = null;
        try {
            file = new RandomAccessFile(
                    name, "r");
            inChannel = file.getChannel();
            long size = inChannel.size();
            ByteBuffer buffer = ByteBuffer.allocate((int) size);
            inChannel.read(buffer);
            buffer.flip();
            return buffer.array();
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                file.close();
                inChannel.close();
            } catch (IOException e) {
            }

        }
        return null;
    }

    public static void main(String[] args) throws IOException {
//        LOGGER.info(Utils.getConfig("saslUserName"));
        LOGGER.info(readFile("test.file").length + "");
    }
}
