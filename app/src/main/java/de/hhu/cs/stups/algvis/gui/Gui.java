package de.hhu.cs.stups.algvis.gui;

import de.hhu.cs.stups.algvis.gui.actions.PluginReset;
import de.hhu.cs.stups.algvis.gui.actions.PluginStep;
import de.hhu.cs.stups.algvis.gui.actions.showPlugin;
import de.hhu.cs.stups.algvis.plugins.Plugin;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class Gui{
  private static final String WINDOW_TITLE = "AlgVis";

  private final JFrame frame;
  private final JMenuBar menuBar;
  private final JMenu pluginMenu;
  private final HashMap<JMenuItem, Plugin> pluginMap;
  private final JToolBar toolBar;
  private final JButton nextButton, resetButton;
  private final ContentPanel contentPanel;

  private JMenuItem test;
  public Gui(List<Plugin> installedPlugins){
    // INIT Frame
    // CONTENTS: MenuBar - ContentPanel
    frame = new JFrame(WINDOW_TITLE);
    initJFrame();

    // ADD ContentPanel
    contentPanel = new ContentPanel();
    frame.add(contentPanel, BorderLayout.CENTER);

    // INIT MenuBar
    // CONTENTS: Plugins dropdown
    menuBar = new JMenuBar();
    menuBar.setBackground(Color.red);
    frame.add(menuBar, BorderLayout.NORTH);

    // INIT Plugins dropdown
    pluginMenu = new JMenu("Plugins");
    menuBar.add(pluginMenu);
    pluginMap = new HashMap<>();
    for (Plugin plugin : installedPlugins) {
      JMenuItem menuItem = new JMenuItem(plugin.getName());
      menuItem.addActionListener(new showPlugin(plugin, contentPanel, frame));
      pluginMenu.add(menuItem);
      pluginMap.put(menuItem, plugin);
    }

    // INIT toolbar
    // CONTENTS: next
    toolBar = new JToolBar();
    {
      nextButton = new JButton("step");
      nextButton.addActionListener(new PluginStep(contentPanel));
      toolBar.add(nextButton);

      resetButton = new JButton("reset");
      resetButton.addActionListener(new PluginReset(contentPanel));
      toolBar.add(resetButton);
    }
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