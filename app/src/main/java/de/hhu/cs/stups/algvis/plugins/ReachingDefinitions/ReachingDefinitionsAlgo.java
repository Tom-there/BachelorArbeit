package de.hhu.cs.stups.algvis.plugins.ReachingDefinitions;

import de.hhu.cs.stups.algvis.data.BasicBlock;
import de.hhu.cs.stups.algvis.data.ThreeAddressCode;
import de.hhu.cs.stups.algvis.data.ThreeAddressCodeInstruction;

import java.util.*;

public class ReachingDefinitionsAlgo {
    private final List<BasicBlock> basicBlocks;
    private final HashMap<BasicBlock, List<BasicBlock>> ancestors;
    private final ThreeAddressCode code;
    //____________list_map_blocks_ids____id
    private final List<Map<BasicBlock, List<String>>> in;
    private final List<Map<BasicBlock, List<String>>> out;
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

        List<String> currentIn = in.get(currentIteration).getOrDefault(currentBlock, new ArrayList<>());

        for (BasicBlock ancestorBlock:ancestorBlocks) {
            List<String> lastOut;
            if(currentIteration>0)
                lastOut = out.get(currentIteration-1).get(ancestorBlock);
            else
                lastOut = new ArrayList<>(0);
            currentIn.addAll(lastOut);
        }
        currentIn = currentIn.stream().sorted().distinct().toList();

        List<String> currentOut = new ArrayList<>(currentIn);
        currentOut.removeAll(killedIdentifiers);
        currentOut.addAll(generatedIdentifiers);

        //check for change todo
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
    public List<Map<BasicBlock, List<String>>> getIn(){return in;}
    public List<Map<BasicBlock, List<String>>> getOut(){return out;}
}
