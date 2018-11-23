package net.tianzx.netty.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;

/**
 * Author: tianzx
 * Date: 2018-11-23  22:20
 * Email: zixuan.tian@nio.com
 */
public class InflightResender extends ChannelDuplexHandler {

    private static final long MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1);

    private final long resenderTimeNanos;
    volatile ScheduledFuture<?> resenderTimeout;
    volatile long lastExecutionTime;

    private volatile int state; // 0 - none, 1 - initialized, 2 - destroyed

    public InflightResender(long writerIdleTime, TimeUnit unit) {
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        resenderTimeNanos = Math.max(unit.toNanos(writerIdleTime), MIN_TIMEOUT_NANOS);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().isActive() && ctx.channel().isRegistered()) {
            // channelActive() event has been fired already, which means this.channelActive() will
            // not be invoked. We have to initialize here instead.
            initialize(ctx);
        } else {
            // channelActive() event has not been fired yet. this.channelActive() will be invoked
            // and initialization will occur there.
        }
    }
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        destroy();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        // Initialize early if channel is active already.
        if (ctx.channel().isActive()) {
            initialize(ctx);
        }
        super.channelRegistered(ctx);
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // This method will be invoked only if this handler was added
        // before channelActive() event is fired. If a user adds this handler
        // after the channelActive() event, initialize() will be called by beforeAdd().
        initialize(ctx);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        destroy();
        super.channelInactive(ctx);
    }
    private void initialize(ChannelHandlerContext ctx) {
        // Avoid the case where destroy() is called before scheduling timeouts.
        // See: https://github.com/netty/netty/issues/143

        switch (state) {
            case 1:
            case 2:
                return;
        }

        state = 1;

        EventExecutor loop = ctx.executor();

        lastExecutionTime = System.nanoTime();
        resenderTimeout = loop.schedule(new WriterIdleTimeoutTask(ctx), resenderTimeNanos, TimeUnit.NANOSECONDS);
    }

    private void destroy() {
        state = 2;

        if (resenderTimeout != null) {
            resenderTimeout.cancel(false);
            resenderTimeout = null;
        }
    }


    private void resendNotAcked(ChannelHandlerContext ctx/* , IdleStateEvent evt */) {
        ctx.fireUserEventTriggered(new ResendNotAckedPublishes());
    }

    public static class ResendNotAckedPublishes {
    }

    private final class WriterIdleTimeoutTask implements Runnable {
        private final ChannelHandlerContext ctx;

        WriterIdleTimeoutTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            long nextDelay = resenderTimeNanos - (System.nanoTime() - lastExecutionTime);
            if (nextDelay <= 0) {
                resenderTimeout = ctx.executor().schedule(this, resenderTimeNanos, TimeUnit.NANOSECONDS);
                try {
                    resendNotAcked(ctx/* , event */);
                } catch (Throwable t) {
                    ctx.fireExceptionCaught(t);
                }
            }else{
                resenderTimeout = ctx.executor().schedule(this, nextDelay, TimeUnit.NANOSECONDS);
            }
        }


    }
}
