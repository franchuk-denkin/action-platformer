import java.awt.*;

public abstract class Enemy extends MovableObject {
    protected int health;
    protected GameEngine engine;
    protected boolean stunned = false;
    protected long stunnedAt;
    protected long stunTime = 1000000000;

    public Enemy(GameEngine eng, int x, int y) {
        super(x, y, eng);
        engine = eng;
    }

    public void attackIt(int amount) {
        if(stunned)
            amount *= 2;
        health -= amount;
        if (health <= 0) {
            engine.deleteObject(this);
            if(isBoss())
                engine.nextLevel();
        }
    }

    public boolean isBoss() {
        return false;
    }

    protected void iStun() {
        stunned = true;
        stunnedAt = engine.getGameTime();
    }

    public void stun() {
        if (!isBoss()) {
            iStun();
        }
    }

    @Override
    public void update(long delta, Graphics2D g) {
        if (stunned && engine.getGameTime() - stunnedAt >= stunTime)
            stunned = false;
        super.update(delta, g);
    }
}
