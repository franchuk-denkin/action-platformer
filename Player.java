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
        drawable = new ImageSetDrawable(x, y);
        geometry = new BoxedGeometry(x, y, width, height);
        this.x = x;
        this.y = y;
        this.engine = engine;
    }

    @Override
    public void update(long delta, Graphics2D g) {
        // Гравітація
        SortedSet<GameObject> objList = engine.getObjList();
        int min_y = Integer.MAX_VALUE;
        int multiplier = 0;
        int cx;
        if(engine.getKeys().contains(KeyEvent.VK_A))
        multiplier = -1;
        else if(engine.getKeys().contains(KeyEvent.VK_D))
            multiplier = 1;
        delta_x_sum += speed*multiplier*delta/1000000000.0;
        double delta_x = delta_x_sum;
        int i = multiplier;
        boolean stolbik ;
        while(Math.abs(i) <= Math.abs(delta_x) && multiplier != 0) {
            stolbik=false;
            for (int j = y; j < y + height; j++)
            {cx = multiplier == 1 ? x + width + i : x + i;
                for(GameObject obj: engine.getObjList())
                    if (obj.getGeometry().checkCoverage(cx,j) && obj != this)
                        stolbik=true;
            }
            if (!stolbik)
                x += multiplier;
            i += multiplier;
            delta_x_sum -= multiplier;
        }

        for (int x_ray = x; x_ray < x + width; x_ray++)
            for (GameObject obj : objList)
                if (obj != this) {
                    int y_int = obj.getGeometry().intersectWithDownRay(x_ray, y);
                    assert y_int >= y + height;
                    min_y = Math.min(min_y, y_int);
                }
        if (min_y > y + height) {
            long timestamp = System.nanoTime();
            if (!falling) {
                falling = true;
                gravityFallingStartTime = timestamp;
                gravityFallingStartY = y;
            }
            double delta_t = (System.nanoTime() - gravityFallingStartTime) / 1000000000.0;
            double delta_y = acceleration * delta_t * delta_t / 2 + gravityFallingStartY - y;
            if (y + delta_y + height <= min_y)
                y += delta_y;
            else {
                y = min_y - height;
                falling = false;
            }
        }

        drawable.setCoords(x, y);
        ((BoxedGeometry)geometry).setCoords(x, y);

        super.update(delta, g);
    }
}
