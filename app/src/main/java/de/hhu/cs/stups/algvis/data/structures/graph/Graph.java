package de.hhu.cs.stups.algvis.data.structures.graph;

import de.hhu.cs.stups.algvis.data.structures.Content;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Graph implements Content {

    private final org.graphstream.graph.Graph graph;
    private final View view;
    private Set<Node> nodes;
    private Set<Edge> edges;
    public Graph(){this(GraphMode.normal);}
    public Graph(GraphMode mode){
        graph = new SingleGraph("test");
        nodes = new HashSet<>();
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
        System.setProperty("org.graphstream.ui", "swing");
        SwingViewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        view = viewer.addDefaultView(false);

        viewer.enableAutoLayout();
    }

    @Override
    public Component getSwingComponent() {
        return ((Component) view);
    }

    public void addNode(Node newNode){
        nodes.add(newNode);
        graph.addNode(newNode.getID());
        graph.getNode(newNode.getID()).setAttribute("ui.label", newNode.getText());
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
