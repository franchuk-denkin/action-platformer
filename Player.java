import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.SortedSet;

public class Player extends GameObject {
    private int x, y;
    private GameEngine engine;
    public final int width = 30, height = 50, speed = 200;

    private long gravityFallingStartTime;
    private long gravityFallingStartY;
    public double delta_x_sum = 0;
    private boolean falling = false;
    private final int acceleration = 100;

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    Player(int x, int y, GameEngine engine) {
        engine.setPlayer(this);
        drawable = new ImageSetDrawable("player.png", x, y);
        geometry = new BoxedGeometry(x, y, width, height);
        this.x = x;
        this.y = y;
        this.engine = engine;
    }

    int orientation = 1;

    private void move(long delta) {
        // Горизонтальний рух
        SortedSet<GameObject> objList = engine.getObjList();
        int multiplier = 0;
        int cx;
        if(engine.getKeys().contains(KeyEvent.VK_A))
            orientation = multiplier = -1;
        else if(engine.getKeys().contains(KeyEvent.VK_D))
            orientation = multiplier = 1;
        delta_x_sum += speed*multiplier*delta/1000000000.0;
        double delta_x = delta_x_sum;
        int i = multiplier;
        boolean column ;
        while(Math.abs(i) <= Math.abs(delta_x) && multiplier != 0) {
            column=false;
            for (int j = y; j < y + height; j++)
            {cx = multiplier == 1 ? x + width + i : x + i;
                for(GameObject obj: engine.getObjList())
                    if (obj.getGeometry().checkCoverage(cx,j) && obj != this)
                        column=true;
            }
            if (!column)
                x += multiplier;
            i += multiplier;
            delta_x_sum -= multiplier;
        }
        // Гравітація
        int min_y = Integer.MAX_VALUE;
        for (int x_ray = x; x_ray < x + width; x_ray++)
            for (GameObject obj : objList)
                if (obj != this) {
                    int y_int = obj.getGeometry().intersectWithDownRay(x_ray, y);
                    assert y_int >= y + height;
                    min_y = Math.min(min_y, y_int);
                }
        if (min_y > y + height) {
            long timestamp = engine.getGameTime();
            if (!falling) {
                falling = true;
                gravityFallingStartTime = timestamp;
                gravityFallingStartY = y;
            }
            double delta_t = (engine.getGameTime() - gravityFallingStartTime) / 1000000000.0;
            double delta_y = acceleration * delta_t * delta_t / 2 + gravityFallingStartY - y;
            if (y + delta_y + height <= min_y)
                y += delta_y;
            else {
                y = min_y - height;
                falling = false;
            }
        }
        // Оновити координати відображення та геометрії
        drawable.setCoords(x, y);
        ((BoxedGeometry)geometry).setCoords(x, y);
    }

    boolean attacking = false;
    long attackStartTime;
    final long attackDuration = 400000000; // 0.4s
    final int swordAttack = 5;
    final int attackRange = 10;
    private void performAttack(long delta) {
        if(engine.getGameTime() - attackStartTime > attackDuration && attacking)
            attacking = false;
        if(!attacking && engine.getKeys().contains(KeyEvent.VK_K)) {
            attacking = true;
            attackStartTime = engine.getGameTime();
            for (GameObject obj : engine.getObjList()) {
                if(obj instanceof Enemy) {
                    if ((obj.getGeometry().checkCoverage(x + width + attackRange, y + height / 2) && orientation == 1) // атака вправо
                        || (obj.getGeometry().checkCoverage(x - attackRange, y + height / 2) && orientation == -1)) // атака вліво
                        ((Enemy)obj).attackIt(swordAttack);
                }
            }
        }
    }

    @Override
    public void update(long delta, Graphics2D g) {
        move(delta);
        performAttack(delta);
        super.update(delta, g);
    }
}
