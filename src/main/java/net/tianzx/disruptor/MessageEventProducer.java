package net.tianzx.disruptor;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * Author: tianzx
 * Date: 2018/11/15  7:49 PM
 * Email: zixuan.tian@nio.com
 */
public class MessageEventProducer {

    private final RingBuffer<MessageEvent> ringBuffer;

    public MessageEventProducer(RingBuffer<MessageEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(ByteBuffer bb) {
        int i = 0;
        while (true) {
            long sequence = ringBuffer.next();
            try {
                MessageEvent event = ringBuffer.get(sequence);
                Message message = new Message(i, bb.toString());
                event.setMessage(message);
            } finally {
                ringBuffer.publish(sequence);
            }
            i++;
            System.err.println("send ..." +i);
            if (i==1_000) {
                return;
            }
        }

    }
}
