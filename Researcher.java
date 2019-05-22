import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Researcher {
    private class FSMNode {
        int index;
        String ch;
        int nextState1;
        int nextState2;
        FSMNode next;
    }

    FSMNode root;
    Deque deque = new Deque();

    public Researcher() {
        root = new FSMNode();
        root.index = -1;
    }

    public void addFSMNode(FSMNode node) {
        root = addFSMNode(root, node);
    }

    private FSMNode addFSMNode(FSMNode currentNode, FSMNode newNode) {
        if (currentNode == null) {
            return newNode;
        }
        currentNode.next = addFSMNode(currentNode.next, newNode);
        return currentNode;
    }

    private FSMNode get(int index) {
        return get(root, index);
    }

    private FSMNode get(FSMNode n, int index) {
        while (n != null && n.index != index && n.index <= index) {
            n = n.next;
        }
        if (n == null || n.index != index) {
            return null;
        }
        return n;
    }

    public void readFSM() {
        try {
            Scanner scanner = new Scanner(System.in);
            String input;

            while (scanner.hasNextLine()) {
                input = scanner.nextLine();
                input = input.trim();
                if (!input.contains("EXIT")) {                    
                    String[] fsmLine = input.split(" ", 4);

                    FSMNode newNode = new FSMNode();
                    newNode.index = Integer.parseInt(fsmLine[0]);
                    newNode.ch = fsmLine[1];
                    newNode.nextState1 = Integer.parseInt(fsmLine[2]);
                    newNode.nextState2 = Integer.parseInt(fsmLine[3]);
                    addFSMNode(newNode);
                    System.out.println(input);
                }

            }

            scanner.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void searchLine(String line) {
        int currentState = 0;
        for (int i = 0; i < line.length(); i++) {
            boolean match = false;
            deque.push(get(currentState));

            FSMNode state = (FSMNode) deque.pop();

            if (state == null) {
                System.out.println(line);
                return;
            }
            //System.out.println(state.ch);
            if(state.ch.contains(".")){
                //System.out.println("here");
                currentState = state.nextState1;
                deque.push(get(currentState));
                state = (FSMNode) deque.pop();
            }
            // System.out.println(state.ch.charAt(0) + ": " + line.charAt(i));
            if (!isSpecial(state.ch)) {
                if (state.ch.charAt(0) == line.charAt(i)) {
                    match = true;
                    deque.put(get(state.nextState1));
                    // System.out.println(state.ch.charAt(0) + ": " + line.charAt(i));
                }
            } else {
                deque.push(get(state.nextState1));
                deque.push(get(state.nextState1));
            }

            if (match) {
                currentState = state.nextState1;
                if (i == line.length() - 1 && get(currentState) == null) {
                    System.out.println(line);
                    return;
                }
            } else {
                currentState = 0;
            }
        }

    }

    private boolean isSpecial(String ch) {
        if (ch == "|" || ch == "*") {
            return true;
        }
        return false;
    }

    public void searchFile(String fname) {
        File file = new File(fname);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s;
            while ((s = br.readLine()) != null) {
                // System.out.println(s);
                searchLine(s);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("File " + fname + " not found");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java Researcher <filename>");
            return;
        }
        Researcher r = new Researcher();
        r.readFSM();
        r.searchFile(args[0]);
    }
}