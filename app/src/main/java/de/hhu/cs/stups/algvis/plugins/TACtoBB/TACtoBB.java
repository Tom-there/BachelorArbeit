package de.hhu.cs.stups.algvis.plugins.TACtoBB;

import de.hhu.cs.stups.algvis.data.DataRepresentation;
import de.hhu.cs.stups.algvis.data.structures.Table;
import de.hhu.cs.stups.algvis.pluginSpecs.ToolBarButton;
import de.hhu.cs.stups.algvis.pluginSpecs.Plugin;
import de.hhu.cs.stups.algvis.pluginSpecs.toolBarButtons.LoadCodeFromFile;
import de.hhu.cs.stups.algvis.pluginSpecs.toolBarButtons.SimpleSteps;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TACtoBB implements Plugin, SimpleSteps, LoadCodeFromFile {
    private final Table code;
    private TACtoBBAlgo pluginInstance;
    private String currentlyLoadedCode;
    public TACtoBB() {
        this.code = new Table();
        currentlyLoadedCode = "empty";
    }

    //implementing Plugin
    @Override
    public String getName() {
        return "3-Address-Code to Basic Blocks";
    }
    @Override
    public void onPluginLoad() {
        reset();
    }
    @Override
    public Set<DataRepresentation> getGuiElements() {
        return Set.of(code);
    }
    @Override
    public List<ToolBarButton> getToolBarButtons() {
        List<ToolBarButton> buttons = new LinkedList<>();
        buttons.add(LoadCodeFromFile.getButton(this));
        buttons.addAll(SimpleSteps.getButtons(this));
        return buttons;
    }

    public void refreshGuiElements() {
        for (int i = 0; i < pluginInstance.getCode().size(); i++) {
            code.setRowTo(pluginInstance.getCode().get(i).getRepresentationAsStringArray(), i);
        }
        code.highlightLine(pluginInstance.getCurrentInstructionAddress());
    }

    //implementing SimpleSteps
    @Override
    public void reset() {
        pluginInstance = new TACtoBBAlgo(currentlyLoadedCode);
        code.resizeTable(pluginInstance.getCode().size(), 8);
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

    //implementing LoadCodeFromFile
    @Override
    public void loadCode(String code) {
        currentlyLoadedCode = code;
        reset();
    }
}