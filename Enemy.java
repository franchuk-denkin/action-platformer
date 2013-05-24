import java.awt.*;

public abstract class Enemy extends MovableObject {
    protected int health;
    protected GameEngine engine;
    protected boolean stunned = false;
    protected long stunnedAt;
    protected final long stunTime = 1000000000;

    public Enemy(GameEngine eng, int x, int y) {
        super(x, y, eng);
        engine = eng;
    }

    public void attackIt(int amount) {
        health -= amount;
        if (health <= 0)
            engine.deleteObject(this);
    }

    public boolean isBoss() {
        return false;
    }

    public void stun() {
        if (!isBoss()) {
            stunned = true;
            stunnedAt = engine.getGameTime();
        }
    }

    @Override
    public void update(long delta, Graphics2D g) {
        if (stunned && engine.getGameTime() - stunnedAt >= stunTime)
            stunned = false;
        super.update(delta, g);
    }
}
