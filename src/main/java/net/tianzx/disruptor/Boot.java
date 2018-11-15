package net.tianzx.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author: tianzx
 * Date: 2018/11/15  10:21 PM
 * Email: zixuan.tian@nio.com
 */
public class Boot {
    public static void main(String[] args) {
        Executor service = new ThreadPoolExecutor(1, 10, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(100));

        int bufferSize = 1024;
        Disruptor<MessageEvent> disruptor = new Disruptor<MessageEvent>(MessageEvent::new, bufferSize, service);
//        disruptor.handleEventsWith((event, sequence, endOfBatch) -> System.out.println("Event: " + event));
        disruptor.handleEventsWith(new MessageEventHandler());
        disruptor.start();
        RingBuffer<MessageEvent> ringBuffer = disruptor.getRingBuffer();
        ByteBuffer buf = ByteBuffer.allocate(8);
//        for (long l = 0; true; l++) {
//            final long i = l;
//            buf.putLong(0, l);
//            ringBuffer.publishEvent((event, sequence, buffer) -> event.setMessage(new Message(i, String.valueOf(i))));
//        }
        MessageEventProducer producer = new MessageEventProducer(ringBuffer);
        producer.onData(buf);
    }
}
