package de.hhu.cs.stups.algvis.data.structures.graph;

import java.util.Objects;

public class Node {
    private final String ID;
    private String text;
    public Node(String id, String text){
        this.text = text;
        this.ID = id;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getID() {
        return ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(ID, node.ID) && Objects.equals(text, node.text);
    }
}
