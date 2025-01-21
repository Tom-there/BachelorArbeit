package de.hhu.cs.stups.algvis.data;

import java.util.*;

public record BasicBlock(List<ThreeAddressCodeInstruction> fullCode, int firstAddress, int lastAddress, List<Integer> firstAddressesOfSuccessors){

    public List<Integer> gen(){
        return instructions()
                .stream()
                .filter(ThreeAddressCodeInstruction::writesValue)
                .map(ThreeAddressCodeInstruction::getAddress).toList();
    }

    public List<Integer> kill(){
        //get gen list
        List<String> genList = gen().stream()
            .map(fullCode::get)
            .map(ThreeAddressCodeInstruction::getDestination).toList();
        //see which instructions also write to the identifiers of gen list items
        List<ThreeAddressCodeInstruction> instructionsThatWriteToGenListIdentifiers = new LinkedList<>();
        for (String identifier:genList) {
            for (ThreeAddressCodeInstruction instruction:fullCode) {
                if(instruction.getDestination().equals(identifier))
                    instructionsThatWriteToGenListIdentifiers.add(instruction);
            }
        }
        //filter out gen list items and return
        return instructionsThatWriteToGenListIdentifiers.stream().filter( i -> {
            return ((i.getAddress()<firstAddress)
                ||  (i.getAddress()>lastAddress));})
                .map(ThreeAddressCodeInstruction::getAddress).toList();
    }

    private List<ThreeAddressCodeInstruction> instructions(){
        List<ThreeAddressCodeInstruction> instructions = new ArrayList<>(lastAddress-firstAddress);
        for (int i = firstAddress; i < lastAddress+1; i++){
            instructions.add(fullCode.get(i));
        }
        return instructions;
    }
    //Gen Basic Blocks
    public static List<BasicBlock> toBBList(List<ThreeAddressCodeInstruction> code){
        //get indices of all leaders
        Set<ThreeAddressCodeInstruction> leaders = new HashSet<>(1);
        if(!code.isEmpty())
            leaders.add(code.get(0));
        for (int i = 0; i < code.size(); i++) {
            if(code.get(i).canJump()){
                ThreeAddressCodeInstruction destination = code.get(Integer.parseInt(code.get(i).getDestination()));
                leaders.add(destination);
                if(i+1<code.size() && code.get(i).getOperation()!= ThreeAddressCodeInstruction.Operation.jmp)
                    leaders.add(code.get(i+1));
            }
        }
        List<ThreeAddressCodeInstruction> sortedLeaders = leaders.stream().sorted((a, b) -> a.getAddress() - b.getAddress()).toList();

        List<Integer> firstAddresses = sortedLeaders.stream().map(ThreeAddressCodeInstruction::getAddress).toList();
        List<Integer> lastAddresses = new ArrayList<>(leaders.size());
        List<List<Integer>> firstAddressesOfSuccessors = new ArrayList<>(leaders.size());
        for (int i = 1; i < sortedLeaders.size(); i++) {
            lastAddresses.add(firstAddresses.get(i)-1);
        }
        lastAddresses.add(code.getLast().getAddress());

        for (int i = 0; i < lastAddresses.size(); i++) {
            int lastAddress = lastAddresses.get(i);
            List<Integer> successors = new ArrayList<>(0);
            firstAddressesOfSuccessors.add(successors);
            switch(code.get(lastAddress).getOperation()){
                case jmp -> {
                    successors.add(Integer.valueOf(code.get(lastAddress).getDestination()));
                }
                case booleanJump, negatedBooleanJump -> {
                    successors.add(Integer.valueOf(code.get(lastAddress).getDestination()));
                    if(lastAddress > code.size()-1)
                        System.err.println("WRN - at end of code, cannot add new successor to conditional jump");//todo: proper warning message
                    else
                        successors.add(lastAddress+1);
                }
                default -> {
                    if(lastAddress > code.size()-2)
                        System.err.println("WRN - at end of code, did not add a new successor");//todo: proper warning message
                    else
                        successors.add(lastAddress+1);
                }
            }
        }
        List<BasicBlock> basicBlocks= new ArrayList<>(leaders.size());
        for (int i = 0; i < firstAddresses.size(); i++) {
            int firstAddress = firstAddresses.get(i);
            int lastAddress = lastAddresses.get(i);
            List<Integer> successors = firstAddressesOfSuccessors.get(i);
            BasicBlock basicBlock = new BasicBlock(code, firstAddress, lastAddress, successors);
            basicBlocks.add(basicBlock);
        }
        return basicBlocks;
    }
}
