package ui;

import util.ConcreteNode;
import util.Node;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;

public class AppPanel extends JPanel {
    // Determines whether the walls use a random seed or the given seed
    private static final boolean IS_RANDOM = true;
    private static final long RANDOM_SEED = 42069;

    // Determines starting position and GOAL position
    private static final int GOAL_X_INDEX = 39;
    private static final int GOAL_Y_INDEX = 39;
    public static final float GOAL_X = GOAL_X_INDEX * 10;
    public static final float GOAL_Y = GOAL_Y_INDEX * 10;

    private static final int START_X_INDEX = 5;
    private static final int START_Y_INDEX = 5;

    private static final double WALL_CHANCE = 0.3;

    private static final int MAP_LENGTH = 60;
    private static final int MAP_HEIGHT = 60;
    private static final int TILE_SIZE = 10;

    private static final int WINDOW_WIDTH = MAP_LENGTH * TILE_SIZE;
    private static final int WINDOW_HEIGHT = MAP_HEIGHT * TILE_SIZE;

    private PriorityQueue<Node> nodeQueue = new PriorityQueue<>(); // Represents list of nodes to process
    private HashSet<Node> discard = new HashSet<>(100); // Represents discard
    private boolean[][] walls = new boolean[MAP_LENGTH][MAP_HEIGHT]; // represents where walls are

    private Node[][] nodesArray = new Node[MAP_LENGTH][MAP_HEIGHT];
    private Node goalNode;

    // EFFECTS: initializes AppPanel with given width, height and initializes nodes, walls and nodal connections
    AppPanel() {
        super();
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setBackground(Color.WHITE);

        initWalls();
        initNodes();
        initNodeConnections();
    }

    private Graphics2D graphicsObject;

    @Override
    // MODIFIES: this
    // EFFECTS: processes one node and draws the outcome to the window
    public void paintComponent(Graphics g) {
        graphicsObject = (Graphics2D) g;
        drawBackground();
        determineIfFinished();
        processNodeQueue();
        calculatePathIfExists();
        drawTileMap();
        drawNoSolution();
    }

    // MODIFIES: this
    // EFFECTS: draws the background (white color);
    private void drawBackground() {
        graphicsObject.setColor(Color.WHITE);
        graphicsObject.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    // MODIFIES: this
    // EFFECTS: initializes walls
    private void initWalls() {
        Random random;
        if (IS_RANDOM) random = new Random();
        else random = new Random(RANDOM_SEED);
        for (int i = 0; i < MAP_LENGTH; i++) {
            for (int j = 0; j < MAP_HEIGHT; j++) {
                if (i == START_X_INDEX && j == START_Y_INDEX) continue; // don't generate walls over start/end
                if (i == GOAL_X_INDEX && j == GOAL_Y_INDEX) continue;
                if (random.nextDouble() < WALL_CHANCE) walls[i][j] = true;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes nodeQueue, resets GOAL position, sets goalNode, and adds
    private void initNodes() {
        for (int i = 0; i < MAP_LENGTH; i++) {
            for (int j = 0; j < MAP_HEIGHT; j++) {
                if (walls[i][j]) continue;
                nodesArray[i][j] = new ConcreteNode((i == START_X_INDEX && j == START_Y_INDEX), i * TILE_SIZE, j * TILE_SIZE);
            }
        }
        goalNode = nodesArray[GOAL_X_INDEX][GOAL_Y_INDEX];

        nodeQueue.add(nodesArray[START_X_INDEX][START_Y_INDEX]);
    }

    // MODIFIES: this
    // EFFECTS: initializes connections between nodeQueue
    private void initNodeConnections() {
        for (int i = 0; i < MAP_LENGTH; i++) {
            for (int j = 0; j < MAP_HEIGHT; j++) {
                if (!exists(i, j)) continue;
                Node n1 = exists(i - 1, j) ? nodesArray[i - 1][j] : null;
                Node n2 = exists(i + 1, j) ? nodesArray[i + 1][j] : null;
                Node n3 = exists(i, j - 1) ? nodesArray[i][j - 1] : null;
                Node n4 = exists(i, j + 1) ? nodesArray[i][j + 1] : null;
                if (n1 != null) nodesArray[i][j].addConnection(n1, 1);
                if (n2 != null) nodesArray[i][j].addConnection(n2, 1);
                if (n3 != null) nodesArray[i][j].addConnection(n3, 1);
                if (n4 != null) nodesArray[i][j].addConnection(n4, 1);
            }
        }
    }

    private boolean finished = false;
    private HashSet<Node> path = new HashSet<>(MAP_LENGTH);
    private boolean noSolution = false;

    // MODIFIES: this
    // EFFECTS: determines the given path if there exists a solution
    private void calculatePathIfExists() {
        if (!noSolution && finished && path.isEmpty()) {
            for (Node currentNode = goalNode; currentNode != null; currentNode = currentNode.getPreviousNode()) {
                path.add(currentNode);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: processes the first node in the node queue
    private void processNodeQueue() {
        if (!noSolution && !finished) {
            updateFirstNode();
        }
    }

    // MODIFIES: this
    // EFFECTS: determines if other nodes in queue have a weight lower than weight of GOAL
    private void determineIfFinished() {
        boolean pathExists = false;
        float goalWeight = Float.MAX_VALUE;
        if (!noSolution && !finished) {
            for (Node n : discard) {
                if (n.isGoalNode()) {
                    pathExists = true;
                    goalWeight = n.getPublicWeight();
                }
            }
            if (discard.contains(goalNode)) {
                pathExists = true;
                goalWeight = goalNode.getPublicWeight();
            }
            if (pathExists) {
                for (Node n : nodeQueue) {
                    if (n.getPublicWeight() < goalWeight) {
                        return; // more paths can be explored
                    }
                }
                finished = true;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: draws the tile map
    private void drawTileMap() {
        for (int i = 0; i < MAP_LENGTH; i++) {
            for (int j = 0; j < MAP_HEIGHT; j++) {
                if (!exists(i, j)) {
                    graphicsObject.setColor(Color.BLACK);
                    graphicsObject.fillRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    continue;
                }
                Node n = nodesArray[i][j];
                if (n.isGoalNode() || n.isStartingNode()) {
                    graphicsObject.setColor(Color.RED);
                } else if (n.getPublicWeight() == Float.MAX_VALUE) {
                    graphicsObject.setColor(Color.WHITE);
                } else if (path.contains(n)) {
                    graphicsObject.setColor(Color.BLUE);
                } else {
                    graphicsObject.setColor(Color.GREEN);
                }
                graphicsObject.fillRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    // EFFECTS: determines whether the given index of array isn't a wall and isn't out of bounds
    private boolean exists(int i, int j) {
        return i >= 0 && i < MAP_LENGTH && j >= 0 && j < MAP_HEIGHT && !walls[i][j];
    }

    // MODIFIES: this
    // EFFECTS: draws the no solution text if there is no solution
    private void drawNoSolution() {
        if (noSolution) {
            graphicsObject.setColor(Color.RED);
            Font font = graphicsObject.getFont();
            font = font.deriveFont(Font.PLAIN, 50);
            graphicsObject.setFont(font);
            graphicsObject.drawString("NO SOLUTION", WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
        }
    }

    // MODIFIES: this
    // EFFECTS: gets the node with the lowest weight, and updates the weight of their neighbours
    private void updateFirstNode() {
        if (nodeQueue.size() == 0) {
            noSolution = true;
            return;
        }
        Node n = nodeQueue.poll();
        discard.add(n);
        assert n != null;
        nodeQueue.addAll(n.updateConnectedNodes());
    }
}
