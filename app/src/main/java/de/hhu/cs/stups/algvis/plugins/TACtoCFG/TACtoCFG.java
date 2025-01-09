package de.hhu.cs.stups.algvis.plugins.TACtoCFG;

import de.hhu.cs.stups.algvis.data.structures.code.Code;
import de.hhu.cs.stups.algvis.data.structures.Content;
import de.hhu.cs.stups.algvis.data.structures.graph.Graph;
import de.hhu.cs.stups.algvis.data.structures.graph.GraphMode;
import de.hhu.cs.stups.algvis.gui.Locator;
import de.hhu.cs.stups.algvis.gui.ToolBarButton;
import de.hhu.cs.stups.algvis.plugins.specs.LoadCodeFromFile;
import de.hhu.cs.stups.algvis.plugins.Plugin;
import de.hhu.cs.stups.algvis.plugins.specs.SimpleSteps;

import java.util.*;

public class TACtoCFG implements Plugin, SimpleSteps, LoadCodeFromFile {
    private final Code tac;
    private final Graph cfg;
    private TACtoCFGAlgo currentPluginInstance;


    private String currentlyLoadedCode;
    public TACtoCFG() {
        this.tac = new Code(Locator.left);
        this.cfg = new Graph(GraphMode.CodeInNode, Locator.center);
        currentlyLoadedCode = "empty";
    }

    //implementing Plugin
    @Override
    public String getName() {
        return "CFG to TAC";
    }
    @Override
    public void onPluginLoad() {
        reset();
    }
    @Override
    public Set<Content> getGuiElements() {
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
        tac.highlightLine(currentPluginInstance.getCurrentLineNumber());

        currentPluginInstance.getNodes().forEach(cfg::addNode);
        currentPluginInstance.getEdges().forEach(cfg::addEdge);
    }

    //implementing SimpleSteps
    @Override
    public void reset() {
        currentPluginInstance = new TACtoCFGAlgo(currentlyLoadedCode);
        refreshGuiElements();
    }
    @Override
    public void step() {
        currentPluginInstance.step();
        refreshGuiElements();
    }

    //implementing LoadCodeFromFile
    @Override
    public void loadCode(String code) {
        currentlyLoadedCode = code;
        reset();
    }
}