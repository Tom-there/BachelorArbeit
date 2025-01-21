package de.hhu.cs.stups.algvis.plugins.TACtoBB;

import de.hhu.cs.stups.algvis.data.ThreeAddressCode;
import de.hhu.cs.stups.algvis.data.ThreeAddressCodeInstruction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TACtoBBAlgo {
    private final ThreeAddressCode code;
    private final List<ThreeAddressCodeInstruction> leaders;
    private int currentInstructionAddress;
    private ThreeAddressCodeInstruction lastLeader;
    private Mode mode;
    private enum Mode{findLeaders, mapLeaders, done}
    public TACtoBBAlgo(String input){
        leaders = new ArrayList<>(1);
        code = new ThreeAddressCode(input);

        currentInstructionAddress = 0;
        mode = Mode.findLeaders;
    }

    public boolean hasNextStep(){
        return mode != Mode.done;
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
            case null, default -> currentInstructionAddress = 1;
        }
    }

    private void makeInstructionJumpTo(ThreeAddressCodeInstruction instruction, List<ThreeAddressCodeInstruction> destinations){
        StringBuilder comment = new StringBuilder(instruction.getComment());
        if(!destinations.isEmpty())
            comment.append(" jumps to: ");
        for (int i = 0; i < destinations.size(); i++) {
            comment.append(destinations.get(i).getAddress());
            if(i+1 < destinations.size())
                comment.append(" and ");
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
         }catch (IndexOutOfBoundsException e){
            System.err.println("ERR - tried to set line " + (index) + " as leader. But it does not exist.");
        }
    }

    //getters
    public ThreeAddressCode getCode(){
        return code;
    }
    public int getCurrentInstructionAddress() {
        return currentInstructionAddress;
    }
}