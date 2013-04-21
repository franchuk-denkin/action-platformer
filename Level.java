import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Level extends GameObject {
    BufferedImage layout, visual;
    GameEngine engine;

    Level(GameEngine eng, String file_prefix) {
        engine = eng;
        try {
            layout = ImageIO.read(new File(file_prefix + ".layout.png"));
            visual = ImageIO.read(new File(file_prefix + ".visual.png"));
            LevelGeometry g = new LevelGeometry(engine, layout, visual);
            drawable = g;
            geometry = g;
        }
        catch (Exception e) {
        }
    }

    @Override
    public void update(long delta, Graphics2D g) {
        super.update(delta, g);
    }
}
