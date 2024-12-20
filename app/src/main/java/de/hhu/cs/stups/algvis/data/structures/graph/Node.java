package de.hhu.cs.stups.algvis.data.structures.graph;

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
}
