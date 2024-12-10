package de.hhu.cs.stups;

import de.hhu.cs.stups.algvis.gui.ContentPanel;
import de.hhu.cs.stups.algvis.gui.ToolBar;
import de.hhu.cs.stups.algvis.plugins.CFGtoTAC;
import de.hhu.cs.stups.algvis.plugins.DummyGraphPlugin;
import de.hhu.cs.stups.algvis.plugins.DummyPlugin;
import de.hhu.cs.stups.algvis.plugins.Plugin;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class PluginManager {
    public static Set<Plugin> plugins(){
        Set<Plugin> installedPlugins = new HashSet<>();

        installedPlugins.add(new DummyPlugin());
        installedPlugins.add(new DummyGraphPlugin());
        installedPlugins.add(new CFGtoTAC());

        return installedPlugins;
    }


    private final ContentPanel contentPanel;
    private final ToolBar toolBar;
    private final Set<Plugin> installedPlugins;
    private Plugin currentPlugin;
    public PluginManager(){
        contentPanel = new ContentPanel();
        toolBar = new ToolBar(this);
        installedPlugins = plugins();
    }

    public Set<Plugin> getInstalledPlugins() {
        return installedPlugins;
    }

    public ContentPanel getContentPanel(){
        return contentPanel;
    }

    public void resetCurrentPlugin(){
    }

    public void switchToPlugin(Plugin plugin) {
        currentPlugin = plugin;
        contentPanel.switchToPlugin(plugin);
        toolBar.setEnabledButtons(plugin.getEnabledToolBarButtons());
    }

    public void stepCurrentPlugin() {
        currentPlugin.doStep();
    }

    public JToolBar getToolbar() {
        return toolBar;
    }
}
