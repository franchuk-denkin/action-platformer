import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Menu {
    final Color inactiveColor = new Color(255, 255, 255);
    final Color activeColor = new Color(255, 255, 100);
    final long timeToChange = 100000000;
    long timePassed = 0;
    boolean pressedInThisPass = false;

    int activeItem = 0;

    class MenuItem {
        public String title;
        public Runnable runnable;
        MenuItem(String _title, Runnable _runnable) {
            title = _title;
            runnable = _runnable;
        }
    }

    ArrayList<MenuItem> itemList;
    GameEngine engine;

    Menu(GameEngine eng) {
        engine = eng;
        itemList = new ArrayList<MenuItem>();
    }

    public void draw(long delta, Graphics2D canvas) {
        if(engine.keyPressed(KeyEvent.VK_DOWN)) {
            activeItem++;
            activeItem %= itemList.size();
        }
        if(engine.keyPressed(KeyEvent.VK_UP)) {
            activeItem--;
            if (activeItem < 0)
                activeItem += itemList.size();
        }
        if (engine.keyPressed(KeyEvent.VK_ENTER))
            itemList.get(activeItem).runnable.run();
        Font font = new Font(Font.SERIF, Font.BOLD, 40);
        FontMetrics metrics = canvas.getFontMetrics(font);
        int vsize = metrics.getHeight() * itemList.size();
        int vstart = engine.height() / 2 - vsize / 2;
        canvas.setFont(font);
        for (int i = 0; i < itemList.size(); i++) {
            if (i == activeItem) canvas.setColor(activeColor);
            else canvas.setColor(inactiveColor);
            MenuItem item = itemList.get(i);
            int width = metrics.stringWidth(item.title);
            canvas.drawString(item.title, engine.width() / 2 - width / 2, vstart + i * metrics.getHeight());
        }

    }

    void addItem(String title, Runnable runnable) {
        itemList.add(new MenuItem(title, runnable));
    }
}