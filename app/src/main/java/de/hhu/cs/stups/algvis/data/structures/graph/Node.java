package de.hhu.cs.stups.algvis.data.structures.graph;

import java.util.Objects;

public class Node {
    private final String ID;
    private String label;
    public Node(String id, String label){
        this.label = label;
        this.ID = id;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getID() {
        return ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(ID, node.ID) && Objects.equals(label, node.label);
    }
}
