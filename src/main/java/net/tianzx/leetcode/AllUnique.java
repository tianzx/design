package net.tianzx.leetcode;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: tianzx
 * Date: 2018/11/19  5:46 PM
 */
public class AllUnique {

    public static int allUnique(String s) {
        int length = 1;

        if (s.isEmpty()) {
            return 0;
        } else {
            int len = s.length();
            for (int index = 0; index < len - 1; index++) {
                Set set = new HashSet();
                set.add(s.charAt(index));
                for (int sIndex = index + 1; sIndex < len; sIndex++) {
                    if (!set.contains(s.charAt(sIndex))) {
                        set.add(s.charAt(sIndex));
                    } else {
                        break;
                    }
                }
                if (length < set.size()) {
                    length = set.size();
                }
            }
        }
        return length;
    }

    public static void main(String[] args) {
        System.err.println(allUnique("abcabcbb"));
        System.err.println(allUnique("abcabcdefbb"));
        System.err.println(allUnique("pwwkew"));
        System.err.println(allUnique("bbbbb"));
        System.err.println(allUnique(""));
        System.err.println(allUnique("b"));
    }
}
