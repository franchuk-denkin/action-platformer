import java.awt.event.KeyEvent;
import java.util.SortedSet;

public abstract class MovableObject extends GameObject {
    protected GameEngine engine;
    protected int orientation = 1;
    protected int multiplier = 0;
    protected int speed = 200;
    protected int x, y;
    protected int width, height;
    protected boolean jumpStartCondition = false, couldMove = false;
    private double delta_x_sum = 0;
    private long gravityFallingStartTime;
    private long gravityFallingStartY;
    private boolean falling = false;
    private final int acceleration = 100;
    private final int jumpingV = -50;
    private boolean jumped = false;

    private int startV = 0;

    MovableObject(int x, int y, GameEngine engine) {
        this.x = x;
        this.y = y;
        this.engine = engine;
    }

    protected void move(long delta) {
        // Горизонтальний рух
        SortedSet<GameObject> objList = engine.getObjList();
        int cx;

        delta_x_sum += speed * multiplier * delta / 1000000000.0;
        double delta_x = delta_x_sum;
        int i = multiplier;
        boolean column;
        couldMove = Math.abs(delta_x) < 1;
        while (Math.abs(i) <= Math.abs(delta_x) && multiplier != 0) {
            column = false;
            for (int j = y; j < y + height; j++) {
                cx = multiplier == 1 ? x + width + i : x + i;
                for (GameObject obj : engine.getObjList())
                    if (obj.getGeometry().checkCoverage(cx, j) && obj != this)
                        column = true;
            }
            if (!column) {
                x += multiplier;
                couldMove = true;
            }
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
        if (min_y > y + height || jumped) {
            jumped = false;
            long timestamp = engine.getGameTime();
            if (!falling) {
                falling = true;
                gravityFallingStartTime = timestamp;
                gravityFallingStartY = y;
            }
            double delta_t = (engine.getGameTime() - gravityFallingStartTime) / 1000000000.0;
            double delta_y = acceleration * delta_t * delta_t / 2 + startV * delta_t + gravityFallingStartY - y;
            if (y + delta_y + height <= min_y)
                y += delta_y;
            else {
                y = min_y - height;
                falling = false;
                startV = 0;
            }
        } else if (jumpStartCondition) {
            jumped = true;
            startV = jumpingV;
        }

        drawable.setCoords(x, y);
        ((BoxedGeometry)geometry).setCoords(x, y);
    }
}
