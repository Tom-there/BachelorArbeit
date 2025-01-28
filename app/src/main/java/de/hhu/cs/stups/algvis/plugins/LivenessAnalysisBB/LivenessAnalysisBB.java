package de.hhu.cs.stups.algvis.plugins.LivenessAnalysisBB;

import de.hhu.cs.stups.algvis.data.code.BasicBlock;
import de.hhu.cs.stups.algvis.data.DataRepresentation;
import de.hhu.cs.stups.algvis.data.code.threeAddressCode.ThreeAddressCodeInstruction;
import de.hhu.cs.stups.algvis.data.structures.Graph;
import de.hhu.cs.stups.algvis.data.structures.Table;
import de.hhu.cs.stups.algvis.data.structures.graph.Edge;
import de.hhu.cs.stups.algvis.data.structures.graph.Node;
import de.hhu.cs.stups.algvis.pluginSpecs.LoadCodeFromFile;
import de.hhu.cs.stups.algvis.pluginSpecs.Plugin;
import de.hhu.cs.stups.algvis.pluginSpecs.SimpleSteps;
import de.hhu.cs.stups.algvis.pluginSpecs.ToolBarButton;

import java.util.*;
import java.util.stream.Collectors;

public class LivenessAnalysisBB implements Plugin, LoadCodeFromFile, SimpleSteps {
    private final Table instructionList, dataFlow;
    private final Graph basicBlockRelationGraph;
    private LivenessAnalysisBBAlgo pluginInstance;
    private String currentlyLoadedCode;
    public LivenessAnalysisBB(){
        currentlyLoadedCode = "empty";
        this.instructionList = new Table(DataRepresentation.Location.left);
        this.dataFlow = new Table(DataRepresentation.Location.right);
        this.basicBlockRelationGraph = new Graph();
    }
    @Override
    public String getName() {
        return "Liveness Analysis for Basic Blocks";
    }
    @Override
    public void onPluginLoad() {
        reset();
    }
    @Override
    public Set<DataRepresentation> getGuiElements() {
        return Set.of(instructionList, basicBlockRelationGraph, dataFlow);
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
        List<BasicBlock> basicBlocks = pluginInstance.getBasicBlocks();
        //updating Code representation
        List<ThreeAddressCodeInstruction> code = new LinkedList<>();
        for (int i = 0; i < basicBlocks.size(); i++) {
            BasicBlock bb = basicBlocks.get(i);
            for (int j = bb.firstAddress(); j <= bb.lastAddress(); j++) {
                ThreeAddressCodeInstruction instruction = pluginInstance.getCode().get(j);
                instruction.setComment("B_"+i);
                code.add(instruction);
            }
        }
        instructionList.resizeTable(code.size(), 7);
        for (int i = 0; i < pluginInstance.getCode().size(); i++) {
            instructionList.setRowTo(pluginInstance.getCode().get(i).getRepresentationAsStringArray(), i);
        }

        //updating graph
        HashMap<Integer, Node> nodeMap = new HashMap<>();
        for (BasicBlock block : basicBlocks) {
            //adding all Nodes
            String nodeID = Integer.toString(block.firstAddress());
            String nodeLabel = code.get(block.firstAddress()).getComment();
            nodeMap.put(block.firstAddress(), new Node(nodeID, nodeLabel));
            basicBlockRelationGraph.addNode(nodeMap.get(block.firstAddress()));
        }
        for (BasicBlock basicBlock : basicBlocks) {
            //adding all Edges
            Node n = nodeMap.get(basicBlock.firstAddress());
            List<Integer> successors = basicBlock.firstAddressesOfSuccessors();
            successors.forEach(s -> basicBlockRelationGraph.addEdge(new Edge(n, nodeMap.get(s))));
        }

        //updating data flow
        List<Map<BasicBlock, Set<String>>> inTable = pluginInstance.getIn();
        List<Map<BasicBlock, Set<String>>> outTable = pluginInstance.getOut();
        int rows = pluginInstance.getIn().size()*2+3;
        int cols = basicBlocks.size()+1;
        dataFlow.resizeTable(rows, cols);

        //set Headers
        dataFlow.setValueAt("B:", 0, 0);
        for (int col = 0; col < basicBlocks.size(); col++) {
            dataFlow.setValueAt("B_"+col,0, col+1);
        }
        //print gen/kill sets
        dataFlow.setValueAt("def[B]", 1, 0);
        dataFlow.setValueAt("use[B]", 2, 0);
        for (int col = 0; col < basicBlocks.size(); col++) {
            BasicBlock block = basicBlocks.get(col);
            String genBlock = collectIdentifierSetToString(pluginInstance.getCode().def(block).stream().map(String::valueOf).collect(Collectors.toSet()));
            String killBlock = collectIdentifierSetToString(pluginInstance.getCode().use(block).stream().map(String::valueOf).collect(Collectors.toSet()));
            dataFlow.setValueAt(genBlock, 1, col+1);
            dataFlow.setValueAt(killBlock, 2, col+1);
        }

        //print each iteration of in and out sets
        for (int i = 0; i < inTable.size();i++) {
            int rowIndex = (i*2)+3;
            Map<BasicBlock, Set<String>> inMap = inTable.get(i);
            Map<BasicBlock, Set<String>> outMap = outTable.get(i);
            dataFlow.setValueAt("in[B]^"+i, rowIndex, 0);
            dataFlow.setValueAt("out[B]^"+i, rowIndex+1, 0);
            for (int j = 0; j < basicBlocks.size(); j++) {
                Set<String> inBlock = inMap.getOrDefault(basicBlocks.get(j), Set.of("-"));
                String inString = collectIdentifierSetToString(inBlock);
                if(inString.isEmpty())
                    dataFlow.setValueAt("", rowIndex, j+1);
                else
                    dataFlow.setValueAt(inString, rowIndex, j+1);

                Set<String> outBlock = outMap.getOrDefault(basicBlocks.get(j), Set.of("-"));
                String outString = collectIdentifierSetToString(outBlock);
                if(outString.isEmpty())
                    dataFlow.setValueAt("", rowIndex+1, j+1);
                else
                    dataFlow.setValueAt(outString, rowIndex+1, j+1);
            }
        }
    }

    private static String collectIdentifierSetToString(Set<String> identifiers) {
        StringBuilder ret = new StringBuilder();
        List<String> identifierList = identifiers.stream().toList();
        for (int k = 0; k < identifierList.size(); k++) {
            ret.append(identifierList.get(k));
            if(k+1 < identifierList.size())
                ret.append(", ");
        }
        return ret.toString();
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
        pluginInstance = new LivenessAnalysisBBAlgo(currentlyLoadedCode);
        basicBlockRelationGraph.purge();
        refreshGuiElements();
    }
    @Override
    public void step() {
        pluginInstance.step();
        refreshGuiElements();
    }
    @Override
    public boolean hasNextStep() {
        return !pluginInstance.isFinished();
    }
}
