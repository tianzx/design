package net.tianzx.queue;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Author: tianzx
 * Date: 2018-12-15  18:46
 */
public class LockFreeQueue<T> {

    private static class Node<E> {
        E val;
        volatile Node<E> next;

        public Node(E val) {
            this.val = val;
        }
    }

    private AtomicReference<Node<T>> head, tail;

    public LockFreeQueue() {
        Node<T> dummyNode = new Node<T>(null);
        head = new AtomicReference<>(dummyNode);
        tail = new AtomicReference<>(dummyNode);
    }

    public void putObject(T value) {

    }

    public T getObject() {
        return null;
    }
}
