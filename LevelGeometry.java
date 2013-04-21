import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelGeometry implements GeometricObject, Drawable {
    BufferedImage layout, visual;
    GameEngine engine;

    LevelGeometry(GameEngine eng, BufferedImage _layout, BufferedImage _visual) {
        engine = eng;
        layout = _layout;
        visual = _visual;
    }

    @Override
    public void draw(Graphics2D canvas) {
        canvas.translate(engine.width() / 2, engine.height() / 2);
        canvas.drawImage(visual, -visual.getWidth() / 2, -visual.getHeight() / 2, null);
    }

    @Override
    public boolean checkColission(GeometricObject g) {
        return false;
    }
}
