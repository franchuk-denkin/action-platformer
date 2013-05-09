import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Level extends GameObject {
    BufferedImage layout, visual;
    GameEngine engine;

    Level(GameEngine eng, String file_prefix) {
        priority = -1;
        engine = eng;
        try {
            layout = ImageIO.read(new File(file_prefix + ".layout.png"));
            visual = ImageIO.read(new File(file_prefix + ".visual.png"));
            LevelGeometry g = new LevelGeometry(engine, layout, visual);
            drawable = g;
            geometry = g;

            layoutLoop: for (int i = 0; i < layout.getHeight(); i++)
                for (int j = 0; j < layout.getWidth(); j++)
                    if(layout.getRGB(j, i) == 0xffffff00) {
                        engine.addObject(new Player(j, i, engine));
                        break layoutLoop;
                    }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
