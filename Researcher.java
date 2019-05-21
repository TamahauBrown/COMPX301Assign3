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
        root = addFSMNode(root, node, 0);
    }

    private FSMNode addFSMNode(FSMNode currentNode,  FSMNode newNode, int i) {
        if(currentNode == null){
            newNode.index = i;
            return newNode;
        } 
        currentNode.next = addFSMNode(currentNode.next, newNode, ++i);
        return currentNode;
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            String input;
            Researcher r = new Researcher();
            while(scanner.hasNextLine()){
                input = scanner.nextLine();
                input = input.trim();
                String[] fsmLine = input.split(" ", 4);
                
                FSMNode newNode = r.new FSMNode();
                newNode.ch = fsmLine[1];
                newNode.nextState1 = Integer.parseInt(fsmLine[2]);
                newNode.nextState2 = Integer.parseInt(fsmLine[3]);
                r.addFSMNode(newNode);
                System.out.println(input);
                

            }

            scanner.close();

            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}