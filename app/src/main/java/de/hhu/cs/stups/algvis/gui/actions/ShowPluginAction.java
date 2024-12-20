package de.hhu.cs.stups.algvis.gui.actions;

import de.hhu.cs.stups.PluginManager;
import de.hhu.cs.stups.algvis.plugins.Plugin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowPluginAction implements ActionListener {
    private final Plugin plugin;
    private final PluginManager pluginManager;
    private final JFrame frame;
    public ShowPluginAction(Plugin plugin, PluginManager pluginManager, JFrame frame){
        this.plugin = plugin;
        this.pluginManager = pluginManager;
        this.frame = frame;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        pluginManager.switchToPlugin(plugin);
        frame.setVisible(true);
    }
}
