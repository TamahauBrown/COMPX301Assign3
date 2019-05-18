public class Dequeue{
    private class Node{
        Object value;
        Node next;
    }

    Node root;
    Node scan;
    Node tail;

    public Dequeue() {
        scan = new Node();
        scan.value = "SCAN";
        root = scan;
        tail = scan;
    }

    public void push(Object val){
        Node newRoot = new Node();
        newRoot.value = val;
        newRoot.next = root;
        root = newRoot;
    }

    public void pop(){

    }

    public void enqueue(Object val){
        if(tail == scan){
            tail = new Node();
            tail.value = val;
            scan.next = tail;
        } else {
            Node newNode = new Node();
            newNode.value = val;
            newNode.next = scan.next;
            scan.next = newNode;
        }
    }

    public void dequeue(){

    }

    public static void main(String[] args) {
        System.out.println("Dequeue");
        Dequeue d = new Dequeue();

        String value1 = "Top of stack";
        String value2 = "Tail";
        String value3 = "In queue";

        d.push(value1);

        d.enqueue(value2);
        d.enqueue(value3);

        System.out.println("Done");
    }
}