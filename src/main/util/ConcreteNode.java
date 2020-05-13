package util;

import static ui.AppPanel.GOAL_X;
import static ui.AppPanel.GOAL_Y;

public class ConcreteNode extends Node {
    private float xPos;
    private float yPos;

    // EFFECTS: initializes ConcreteNode with starting position
    public ConcreteNode(boolean isStarting, float xPos, float yPos) {
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
        return (float) Math.sqrt(Math.pow((yPos - GOAL_Y), 2) + Math.pow((xPos - GOAL_X), 2));
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
        hash = 31 * hash + Float.floatToIntBits(xPos);
        hash = 31 * hash + Float.floatToIntBits(yPos);
        return hash;
    }
}
