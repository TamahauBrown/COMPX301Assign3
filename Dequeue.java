public class Dequeue{
    public class Node{
        Object value;
        Node next;
    }

    public Dequeue() {
        super();
    }

    public void push(){

    }

    public void pop(){

    }

    public static void main(String[] args) {
        System.out.println("Dequeue");
        Dequeue d = new Dequeue();
        Node test = d.new Node();
        test.value = new String("Test");

        System.out.println(test.value);
    }
}