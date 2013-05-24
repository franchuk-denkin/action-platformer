import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageSetDrawable extends Drawable {
    private BufferedImage[] imgs;
    private double direction = 0;
    private boolean proj = false;
    private int currentImg = 0;

    public void setProj(boolean proj) {
        this.proj = proj;
    }

    public ImageSetDrawable(String file, int x, int y) {
        setCoords(x, y);
        imgs = new BufferedImage[1];
        try {
            imgs[0] = ImageIO.read(new File(file));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ImageSetDrawable(String[] files, int x, int y) {
        setCoords(x, y);
        imgs = new BufferedImage[files.length];
        try {
            for (int i = 0; i < files.length; i++)
                imgs[i] = ImageIO.read(new File(files[i]));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ImageSetDrawable(String file, int x, int y, double direction) {
        this(file, x, y);
        this.direction = direction;
    }

    public ImageSetDrawable(String[] files, int x, int y, double direction) {
        this(files, x, y);
        this.direction = direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public void setImg(int num) {
        currentImg = num;
    }

    @Override
    public void draw(Graphics2D canvas) {
        AffineTransform t = canvas.getTransform();
        canvas.translate(translationX, translationY);
        canvas.rotate(direction);
        if (proj)
            canvas.translate(0, -imgs[currentImg].getHeight()/2);
        canvas.drawImage(imgs[currentImg], 0, 0, null);
        canvas.setTransform(t);
    }
}
