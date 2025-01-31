package de.hhu.cs.stups.algvis.plugins.LivenessAnalysisTAC;

import de.hhu.cs.stups.algvis.data.DataRepresentation;
import de.hhu.cs.stups.algvis.data.code.threeAddressCode.ThreeAddressCodeInstruction;
import de.hhu.cs.stups.algvis.data.structures.Graph;
import de.hhu.cs.stups.algvis.data.structures.Table;
import de.hhu.cs.stups.algvis.data.structures.graph.Edge;
import de.hhu.cs.stups.algvis.data.structures.graph.Node;
import de.hhu.cs.stups.algvis.pluginSpecs.toolBarButtons.LoadCodeFromFile;
import de.hhu.cs.stups.algvis.pluginSpecs.Plugin;
import de.hhu.cs.stups.algvis.pluginSpecs.toolBarButtons.SimpleSteps;
import de.hhu.cs.stups.algvis.pluginSpecs.ToolBarButton;

import java.util.*;

public class LivenessAnalysisTAC implements Plugin, LoadCodeFromFile, SimpleSteps {
    private final Table dataFlow;
    private final Graph instructionFlowGraph;
    private LivenessAnalysisTACAlgo pluginInstance;
    private String currentlyLoadedCode;
    public LivenessAnalysisTAC(){
        currentlyLoadedCode = "empty";
        this.dataFlow = new Table(DataRepresentation.Location.right);
        this.instructionFlowGraph = new Graph();
    }
    @Override
    public String getName() {
        return "Liveness Analysis for single Instructions";
    }
    @Override
    public void onPluginLoad() {
        reset();
    }
    @Override
    public Set<DataRepresentation> getGuiElements() {
        return Set.of(instructionFlowGraph, dataFlow);
    }
    @Override
    public List<ToolBarButton> getToolBarButtons() {
        List<ToolBarButton> buttons = new ArrayList<>();
        buttons.add(LoadCodeFromFile.getButton(this));
        buttons.addAll(SimpleSteps.getButtons(this));
        return buttons;
    }
    public void refreshGuiElements() {
        List<ThreeAddressCodeInstruction> instructions = pluginInstance.getInstructions();
        //updating graph

        //updating data flow
        List<Map<ThreeAddressCodeInstruction, Set<String>>> inTable = pluginInstance.getIn();
        List<Map<ThreeAddressCodeInstruction, Set<String>>> outTable = pluginInstance.getOut();
        int rows = instructions.size()+1;
        int cols = (inTable.size()*2)+3;
        dataFlow.resizeTable(rows, cols);

        //set Headers
        dataFlow.setValueAt("Iterations:", 0, 0);
        //print gen/use sets
        dataFlow.setValueAt("def[i]", 0, 1);
        dataFlow.setValueAt("use[i]", 0, 2);
        for (int row = 0; row < instructions.size(); row++) {
            //Header
            String[] rep = new String[5];
            System.arraycopy(instructions.get(row).getRepresentationAsStringArray(), 1, rep, 0, 5);
            String instructionRepresentation = Arrays.stream(rep).reduce("", (a, b) -> a + " " + b);
            dataFlow.setValueAt(instructionRepresentation,row+1, 0);
            //def/use set
            String instructionDef = instructions.get(row).writesValue() ? instructions.get(row).getDestination() : "";
            String instructionUse = collectIdentifierSetToString(instructions.get(row).getUsedIdentifiers());
            dataFlow.setValueAt(instructionDef, row+1, 1);
            dataFlow.setValueAt(instructionUse, row+1, 2);
        }
        //print each iteration of in and out sets
        for (int currentIteration = 0; currentIteration < inTable.size();currentIteration++) {
            int colIndex = (currentIteration*2)+3;
            Map<ThreeAddressCodeInstruction, Set<String>> inMap = inTable.get(currentIteration);
            Map<ThreeAddressCodeInstruction, Set<String>> outMap = outTable.get(currentIteration);
            dataFlow.setValueAt("in[B]^"+currentIteration, 0, colIndex);
            dataFlow.setValueAt("out[B]^"+currentIteration, 0, colIndex+1);
            for (int currentInstructionAddress = 0; currentInstructionAddress < instructions.size(); currentInstructionAddress++) {
                Set<String> instructionIn = inMap.getOrDefault(instructions.get(currentInstructionAddress), Set.of("-"));
                String inString = collectIdentifierSetToString(instructionIn);
                if(inString.isEmpty())
                    dataFlow.setValueAt("", currentInstructionAddress+1, colIndex);
                else
                    dataFlow.setValueAt(inString, currentInstructionAddress+1, colIndex);

                Set<String> outBlock = outMap.getOrDefault(instructions.get(currentInstructionAddress), Set.of("-"));
                String outString = collectIdentifierSetToString(outBlock);
                if(outString.isEmpty())
                    dataFlow.setValueAt("", currentInstructionAddress+1, colIndex+1);
                else
                    dataFlow.setValueAt(outString, currentInstructionAddress+1, colIndex+1);
            }
        }
    }

    private static String collectIdentifierSetToString(Collection<String> identifiers) {
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
        pluginInstance = new LivenessAnalysisTACAlgo(currentlyLoadedCode);

        instructionFlowGraph.purge();
        HashMap<Integer, Node> nodeMap = new HashMap<>();
        for (ThreeAddressCodeInstruction instruction : pluginInstance.getInstructions()) {
            //generate representation
            String[] rep = new String[6];
            System.arraycopy(instruction.getRepresentationAsStringArray(), 1, rep, 0, 6);
            String nodeLabel = Arrays.stream(rep).filter(s -> !s.isEmpty()).reduce("", (a, b) -> a + " " + b);
            //add Node
            Node node = new Node();
            nodeMap.put(instruction.getAddress(), node);
            instructionFlowGraph.addNode(nodeMap.get(instruction.getAddress()));
            instructionFlowGraph.setLabelOfNode(node, nodeLabel);
        }
        for (ThreeAddressCodeInstruction instruction : pluginInstance.getInstructions()) {
            //adding all Edges
            Node n = nodeMap.get(instruction.getAddress());
            Set<Integer> successors = instruction.nextPossibleInstructionAdresses();
            successors.forEach(s -> {
                if(nodeMap.containsKey(s))
                    instructionFlowGraph.addEdge(new Edge(n, nodeMap.get(s)));
            });
        }

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
