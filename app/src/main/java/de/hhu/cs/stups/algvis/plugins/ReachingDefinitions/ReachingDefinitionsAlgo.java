package de.hhu.cs.stups.algvis.plugins.ReachingDefinitions;

import de.hhu.cs.stups.algvis.data.code.BasicBlock;
import de.hhu.cs.stups.algvis.data.code.ThreeAddressCode;
import de.hhu.cs.stups.algvis.data.code.threeAddressCode.ThreeAddressCodeInstruction;

import java.util.*;
import java.util.stream.Collectors;

public class ReachingDefinitionsAlgo {
    private final List<BasicBlock> basicBlocks;
    private final HashMap<BasicBlock, List<BasicBlock>> ancestors;
    private final HashMap<BasicBlock, Set<Integer>> gen, kill;
    private final ThreeAddressCode code;
    //____________list_map_blocks_ids____id
    private final List<Map<BasicBlock, Set<String>>> in;
    private final List<Map<BasicBlock, Set<String>>> out;
    private boolean changeInCurrentIteration, changeInLastIteration;
    private int currentIndex, currentIteration;
    public ReachingDefinitionsAlgo(String rawCode) {
        code = new ThreeAddressCode(rawCode);
        basicBlocks = code.getBasicBlocks();

        //generate list of ancestors for each basic block
        //generate sets for gen and kill dataflow
        int size = basicBlocks.size();
        ancestors = new HashMap<>(size);
        gen = new HashMap<>(size);
        kill = new HashMap<>(size);
        for (BasicBlock block:basicBlocks) {
            ancestors.put(block, new LinkedList<>());
            gen.put(block, new HashSet<>());
            kill.put(block, new HashSet<>());
        }
        for (BasicBlock block:basicBlocks) {
            //ancestors
            List<Integer> firstAddressesOfSuccessors = block.firstAddressesOfSuccessors();
            for (int address:firstAddressesOfSuccessors) {
                for (BasicBlock potentialSuccessor:basicBlocks) {
                    if(potentialSuccessor.firstAddress() == address)
                        ancestors.get(potentialSuccessor).add(block);
                }
            }
            //gen
            Set<Integer> currentGenSet = gen.get(block);
            for (int i = block.lastAddress(); i >= block.firstAddress(); i--) {
                ThreeAddressCodeInstruction currentInstruction = code.get(i);
                if(!currentInstruction.writesValue())
                    continue;
                if(currentGenSet.contains(i))
                    continue;
                currentGenSet.add(i);
            }
            //kill
            Set<Integer> currentKillSet = kill.get(block);
            for (Integer i:currentGenSet) {
                for (int j = 0; j < code.size(); j++) {
                    if(!code.get(j).writesValue())
                        continue;
                    if(j == i)
                        continue;
                    if(code.get(j).getDestination().equals(code.get(i).getDestination()))
                        currentKillSet.add(j);
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
    private void lookAtBlock(int i){
        BasicBlock currentBlock = basicBlocks.get(i);
        List<BasicBlock> ancestorBlocks = ancestors.get(currentBlock);
        List<String> generatedIdentifiers = gen.get(currentBlock).stream().map(j -> code.get(j).getDestination()).toList();
        List<String> killedIdentifiers = kill.get(currentBlock).stream().map(j -> code.get(j).getDestination()).toList();

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
    public Map<BasicBlock, Set<Integer>> getGen(){return gen;}
    public Map<BasicBlock, Set<Integer>> getKill(){return kill;}

}
