package util;

import static ui.AppPanel.*;

public class ConcreteNode extends Node {
    // If true, prioritizes distance to target based on perceived distance (i.e. tile size)
    //          however, it may not find the shortest path
    private final static boolean SCALE_DISTANCE_BY_TILE_SIZE = true;

    public int xPos;
    public int yPos;

    // EFFECTS: initializes ConcreteNode with starting position
    public ConcreteNode(boolean isStarting, int xPos, int yPos) {
        super(isStarting);
        this.xPos = xPos;
        this.yPos = yPos;
        initWeights();
    }

    @Override
    public boolean isGoalNode() {
        return xPos == GOAL_X && yPos == GOAL_Y;
    }

    @Override
    protected float calculateHeuristic() {
        if (SCALE_DISTANCE_BY_TILE_SIZE) {
            return (float) Math.sqrt(Math.pow((yPos - GOAL_Y) * TILE_SIZE, 2) + Math.pow((xPos - GOAL_X) * TILE_SIZE, 2));
        } else {
            return (float) Math.sqrt(Math.pow((yPos - GOAL_Y), 2) + Math.pow((xPos - GOAL_X), 2));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ConcreteNode)) return false;
        ConcreteNode n = (ConcreteNode) o;
        return n.xPos == xPos && n.yPos == yPos;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + xPos;
        hash = 31 * hash + yPos;
        return hash;
    }
}
