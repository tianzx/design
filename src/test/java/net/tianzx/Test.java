package net.tianzx;

import net.tianzx.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Author: tianzx
 * Date: 2018-11-28  11:53
 * Email: zixuan.tian@nio.com
 */
public class Test {
    private static final Logger LOGGER = LoggerFactory.getLogger(Test.class);

    Semaphore semaphore = new Semaphore(2);

    ExecutorService service = Executors.newFixedThreadPool(10);


    static class MyTask implements  Runnable {
        Semaphore semaphore;

        public MyTask(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            System.err.println(Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            semaphore.release();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        String saslConfigFormat = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";";
        String saslUserName = Utils.getConfig("saslUserName");
        String saslPassword = Utils.getConfig("saslPassword");
        String saslConfig = String.format(saslConfigFormat, saslUserName, saslPassword);
        LOGGER.error(saslConfig);
    }
}
