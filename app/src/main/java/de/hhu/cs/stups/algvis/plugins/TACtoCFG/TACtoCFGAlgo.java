package de.hhu.cs.stups.algvis.plugins.TACtoCFG;

import de.hhu.cs.stups.algvis.data.code.ThreeAddressCode;
import de.hhu.cs.stups.algvis.data.code.threeAddressCode.ThreeAddressCodeInstruction;
import de.hhu.cs.stups.algvis.data.structures.graph.Edge;
import de.hhu.cs.stups.algvis.data.structures.graph.Node;

import java.util.*;

public class TACtoCFGAlgo {
    private final ThreeAddressCode code;
    private final HashMap<ThreeAddressCodeInstruction, Node> nodes;
    private final HashSet<Edge> edges;
    private final List<ThreeAddressCodeInstruction> leaders;
    private int currentInstructionAddress;
    private ThreeAddressCodeInstruction lastLeader;
    private Mode mode;
    private enum Mode{findLeaders, mapLeaders, done}
    public TACtoCFGAlgo(String input){
        nodes = new HashMap<>();
        edges = new HashSet<>();
        leaders = new ArrayList<>(1);
        code = new ThreeAddressCode(input);

        mode = Mode.findLeaders;
        currentInstructionAddress = 0;
    }
    public boolean hasNextStep(){
        return mode!=Mode.done;
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
        if (currentInstructionAddress < 0) {
            System.err.println("ERROR - Instruction Addresses cannot be negative");
            return;
        }
        switch (mode){
            case findLeaders -> {
                //check if currentInstructionAddress is valid
                if ((currentInstructionAddress == code.size())) {
                    mode = Mode.mapLeaders;
                    currentInstructionAddress = 0;
                    return;
                }else if(currentInstructionAddress > code.size()){
                    System.err.println("ERR - tried to get a line number that does not exist(should be unreachable)");
                }
                if(currentInstructionAddress == 0) {
                    //First Instruction is always a leader
                    makeIndexLeader(currentInstructionAddress);
                }else if(leaders.contains(code.get(currentInstructionAddress))){
                    System.out.println("current line is already a leader");
                }else if(code.get(currentInstructionAddress).canJump()){
                    code.get(currentInstructionAddress).setComment("end");
                    //next line is a leader
                    makeIndexLeader(currentInstructionAddress+1);
                    //destination of jump is a leader
                    makeDestinationOfJumpLeader(currentInstructionAddress);
                }
                else{
                    code.get(currentInstructionAddress).setComment("|");
                }

                currentInstructionAddress++;
            }
            case mapLeaders -> {
                 //exit if done
                if (!(currentInstructionAddress < code.size())) {
                    mode = Mode.done;
                    return;
                }
                ThreeAddressCodeInstruction currentInstruction = code.get(currentInstructionAddress);
                if(leaders.contains(code.get(currentInstructionAddress))){
                    //set current leader
                    lastLeader = currentInstruction;
                    //marks instruction as leader
                    currentInstruction.setComment(currentInstructionAddress + " Leader");
                }
                else{
                    //maps instruction to leader
                    currentInstruction.setComment(String.valueOf(lastLeader.getAddress()));
                }
                //if block ends here, tell to which Leaders it jumps
                List<ThreeAddressCodeInstruction> destinations = new LinkedList<>();
                if(currentInstruction.canJump())
                    destinations.add(code.get(Integer.parseInt(currentInstruction.getDestination())));
                if(currentInstructionAddress+1<code.size())
                    if(leaders.contains(code.get(currentInstructionAddress+1)))
                        destinations.add(code.get(currentInstructionAddress+1));
                makeInstructionJumpTo(currentInstruction, destinations);
                currentInstructionAddress++;

            }
            case null, default -> currentInstructionAddress =1;
        }
    }

    //adding edges
    private void makeInstructionJumpTo(ThreeAddressCodeInstruction instruction, List<ThreeAddressCodeInstruction> destinations){
        StringBuilder comment = new StringBuilder(instruction.getComment());
        if(!destinations.isEmpty())
            comment.append(" jumps to: ");
        for (int i = 0; i < destinations.size(); i++) {
            comment.append(destinations.get(i).getAddress());
            if(i+1 < destinations.size())
                comment.append(" and ");
            Node currentNode = nodes.get(lastLeader);
            Node nextNode = nodes.get(destinations.get(i));
            edges.add(new Edge(currentNode, nextNode));
        }
        instruction.setComment(comment.toString());
    }
    //adding leaders
    private void makeDestinationOfJumpLeader(int line){
        try {
            int destinationIndex = Integer.parseInt(code.get(line).getDestination());
            makeIndexLeader(destinationIndex);
        } catch (NumberFormatException e) {
            System.err.println("ERR - could not parse String '" + code.get(line).getDestination() + "' to a line number");
        }
    }
    private void makeIndexLeader(int index){
        try{
            ThreeAddressCodeInstruction lastLeader = code.get(index);
            code.get(index).setComment(index + " Leader");
            if(!leaders.contains(lastLeader))
                leaders.add(code.get(index));
            if(!nodes.containsKey(lastLeader))
                nodes.put(lastLeader, new Node(Integer.toString(index), Integer.toString(index)));
        }catch (IndexOutOfBoundsException e){
            System.err.println("ERR - tried to set line " + (index) + " as leader. But it does not exist.");
        }
    }

    //getters
    public ThreeAddressCode getCode(){
        return code;
    }
    public Collection<Node> getNodes() {
        return nodes.values();
    }
    public Collection<Edge> getEdges() {
        return edges;
    }
    public int getCurrentInstructionAddress() {
        return currentInstructionAddress;
    }
}