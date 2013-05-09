import java.awt.*;

public abstract class Drawable {
    protected int translationX = 0, translationY = 0;

    public abstract void draw(Graphics2D canvas);
    public void setCoords(int x, int y) {
        translationX = x;
        translationY = y;
    }
}
