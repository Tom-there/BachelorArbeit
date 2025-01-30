package de.hhu.cs.stups.algvis.data.structures;

import de.hhu.cs.stups.algvis.data.DataRepresentation;
import de.hhu.cs.stups.algvis.data.structures.graph.Edge;
import de.hhu.cs.stups.algvis.data.structures.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.layout.Layout;
import org.graphstream.ui.layout.springbox.implementations.LinLog;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Graph implements DataRepresentation {
    private final JPanel exportedPanel;
    private org.graphstream.graph.Graph graph;
    private final SwingViewer viewer;
    private final Layout layout;
    private final DataRepresentation.Location location;
    private Set<Node> nodes;
    private Set<Edge> edges;
    public Graph(){this(DataRepresentation.Location.center);}
    public Graph(DataRepresentation.Location location){
        this.location = location;
        exportedPanel = new JPanel(new BorderLayout());
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

        nodes = new HashSet<>();
        edges = new HashSet<>();
        graph = new MultiGraph("Graph");
        graph.setAttribute("ui.stylesheet", "graph { padding: 40px; } node { text-alignment: at-right; text-padding: 3px, 2px; text-background-mode: rounded-box; text-background-color: #EBA; text-color: #222; }");
        viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        View view = viewer.addDefaultView(false);

        layout = new LinLog();
        viewer.enableAutoLayout(layout);

        exportedPanel.removeAll();
        exportedPanel.add((Component) view, BorderLayout.CENTER);
    }

    @Override
    public Component getSwingComponent() {
        return exportedPanel;
    }

    @Override
    public DataRepresentation.Location getComponentLocation() {
        return location;
    }

    public void addNode(Node newNode){
        if(nodes.contains(newNode))
            return;
        nodes.add(newNode);
        graph.addNode(newNode.getId());
        layout.shake();
    }
    public void changeLabelOfNode(Node node, String label) {
        if(nodes.contains(node))
            graph.getNode(node.getId()).setAttribute("ui.label", label);

    }
    public void addNodeWithEdges(Node newNode, Collection<Node> targets){
        addNode(newNode);
        for (Node target:targets) {
            addEdgeFromNodes(newNode, target);
        }
    }
    public void addEdge(Edge newEdge){
        if(!edges.contains(newEdge)) {
            edges.add(newEdge);
            graph.addEdge(newEdge.getID(), newEdge.sourceNode().getId(), newEdge.targetNode().getId(), true);
        }
    }
    public void addEdgeFromNodes(Node sourceNode, Node targetNode){
        addEdge(new Edge(sourceNode, targetNode));
    }
    public void purge() {
        for (Edge edge: new HashSet<>(edges)) {
            graph.removeEdge(edge.getID());
            edges.remove(edge);
        }
        for (Node node: new HashSet<>(nodes)) {
            graph.removeNode(node.getId());
            nodes.remove(node);
        }
    }

    public  Set<Node> getNodes(){
        return nodes;
    }
    public  Set<Edge> getEdges(){
        return edges;
    }
}
