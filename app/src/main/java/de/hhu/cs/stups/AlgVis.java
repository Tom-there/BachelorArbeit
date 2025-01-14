package de.hhu.cs.stups;

import de.hhu.cs.stups.algvis.Gui;
import de.hhu.cs.stups.algvis.pluginSpecs.Plugin;
import de.hhu.cs.stups.algvis.plugins.ReachingDefinitions.ReachingDefinitions;
import de.hhu.cs.stups.algvis.plugins.TACtoBB.TACtoBB;
import de.hhu.cs.stups.algvis.plugins.TACtoCFG.TACtoCFG;

import java.util.Collection;
import java.util.HashSet;

class AlgVis {
    private static Collection<Plugin> plugins() {
        Collection<Plugin> installedPlugins = new HashSet<>();

        installedPlugins.add(new TACtoCFG());
        installedPlugins.add(new TACtoBB());
        installedPlugins.add(new ReachingDefinitions());

        return installedPlugins;
    }
    public static void main(String[] args) {
        new Gui(plugins());
    }
}