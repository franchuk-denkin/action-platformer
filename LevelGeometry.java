import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelGeometry extends Drawable implements GeometricObject {
    BufferedImage layout, visual;
    GameEngine engine;

    LevelGeometry(GameEngine eng, BufferedImage _layout, BufferedImage _visual) {
        engine = eng;
        layout = _layout;
        visual = _visual;
    }

    @Override
    public void draw(Graphics2D canvas) {
        canvas.translate(translationX, translationY);
        canvas.drawImage(visual, 0, 0, null);
        canvas.translate(-translationX, -translationY);
    }

    @Override
    public boolean checkCollision(GeometricObject g) {
        return false;
    }

    @Override
    public int intersectWithDownRay(int x, int y) {
        for (int i = y; i < layout.getHeight(); i++)
            if(layout.getRGB(x, i) == 0xff000000)
                return i;
        return Integer.MAX_VALUE;
    }
}
