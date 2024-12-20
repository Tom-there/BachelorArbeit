package de.hhu.cs.stups.algvis.data.structures.graph;

public class Edge {
    private final Node sourceNode, targetNode;
    public Edge(Node sourceNode, Node targetNode) {
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
    }
    public String getID(){
        return "(" + sourceNode.getID() + ") -> ("+ targetNode.getID() + ")";
    }
    public Node getSourceNode() {
        return sourceNode;
    }
    public Node getTargetNode() {
        return targetNode;
    }
}
