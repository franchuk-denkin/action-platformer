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
    HashSet<Integer> pressedKeys;
    Menu mainMenu, inGameMenu;
    boolean paused;
    String leveln;
    boolean mainMenuDisplayed, inGameMenuDisplayed;
    SortedSet<GameObject> objList;
    Player player;
    long gameTime = 0;
    int curlevel = 1;
    private boolean messageScreenDisplayed = false;
    private String message;
    private long messageTime = 5000000000l;
    private long messageTimePassed = 0;
    private Runnable postMessageCallback;
    private final Color messageColor = new Color(255, 255, 0);

    GameEngine() {
        keys = new HashSet<Integer>();
        pressedKeys = new HashSet<Integer>();

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
                pressedKeys.add(keyEvent.getKeyCode());
                //keyTimes.put(keyEvent.getKeyCode(), -keyDelay);
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                keys.remove(keyEvent.getKeyCode());
                //keyTimes.remove(keyEvent.getKeyCode());
            }
        });

        showMainMenu();
        objList = new TreeSet<GameObject>();
        window.setVisible(true);
    }

    void setPlayer(Player player) {
        this.player = player;
    }

    void nextFrame(long delta, Graphics2D g) {
        if (keyPressed(KeyEvent.VK_ESCAPE))
            pause();
        if (mainMenuDisplayed)
            mainMenu.draw(delta, g);
        else if (inGameMenuDisplayed)
            inGameMenu.draw(delta, g);
        else if (messageScreenDisplayed)
            drawMessageScreen(delta, g);
        else {
            gameTime += delta;
            g.translate(-player.getX() - player.width / 2 + width() / 2, -player.getY() - player.height / 2 + height() / 2);
            for (Object go : objList.toArray())
                ((GameObject) go).update(delta, g);
        }
        pressedKeys.clear();
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

    void deleteObject(GameObject g) {
        objList.remove(g);
    }

    void pause() {
        paused = true;
        showInGameMenu();
    }

    void saveGame(String file) {
        try {
            PrintStream out = new PrintStream(new FileOutputStream("C:\\actionplatformer.sav"));
            out.print(file);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadGame() {
        try {
            Scanner in = new Scanner(new FileInputStream("C:\\actionplatformer.sav"));
            leveln = in.next();
            in.close();
            loadLevel(leveln);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void resume() {
        paused = false;
        inGameMenuDisplayed = false;
    }

    void exit() {
        window.setVisible(false);
    }

    void showInGameMenu() {
        inGameMenuDisplayed = true;
        final GameEngine engine = this;
        if (inGameMenu == null) {
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
        curlevel = Integer.parseInt(file);
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

    public boolean keyPressed(int code) {
        return pressedKeys.contains(code);
    }

    public long getGameTime() {
        return gameTime;
    }

    public void gameOver() {
        messageScreenDisplayed = true;
        message = "Гру закінчено";
        messageTimePassed = 0;
        final GameEngine engine = this;
        postMessageCallback = new Runnable() {
            @Override
            public void run() {
                engine.messageScreenDisplayed = false;
                engine.loadGame();
            }
        };
    }

    private void drawMessageScreen(long delta, Graphics2D g) {
        messageTimePassed += delta;
        if (messageTimePassed > messageTime)
            postMessageCallback.run();
        else {
            Font font = new Font(Font.SERIF, Font.BOLD, 40);
            FontMetrics metrics = g.getFontMetrics(font);
            int vsize = metrics.getHeight();
            int vstart = height() / 2 - vsize / 2;
            g.setFont(font);
            g.setColor(messageColor);
            int width = metrics.stringWidth(message);
            g.drawString(message, width() / 2 - width / 2, vstart);
        }
    }

    public void nextLevel() {
        curlevel++;
        if(curlevel > 3) {
            messageScreenDisplayed = true;
            message = "Ви пройшли всю гру";
            messageTimePassed = 0;
            final GameEngine engine = this;
            postMessageCallback = new Runnable() {
                @Override
                public void run() {
                    engine.messageScreenDisplayed = false;
                    engine.showMainMenu();
                }
            };
        }
        loadLevel(Integer.toString(curlevel));
    }
}
