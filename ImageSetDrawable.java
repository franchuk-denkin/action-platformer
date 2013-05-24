import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageSetDrawable extends Drawable {
    private BufferedImage img;
    private double direction = 0;

    public ImageSetDrawable(String file, int x, int y) {
        setCoords(x, y);
        try {
            img = ImageIO.read(new File(file));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ImageSetDrawable(String file, int x, int y, double direction) {
        this(file, x, y);
        this.direction = direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    @Override
    public void draw(Graphics2D canvas) {
        AffineTransform t = canvas.getTransform();
        canvas.translate(translationX, translationY);
        canvas.rotate(direction);
        canvas.drawImage(img, 0, 0, null);
        canvas.setTransform(t);
    }
}
