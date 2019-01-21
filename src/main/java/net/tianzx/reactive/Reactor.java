package net.tianzx.reactive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Author: tianzx
 * Date: 2019-01-21  14:14
 */
public class Reactor implements Runnable {

    private Selector selector;
    private ServerSocketChannel ssc;
    private static final Logger LOGGER = LoggerFactory.getLogger(Reactor.class);

    public Reactor(int port) throws Exception {
        if (port <= 0 || port >= 65535) {
            throw new Exception("invalid port : " + port);
        }
        selector = Selector.open();
        ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(port));
        ssc.configureBlocking(false);
        SelectionKey sk = ssc.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new Acceptor());
    }

    @Override
    public void run() {
        while (true) {
            try {
                //need resolve 100% cpu bug
                selector.select();
                Set<SelectionKey> sks = selector.selectedKeys();
                Iterator it = sks.iterator();
                while (it.hasNext()) {
                    SelectionKey sk = (SelectionKey) it.next();
                    dispatch(sk);
                    sks.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void dispatch(SelectionKey k) {
        Runnable r = (Runnable) (k.attachment());
        if (r != null) {
            r.run();
        }
    }

    private class Acceptor implements Runnable {
        @Override
        public void run() {
            try {
                SocketChannel sc = ssc.accept();
                if (sc != null) {
                    new Handler(selector, sc);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    final class Handler implements Runnable {
        private SocketChannel sc;
        private SelectionKey sk;
        private ByteBuffer readBuf = ByteBuffer.allocate(1024);
        private ByteBuffer writeBuf = ByteBuffer.allocate(1024);
        private final int READING = 0;
        private final int WRITING = 1;
        private int state = READING;

        public Handler(Selector selector, SocketChannel socketChannel) {
            sc = socketChannel;
            try {
                sc.configureBlocking(false);
//                selector.
                sk = sc.register(selector, 0);
                sk.attach(this);
                selector.wakeup();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        boolean inputIsComplete() {
            return true;
        }

        boolean outputIsComplete() {
            return true;
        }

        void process() {

        }

        @Override
        public void run() {
            try {
                if (state == READING) {
                    read();
                } else {
                    write();
                }
            } catch (Exception e) {

            }
        }

        void read() {
            try {
                sc.read(readBuf);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (inputIsComplete()) {
                process();
                state = WRITING;
                sk.interestOps(SelectionKey.OP_WRITE);
            }
        }

        void write() {
            try {
                sc.write(writeBuf);
            } catch (IOException e) {
                e.printStackTrace();
            }
            sk.cancel();
        }
    }
}
