package de.hhu.cs.stups.algvis.data.structures;

import de.hhu.cs.stups.algvis.data.DataRepresentation;
import de.hhu.cs.stups.algvis.data.structures.graph.Edge;
import de.hhu.cs.stups.algvis.data.structures.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Graph implements DataRepresentation {
    private final JPanel exportedPanel;
    private org.graphstream.graph.Graph graph;
    private View view;
    private final DataRepresentation.Location location;
    private HashMap<String, Node> nodes;
    private HashMap<String, Edge> edges;
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
        purge();
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
        if(!nodes.containsKey(newNode.getID())){
            nodes.put(newNode.getID(), newNode);
            graph.addNode(newNode.getID());
            graph.getNode(newNode.getID()).setAttribute("ui.label", newNode.getLabel());
        }
    }
    public void changeLabelOfNode(String nodeID, String label) {
        if(nodes.containsKey(nodeID))
            graph.getNode(nodeID).setAttribute("ui.label", label);
    }
    public void addNodeWithEdges(Node newNode, List<Node> targets){
        addNode(newNode);
        for (Node target:targets) {
            addEdgeFromNodes(newNode, target);
        }
    }
    public void addEdge(Edge newEdge){
        if(!edges.containsKey(newEdge.getID())) {
            edges.put(newEdge.getID(), newEdge);
            graph.addEdge(newEdge.getID(), newEdge.sourceNode().getID(), newEdge.targetNode().getID(), true);
        }
    }
    public void addEdgeFromNodes(Node sourceNode, Node targetNode){
        addEdge(new Edge(sourceNode, targetNode));
    }
    public void purge() {
        graph = new MultiGraph("Graph");
        nodes = new HashMap<>();
        edges = new HashMap<>();

        graph.setAttribute("ui.stylesheet", "graph { padding: 40px; } node { text-alignment: at-right; text-padding: 3px, 2px; text-background-mode: rounded-box; text-background-color: #EBA; text-color: #222; }");
        SwingViewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        view = viewer.addDefaultView(false);

        viewer.enableAutoLayout();
        exportedPanel.removeAll();
        exportedPanel.add((Component) view, BorderLayout.CENTER);
    }

    public  HashMap<String, Node> getNodes(){
        return nodes;
    }
    public  HashMap<String, Edge> getEdges(){
        return edges;
    }
}
