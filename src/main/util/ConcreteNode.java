package util;

import processing.core.PVector;

import static ui.AStarAttempt.GOAL;

// Represents a node with a given position and a given GOAL
public class ConcreteNode extends Node {
    private PVector pos;

    // EFFECTS: initializes ConcreteNode with starting position
    public ConcreteNode(boolean isStarting, PVector pos) {
        super(isStarting);
        this.pos = pos;
        initWeights();
    }

    @Override
    public boolean isGoalNode() {
        return pos.equals(GOAL);
    }

    @Override
    protected float calculateHeuristic() {
        return pos.dist(GOAL);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ConcreteNode)) return false;
        ConcreteNode n = (ConcreteNode) o;
        return n.pos.equals(this.pos);
    }

    @Override
    public int hashCode() {
        return (31 * 17) + pos.hashCode();
    }
}
