package de.hhu.cs.stups.algvis.data.structures.graph;

import de.hhu.cs.stups.algvis.data.DataRepresentation;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Graph implements DataRepresentation {
    private final JPanel exportedPanel;
    private org.graphstream.graph.Graph graph;
    private View view;
    private final DataRepresentation.Location location;
    private Set<Node> nodes;
    private Set<Edge> edges;
    public Graph(){this(DataRepresentation.Location.center);}
    public Graph(DataRepresentation.Location location){
        this.location = location;
        exportedPanel = new JPanel(new BorderLayout());
        graph = new MultiGraph("Graph");
        nodes = new HashSet<>();
        edges = new HashSet<>();
        graph.setAttribute("ui.stylesheet", "node { size-mode: fit; shape: rounded-box; fill-color: white; stroke-mode: plain; padding: 3px, 2px; }");

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
    public DataRepresentation.Location getLocation() {
        return location;
    }

    public void addNode(Node newNode){
        boolean refreshGraph = !nodes.contains(newNode);//todo;
        if(refreshGraph){
            nodes.add(newNode);
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
        if(!edges.contains(edge)) {
            edges.add(edge);
            graph.addEdge(edge.getID(), edge.getSourceNode().getID(), edge.getTargetNode().getID(), true);
        }
    }
    public void addEdgeFromNodes(Node sourceNode, Node targetNode){
        addEdge(new Edge(sourceNode, targetNode));
    }
    public void refresh(){
        nodes.forEach(n -> {graph.getNode(n.getID()).setAttribute("ui.label", n.getText());});
    }
    public void purge() {
        graph = new MultiGraph("Graph");
        nodes = new HashSet<>();
        edges = new HashSet<>();
        graph.setAttribute("ui.stylesheet", "node { size-mode: fit; shape: rounded-box; fill-color: white; stroke-mode: plain; padding: 3px, 2px; }");
        SwingViewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        view = viewer.addDefaultView(false);

        viewer.enableAutoLayout();
        exportedPanel.removeAll();
        exportedPanel.add((Component) view, BorderLayout.CENTER);
    }

    public Set<Edge> getEdges(){
        return edges;
    }
    public Set<Node> getNodes(){
        return nodes;
    }
    public enum GraphMode {
        CodeInNode, normal
    }

}
