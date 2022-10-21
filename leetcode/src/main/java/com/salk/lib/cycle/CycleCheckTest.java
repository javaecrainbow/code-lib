package com.salk.lib.cycle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author salkli
 **/
public class CycleCheckTest {
    private static List<Rule> list;
    static List<Node> tops=new ArrayList<>();

    private static void mockFile() {
        list = new ArrayList();
        list.add(new Rule("3", "4"));
        list.add(new Rule("1", "2"));
        list.add(new Rule("2", "3"));
        list.add(new Rule("2", "6"));
        list.add(new Rule("5", "3"));
        list.add(new Rule("6", "7"));
        list.add(new Rule("7", "8"));
        list.add(new Rule("8", "9"));
        list.add(new Rule("10", "2"));
    }
    public static void main(String[] args) {
        System.out.println("123");
        mockFile();
        for(Rule rule:list){
            System.out.println("current========"+rule.getCurrentData()+"reference===="+rule.getReferenceData());
            addNode(rule);
        }
        System.out.println(tops);
        dealNode("10");

    }
    public static void dealNode(String currentData){
        for(Node node:tops){
            Node node1 = node.findNode(currentData);
            if(node1==null){
                continue;
            }else{
                //doNext
                node1.execute();
            }

        }

    }


    private static boolean cycle(Rule rule){
        for(Node node:tops){
            Node node1 = node.findNode(rule.getCurrentData());
            if(node1!=null && node1.getNextNode(rule.getReferenceData())!=null){
                return true;
            }
        }
        return false;
    }


    public static void addNode(Rule rule) {
        // Node node1 = new Node(rule.getCurrentData()).addNext(rule.getReferenceData(), null);
        boolean exist = false;
        Node currentNode = null;
        for (Node topNode : tops) {
            Node node1 = topNode.findNode(rule.getCurrentData());
            if (node1 != null) {
                exist = true;
                currentNode = node1;
                break;
            }
        }
        if (!exist) {
            Node node1 = new Node(rule.getCurrentData()).addNext(rule.getReferenceData(), tops);
            tops.add(node1);
            return;
        }
        // 查找当前rule的node的
        Node node1 = currentNode.findNode(rule.getCurrentData());
        if (node1 != null) {
            node1.addNext(rule.getReferenceData(), tops);
            return;
        }
        Node node2 = new Node(rule.getCurrentData()).addNext(rule.getReferenceData(), tops);
        tops.add(node2);
    }

    static class Rule {
        private String currentData;
        private String referenceData;

        public Rule(String currentData, String referenceData) {
            this.currentData = currentData;
            this.referenceData = referenceData;
        }

        public String getCurrentData() {
            return currentData;
        }

        public void setCurrentData(String currentData) {
            this.currentData = currentData;
        }

        public String getReferenceData() {
            return referenceData;
        }

        public void setReferenceData(String referenceData) {
            this.referenceData = referenceData;
        }
    }

    static class Node {
        private String data;
        private List<Node> next = new ArrayList();
        private List<Node> before = new ArrayList<>();

        public Node() {
        }


        public Node addNext(String date, List<Node> tops) {
            // 查看tops里面是不是有
            Node node =null;
            if(tops!=null) {
                //查找node
                for (Node topNode : tops) {
                    Node childNode = topNode.findNode(date);
                    if (childNode == null) {
                        continue;
                    }
                    node= childNode;
                }
            }
            if (node == null) {
                node = new Node(date);
            }

            next.add(node);
            //remove top
            Iterator<Node> iterator = tops.iterator();
            while(iterator.hasNext()){
                Node next = iterator.next();
                if(next.getData().equals(date)){
                    iterator.remove();
                }
            }
            //node.addBefore(node);
            return this;

        }
        public Node addBefore(Node node){
            before.add(node);
            return this;
        }

        public Node(String data) {
            this.data = data;
        }
        public Node(String data,Node node) {
            this.data = data;
            before.add(node);
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public List<Node> getNext() {
            return next;
        }

        public void setNext(List<Node> next) {
            this.next = next;
        }

        public List<Node> getBefore() {
            return before;
        }

        public void setBefore(List<Node> before) {
            this.before = before;
        }


        public Node findNode(String data) {
            if (this.getData()!=null && this.getData().equals(data)) {
                return this;
            }
            if (next.size() != 0) {
                return getNextNode(data);
            }
            return null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()){
                return false;

            }
            Node node = (Node)o;

            return data.equals(node.data);
        }

        @Override
        public int hashCode() {
            return data.hashCode();
        }

        public void execute(){
            for (Node node : next) {
                if (node.getNext() != null || node.getNext().size()>0) {
                    node.execute();
                }
            }
            System.out.println("数据===="+this.data + " begin execute" +System.currentTimeMillis());
        }



        public Node getNextNode(String data) {
            //只有一个服务
            for (Node node : next) {
                Node childNode = node.findNode(data);
                if (childNode == null) {
                    continue;
                }
                return childNode;
            }
            return null;
        }

        /**
         * 执行child逻辑
         * @return
         */
        public Node doNextNode() {
            //只有一个服务
            for (Node node : next) {
                node.execute();
                Node childNode = node.findNode(data);
                if (childNode == null) {
                    node.execute();
                    continue;
                }
                return childNode;
            }
            return null;
        }



        public Node getBeforeNode(String data) {
            return before.stream().filter(item -> item!=null && item.getData().equals(data)).findFirst().orElse(null);
        }
    }
}
