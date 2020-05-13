package util;

import java.util.ArrayList;
import java.util.HashMap;

// Represents a node
public abstract class Node implements Comparable<Node> {
    private boolean isStartingNode;
    private float weight = Float.MAX_VALUE;
    private float publicWeight = Float.MAX_VALUE; // weight including heuristic
    private Node previousNode = null; // previous connected node
    private final HashMap<Node, Float> pathWeights = new HashMap<>(); // from this node to others

    // EFFECTS: initializes whether this node is a starting node
    Node(boolean isStarting) {
        this.isStartingNode = isStarting;
    }

    // MODIFIES: this
    // EFFECTS: initializes the weight of this node
    void initWeights() {
        if (isStartingNode) {
            weight = 0;
            publicWeight = calculateHeuristic();
        }
    }

    // EFFECTS: determines whether this node is the GOAL node
    public abstract boolean isGoalNode();

    // EFFECTS: returns the heuristic (i.e. approximation of distance to GOAL)
    protected abstract float calculateHeuristic();

    // MODIFIES: this
    // EFFECTS: given a potential previous node, update current node if node  has lower weight than previous node
    private boolean updatePreviousNode(Node n) {
        if (n.weight == Float.MAX_VALUE || !pathWeights.containsKey(n)) throw new IllegalArgumentException();
        float anticipatedWeight = n.weight + n.pathWeights.get(this);
        if (!isStartingNode && (previousNode == null || anticipatedWeight < weight)) {
            this.weight = anticipatedWeight;
            this.publicWeight = anticipatedWeight + calculateHeuristic();
            this.previousNode = n;
            return true;
        }
        return false;
    }

    // EFFECTS: gets the public weight (including heuristic)
    public float getPublicWeight() {
        return publicWeight;
    }

    // EFFECTS: gets the weight of the node (i.e. sum of all previous paths)
    public float getWeight() {
        return weight;
    }

    // EFFECTS: gets the previous node
    public Node getPreviousNode() {
        return previousNode;
    }

    // MODIFIES: this
    // EFFECTS: updates connected nodes
    public ArrayList<Node> updateConnectedNodes() {
        ArrayList<Node> updatedNodes = new ArrayList<>();
        for (Node n : pathWeights.keySet()) {
            if (n != previousNode) {
                boolean updated = n.updatePreviousNode(this);
                if (updated) updatedNodes.add(n);
            }
        }
        return updatedNodes;
    }

    // MODIFIES: this, n
    // EFFECTS: adds a connection between this node and the nth node
    public void addConnection(Node n, float pathWeight) {
        if (this.pathWeights.containsKey(n)) return;
        this.pathWeights.put(n,pathWeight);
        n.addConnection(this,pathWeight);
    }

    // EFFECTS: returns whether this node is a starting node
    public boolean isStartingNode() {
        return isStartingNode;
    }

    @Override
    // EFFECTS: determines whether the weight of this node is greater than, equal to, or less than another given node
    public int compareTo(Node o) {
        return Float.compare(publicWeight,o.publicWeight);
    }
}