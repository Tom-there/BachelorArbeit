package de.hhu.cs.stups;

import de.hhu.cs.stups.algvis.PluginManager;
import de.hhu.cs.stups.algvis.gui.Gui;

class AlgVis{
  public static void main(String[] args) {
    PluginManager pluginManager = new PluginManager();
    new Gui(pluginManager);
  }
}