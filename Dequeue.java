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

    public void enqueue(){

    }

    public void dequeue(){

    }

    public static void main(String[] args) {
        System.out.println("Dequeue");
        Dequeue d = new Dequeue();

        String value = "Test";
        d.push(value);

        System.out.println(value);
    }
}