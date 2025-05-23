package de.hhu.cs.stups.algvis.data.code;

import de.hhu.cs.stups.algvis.data.code.threeAddressCode.ThreeAddressCodeInstruction;
import de.hhu.cs.stups.algvis.data.code.threeAddressCode.ThreeAddressCodeOperation;

import java.util.*;
import java.util.stream.Collectors;

public class ThreeAddressCode{
    private final List<ThreeAddressCodeInstruction> code;
    private final List<BasicBlock> basicBlocks;
    public ThreeAddressCode(String raw){
        List<String> inputLines = raw.lines().toList();
        code = new ArrayList<>(inputLines.size());
        for (int i = 0; i < inputLines.size(); i++) {
            code.add(new ThreeAddressCodeInstruction(inputLines.get(i), i));
        }
        basicBlocks = toBBList(code);
    }
    private static List<BasicBlock> toBBList(List<ThreeAddressCodeInstruction> code){
        //get all leaders
        Set<ThreeAddressCodeInstruction> leaders = new HashSet<>(1);
        if(!code.isEmpty())
            leaders.add(code.get(0));
        for (int i = 0; i < code.size(); i++) {
            if(code.get(i).canJump()){
                ThreeAddressCodeInstruction destination = code.get(Integer.parseInt(code.get(i).getDestination()));
                leaders.add(destination);
                if(i+1<code.size())
                    leaders.add(code.get(i+1));
            }
        }
        List<ThreeAddressCodeInstruction> sortedLeaders = leaders.stream().sorted(Comparator.comparingInt(ThreeAddressCodeInstruction::getAddress)).toList();

        //map instruction to leader
        List<Integer> firstAddresses = sortedLeaders.stream().map(ThreeAddressCodeInstruction::getAddress).toList();
        List<Integer> lastAddresses = new ArrayList<>(leaders.size());
        List<List<Integer>> firstAddressesOfSuccessors = new ArrayList<>(leaders.size());
        for (int i = 1; i < sortedLeaders.size(); i++) {
            lastAddresses.add(firstAddresses.get(i)-1);
        }
        lastAddresses.add(code.getLast().getAddress());

        for (int lastAddress : lastAddresses) {
            List<Integer> successors = new ArrayList<>(0);
            firstAddressesOfSuccessors.add(successors);
            switch (code.get(lastAddress).getOperation()) {
                case jmp -> successors.add(Integer.valueOf(code.get(lastAddress).getDestination()));
                case booleanJump, negatedBooleanJump, eqJump, geJump, gtJump, leJump, ltJump, neJump -> {
                    successors.add(Integer.valueOf(code.get(lastAddress).getDestination()));
                    if (lastAddress+1 < code.size())
                        successors.add(lastAddress + 1);
                }
                default -> {
                    if (lastAddress+1 < code.size())
                        successors.add(lastAddress + 1);
                }
            }
        }
        List<BasicBlock> basicBlocks= new ArrayList<>(leaders.size());
        for (int i = 0; i < firstAddresses.size(); i++) {
            int firstAddress = firstAddresses.get(i);
            int lastAddress = lastAddresses.get(i);
            List<Integer> successors = firstAddressesOfSuccessors.get(i);
            BasicBlock basicBlock = new BasicBlock(firstAddress, lastAddress, successors);
            basicBlocks.add(basicBlock);
        }
        return basicBlocks;
    }
    public int size() {
        return code.size();
    }
    public ThreeAddressCodeInstruction get(int address) {
        return code.get(address);
    }
    public List<BasicBlock> getBasicBlocks() {
        return basicBlocks;
    }
    public List<ThreeAddressCodeInstruction> getInstructions() {
        return code;
    }
    public ThreeAddressCodeInstruction getLast() {
        return code.getLast();
    }
}
