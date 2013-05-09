import java.awt.*;
import java.util.SortedSet;

public class Player extends GameObject {
    private int x, y;
    private GameEngine engine;
    public final int width = 30, height = 50;

    private long gravityFallingStartTime;
    private long gravityFallingStartY;
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
