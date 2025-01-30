package de.hhu.cs.stups.algvis.data.structures.graph;

public class Node {
    private static int UID=0;
    private final String id;
    public Node(){
        this.id = Integer.toString(++UID);
    }
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id == node.getId();
    }
}
