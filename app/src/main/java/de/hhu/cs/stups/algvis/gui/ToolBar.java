package de.hhu.cs.stups.algvis.gui;

import de.hhu.cs.stups.PluginManager;
import de.hhu.cs.stups.algvis.gui.actions.PluginLoadFileAction;
import de.hhu.cs.stups.algvis.gui.actions.PluginResetAction;
import de.hhu.cs.stups.algvis.gui.actions.PluginStepAction;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;

public class ToolBar extends JToolBar {

    private final HashMap<ToolBarButtons, JButton> buttons;
    public ToolBar(PluginManager pluginManager){
        super();
        buttons = new HashMap<>();

        JButton stepButton = new JButton("step");
        stepButton.addActionListener(new PluginStepAction(pluginManager));
        buttons.put(ToolBarButtons.step, stepButton);

        JButton resetButton = new JButton("reset");
        resetButton.addActionListener(new PluginResetAction(pluginManager));
        buttons.put(ToolBarButtons.reset, resetButton);

        JButton loadButton = new JButton("load");
        loadButton.addActionListener(new PluginLoadFileAction(pluginManager));
        buttons.put(ToolBarButtons.load, loadButton);
    }

    public void setEnabledButtons(List<ToolBarButtons> enabledButtons){
        this.removeAll();
        for(ToolBarButtons id : enabledButtons){
            this.add(buttons.get(id));
        }
    }
}
