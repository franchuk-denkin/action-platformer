public class Enemy extends GameObject {
    protected int health;
    protected GameEngine engine;

    public Enemy(GameEngine eng) {
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
}
