package de.hhu.cs.stups.algvis.plugins.ReachingDefinitions;

import de.hhu.cs.stups.algvis.data.ThreeAddressCode;
import de.hhu.cs.stups.algvis.data.DataRepresentation;
import de.hhu.cs.stups.algvis.data.structures.code.Code;
import de.hhu.cs.stups.algvis.data.structures.graph.Edge;
import de.hhu.cs.stups.algvis.data.structures.graph.Graph;
import de.hhu.cs.stups.algvis.data.structures.graph.Node;
import de.hhu.cs.stups.algvis.pluginSpecs.ToolBarButton;
import de.hhu.cs.stups.algvis.pluginSpecs.Plugin;
import de.hhu.cs.stups.algvis.pluginSpecs.LoadCodeFromFile;
import de.hhu.cs.stups.algvis.pluginSpecs.SimpleSteps;

import java.util.*;

public class ReachingDefinitions implements Plugin, LoadCodeFromFile, SimpleSteps {
    private final Code basicBlocks;
    private final Graph blockRelations;
    private ReachingDefinitionsAlgo pluginInstance;
    private String currentlyLoadedCode;
    public ReachingDefinitions(){
        currentlyLoadedCode = "empty";
        this.basicBlocks = new Code(DataRepresentation.Location.left);
        this.blockRelations = new Graph();
    }
    @Override
    public String getName() {
        return "Reaching Definitions";
    }
    @Override
    public void onPluginLoad() {
        reset();
    }
    @Override
    public Set<DataRepresentation> getGuiElements() {
        return Set.of(basicBlocks, blockRelations);
    }
    @Override
    public List<ToolBarButton> getToolBarButtons() {
        List<ToolBarButton> buttons = new ArrayList<>();
        buttons.add(LoadCodeFromFile.getButton(this));
        buttons.addAll(SimpleSteps.getButtons(this));
        return buttons;
    }
    @Override
    public void refreshGuiElements() {
        List<BasicBlock> bbs = pluginInstance.getBasicBlocks().stream().toList();
        //updating basic block representation
        List<ThreeAddressCode> code = new LinkedList<>();
        for (int i = 0; i < bbs.size(); i++) {
            BasicBlock bb = bbs.get(i);
            for (ThreeAddressCode instruction:bb.instructions()) {
                instruction.setComment("B_"+i);
                code.add(instruction);
            }
        }
        basicBlocks.setCode(code);
        //updating graph
        HashMap<BasicBlock, Node> hm = new HashMap<>();
        for (int i = 0; i < bbs.size(); i++) {
            //adding all Nodes
            hm.put(bbs.get(i), new Node("B_"+i, "B_"+i));
            blockRelations.addNode(hm.get(bbs.get(i)));
        }
        for (BasicBlock bb : bbs) {
            //adding all Edges
            Node n = hm.get(bb);
            Collection<BasicBlock> successors = bb.successors();
            successors.forEach(s -> blockRelations.addEdge(new Edge(n, hm.get(s))));
        }

    }
    //implementing LoadCodeFromFile
    @Override
    public void loadCode(String s) {
        currentlyLoadedCode = s;
        reset();
    }
    //implementing SimpleSteps
    @Override
    public void reset() {
        pluginInstance = new ReachingDefinitionsAlgo(currentlyLoadedCode);
        refreshGuiElements();
    }
    @Override
    public void step() {
        pluginInstance.step();
        refreshGuiElements();
    }

    @Override
    public void run() {

    }
}
