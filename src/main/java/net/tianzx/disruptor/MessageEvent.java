package net.tianzx.disruptor;

/**
 * Author: tianzx
 * Date: 2018/11/15  4:36 PM
 * Email: zixuan.tian@nio.com
 */
public class MessageEvent {
    private Message message;

    public MessageEvent() {
    }

    public MessageEvent(Message message) {
        this.message = message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
