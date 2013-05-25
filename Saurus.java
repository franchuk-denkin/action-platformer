import java.awt.*;

public class Saurus extends Enemy {

    private final int dWidth = 85, dHeight = 56, initialHealth = 200;
    private final String file = "saurus.png";
    private final long neededDelay = 500000000;
    private final int attackRange = 5;
    private final int horiz = 6, vert = 2;
    private long delay = 0;

    public Saurus(GameEngine eng, int x, int y) {
        super(eng, x, y);
        width = dWidth;
        height = dHeight;
        health = initialHealth;
        drawable = new ImageSetDrawable(file, x, y, horiz, vert);
        geometry = new BoxedGeometry(x, y, width, height);
        speed = 60;
        orientation = 1;
        stunTime = 3000000000l;
    }

    @Override
    public void attackIt(int amount) {
        if(!stunned)
            if(engine.player.x > x)
                orientation = 1;
            else
                orientation = -1;
        super.attackIt(amount);
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
        long et = engine.getGameTime() / 166666666;
        int base = orientation == 1 ? 0 : 6;
        if (!stunned)
            ((ImageSetDrawable)drawable).setImg((int)(et % 6 + base));
        else
            ((ImageSetDrawable)drawable).setImg(base);
        if(stunned)
            multiplier = 0;
        move(delta);
        if (!couldMove) {
            orientation = -orientation;
            iStun();
        }
        performAttack(delta);
        super.update(delta, g);
    }

    @Override
    public boolean isBoss() {
        return true;
    }
}
