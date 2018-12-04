package net.tianzx.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Author: tianzx
 * Date: 2018-12-03  20:38
 * Email: zixuan.tian@nio.com
 */
public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    private static Properties prop;

    private Utils() throws IOException {
    }

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
        LOGGER.info(prop.stringPropertyNames().toString());
        return prop.getProperty(key);
    }


    public static void main(String[] args) throws IOException {
        LOGGER.info(Utils.getConfig("saslUserName"));
    }
}
