package net.tianzx.disruptor;

/**
 * Author: tianzx
 * Date: 2018/11/15  5:43 PM
 */
public class Message {
    private long id;
    private String content;

    public Message(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public Message() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .append("id", id)
                .append("content", content)
                .toString();
    }
}
