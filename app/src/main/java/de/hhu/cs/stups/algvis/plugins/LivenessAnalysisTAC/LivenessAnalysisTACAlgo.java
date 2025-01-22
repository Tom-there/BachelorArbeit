package de.hhu.cs.stups.algvis.plugins.LivenessAnalysisTAC;

import de.hhu.cs.stups.algvis.data.code.ThreeAddressCode;
import de.hhu.cs.stups.algvis.data.code.threeAddressCode.ThreeAddressCodeInstruction;

import java.util.*;

public class LivenessAnalysisTACAlgo {
    private final ThreeAddressCode code;
    private final List<Map<ThreeAddressCodeInstruction, Set<String>>> in;
    private final List<Map<ThreeAddressCodeInstruction, Set<String>>> out;
    private boolean changeInCurrentIteration, changeInLastIteration;
    private int currentIndex, currentIteration;
    public LivenessAnalysisTACAlgo(String rawCode) {
        code = new ThreeAddressCode(rawCode);
        in = new ArrayList<>(1);
        out = new ArrayList<>(1);
        currentIndex = code.size()-1;
        currentIteration = 0;
        changeInLastIteration = true;
        changeInCurrentIteration = false;
    }

    public boolean isFinished() {
        return !changeInLastIteration;
    }
    public void step() {
        if(!changeInLastIteration)
            return;
        if(currentIteration>= in.size())
            in.add(new HashMap<>(code.size()));
        if(currentIteration>= out.size())
            out.add(new HashMap<>(code.size()));

        //nur einen Basicblock anschauen
        lookAtInstruction(currentIndex);

        currentIndex--;
        if(currentIndex < 0) {
            currentIndex = code.size()-1;
            currentIteration++;
            changeInLastIteration = changeInCurrentIteration;
            changeInCurrentIteration = false;
        }
    }
    //returns true if something changed
    private void lookAtInstruction(int i){
        ThreeAddressCodeInstruction currentInstruction = code.get(i);
        Set<ThreeAddressCodeInstruction> successors = new HashSet<>();
        for (int successor:currentInstruction.nextPossibleInstructionAdresses()) {
            if(successor<code.size())
                successors.add(code.get(successor));
        }

        Set<String> usedIdentifiers = new HashSet<>(currentInstruction.getUsedIdentifiers());

        //out_i = U in_s s iterates all successors
        Set<String> currentOut = new HashSet<>();
        for (ThreeAddressCodeInstruction successor:successors) {
            Set<String> lastInSuccessor = (currentIteration>0)
                                        ? in.get(currentIteration-1).get(successor)
                                        : Set.of();
            currentOut.addAll(lastInSuccessor);
        }

        //in_i = use u (out - def)
        Set<String> currentIn = new HashSet<>(usedIdentifiers);

        if(currentIteration>0){
            if(!out.get(currentIteration-1).get(currentInstruction).equals(currentOut))
                changeInCurrentIteration = true;

            if(!out.get(currentIteration-1).get(currentInstruction).equals(currentOut))
                changeInCurrentIteration = true;

        }else{
            changeInCurrentIteration = true;
        }
        in.get(currentIteration).put(currentInstruction, currentIn);
        out.get(currentIteration).put(currentInstruction, currentOut);
    }

    //getters/setters
    public List<ThreeAddressCodeInstruction> getInstructions(){
        return code.getInstructions();
    }
    public ThreeAddressCode getCode(){return code;}
    public List<Map<ThreeAddressCodeInstruction, Set<String>>> getIn(){return in;}
    public List<Map<ThreeAddressCodeInstruction, Set<String>>> getOut(){return out;}
}
