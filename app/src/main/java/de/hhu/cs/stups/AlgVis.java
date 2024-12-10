package de.hhu.cs.stups;

import de.hhu.cs.stups.algvis.gui.Gui;
import de.hhu.cs.stups.algvis.plugins.*;

import java.util.List;
import java.util.LinkedList;

class AlgVis{
  public static void main(String[] args) {
    PluginManager pluginManager = new PluginManager();
    new Gui(pluginManager);
  }
}