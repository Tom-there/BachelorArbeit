package de.hhu.cs.stups.algvis.plugins.ReachingDefinitions;

import de.hhu.cs.stups.algvis.data.ThreeAddressCode;

import java.util.Collection;

public class ReachingDefinitionsAlgo {
    private final Collection<BasicBlock> basicBlocks;
    public ReachingDefinitionsAlgo(String code) {
        //TODO: load code directly as a CFG as well
        var tac = ThreeAddressCode.listFromString(code);
        basicBlocks = BasicBlock.toBBCollection(tac);
        System.out.println("test");
    }

    public void step() {

    }

    //getters/setters
    public Collection<BasicBlock> getBasicBlocks(){
        return basicBlocks;
    }
}
