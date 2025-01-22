package de.hhu.cs.stups.algvis.data.code;

import de.hhu.cs.stups.algvis.data.code.threeAddressCode.ThreeAddressCodeInstruction;

import java.util.*;

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
}
