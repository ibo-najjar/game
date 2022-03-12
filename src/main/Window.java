package main;

import javax.swing.JFrame;

public class Window extends JFrame {

    public Window() {
        setTitle("game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close button
        setContentPane(new GamePanel(1280, 720));
        pack(); // fix window size to fit all widgets
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
