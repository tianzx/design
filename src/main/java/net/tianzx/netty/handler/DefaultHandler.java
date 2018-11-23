package net.tianzx.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Author: tianzx
 * Date: 2018-11-23  22:27
 * Email: zixuan.tian@nio.com
 */
@ChannelHandler.Sharable
public class DefaultHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {

    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf bf = (ByteBuf) msg;
        byte[] byteArray = new byte[bf.readableBytes()];
        bf.readBytes(byteArray);
        String result = new String(byteArray);
        System.err.println(result);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
//        if (evt instanceof InflightResender.ResendNotAckedPublishes) {
//            final MQTTConnection mqttConnection = mqttConnection(ctx.channel());
//            mqttConnection.resendNotAckedPublishes();
//        }
        ctx.fireUserEventTriggered(evt);
    }

}
