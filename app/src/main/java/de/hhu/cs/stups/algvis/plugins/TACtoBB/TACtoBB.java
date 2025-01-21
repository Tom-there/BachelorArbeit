package de.hhu.cs.stups.algvis.plugins.TACtoBB;

import de.hhu.cs.stups.algvis.data.DataRepresentation;
import de.hhu.cs.stups.algvis.data.structures.table.Code;
import de.hhu.cs.stups.algvis.pluginSpecs.ToolBarButton;
import de.hhu.cs.stups.algvis.pluginSpecs.Plugin;
import de.hhu.cs.stups.algvis.pluginSpecs.LoadCodeFromFile;
import de.hhu.cs.stups.algvis.pluginSpecs.SimpleSteps;

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
        return "TAC to BB";
    }
    @Override
    public void onPluginLoad() {
        reset();
    }
    @Override
    public Set<DataRepresentation> getGuiElements() {
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
        tac.highlightLine(currentPluginInstance.getCurrentInstructionAddress());
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