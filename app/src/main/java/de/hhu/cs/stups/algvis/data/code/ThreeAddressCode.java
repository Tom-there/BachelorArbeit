package de.hhu.cs.stups.algvis.data.code;

import de.hhu.cs.stups.algvis.data.code.threeAddressCode.ThreeAddressCodeInstruction;

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

        basicBlocks = BasicBlock.toBBList(code);
    }
    private List<ThreeAddressCodeInstruction> instructionsOfBasicBlock(BasicBlock block){
        List<ThreeAddressCodeInstruction> instructions = new ArrayList<>(block.lastAddress()-block.firstAddress());
        for (int i = block.firstAddress(); i < block.lastAddress()+1; i++){
            instructions.add(code.get(i));
        }
        return instructions;
    }

    public Set<Integer> gen(BasicBlock block){
        return instructionsOfBasicBlock(block)
                .stream()
                .filter(ThreeAddressCodeInstruction::writesValue)
                .map(ThreeAddressCodeInstruction::getAddress)
                .collect(Collectors.toSet());
    }
    public Set<Integer> kill(BasicBlock block){
        //get Identifiers of gen set
        Set<String> genList = gen(block).stream()
                .map(code::get)
                .map(ThreeAddressCodeInstruction::getDestination)
                .collect(Collectors.toSet());
        //get all instructionsOfBasicBlock that write to the identifiers of gen set
        Set<ThreeAddressCodeInstruction> instructionsThatWriteToGenListIdentifiers = new HashSet<>();
        for (String identifier:genList) {
            for (ThreeAddressCodeInstruction instruction:code) {
                if(instruction.getDestination().equals(identifier))
                    instructionsThatWriteToGenListIdentifiers.add(instruction);
            }
        }
        //filter out gen set items and return
        return instructionsThatWriteToGenListIdentifiers.stream()
                .filter(i -> ((i.getAddress()<block.firstAddress()) || (i.getAddress()>block.lastAddress())))
                .map(ThreeAddressCodeInstruction::getAddress)
                .collect(Collectors.toSet());
    }
    public Set<String> def(BasicBlock block){
        List<ThreeAddressCodeInstruction> instructions = instructionsOfBasicBlock(block);
        Set<String> definedIdentifiers = new HashSet<>();
        for (int i = 0; i < instructions.size(); i++) {
            ThreeAddressCodeInstruction instruction = instructions.get(i);
            if(!instruction.writesValue())
                break;
            String identifier = instruction.getDestination();
            definedIdentifiers.add(identifier);
            for (int j = 0; j < i; j++) {
                if(instructions.get(j).getUsedIdentifiers().contains(identifier))
                    definedIdentifiers.remove(identifier);
            }
        }
        return definedIdentifiers;
    }
    public Set<String> use(BasicBlock block){
        List<ThreeAddressCodeInstruction> instructions = instructionsOfBasicBlock(block);
        Set<String> usedIdentifiers = new HashSet<>();
        for (int i = instructions.size()-1; i > -1; i--) {
            ThreeAddressCodeInstruction instruction = instructions.get(i);
            usedIdentifiers.remove(instruction.getDestination());
            usedIdentifiers.addAll(instruction.getUsedIdentifiers());
        }
        return usedIdentifiers;
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
