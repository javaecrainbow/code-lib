package com.salk.lib.lc.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author salkli
 * @since 2021/6/12
 **/
public class Tree<T extends Serializable> implements Serializable {
    private Node root = new Node(null, null);

    public Node getRoot() {
        return this.root;
    }

    public class Node {
        private T data;
        private List<Node> children;
        private Node parent;
        private int level;

        private Node(T data, Node parent) {
            this.data = data;
            this.parent = parent;
            if (parent == null) {
                level = 1;
            } else {
                //父亲的level+1
                level = parent.level + 1;
            }
        }

        public List<Node> getChildren() {
            return children;
        }

        public void addChild(T data) {
            if (children == null) {
                this.children = new ArrayList<>();
            }
            children.add(new Node(data, this));
        }

        public void removeChild(Node node) {
            if (children == null) {
                return;
            }
            Iterator<Node> iterator = children.iterator();
            while (iterator.hasNext()) {
                Node next = iterator.next();
                if (next.data.equals(node.data)) {
                    iterator.remove();
                    break;
                }
            }

        }

        public Node getChild(T data) {
            if (children == null) {
                return null;
            }
            Iterator<Node> iterator = children.iterator();
            while (iterator.hasNext()) {
                Node next = iterator.next();
                if (next.data.equals(data)) {
                    return next;
                }
            }
            return null;
        }

        public Node getParent() {
            return parent;
        }

        public void setData(T data) {
            this.data = data;
        }

        public int getLevel() {
            return this.level;
        }


    }


}
