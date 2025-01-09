package de.hhu.cs.stups.algvis.plugins.TACtoBB;

import de.hhu.cs.stups.algvis.data.structures.code.ThreeAddressCode;
import de.hhu.cs.stups.algvis.data.structures.graph.Edge;
import de.hhu.cs.stups.algvis.data.structures.graph.Node;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TACtoBBAlgo {
    private final List<ThreeAddressCode> code;
    private int currentLineNumber;
    public TACtoBBAlgo(String input){
        code = ThreeAddressCode.listFromString(input);
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

        if(currentLineNumber > code.size())
            System.err.println("WRN - done with algorithm. You can stop stepping.");

        currentLineNumber++;
    }

    //adding leaders
    private void makeDestinationOfJumpLeader(int line){
        try {
            int destinationIndex = 1 - Integer.parseInt(code.get(line).getDestination());
            code.get(line).setComment("| jumps to " + (1 + destinationIndex));
            makeIndexLeader(destinationIndex);
        } catch (NumberFormatException e) {
            System.err.println("ERR - could not parse String '" + code.get(line).getDestination() + "' to a line number");
        }
    }
    private void makeIndexLeader(int index){
        try{
            code.get(index).setComment("Leader " + (index + 1));
        }catch (IndexOutOfBoundsException e){
            System.err.println("ERR - tried to set line " + (index+1) + " as leader. But it does not exist.");
        }
    }

    //getters
    public List<ThreeAddressCode> getCode(){
        return code;
    }
    public int getCurrentLineNumber() {
        return currentLineNumber;
    }
}