package net.tianzx.leetcode;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: tianzx
 * Date: 2018/11/20  6:20 PM
 */
public class BinaryTree {
    //input [1,null,2,3]
    //output [1,3,2] inorder travle
    private static class TreeNode {
        public Integer val;
        public TreeNode left;
        public TreeNode right;

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("value", val)
                    .append("leftNode", left)
                    .append("rightNode", right)
                    .toString();
        }
    }

    List<Integer> list = new ArrayList();

    public List<Integer> inorderTraversal(TreeNode root) {
        if (root != null) {
            inorderTraversal(root.left);
            list.add(root.val);
            inorderTraversal(root.right);
        }
        return list;
    }
}
