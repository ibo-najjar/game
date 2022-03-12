package main.states;

import main.utils.KeyHandler;
import main.utils.MouseHandler;

import java.awt.*;

public class PlayState extends GameState {

    public PlayState(GameStateManager gsm) {
        super(gsm);
    }

    public void update() {

    }
    public void input(MouseHandler mouse, KeyHandler key) {

    }
    public void render(Graphics2D g) {
        g.setColor(Color.CYAN);
        g.fillRect(100,100,200,200);
    }

}
