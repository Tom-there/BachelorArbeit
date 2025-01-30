package de.hhu.cs.stups.algvis.data.structures.graph;

public record Edge(Node sourceNode, Node targetNode){
    public String getID(){
        return "(" + sourceNode.getId() + ") -> ("+ targetNode.getId() + ")";
    }
}
