package com.sanri.algorithm.a;

public class LinkedList {
    private Node first,last;

    class Node{
        private int data;
        private Node next;

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

    public LinkedList(){
        first = new Node();
        last = first;

        first.next = null;
        first.data = -1;
    }

    public void insertHeader(int data){
        Node current = new Node();
        current.data = data;

        current.next = first.next;
        first.next = current;

        last = current;
    }

    public void insertFoot(int data){
        Node current = new Node();
        current.data = data;

        current.next = last.next;
        last = current;
    }

    @Override
    public String toString() {
        Node current = first.next;
        if(current == null)return "";

        StringBuffer datas = new StringBuffer();
        while (current != null){
            datas.append(current.data+" ");
            current = current.next;
        }
        return datas.toString();
    }

    public static void main(String[] args) {
        LinkedList linkedList = new LinkedList();
//        linkedList.insertHeader(1);
//        linkedList.insertHeader(5);
//        linkedList.insertHeader(10);

        linkedList.insertFoot(1);
        linkedList.insertFoot(5);
        linkedList.insertFoot(10);
        System.out.println(linkedList);
    }
}
