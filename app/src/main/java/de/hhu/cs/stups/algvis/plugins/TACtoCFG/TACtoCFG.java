package de.hhu.cs.stups.algvis.plugins.TACtoCFG;

import de.hhu.cs.stups.algvis.data.structures.table.Code;
import de.hhu.cs.stups.algvis.data.DataRepresentation;
import de.hhu.cs.stups.algvis.data.structures.Graph;
import de.hhu.cs.stups.algvis.pluginSpecs.ToolBarButton;
import de.hhu.cs.stups.algvis.pluginSpecs.LoadCodeFromFile;
import de.hhu.cs.stups.algvis.pluginSpecs.Plugin;
import de.hhu.cs.stups.algvis.pluginSpecs.SimpleSteps;

import java.util.*;

public class TACtoCFG implements Plugin, SimpleSteps, LoadCodeFromFile {
    private final Code tac;
    private final Graph cfg;
    private TACtoCFGAlgo currentPluginInstance;
    private String currentlyLoadedCode;
    public TACtoCFG() {
        this.tac = new Code(DataRepresentation.Location.left);
        this.cfg = new Graph(DataRepresentation.Location.center);
        currentlyLoadedCode = "empty";
    }

    //implementing Plugin
    @Override
    public String getName() {
        return "TAC to CFG";
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
    @Override
    public void refreshGuiElements() {
        tac.setCode(currentPluginInstance.getCode());
        tac.highlightLine(currentPluginInstance.getCurrentInstructionAddress());

        currentPluginInstance.getNodes().forEach(cfg::addNode);
        currentPluginInstance.getEdges().forEach(cfg::addEdge);

    }

    //implementing SimpleSteps
    @Override
    public void reset() {
        currentPluginInstance = new TACtoCFGAlgo(currentlyLoadedCode);
        cfg.purge();
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