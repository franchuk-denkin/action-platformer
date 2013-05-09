import java.awt.*;

public class GameObject implements Comparable<GameObject> {
    protected Drawable drawable;
    protected GeometricObject geometry;
    protected int priority = 0;

    public Drawable getDrawable() {
        return drawable;
    }

    public GeometricObject getGeometry() {
        return geometry;
    }

    public void update(long delta, Graphics2D g) {
        drawable.draw(g);
    }

    @Override
    public int compareTo(GameObject o) {
        if(priority < o.priority)
            return -1;
        if(priority > o.priority)
            return 1;
        return 0;
    }
}
