package util;

import java.util.Comparator;

// Represents a comparator that compares the public weights of two nodes
public class NodeComparator implements Comparator<Node> {
    @Override
    public int compare(Node o1, Node o2) {
        return Float.compare(o1.publicWeight,o2.publicWeight);
    }
}