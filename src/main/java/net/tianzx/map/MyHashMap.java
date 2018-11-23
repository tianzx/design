package net.tianzx.map;

import java.util.HashMap;
import java.util.List;

/**
 * Author: tianzx
 * Date: 2018/11/21  9:22 PM
 * Email: zixuan.tian@nio.com
 */
public class MyHashMap<K, V> {

    private class Node {
        private List<?> list;
    }

    private Node[] nodes;

    public static void main(String[] args) {
        HashMap map = new HashMap(16);
        map.put(1, 1);
        System.err.println(map.size());
//        System.err.println(map.);
    }
}
