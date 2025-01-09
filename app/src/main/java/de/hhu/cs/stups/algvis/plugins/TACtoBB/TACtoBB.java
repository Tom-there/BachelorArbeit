package de.hhu.cs.stups.algvis.plugins.TACtoBB;

import de.hhu.cs.stups.algvis.data.structures.Content;
import de.hhu.cs.stups.algvis.data.structures.code.Code;
import de.hhu.cs.stups.algvis.data.structures.graph.Graph;
import de.hhu.cs.stups.algvis.data.structures.graph.GraphMode;
import de.hhu.cs.stups.algvis.gui.Locator;
import de.hhu.cs.stups.algvis.gui.ToolBarButton;
import de.hhu.cs.stups.algvis.plugins.Plugin;
import de.hhu.cs.stups.algvis.plugins.specs.LoadCodeFromFile;
import de.hhu.cs.stups.algvis.plugins.specs.SimpleSteps;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TACtoBB implements Plugin, SimpleSteps, LoadCodeFromFile {
    private final Code tac;
    private TACtoBBAlgo currentPluginInstance;


    private String currentlyLoadedCode;
    public TACtoBB() {
        this.tac = new Code();
        currentlyLoadedCode = "empty";
    }

    //implementing Plugin
    @Override
    public String getName() {
        return "CFG to BB";
    }
    @Override
    public void onPluginLoad() {
        reset();
    }
    @Override
    public Set<Content> getGuiElements() {
        return Set.of(tac);
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
    }

    //implementing SimpleSteps
    @Override
    public void reset() {
        currentPluginInstance = new TACtoBBAlgo(currentlyLoadedCode);
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