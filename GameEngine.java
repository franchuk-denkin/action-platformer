import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

public class GameEngine {
    JFrame window;
    JPanel area;
    HashSet<Integer> keys;
    Menu mainMenu, inGameMenu;
    boolean paused;
    String leveln;
    boolean mainMenuDisplayed, inGameMenuDisplayed;
    SortedSet<GameObject> objList;
    Player player;

    GameEngine() {
        keys = new HashSet<Integer>();

        window = new JFrame();
        window.setLayout(new BorderLayout());
        window.setSize(800, 600);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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
        objList= new TreeSet<GameObject>();
        window.setVisible(true);
    }

    void setPlayer(Player player) {
        this.player = player;
    }

    void nextFrame(long delta, Graphics2D g) {
        if(mainMenuDisplayed)
            mainMenu.draw(delta, g);
        else if (inGameMenuDisplayed)
            inGameMenu.draw(delta, g);
        else {
            g.translate(-player.getX() - player.width / 2 + width() / 2, -player.getY() - player.height / 2 + height() / 2);
            for (Object go: objList.toArray())
                ((GameObject)go).update(delta, g);
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
            final GameEngine engine = this;
            mainMenu.addItem("Нова гра", new Runnable() {
                @Override
                public void run() {
                    engine.newGame();
                }
            });
            mainMenu.addItem("Завантажити гру", new Runnable() {
                @Override
                public void run() {
                    loadGame();
                }
            });
            mainMenu.addItem("Вийти", new Runnable() {
                @Override
                public void run() {
                    engine.exit();
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

    void pause(){
        paused = true;
        showInGameMenu();
    }
    void saveGame(String file){
        try { PrintStream out = new PrintStream(new FileOutputStream("C:\\actionplatformer.sav"));
        out.print(file);
        out.close();
        }
        catch(Exception e) {
        }
    }

    void loadGame(){
        try { Scanner in = new Scanner(new FileInputStream("C:\\actionplatformer.sav"));
            leveln = in.next();
            in.close();
            loadLevel(leveln);
        }
        catch(Exception e) {
        }
    }
    void resume(){
        paused = false;
        inGameMenuDisplayed = false;
    }
    void exit(){
        window.setVisible(false);
    }

    void showInGameMenu() {
        inGameMenuDisplayed = true;
        final GameEngine engine = this;
        if(inGameMenu == null) {
            inGameMenu = new Menu(this);
            inGameMenu.addItem("Продовжити", new Runnable() {
                @Override
                public void run() {
                    engine.resume();
                }
            });
            inGameMenu.addItem("Вийти в головне меню", new Runnable() {
                @Override
                public void run() {
                    engine.resume();
                    engine.showMainMenu();
                }
            });
        }
    }

    void loadLevel(String file) {
        objList.clear();
        saveGame(file);
        addObject(new Level(this, file));
        mainMenuDisplayed = false;
        inGameMenuDisplayed = false;
    }

    void newGame() {
        loadLevel("1");
    }

    public SortedSet<GameObject> getObjList() {
        return objList;
    }
}
