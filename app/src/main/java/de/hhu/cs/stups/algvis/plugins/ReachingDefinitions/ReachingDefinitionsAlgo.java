package de.hhu.cs.stups.algvis.plugins.ReachingDefinitions;

import de.hhu.cs.stups.algvis.data.code.BasicBlock;
import de.hhu.cs.stups.algvis.data.code.ThreeAddressCode;
import de.hhu.cs.stups.algvis.data.code.threeAddressCode.ThreeAddressCodeInstruction;

import java.util.*;

public class ReachingDefinitionsAlgo {
    private final List<BasicBlock> basicBlocks;
    private final HashMap<BasicBlock, List<BasicBlock>> ancestors;
    private final ThreeAddressCode code;
    //____________list_map_blocks_ids____id
    private final List<Map<BasicBlock, Set<String>>> in;
    private final List<Map<BasicBlock, Set<String>>> out;
    private boolean changeInCurrentIteration, changeInLastIteration;
    private int currentIndex, currentIteration;
    public ReachingDefinitionsAlgo(String rawCode) {
        code = new ThreeAddressCode(rawCode);
        basicBlocks = code.getBasicBlocks();
        ancestors = new HashMap<>(basicBlocks.size());
        for (BasicBlock block:basicBlocks) {
            ancestors.put(block, new LinkedList<>());
        }
        for (BasicBlock block:basicBlocks) {
            List<Integer> firstAddressesOfSuccessors = block.firstAddressesOfSuccessors();
            for (int address:firstAddressesOfSuccessors) {
                for (BasicBlock potentialSuccessor:basicBlocks) {
                    if(potentialSuccessor.firstAddress() == address)
                        ancestors.get(potentialSuccessor).add(block);
                }
            }
        }
        in = new ArrayList<>(1);
        out = new ArrayList<>(1);
        currentIndex = 0;
        currentIteration = 0;
        changeInLastIteration = true;
        changeInCurrentIteration = false;
    }

    public boolean isNotFinished() {
        return changeInLastIteration;
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

        currentIndex++;
        if(currentIndex >= basicBlocks.size()) {
            currentIndex = 0;
            currentIteration++;
            changeInLastIteration = changeInCurrentIteration;
            changeInCurrentIteration = false;
        }
    }
    //returns true if something changed
    private void lookAtBlock(int i){
        BasicBlock currentBlock = basicBlocks.get(i);
        List<BasicBlock> ancestorBlocks = ancestors.get(currentBlock);
        List<String> generatedIdentifiers = currentBlock.gen().stream()
                                            .map(code::get)
                                            .map(ThreeAddressCodeInstruction::getDestination).toList();
        List<String> killedIdentifiers = currentBlock.kill().stream()
                                            .map(code::get)
                                            .map(ThreeAddressCodeInstruction::getDestination).toList();

        Set<String> currentIn = in.get(currentIteration).getOrDefault(currentBlock, new HashSet<>());

        for (BasicBlock ancestorBlock:ancestorBlocks) {
            Set<String> lastOut = (currentIteration>0) ? out.get(currentIteration-1).get(ancestorBlock) : Set.of();
            currentIn.addAll(lastOut);
        }

        Set<String> currentOut = new HashSet<>(currentIn);
        currentOut.removeAll(killedIdentifiers);
        currentOut.addAll(generatedIdentifiers);

        if(currentIteration>0){
            Set<String> lastOutMask = new HashSet<>(out.get(currentIteration-1).get(currentBlock));
            Set<String> currentOutMask = new HashSet<>(currentOut);
            if(!lastOutMask.equals(currentOutMask)){
                changeInCurrentIteration = true;
            }
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
}
