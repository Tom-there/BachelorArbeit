package de.hhu.cs.stups.algvis.plugins.ReachingDefinitions;

import de.hhu.cs.stups.algvis.data.ThreeAddressCode;

import java.util.*;

public record BasicBlock(List<ThreeAddressCode> instructions, Collection<BasicBlock> successors){

    public static Collection<BasicBlock> toBBCollection(List<ThreeAddressCode> code){
        //get indices of all leaders
        List<Integer> leaders = new ArrayList<>(1);
        for (int i = 0; i < code.size(); i++) {
            if(code.get(i).canJump()){
                leaders.add(i);
                leaders.add(i+1);
            } else if(i==0){
                leaders.add(0);
            }
        }

        //generates BasicBlock object
        HashMap<BasicBlock, List<Integer>> followingIndicesMap = new HashMap<>();
        HashMap<Integer, BasicBlock> startIndexMap = new HashMap<>();
        int currentLeader, nextLeader;
        for (int i = 0; i < leaders.size(); i++) {
            currentLeader=leaders.get(i);
            //gets the index of next leader or last index+1
            if(i > leaders.size()-2) {
                nextLeader = leaders.size();
            }else{
                nextLeader = leaders.get(i + 1);
            }

            //get all instructions for each block
            List<ThreeAddressCode> codeInBlock = new ArrayList<>();
            for (int j = currentLeader; j < nextLeader; j++) {
                codeInBlock.add(code.get(j));
            }
            //get all successors for each block
            List<Integer> successorIndices = new ArrayList<>(2);
            switch(code.get(nextLeader-1).getOperation()){
                case jmp -> {
                    successorIndices.add(Integer.parseInt(code.get(nextLeader-1).getDestination()));
                }
                case booleanJump, negatedBooleanJump -> {
                    successorIndices.add(Integer.parseInt(code.get(nextLeader-1).getDestination()));
                    successorIndices.add(nextLeader);
                }
                default -> {
                    System.out.println("Block ends without jump? must be end of file.");
                }
            }
            BasicBlock bb = new BasicBlock(codeInBlock, new ArrayList<>(successorIndices.size()));
            followingIndicesMap.put(bb, successorIndices);
            startIndexMap.put(currentLeader, bb);
        }


        //Translate Hashmaps to Collection
        Collection<BasicBlock> collection = new HashSet<>(followingIndicesMap.size());
        for (BasicBlock bb : followingIndicesMap.keySet()){
            List<Integer> followingIndices = followingIndicesMap.get(bb);
            for(Integer followingIndex : followingIndices){
                bb.successors.add(startIndexMap.get(followingIndex));
            }
            collection.add(bb);
        }
        return collection;
    }
}
