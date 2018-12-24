package net.tianzx.counter;

import com.google.common.util.concurrent.AtomicLongMap;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Author: tianzx
 * Date: 2018-12-24  12:02
 * Email: zixuan.tian@nio.com
 */
public class Counter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Counter.class);

    public static AtomicLongMap<String> counter = AtomicLongMap.create();

    public static void main(String[] args) throws InterruptedException {
//        new ThreadPoolBuilder();
        final ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("Nums-%d")
                .setDaemon(true)
                .build();
        final ExecutorService service = Executors.newFixedThreadPool(10, threadFactory);
        final ExecutorService service2 = Executors.newFixedThreadPool(10, threadFactory);
        for (int i = 0; i <= 199; i++) {
            service.submit(() -> {
                add();
            });
        }
        for (int i = 0; i <= 99; i++) {
            service2.submit(() -> {
                sub();
            });

        }
        while (true) {
            Thread.sleep(1000);
            long i = counter.get("10001");
            LOGGER.info("num {}", i);
        }
    }

    private static void add() {
        long i = counter.incrementAndGet("10001");
//        LOGGER.info("num {}", i);
    }

    private static void sub() {
        long i = counter.decrementAndGet("10001");
//        LOGGER.info("num : {}", i);
    }
}
