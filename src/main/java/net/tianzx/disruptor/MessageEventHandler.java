package net.tianzx.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * Author: tianzx
 * Date: 2018/11/15  6:10 PM
 * Email: zixuan.tian@nio.com
 */
public class MessageEventHandler implements EventHandler<MessageEvent> {

    @Override
    public void onEvent(MessageEvent event, long sequence, boolean endOfBatch) throws Exception {
//        System.out.println("Event: " + event.getMessage().getId());
        System.out.println(event.getMessage().getId());

    }
}
