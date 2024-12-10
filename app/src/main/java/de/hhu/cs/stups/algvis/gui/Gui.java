package de.hhu.cs.stups.algvis.gui;

import de.hhu.cs.stups.PluginManager;
import de.hhu.cs.stups.algvis.gui.actions.PluginShowAction;
import de.hhu.cs.stups.algvis.plugins.Plugin;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Set;

public class Gui{
  private static final String WINDOW_TITLE = "AlgVis";

  private final JFrame frame;
  private final JMenuBar menuBar;
  private final JMenu pluginMenu;
  private final HashMap<JMenuItem, Plugin> pluginMap;
  private final JToolBar toolBar;

  private JMenuItem test;
  public Gui(PluginManager pluginManager) {
    // INIT Frame
    // CONTENTS: MenuBar - ContentPanel
    frame = new JFrame(WINDOW_TITLE);
    initJFrame();

    // ADD ContentPanel
    frame.add(pluginManager.getContentPanel(), BorderLayout.CENTER);

    // INIT MenuBar
    // CONTENTS: Plugins dropdown
    menuBar = new JMenuBar();
    menuBar.setBackground(Color.red);
    frame.add(menuBar, BorderLayout.NORTH);

    // INIT Plugins dropdown
    pluginMenu = new JMenu("Plugins");
    menuBar.add(pluginMenu);
    pluginMap = new HashMap<>();
    for (Plugin plugin : pluginManager.getInstalledPlugins()) {
      JMenuItem menuItem = new JMenuItem(plugin.getName());
      menuItem.addActionListener(new PluginShowAction(plugin, pluginManager, frame));
      pluginMenu.add(menuItem);
      pluginMap.put(menuItem, plugin);
    }

    // INIT toolbar
    // CONTENTS: next
    toolBar = pluginManager.getToolbar();
    frame.add(toolBar, BorderLayout.SOUTH);
    frame.setVisible(true);
  }

  private void initJFrame() {
    //set sizes
    frame.setMinimumSize(new Dimension(720, 480));
    frame.setPreferredSize(new Dimension(1600, 900));
    frame.setMaximumSize(new Dimension(1920, 1080));
    frame.setSize(frame.getPreferredSize());

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());
  }
}