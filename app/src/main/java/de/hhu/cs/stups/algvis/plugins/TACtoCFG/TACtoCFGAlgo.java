package de.hhu.cs.stups.algvis.plugins.TACtoCFG;

import de.hhu.cs.stups.algvis.data.structures.code.ThreeAddressCode;
import de.hhu.cs.stups.algvis.data.structures.graph.Edge;
import de.hhu.cs.stups.algvis.data.structures.graph.Node;

import java.util.*;

public class TACtoCFGAlgo {
    private List<ThreeAddressCode> code;
    private HashMap<String, Node> nodes;
    private HashSet<Edge> edges;
    private int currentLineNumber;
    private String lastLeader;
    private Mode mode;
    private enum Mode{findLeaders, mapLeaders}
    public TACtoCFGAlgo(String input){
        nodes = new HashMap<>();
        edges = new HashSet<>();
        code = ThreeAddressCode.listFromString(input);

        mode = Mode.findLeaders;
        currentLineNumber = 1;
    }
    public void step() {
        /* Nach Drachenbuch Algorithmus 8.5
         * IN: List of TAC
         * OUT: List of Basic Blocks and a mapping of every TAC instruction to a block
         * METHOD:
         *   1. find leaders
         *   1.1. first instruction is always a leader
         *   1.2. every instruction that is a destination of a jump is a leader
         *   1.3. every instruction that is follows a jump is a leader
         *   2. map every instruction to previous leader
         */
        final int currentLineIndex = currentLineNumber - 1;
        if (currentLineIndex < 0) {
            System.err.println("ERROR - current line number cannot be zero or negative");
            return;
        }
        switch (mode){
            case findLeaders -> {
                if(currentLineNumber == 1) {
                    //First Instruction is always a leader
                    makeIndexLeader(currentLineIndex);
                }
                else if(code.get(currentLineIndex).canJump()){
                    code.get(currentLineIndex).setComment("|");
                    //next line is a leader
                    makeIndexLeader(currentLineIndex+1);
                    //destination of jump is a leader
                    makeDestinationOfJumpLeader(currentLineIndex);
                }
                else{
                    code.get(currentLineIndex).setComment("|");
                }
                if ((currentLineNumber == code.size())) {
                    mode = Mode.mapLeaders;
                    currentLineNumber = 1;
                    return;
                }else if(currentLineNumber > code.size()){
                    System.err.println("ERR - tried to get a line number that does not exist(should be unreachable)");
                }
                currentLineNumber++;
            }
            case mapLeaders -> {
                if (!(currentLineIndex < code.size())) {
                    mode = null;
                    System.out.println("Done");
                    return;
                }
                else if(code.get(currentLineIndex).getComment().contains("Leader")){
                    lastLeader = code.get(currentLineIndex).getComment();
                }else{
                    if(code.get(currentLineIndex).canJump()) {
                        String destination = code.get(currentLineIndex).getDestination();
                        String nodeString = "Leader " + destination;
                        if (nodes.containsKey(nodeString)){
                            // currentBlock -> lastLeader(Block)
                            Node currentNode = nodes.get(lastLeader);
                            Node nextNode = nodes.get(nodeString);
                            edges.add(new Edge(currentNode, nextNode));
                        }else{
                            System.err.println("ERR - tried to find a leader that does not exist");
                        }
                    }
                    code.get(currentLineIndex).setComment("Block of " + lastLeader);
                }
                currentLineNumber++;
            }
            case null, default -> {
                currentLineNumber=1;
                return;
            }
        }
    }

    //adding leaders
    private void makeDestinationOfJumpLeader(int line){
        try {
            int destinationIndex = 1 - Integer.parseInt(code.get(line).getDestination());
            makeIndexLeader(destinationIndex);
        } catch (NumberFormatException e) {
            System.err.println("ERR - could not parse String '" + code.get(line).getDestination() + "' to a line number");
        }
    }
    private void makeIndexLeader(int index){
        try{
            lastLeader = "Leader " + (index+1);

            code.get(index).setComment(lastLeader);
            if(!nodes.containsKey(lastLeader))
                nodes.put(lastLeader, new Node(lastLeader, lastLeader));
        }catch (IndexOutOfBoundsException e){
            System.err.println("ERR - tried to set line " + (index+1) + " as leader. But it does not exist.");
        }
    }

    //getters
    public List<ThreeAddressCode> getCode(){
        return code;
    }
    public Collection<Node> getNodes() {
        return nodes.values();
    }
    public Collection<Edge> getEdges() {
        return edges;
    }
    public int getCurrentLineNumber() {
        return currentLineNumber;
    }
}