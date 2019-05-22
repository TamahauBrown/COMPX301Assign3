public class Deque{
    private class Node{
        Object value;        
        Node next;
    }

    Node root;
    Node scan;
    Node tail;

    public Deque() {
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

    public Object pop(){
        if(root.value == "SCAN"){
            return null;
        }
        Object val = root.value;
        root = root.next;
        return val;
    }

    public void put(Object val){
        tail.next = new Node();
        tail = tail.next;
        tail.value = val;
    }    

    public static void main(String[] args) {
        System.out.println("Dequeue");
        Deque d = new Deque();

        String value1 = "Top of stack";
        String value2 = "1st in";
        String value3 = "2nd in";

        d.push(value1);

        d.put(value2);
        d.put(value3);

        System.out.println(d.pop());
        System.out.println(d.pop());
        System.out.println(d.pop());
        System.out.println(d.pop());
    }
}