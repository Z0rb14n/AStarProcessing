package ui;

import javax.swing.*;

public class AppFrame extends JFrame {
    private static final int UPDATE_RATE = 20;
    private AppPanel ap;

    private AppFrame() {
        super(" l m a o");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(false);
        setVisible(true);
        ap = new AppPanel();
        add(ap);
        pack();
        Timer timer = new Timer(UPDATE_RATE, ae -> ap.repaint());
        timer.start();
    }

    public static void main(String[] args) {
        new AppFrame();
    }
}
