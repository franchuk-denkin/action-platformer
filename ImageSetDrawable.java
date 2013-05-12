import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageSetDrawable extends Drawable {
    private BufferedImage img;

    public ImageSetDrawable(String file, int x, int y) {
        setCoords(x, y);
        try {
            img = ImageIO.read(new File(file));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics2D canvas) {
        canvas.translate(translationX, translationY);
        canvas.drawImage(img, 0, 0, null);
        canvas.translate(-translationX, -translationY);
    }
}
