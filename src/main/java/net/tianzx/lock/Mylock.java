package net.tianzx.lock;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: tianzx
 * Date: 2018/10/29  10:45 PM
 */
public class Mylock {
    private static Unsafe unsafe;
    private volatile int state;
    private static Long stateOffset;
    private ConcurrentHashMap<String, Thread> map = new ConcurrentHashMap<String, Thread>();

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
            stateOffset = unsafe.objectFieldOffset(Mylock.class.getDeclaredField("state"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void lock() {
        while (true) {
            if (unsafe.compareAndSwapInt(this, stateOffset, 0, 1)) {
                System.err.println("thread : " + Thread.currentThread().getName() + "get lock");
                break;
            } else {
                unsafe.park(false, 0);
                map.put(Thread.currentThread().getName(), Thread.currentThread());
            }
        }

    }

    public void unlock() {
        unsafe.compareAndSwapInt(this, stateOffset, 1, 0);
        for (Map.Entry<String, Thread> entry : map.entrySet()) {
            unsafe.unpark(entry.getValue());
            map.remove(entry.getKey());
        }
        System.err.println("thread : " + Thread.currentThread().getName() + "release lock");
    }

}
