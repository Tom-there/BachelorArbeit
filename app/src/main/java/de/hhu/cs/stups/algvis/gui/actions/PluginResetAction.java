package de.hhu.cs.stups.algvis.gui.actions;

import de.hhu.cs.stups.PluginManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PluginResetAction implements ActionListener {
    private final PluginManager pluginManager;
    public PluginResetAction(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        pluginManager.resetCurrentPlugin();
    }
}
