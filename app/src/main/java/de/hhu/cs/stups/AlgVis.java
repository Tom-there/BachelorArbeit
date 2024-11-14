package de.hhu.cs.stups;

import de.hhu.cs.stups.algvis.Plugin;

import java.util.List;
import java.util.LinkedList;

class AlgVis{
 
  private List<Plugin> installedPlugins;

  public static List<Plugin> installedPlugins(){
    List<Plugin> plugins = new LinkedList();
    return plugins;
  }


  private AlgVis(List<Plugin> installedPlugins){
    this.installedPlugins = installedPlugins;
  }


  public static void main(String[] args) {
    AlgVis vis = new AlgVis(installedPlugins());
  }
}
