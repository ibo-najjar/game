package main;

import main.states.GameStateManager;
import main.utils.KeyHandler;
import main.utils.MouseHandler;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {

    public static int width;
    public static int height;

    private Thread thread;
    private boolean running = false;

    private BufferedImage img;
    private Graphics2D g;

    private MouseHandler mouse;
    private KeyHandler key;

    private GameStateManager gsm;

    public GamePanel(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        setFocusable(true); // allow JPanel to have input
        requestFocus();
    }

    public void addNotify() {
        super.addNotify();

        if(thread == null) {
            thread = new Thread(this, "GameThread");
            thread.start();
        }
    }

    public void init() {
        running = true;

        img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        g = (Graphics2D) img.getGraphics();

        mouse = new MouseHandler();
        key = new KeyHandler();

        gsm = new GameStateManager();
    }

    public void run() {
        init();

        final double GAME_HERTZ = 60.0; // hertz per second
        final double TBU = 1000000000 / GAME_HERTZ; //time before update

        final int MUBR = 5; // must update before render

        double lastUpdateTime = System.nanoTime(); // 10^-9 nano time
        double lastRenderTime;

        final double TARGET_FPS = 60;
        final double TTBR = 1000000000 / TARGET_FPS; //Total time before render

        int frameCount = 0;
        int lastSecondTime = (int) (lastUpdateTime / 1000000000);
        int oldFrameCount = 0;

        //fixed time game loop

        while (running) {
            double now = System.nanoTime();
            int updateCount = 0;
            while (((now - lastUpdateTime) > TBU ) && (updateCount < MUBR)) {
                update();
                input(mouse, key);
                lastUpdateTime += TBU;
                updateCount++;
            }

            if (now - lastUpdateTime > TBU) {
                lastUpdateTime = now - TBU;
            }

            input(mouse, key);
            renders();
            draw();
            lastRenderTime = now;
            frameCount++;

            int thisSecond = (int) (lastUpdateTime / 1000000000);
            if(thisSecond > lastSecondTime) {
                if ( frameCount != oldFrameCount ) {
                    System.out.println("NEW SECOND " + thisSecond + " " + frameCount);
                    oldFrameCount = frameCount;
                }
                frameCount = 0;
                lastSecondTime = thisSecond;
            }

            while (now - lastRenderTime < TTBR && now - lastUpdateTime < TBU) {
                Thread.yield();

                try {
                    Thread.sleep(1);
                } catch(Exception e) {
                    System.out.println("Error: yielding thread");
                }

                now = System.nanoTime();
            }
        }
    }

    public void update() {
        gsm.update();
    }

    public void input(MouseHandler mouse, KeyHandler key) {
        gsm.input(mouse, key);
    }

    public void renders() {
        if(g != null) {
            g.setColor(new Color(47, 47, 47)); // bg color
            g.fillRect(0,0,width,height);
            gsm.render(g);
        }
    }

    public void draw() {
        Graphics g2 = (Graphics) this.getGraphics();
        g2.drawImage(img,0,0,width,height,null);
        g2.dispose();
    }
}
