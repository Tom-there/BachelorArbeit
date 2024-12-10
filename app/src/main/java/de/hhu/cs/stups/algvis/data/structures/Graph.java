package de.hhu.cs.stups.algvis.data.structures;

import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import java.awt.*;
import java.util.List;

public class Graph implements Content{

    private final org.graphstream.graph.Graph graph;
    private final View view;

    public Graph(){
        System.setProperty("org.graphstream.ui", "swing");
        graph = new SingleGraph("test");
        SwingViewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        view = viewer.addDefaultView(false);

        viewer.enableAutoLayout();
    }

    @Override
    public Component getSwingComponent() {
        return ((Component) view);
    }

    public void addNode(String newNode){
        graph.addNode(newNode);
    }
    public void addNodeWithEdges(String newNode, List<String> targets){
        graph.addNode(newNode);
        for (String targetNode:targets) {
            graph.addEdge(("'"+newNode+"'->'"+targetNode+"'"), newNode, targetNode);
        }
    }
    public void addEdge(String sourceNode, String targetNode){
        graph.addEdge(("'"+sourceNode+"'->'"+targetNode+"'"), sourceNode, targetNode);
    }

    public void purge() {
        graph.edges().forEach(graph::removeEdge);
        graph.nodes().forEach(graph::removeNode);
    }
}
