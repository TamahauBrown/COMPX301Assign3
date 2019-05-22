/**
 * REsearcher
 * ==========================
 * Tamahau Brown - 1314934
 * Troy Dean - 0222566
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class REsearcher {
    // A node for representing an FSM state
    private class FSMNode {
        int index;
        String ch;
        int nextState1;
        int nextState2;
        FSMNode next;
    }

    FSMNode root;
    Deque deque = new Deque();

    public REsearcher() {
        //Create a root node to build the list of states
        root = new FSMNode();
        root.index = -1;
    }

    // Add node to list off states
    public void addFSMNode(FSMNode node) {        
        root = addFSMNode(root, node);
    }

    // Add new node at tail of list of states
    private FSMNode addFSMNode(FSMNode currentNode, FSMNode newNode) {
        if (currentNode == null) {
            return newNode;
        }
        currentNode.next = addFSMNode(currentNode.next, newNode);
        return currentNode;
    }

    // Get a node withthe index
    public FSMNode get(int index) {
        return get(root, index);
    }

    // Find and return the correct node or null if that index is not found
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
            // Read output of REcompiler line by line from std in
            Scanner scanner = new Scanner(System.in);
            String input;

            while (scanner.hasNextLine()) {
                input = scanner.nextLine();
                input = input.trim();
                //Look for exit message on compiler output
                if (!input.contains("EXIT")) {            
                    // Split string into components        
                    String[] fsmLine = input.split(" ", 4);

                    //Create a new node to represnt the state being read
                    FSMNode newNode = new FSMNode();
                    newNode.index = Integer.parseInt(fsmLine[0]);
                    newNode.ch = fsmLine[1];
                    newNode.nextState1 = Integer.parseInt(fsmLine[2]);
                    newNode.nextState2 = Integer.parseInt(fsmLine[3]);

                    //Add the new state node to the list
                    addFSMNode(newNode);
                    
                }

            }

            scanner.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void searchLine(String line) {
        // set current state to 0
        int currentState = 0;
        for (int i = 0; i < line.length(); i++) {
            //work through the line to be checked character by character
            boolean match = false;
            //Get the current state and push it to the stack
            deque.push(get(currentState));

            //Get the current state from the stack
            FSMNode state = (FSMNode) deque.pop();

            //if the state is null a match has been found (all states traversed)
            if (state == null) {
                System.out.println(line);
                return;
            }
            
            // Handle . operator
            if(state.ch.contains(".")){            
                currentState = state.nextState1;
                deque.push(get(currentState));
                state = (FSMNode) deque.pop();
            }
            // Handle non special operators
            if (!isSpecial(state.ch)) {
                if (state.ch.charAt(0) == line.charAt(i)) {
                    match = true;
                    deque.put(get(state.nextState1));                    
                }
            } else {
                // Handle branch states
                deque.push(get(state.nextState1));
                deque.push(get(state.nextState2));                
            }

            if (match) {
                // If a match is found move to the next character and state
                currentState = state.nextState1;
                if (i == line.length() - 1 && get(currentState) == null) {
                    // If this is the last character in the string, check if the state list has been exhausted
                    // if so this is a match 
                    System.out.println(line);
                    return;
                }
            } else {
                // If character was not a match reset current state and move to the next character in the string
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
        //Read a file in line by line
        File file = new File(fname);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s;
            while ((s = br.readLine()) != null) {                
                //Search each line for at least 1 match ogf the regex
                searchLine(s);
            }
        } catch (FileNotFoundException e) {            
            System.out.println("File " + fname + " not found");
        } catch (IOException e) {            
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java Researcher <filename>");
            return;
        }
        REsearcher r = new REsearcher();
        r.readFSM();
        r.searchFile(args[0]);
    }
}