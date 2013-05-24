import java.awt.*;

public class WolfSnake extends Enemy {
    private static final int wolfWidth = 100, wolfHeight = 35;
    private static final int snakeWidth = 40, snakeHeight = 15;
    private static final int snowWolfWidth = 100, snowWolfHeight = 50;

    private static final String[] wolf_fn = new String[]{"wolf.png"};
    private static final String[] snake_fn = new String[]{"snake.png"};
    private static final String[] snowWolf_fn = new String[]{"swolf.png"};

    private final int initialHealth = 7;

    private WolfSnake(GameEngine eng, int x, int y, int width, int height, String[] files) {
        super(eng, x, y);
        this.width = width;
        this.height = height;
        drawable = new ImageSetDrawable(files, x, y);
        geometry = new BoxedGeometry(x, y, width, height);
        health = initialHealth;
        speed = 30;
    }

    public static WolfSnake createWolf(GameEngine eng, int x, int y) {
        return new WolfSnake(eng, x, y, wolfWidth, wolfHeight, wolf_fn);
    }

    public static WolfSnake createSnake(GameEngine eng, int x, int y) {
        return new WolfSnake(eng, x, y, snakeWidth, snakeHeight, snake_fn);
    }

    public static WolfSnake createSnowWolf(GameEngine eng, int x, int y) {
        return new WolfSnake(eng, x, y, snowWolfWidth, snowWolfHeight, snowWolf_fn);
    }
    private final long neededDelay = 200000000;
    private long delay = 0;
    private final int attackRange = 5;
    public void performAttack(long delta) {
        int cx;
        if (orientation == 1)
            cx = x + width + attackRange;
        else
            cx = x - attackRange;
        if(engine.player.getGeometry().checkCoverage(cx, y + height / 2)) {
            if (delay >= neededDelay) {
                engine.player.attackIt(this);
                delay = 0;
            }
            else
                delay += delta;
        }
        else
            delay = 0;
    }

    @Override
    public void update(long delta, Graphics2D g) {
        multiplier = orientation = -1;
        move(delta);
        performAttack(delta);
        super.update(delta, g);
    }
}
