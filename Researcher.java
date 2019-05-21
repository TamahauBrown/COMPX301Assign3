import java.io.IOException;
import java.util.Scanner;

public class Researcher {
    private class FSMNode{
        int index;
        String ch;
        int nextState1;
        int nextState2;
        FSMNode next;
    }

    FSMNode root;

    public Researcher(){
        root = new FSMNode();
        root.index = -1;
    }

    public void addFSMNode(FSMNode node){
        root = addFSMNode(root, node);
    }

    private FSMNode addFSMNode(FSMNode currentNode,  FSMNode newNode) {
        if(currentNode == null){            
            return newNode;
        } 
        currentNode.next = addFSMNode(currentNode.next, newNode);
        return currentNode;
    }

    public void readFSM(){
        try {
            Scanner scanner = new Scanner(System.in);
            String input;
            
            while(scanner.hasNextLine()){
                input = scanner.nextLine();
                input = input.trim();
                String[] fsmLine = input.split(" ", 4);
                
                FSMNode newNode = new FSMNode();
                newNode.ch = fsmLine[1];
                newNode.nextState1 = Integer.parseInt(fsmLine[2]);
                newNode.nextState2 = Integer.parseInt(fsmLine[3]);
                addFSMNode(newNode);
                System.out.println(input);                

            }

            scanner.close();

            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Researcher r = new Researcher();
        r.readFSM();
    }
}