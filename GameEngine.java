import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

public class GameEngine {
    JFrame window;
    JPanel area;
    HashSet<Integer> keys;

    GameEngine() {
        keys = new HashSet<Integer>();

        window = new JFrame();
        window.setLayout(new BorderLayout());
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        area = new JDrawingArea(this);
        window.add(area, BorderLayout.CENTER);

        window.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                keys.add(keyEvent.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                keys.remove(keyEvent.getKeyCode());
            }
        });
    }

    void nextFrame(long delta, Graphics2D g) {

    }
}
