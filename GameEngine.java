import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.LinkedList;

public class GameEngine {
    JFrame window;
    JPanel area;
    HashSet<Integer> keys;
    Menu mainMenu;
    boolean mainMenuDisplayed;
    LinkedList<GameObject> objList;

    GameEngine() {
        keys = new HashSet<Integer>();

        window = new JFrame();
        window.setLayout(new BorderLayout());
        window.setSize(800, 600);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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

        showMainMenu();
    }

    void nextFrame(long delta, Graphics2D g) {
        if(mainMenuDisplayed)
            mainMenu.draw(delta, g);
        else {
            for (GameObject go:objList)
                go.update(delta);
        }
    }

    int width() {
        return area.getWidth();
    }

    int height() {
        return area.getHeight();
    }

    HashSet<Integer> getKeys() {
        return keys;
    }

    void showMainMenu() {
        if (mainMenu == null) {
            mainMenu = new Menu(this);
            mainMenu.addItem("Нова гра", new Runnable() {
                @Override
                public void run() {
                }
            });
            mainMenu.addItem("Завантажити гру", new Runnable() {
                @Override
                public void run() {
                }
            });
            mainMenu.addItem("Вийти", new Runnable() {
                @Override
                public void run() {
                }
            });
        }
        mainMenuDisplayed = true;
    }

    void addObject(GameObject g) {
        objList.add(g);
    }

    void deleteObject(GameObject g){
        objList.remove(g);
    }
}
