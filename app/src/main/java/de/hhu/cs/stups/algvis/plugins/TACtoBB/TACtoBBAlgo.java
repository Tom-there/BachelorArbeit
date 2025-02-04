package de.hhu.cs.stups.algvis.plugins.TACtoBB;

import de.hhu.cs.stups.algvis.data.code.ThreeAddressCode;
import de.hhu.cs.stups.algvis.data.code.threeAddressCode.ThreeAddressCodeInstruction;
import de.hhu.cs.stups.algvis.data.code.threeAddressCode.ThreeAddressCodeOperation;

import java.util.*;

public class TACtoBBAlgo {
    private final ThreeAddressCode code;
    private final Set<ThreeAddressCodeInstruction> leaders;
    private int currentInstructionAddress;
    private Mode mode;
    private enum Mode{findLeaders, mapLeaders, done}
    public TACtoBBAlgo(String input){
        code = new ThreeAddressCode(input);
        leaders = new HashSet<>(code.getBasicBlocks().size());
        currentInstructionAddress = 0;
        mode = Mode.findLeaders;
    }

    public boolean isFinished(){
        return mode == Mode.done;
    }
   public void step() {
       switch (mode){
           case findLeaders -> {
               if(currentInstructionAddress == 0) {
                   code.get(0).setComment("Leader");
                   leaders.add(code.get(0));
               }else
                   checkJump(currentInstructionAddress);
           }
           case mapLeaders -> mapAddressToItsBlock(currentInstructionAddress);
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
    private void checkJump(int address) {
        ThreeAddressCodeInstruction instruction = code.get(address);
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
    private void mapAddressToItsBlock(int address) {
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