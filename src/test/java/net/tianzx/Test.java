package net.tianzx;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Author: tianzx
 * Date: 2018-11-28  11:53
 * Email: zixuan.tian@nio.com
 */
public class Test {
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
    public static void main(String[] args) throws InterruptedException {
        Test test = new Test();
        MyTask myTask = new MyTask( test.semaphore);
        for(int i=0;i<4;i++) {
            test.semaphore.acquire(1);
            test.service.submit(myTask);
        }
        test.service.shutdown();
    }
}
