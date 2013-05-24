import java.awt.*;

public class Saurus extends Enemy {

    private final int dWidth = 100, dHeight = 50, initialHealth = 200;
    private final String[] files = new String[] {"saurus.png"};
    private final long neededDelay = 200000000;
    private final int attackRange = 5;
    private long delay = 0;

    public Saurus(GameEngine eng, int x, int y) {
        super(eng, x, y);
        width = dWidth;
        height = dHeight;
        health = initialHealth;
        drawable = new ImageSetDrawable(files, x, y);
        geometry = new BoxedGeometry(x, y, width, height);
        speed = 30;
        orientation = 1;
    }

    private void performAttack(long delta) {
        if(!stunned) {
            int cx;
            if (orientation == 1)
                cx = x + width + attackRange;
            else
                cx = x - attackRange;
            if (engine.player.getGeometry().checkCoverage(cx, y + height / 2)) {
                if (delay >= neededDelay) {
                    engine.player.attackIt(this);
                    delay = 0;
                } else
                    delay += delta;
            } else
                delay = 0;
        }
    }

    @Override
    public void update(long delta, Graphics2D g) {
        multiplier = orientation;
        if(stunned)
            multiplier = 0;
        move(delta);
        if (!couldMove) {
            orientation = -orientation;
            stun();
        }
        performAttack(delta);
        super.update(delta, g);
    }
}
