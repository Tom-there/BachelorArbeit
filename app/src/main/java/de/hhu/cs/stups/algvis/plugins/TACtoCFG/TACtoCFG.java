package de.hhu.cs.stups.algvis.plugins.TACtoCFG;

import de.hhu.cs.stups.algvis.data.code.threeAddressCode.ThreeAddressCodeInstruction;
import de.hhu.cs.stups.algvis.data.structures.Table;
import de.hhu.cs.stups.algvis.data.structures.graph.Edge;
import de.hhu.cs.stups.algvis.data.structures.graph.Node;
import de.hhu.cs.stups.algvis.data.DataRepresentation;
import de.hhu.cs.stups.algvis.data.structures.Graph;
import de.hhu.cs.stups.algvis.pluginSpecs.ToolBarButton;
import de.hhu.cs.stups.algvis.pluginSpecs.toolBarButtons.LoadCodeFromFile;
import de.hhu.cs.stups.algvis.pluginSpecs.Plugin;
import de.hhu.cs.stups.algvis.pluginSpecs.toolBarButtons.SimpleSteps;

import java.util.*;

public class TACtoCFG implements Plugin, SimpleSteps, LoadCodeFromFile {
    private final Table tac;
    private final Graph cfg;
    private HashMap<ThreeAddressCodeInstruction, Node> nodeMap;
    private TACtoCFGAlgo currentPluginInstance;
    private String currentlyLoadedCode;
    public TACtoCFG() {
        this.tac = new Table(DataRepresentation.Location.left);
        this.cfg = new Graph(DataRepresentation.Location.center);
        this.nodeMap = new HashMap<>();
        currentlyLoadedCode = "empty";
    }

    //implementing Plugin
    @Override
    public String getName() {
        return "3-Address-Code to Control Flow Graph";
    }
    @Override
    public void onPluginLoad() {
        reset();
    }
    @Override
    public Set<DataRepresentation> getGuiElements() {
        return Set.of(tac, cfg);
    }
    @Override
    public List<ToolBarButton> getToolBarButtons() {
        List<ToolBarButton> buttons = new LinkedList<>();
        buttons.add(LoadCodeFromFile.getButton(this));
        buttons.addAll(SimpleSteps.getButtons(this));
        return buttons;
    }
    public void refreshGuiElements() {
        //set TAC
        for (int i = 0; i < currentPluginInstance.getCode().size(); i++) {
            tac.setRowTo(currentPluginInstance.getCode().get(i).getRepresentationAsStringArray(), i);
        }
        tac.resizeColumnDisplay();
        tac.highlightLine(currentPluginInstance.getCurrentInstructionAddress());


        //set CFG
        List<ThreeAddressCodeInstruction> leaders = currentPluginInstance.getSortedLeaders();
        for (ThreeAddressCodeInstruction leader : currentPluginInstance.getSortedLeaders()) {
            //adding all Nodes
            if(!nodeMap.containsKey(leader)) {
                Node node = new Node();
                nodeMap.put(leader, node);
                cfg.addNode(nodeMap.get(leader));
            }
            cfg.changeLabelOfNode(nodeMap.get(leader), Integer.toString(leaders.indexOf(leader)));
        }
        Map<ThreeAddressCodeInstruction, Set<ThreeAddressCodeInstruction>> successorList = currentPluginInstance.getSuccessorMap();
        for (ThreeAddressCodeInstruction source:successorList.keySet()) {
            Node sourceNode = nodeMap.get(source);
            for (ThreeAddressCodeInstruction destination:successorList.get(source)) {
                Node destinationNode = nodeMap.get(destination);
                cfg.addEdge(new Edge(sourceNode, destinationNode));
            }
        }
    }

    //implementing SimpleSteps
    @Override
    public void reset() {
        currentPluginInstance = new TACtoCFGAlgo(currentlyLoadedCode);
        cfg.purge();
        nodeMap = new HashMap<>();
        tac.resizeTable(currentPluginInstance.getCode().size(), 8);
        refreshGuiElements();
    }
    @Override
    public void step() {
        currentPluginInstance.step();
        refreshGuiElements();
    }
    @Override
    public boolean hasNextStep() {
        return currentPluginInstance.hasNextStep();
    }

    //implementing LoadCodeFromFile
    @Override
    public void loadCode(String code) {
        currentlyLoadedCode = code;
        reset();
    }
}