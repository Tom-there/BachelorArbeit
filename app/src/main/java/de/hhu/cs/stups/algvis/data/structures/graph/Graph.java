package de.hhu.cs.stups.algvis.data.structures.graph;

import de.hhu.cs.stups.algvis.data.structures.Content;
import de.hhu.cs.stups.algvis.gui.Locator;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Graph implements Content {
    private final JPanel exportedPanel;
    private final org.graphstream.graph.Graph graph;
    private final View view;
    private final Locator location;
    private Set<Node> nodes;
    private Set<Edge> edges;
    public Graph(){this(GraphMode.normal, Locator.center);}
    public Graph(GraphMode mode, Locator location){
        this.location = location;
        exportedPanel = new JPanel(new BorderLayout());
        graph = new SingleGraph("test");
        nodes = new HashSet<>();
        edges = new HashSet<>();
        switch (mode){
            case CodeInNode -> {
                graph.setAttribute("ui.stylesheet", "node { size-mode: fit; shape: rounded-box; fill-color: white; stroke-mode: plain; padding: 3px, 2px; }");
            }
            case normal -> {

            }
            case null, default -> {
                System.err.println("ERR");
            }

        }


        switch (location){
            case left, right -> {
                exportedPanel.setMinimumSize(new Dimension(180, 480));
                exportedPanel.setPreferredSize(new Dimension(400, 900));
                exportedPanel.setMaximumSize(new Dimension(960, 1080));
            }
            case center -> {
                exportedPanel.setMinimumSize(new Dimension(360, 480));
                exportedPanel.setPreferredSize(new Dimension(1600, 900));
                exportedPanel.setMaximumSize(new Dimension(1920, 1080));
            }
            case null, default -> System.err.println("ERROR, while generating Graph(Content visualizing). Locator parameter was not able to be interpreted");
        }


        System.setProperty("org.graphstream.ui", "swing");
        SwingViewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        view = viewer.addDefaultView(false);

        viewer.enableAutoLayout();
        exportedPanel.add((Component) view, BorderLayout.CENTER);
    }

    @Override
    public Component getSwingComponent() {
        return exportedPanel;
    }

    @Override
    public Locator getLocation() {
        return location;
    }

    public void addNode(Node newNode){
        boolean refreshGraph = nodes.add(newNode);
        if(refreshGraph){
            graph.addNode(newNode.getID());
            graph.getNode(newNode.getID()).setAttribute("ui.label", newNode.getText());
        }
    }
    public void addNodeWithEdges(Node newNode, List<Node> targets){
        addNode(newNode);
        for (Node target:targets) {
            addEdgeFromNodes(newNode, target);
        }
    }
    public void addEdge(Edge edge){
        edges.add(edge);
        graph.addEdge(edge.getID(), edge.getSourceNode().getID(), edge.getTargetNode().getID());
    }
    public void addEdgeFromNodes(Node sourceNode, Node targetNode){
        addEdge(new Edge(sourceNode, targetNode));
    }
    public void refresh(){
        nodes.forEach(n -> {graph.getNode(n.getID()).setAttribute("ui.label", n.getText());});
    }
    public void purge() {
        System.out.println("edges");
        graph.edges().forEach(System.out::println);
        graph.edges().forEach(graph::removeEdge);
        System.out.println("kept edges");
        graph.edges().forEach(System.out::println);
        System.out.println("Nodes");
        graph.nodes().forEach(System.out::println);
        graph.nodes().forEach(graph::removeNode);
        System.out.println("Kept nodes");
        graph.nodes().forEach(System.out::println);
    }

    public Set<Edge> getEdges(){
        return edges;
    }
    public Set<Node> getNodes(){
        return nodes;
    }
}
