import java.awt.*;

public abstract class Projectile extends GameObject {
    protected GameEngine engine;
    protected int startX = 0, startY = 0, v = 0;
    protected double direction;
    protected boolean sentByPlayer = false;
    protected int length;
    protected long delta_sum = 0;

    Projectile(GameEngine eng, int startX, int startY, double direction) {
        engine = eng;
        geometry = new EmptyGeometry();
        this.startX = startX;
        this.startY = startY;
        this.direction = direction;
        length = 50;
    }

    @Override
    public void update(long delta, Graphics2D g) {
        double dx = Math.cos(direction), dy = Math.sin(direction);
        delta_sum += delta;

        int x = (int)(startX + dx * v * delta_sum / 1000000000.0);
        int y = (int)(startY + dy * v * delta_sum / 1000000000.0);

        for (GameObject obj : engine.getObjList()) {
            if (obj.getGeometry().checkCoverage((int) (x + dx * length), (int) (y + dy * length))) {
                engine.deleteObject(this);
                if (obj instanceof Player)
                    ((Player) obj).attackIt(this);
                else if (obj instanceof Enemy)
                    ((Enemy) obj).attackIt(2);
                return;
            }
        }

        drawable.setCoords(x, y);
        ((ImageSetDrawable)drawable).setDirection(direction);

        super.update(delta, g);
    }

    public void setV(int v) {
        this.v = v;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }
}
