package net.tianzx.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * Author: tianzx
 * Date: 2018/11/15  5:56 PM
 */
public class MessageEventFactory implements EventFactory<MessageEvent> {
    @Override
    public MessageEvent newInstance() {
        return new MessageEvent();
    }
}
