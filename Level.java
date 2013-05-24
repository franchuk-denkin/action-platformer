import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Level extends GameObject {
    BufferedImage layout, visual;
    GameEngine engine;

    int getLayRGB(int x, int y) {
        boolean rgbc = true;
        int rgb = layout.getRGB(x, y);
        if (x > 0)
            if (rgb == layout.getRGB(x - 1, y))
                rgbc = false;
        if (y > 0)
            if (rgb == layout.getRGB(x, y - 1))
                rgbc = false;
        return rgbc ? rgb : 0;
    }

    private final int playerColor = 0xffffff00;
    private final int snakeColor = 0xff80FF80;
    private final int wolfColor = 0xff808080;
    private final int swolfColor = 0xffC0C0C0;
    private final int saurusColor = 0xff800000;

    Level(GameEngine eng, String file_prefix) {
        priority = -1;
        engine = eng;
        try {
            layout = ImageIO.read(new File(file_prefix + ".layout.png"));
            visual = ImageIO.read(new File(file_prefix + ".visual.png"));
            LevelGeometry g = new LevelGeometry(engine, layout, visual);
            drawable = g;
            geometry = g;

            for (int i = 0; i < layout.getHeight(); i++)
                for (int j = 0; j < layout.getWidth(); j++) {
                    int lc = getLayRGB(j, i);
                    if(lc == playerColor)
                        engine.addObject(new Player(j, i, engine));
                    else if(lc == snakeColor)
                        engine.addObject(WolfSnake.createSnake(engine, j, i));
                    else if(lc == wolfColor)
                        engine.addObject(WolfSnake.createWolf(engine, j, i));
                    else if(lc == swolfColor)
                        engine.addObject(WolfSnake.createSnowWolf(engine, j, i));
                    else if(lc == saurusColor)
                        engine.addObject(new Saurus(engine, j, i));
                }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
