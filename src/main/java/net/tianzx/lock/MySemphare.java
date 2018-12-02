package net.tianzx.lock;


import java.io.Serializable;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class MySemphare implements Serializable {

    private final Sync sync;

    public MySemphare(int permits) {
        sync = new MySemphare.NonfairSync(permits);
    }

    abstract static class Sync extends AbstractQueuedSynchronizer {
        Sync(int permits) {
            setState(permits);
        }

        protected int nonfairTryAcquireShared(int acquires) {
            return 0;
        }

    }

    static final class NonfairSync extends MySemphare.Sync {
        private static final long serialVersionUID = -2694183684443567898L;

        NonfairSync(int permits) {
            super(permits);
        }

        protected int tryAcquireShared(int acquires) {
            return nonfairTryAcquireShared(acquires);
        }
    }

    public void acquire() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    public void release(int permits) {
        if (permits < 0) throw new IllegalArgumentException();
        sync.releaseShared(permits);
    }

}
