package com.salk.lib;

import com.salk.lib.lc.tree.Tree;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Tree tree=new Tree<Integer>();
        Tree.Node root = tree.getRoot();
        root.addChild(1);
        root.addChild(2);
        root.getChild(1).addChild(3);
        //Tree.Node node=new Tree.Node(5,root.getChild(2));
        System.out.println(root);
    }
}
