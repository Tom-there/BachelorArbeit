package de.hhu.cs.stups.algvis.plugins.LivenessAnalysisBB;

import de.hhu.cs.stups.algvis.data.code.BasicBlock;
import de.hhu.cs.stups.algvis.data.code.ThreeAddressCode;
import de.hhu.cs.stups.algvis.data.code.threeAddressCode.ThreeAddressCodeInstruction;

import java.util.*;

public class LivenessAnalysisBBAlgo {
    private final List<BasicBlock> basicBlocks;
    private final HashMap<BasicBlock, List<BasicBlock>> successors;
    private final HashMap<BasicBlock, Set<String>> def, use;
    private final ThreeAddressCode code;
    private final List<Map<BasicBlock, Set<String>>> in;
    private final List<Map<BasicBlock, Set<String>>> out;
    private boolean changeInCurrentIteration, changeInLastIteration;
    private int currentIndex, currentIteration;
    public LivenessAnalysisBBAlgo(String rawCode) {
        code = new ThreeAddressCode(rawCode);
        basicBlocks = code.getBasicBlocks();
        int size = basicBlocks.size();
        successors = new HashMap<>(size);
        def = new HashMap<>(size);
        use = new HashMap<>(size);
        for (BasicBlock block:basicBlocks) {
            successors.put(block, new LinkedList<>());
            def.put(block, new HashSet<>());
            use.put(block, new HashSet<>());
        }
        for (BasicBlock block:basicBlocks) {
            //successors
            List<Integer> firstAddressesOfSuccessors = block.firstAddressesOfSuccessors();
            for (int address:firstAddressesOfSuccessors) {
                for (BasicBlock potentialSuccessor:basicBlocks) {
                    if(potentialSuccessor.firstAddress() == address)
                        successors.get(block).add(potentialSuccessor);
                }
            }
            //def
            Set<String> currentDefSet = def.get(block);
            for (int i = block.lastAddress(); i >= block.firstAddress(); i--) {
                ThreeAddressCodeInstruction instruction = code.get(i);
                if(!instruction.writesValue())
                    continue;
                String identifier = instruction.getDestination();
                currentDefSet.add(identifier);
                for (int j = 0; j < i; j++) {
                    if(code.get(j).getUsedIdentifiers().contains(identifier))
                        currentDefSet.remove(identifier);
                }
            }
            //use
            Set<String> currentUseSet = use.get(block);
            for (int i = block.lastAddress(); i >= block.firstAddress(); i--) {
                ThreeAddressCodeInstruction instruction = code.get(i);
                for (String used:instruction.getUsedIdentifiers()) {
                    currentUseSet.add(used);
                    for (int j = block.firstAddress(); j < i; j++) {
                        if(code.get(j).writesValue() && code.get(j).getDestination().equals(used))
                            currentUseSet.remove(used);
                    }
                }
            }
        }
        in = new ArrayList<>(1);
        out = new ArrayList<>(1);
        currentIndex = basicBlocks.size()-1;
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
            in.add(new HashMap<>(basicBlocks.size()));
        if(currentIteration>= out.size())
            out.add(new HashMap<>(basicBlocks.size()));

        //nur einen Basicblock anschauen
        lookAtBlock(currentIndex);

        currentIndex--;
        if(currentIndex < 0) {
            currentIndex = basicBlocks.size()-1;
            currentIteration++;
            changeInLastIteration = changeInCurrentIteration;
            changeInCurrentIteration = false;
        }
    }
    private void lookAtBlock(int i){
        BasicBlock currentBlock = basicBlocks.get(i);
        List<BasicBlock> successorBlocks = successors.get(currentBlock);

        //out_i = U in_s s iterates all successors
        Set<String> currentOut = new HashSet<>();
        for (BasicBlock successor:successorBlocks) {
            Set<String> lastInSuccessor = (currentIteration>0)?in.get(currentIteration-1).get(successor) : Set.of();
            currentOut.addAll(lastInSuccessor);
        }

        //in_i = use u (out - def)
        Set<String> currentIn = new HashSet<>(currentOut);
        currentIn.removeAll(def.get(currentBlock));
        currentIn.addAll(use.get(currentBlock));

        if(currentIteration>0){
            if(!out.get(currentIteration-1).get(currentBlock).equals(currentOut))
                changeInCurrentIteration = true;

            if(!out.get(currentIteration-1).get(currentBlock).equals(currentOut))
                changeInCurrentIteration = true;

        }else{
            changeInCurrentIteration = true;
        }
        in.get(currentIteration).put(currentBlock, currentIn);
        out.get(currentIteration).put(currentBlock, currentOut);
    }

    //getters/setters
    public List<BasicBlock> getBasicBlocks(){
        return basicBlocks;
    }
    public ThreeAddressCode getCode(){return code;}
    public List<Map<BasicBlock, Set<String>>> getIn(){return in;}
    public List<Map<BasicBlock, Set<String>>> getOut(){return out;}
    public Map<BasicBlock, Set<String>> getDef(){return def;}
    public Map<BasicBlock, Set<String>> getUse(){return use;}
}
