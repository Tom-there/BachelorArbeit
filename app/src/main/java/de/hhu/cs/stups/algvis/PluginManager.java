package de.hhu.cs.stups.algvis;

import de.hhu.cs.stups.algvis.gui.ContentPanel;
import de.hhu.cs.stups.algvis.gui.ToolBar;
import de.hhu.cs.stups.algvis.plugins.TACtoBB.TACtoBB;
import de.hhu.cs.stups.algvis.plugins.TACtoCFG.TACtoCFG;
import de.hhu.cs.stups.algvis.plugins.Plugin;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class PluginManager {
    public static Set<Plugin> plugins(){
        Set<Plugin> installedPlugins = new HashSet<>();

        installedPlugins.add(new TACtoCFG());
        installedPlugins.add(new TACtoBB());

        return installedPlugins;
    }


    private final ContentPanel contentPanel;
    private final ToolBar toolBar;
    private final Set<Plugin> installedPlugins;

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

    public void switchToPlugin(Plugin plugin) {
        plugin.onPluginLoad();
        contentPanel.switchToPlugin(plugin);
        toolBar.setEnabledButtons(plugin.getToolBarButtons());
    }

    public JToolBar getToolbar() {
        return toolBar;
    }
}
