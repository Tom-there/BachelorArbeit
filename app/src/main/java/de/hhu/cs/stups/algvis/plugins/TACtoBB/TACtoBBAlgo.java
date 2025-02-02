package de.hhu.cs.stups.algvis.plugins.TACtoBB;

import de.hhu.cs.stups.algvis.data.code.ThreeAddressCode;
import de.hhu.cs.stups.algvis.data.code.threeAddressCode.ThreeAddressCodeInstruction;
import de.hhu.cs.stups.algvis.data.code.threeAddressCode.ThreeAddressCodeOperation;

import java.util.*;

public class TACtoBBAlgo {
    private final ThreeAddressCode code;
    private final Set<ThreeAddressCodeInstruction> leaders;
    private final Map<ThreeAddressCodeInstruction, Set<ThreeAddressCodeInstruction>> successorMap;
    private int currentInstructionAddress;
    private Mode mode;
    private enum Mode{findLeaders, mapLeaders, done}
    public TACtoBBAlgo(String input){
        code = new ThreeAddressCode(input);
        leaders = new HashSet<>(code.getBasicBlocks().size());
        successorMap = new HashMap<>(code.getBasicBlocks().size());
        currentInstructionAddress = 0;
        mode = Mode.findLeaders;
    }

    public boolean isFinished(){
        return mode == Mode.done;
    }
   public void step() {
       if (currentInstructionAddress < 0) {
           System.err.println("ERROR - Instruction Addresses cannot be negative");
           return;
       }
       switch (mode){
           case findLeaders -> checkIfAddressIsALeader(currentInstructionAddress);
           case mapLeaders -> mapAddressToItsLeader(currentInstructionAddress);
           case done -> System.out.println("No more steps");
           case null -> {
               System.err.println("ERR - Mode was null, this should not happen");
               currentInstructionAddress = 0;
           }
       }
       currentInstructionAddress++;
       if ((currentInstructionAddress == code.size()) && mode == Mode.findLeaders) {
           mode = Mode.mapLeaders;
           currentInstructionAddress = 0;
       }
       if (!(currentInstructionAddress < code.size()) && mode == Mode.mapLeaders)
           mode = Mode.done;
    }

    private void checkIfAddressIsALeader(int address) {
        ThreeAddressCodeInstruction instruction = code.get(address);
        if(address == 0) {
            instruction.setComment("Leader");
            leaders.add(instruction);
        }
        if(instruction.canJump()) {
            ThreeAddressCodeInstruction destination = code.get(Integer.parseInt(instruction.getDestination()));
            leaders.add(destination);
            destination.setComment("Leader");
            if(address+1 < code.size() && instruction.getOperation() != ThreeAddressCodeOperation.jmp) {
                ThreeAddressCodeInstruction nextInstruction = code.get(address + 1);
                leaders.add(nextInstruction);
                nextInstruction.setComment("Leader");
            }
        }
    }

    private void mapAddressToItsLeader(int address) {
        ThreeAddressCodeInstruction instruction = code.get(address);
        if(leaders.contains(instruction)){
            //do leader things
            instruction.setComment("B_" + getSortedLeaders().indexOf(instruction) + " Leader");
        }else{
            ThreeAddressCodeInstruction lastLeader = getPreviousLeader(address);
            instruction.setComment("B_" + getSortedLeaders().indexOf(lastLeader));
        }
        if(code.getLast().equals(instruction)) {
            if(instruction.canJump()){
                StringBuilder postfix = new StringBuilder(" jumps to ");
                ThreeAddressCodeInstruction nextInstruction = instruction.nextPossibleInstructionAdresses().stream().map(code::get).min(ThreeAddressCodeInstruction::compareTo).get();
                postfix.append(getSortedLeaders().indexOf(nextInstruction));
                successorMap.put(getPreviousLeader(address), new HashSet<>(Set.of(nextInstruction)));
                instruction.setComment(instruction.getComment() + postfix.toString());
            }
        }else if(leaders.contains(code.get(address+1))) {
            StringBuilder postfix = new StringBuilder(" jumps to ");
            List<ThreeAddressCodeInstruction> nextInstructions = instruction.nextPossibleInstructionAdresses().stream().map(code::get).toList();
            for (int i = 0; i < nextInstructions.size(); i++) {
                postfix.append(getSortedLeaders().indexOf(nextInstructions.get(i)));
                if(i+1<nextInstructions.size())
                    postfix.append(", ");
            }
            successorMap.put(getPreviousLeader(address), new HashSet<>(nextInstructions));
            instruction.setComment(instruction.getComment() + postfix);
        }

        if(code.getLast().equals(instruction)) {
            instruction.setComment(instruction.getComment() + " EOF");
        }
    }
    private ThreeAddressCodeInstruction getPreviousLeader(int threshold) {
        ThreeAddressCodeInstruction closestLeader = getSortedLeaders().getFirst();
        for (ThreeAddressCodeInstruction leader:leaders) {
            if(leader.getAddress() > closestLeader.getAddress() && leader.getAddress() <= threshold)
                closestLeader = leader;
        }
        return closestLeader;
    }
    //getters
    public ThreeAddressCode getCode(){
        return code;
    }
    public List<ThreeAddressCodeInstruction> getSortedLeaders(){
        return leaders.stream().map(ThreeAddressCodeInstruction::getAddress).sorted().map(code::get).toList();
    }
    public int getCurrentInstructionAddress() {
        return currentInstructionAddress;
    }
}