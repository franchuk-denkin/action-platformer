import java.awt.*;

public class GameObject {
    Drawable drawable;
    GeometricObject geometry;

    public Drawable getDrawable() {
        return drawable;
    }

    public GeometricObject getGeometry() {
        return geometry;
    }

    public void update(long delta, Graphics2D g) {
        drawable.draw(g);
    }
}
