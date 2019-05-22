public class Deque{
    //Node to represent values in the Deque
    private class Node{
        Object value;        
        Node next;
    }

    // Handles for the root, scan and tail of the deque for easier management
    Node root;
    Node scan;
    Node tail;

    public Deque() {
        // Create the Deque with the SCAN node in place and set up handles
        scan = new Node();
        scan.value = "SCAN";
        root = scan;
        tail = scan;
    }

    // Push a value on to the stack
    public void push(Object val){
        Node newRoot = new Node();
        newRoot.value = val;
        newRoot.next = root;
        root = newRoot;
    }

    //Pop a value off of the stack or return null if the SCAN item is detected
    public Object pop(){
        if(root.value == "SCAN"){
            return null;
        }
        Object val = root.value;
        root = root.next;
        return val;
    }

    // Put a value into the queue
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