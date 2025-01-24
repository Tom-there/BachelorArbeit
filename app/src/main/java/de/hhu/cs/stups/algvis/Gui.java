package de.hhu.cs.stups.algvis;

import de.hhu.cs.stups.algvis.data.DataRepresentation;
import de.hhu.cs.stups.algvis.pluginSpecs.ToolBarButton;
import de.hhu.cs.stups.algvis.pluginSpecs.Plugin;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public class Gui{
  private final JFrame frame;
  private final JPanel contentPanel;
  private final JToolBar toolBar;
  public Gui(Collection<Plugin> installedPlugins) {
    // INIT Frame
    // CONTENTS: MenuBar - ContentPanel
    frame = new JFrame("AlgVis");
    frame.setMinimumSize(new Dimension(720, 480));
    frame.setPreferredSize(new Dimension(1600, 900));
    frame.setMaximumSize(new Dimension(1920, 1080));
    frame.setSize(frame.getPreferredSize());

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());

    // INIT ContentPanel
    contentPanel = new JPanel();
    contentPanel.setMinimumSize(new Dimension(720, 480));
    contentPanel.setPreferredSize(new Dimension(1600, 900));
    contentPanel.setMaximumSize(new Dimension(1920, 1080));
    contentPanel.setLayout(new BorderLayout());
    contentPanel.setBackground(Color.DARK_GRAY);
    contentPanel.add(genSplashScreen(), BorderLayout.CENTER);
    frame.add(contentPanel, BorderLayout.CENTER);

    // INIT MenuBar
    JMenuBar menuBar = new JMenuBar();
    frame.add(menuBar, BorderLayout.NORTH);

    // INIT Plugins dropdown
    JMenu pluginMenu = new JMenu("Plugins");
    menuBar.add(pluginMenu);
    for (Plugin plugin : installedPlugins) {
      JMenuItem menuItem = new JMenuItem(plugin.getName());
      menuItem.addActionListener(e -> this.displayPlugin(plugin));
      pluginMenu.add(menuItem);
    }

    // INIT toolbar
    toolBar = new JToolBar();
    frame.add(toolBar, BorderLayout.SOUTH);

    refreshFrame();
  }
  public void refreshFrame(){frame.setVisible(true);}
  public void displayPlugin(Plugin plugin){
    switchToPlugin(plugin);
    setToolbarButtons(plugin.getToolBarButtons());
    plugin.onPluginLoad();
  }
  private void switchToPlugin(Plugin plugin){
    contentPanel.removeAll();
    for (DataRepresentation content : plugin.getGuiElements()) {
      switch (content.getComponentLocation()){
        case DataRepresentation.Location.left -> contentPanel.add(content.getSwingComponent(), BorderLayout.WEST);
        case DataRepresentation.Location.right -> contentPanel.add(content.getSwingComponent(), BorderLayout.EAST);
        case DataRepresentation.Location.center -> contentPanel.add(content.getSwingComponent(), BorderLayout.CENTER);
      }
    }
    contentPanel.setVisible(true);
    refreshFrame();
  }
  private void setToolbarButtons(Collection<ToolBarButton> enabledButtons){
    toolBar.removeAll();
    for (ToolBarButton button: enabledButtons){
      JButton jButton = new JButton(button.getText());
      jButton.addActionListener(e -> button.action());
      toolBar.add(jButton);
    }
  }
  private static JPanel genSplashScreen(){
    JPanel splashScreen = new JPanel();
    splashScreen.setBackground(Color.lightGray);
    JLabel text = new JLabel("welcome");
    splashScreen.add(text);
    return splashScreen;
  }
}