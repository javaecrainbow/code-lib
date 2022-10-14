package com.salk.lib.linktable;

/**
 * @author salkli
 * @since 2022/9/26
 **/
public class CycleLinkTest {


    public static void main(String[] args) {
        MyNode myNode = mockData();
        boolean cycle = isCycle(myNode);
        if(cycle){
            System.out.println("有环链表");
        }else{
            System.out.println("无环链表");
        }

    }

    public static boolean isCycle(MyNode myNode) {
        MyNode node1 = myNode;
        MyNode node2 = myNode;
        while (node1.getNext() != null && node1.getNext().getNext() != null) {
            node1 = node1.getNext().getNext();
            node2 = node2.getNext();
            if (node1 == node2) {
                break;
            }
        }
        if (node1.getNext() != null && node1.getNext().getNext() != null) {
            return true;
        } else {
            return false;
        }

    }


    /**
     * 5-> 4-> 3->2->1->3
     * @return
     */
    public static MyNode mockData() {
        MyNode node1 = new MyNode(1);
        MyNode node2 = new MyNode(node1, 2);
        MyNode node3 = new MyNode(node2, 3);
        MyNode node4 = new MyNode(node3, 4);
        MyNode node5 = new MyNode(node4, 5);
        node1.setNext(node3);
        return node5;

    }

}
