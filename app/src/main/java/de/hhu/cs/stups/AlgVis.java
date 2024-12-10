package de.hhu.cs.stups;

import de.hhu.cs.stups.algvis.gui.Gui;
import de.hhu.cs.stups.algvis.plugins.CFGtoTAC;
import de.hhu.cs.stups.algvis.plugins.Plugin;
import de.hhu.cs.stups.algvis.plugins.DummyGraphPlugin;
import de.hhu.cs.stups.algvis.plugins.DummyPlugin;

import java.util.List;
import java.util.LinkedList;

class AlgVis{
  public static void main(String[] args) {
    new Gui(installedPlugins());
  }
  public static List<Plugin> installedPlugins(){
    List<Plugin> plugins = new LinkedList<>();

    plugins.add(new DummyPlugin());
    plugins.add(new DummyGraphPlugin());
    plugins.add(new CFGtoTAC());

    return plugins;
  }
}