package de.hhu.cs.stups.algvis.plugins;

import de.hhu.cs.stups.algvis.data.structures.Content;
import de.hhu.cs.stups.algvis.data.structures.Graph;
import de.hhu.cs.stups.algvis.gui.Locator;

import java.util.HashMap;
import java.util.List;

public class DummyGraphPlugin implements Plugin {

    private Graph graph;

    public DummyGraphPlugin(){
        graph = new Graph();
        reset();
    }
    @Override
    public String getName() {
        return "DummyGraphPlugin";
    }

    @Override
    public HashMap<Locator, Content> getGuiElements() {
        HashMap<Locator, Content> ret = new HashMap<>();
        ret.put(Locator.center, graph);
        return ret;
    }

    @Override
    public void reset() {
        graph.purge();
        graph.addNode("a");
        graph.addNodeWithEdges("b", List.of("a"));
    }

    @Override
    public void doStep() {
        graph.addNodeWithEdges("c", List.of("a", "b"));
    }
}
