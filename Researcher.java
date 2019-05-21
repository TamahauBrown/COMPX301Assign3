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
    }

    public void addFSMNode(FSMNode node){
        root = addFSMNode(node, 0);
    }

    private FSMNode addFSMNode(FSMNode node, int i) {
        return null;
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            String input;

            while(scanner.hasNextLine()){
                input = scanner.nextLine();
                String[] fsmLine = input.split(" ", 4);

                System.out.println(input);
                

            }

            scanner.close();

            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}