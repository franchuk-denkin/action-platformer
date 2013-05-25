import java.awt.*;

public class WolfSnake extends Enemy {
    private static final int wolfWidth = 44, wolfHeight = 38;
    private static final int snakeWidth = 96, snakeHeight = 44;
    private static final int snowWolfWidth = 44, snowWolfHeight = 38;
    private static final String wolf_fn = "wolf.png";
    private static final String snake_fn = "snakee.png";
    private static final String snowWolf_fn = "wolf.png";
    private static final int snakeH = 4, snakeV = 2, wolfH = 5, wolfV = 2;
    private final int initialHealth = 7;
    private final long neededDelay = 200000000;
    private final int attackRange = 5;
    private long delay = 0;
    private static final int wolf = 0, snake = 1, swolf = 2;
    private int type = wolf;

    private WolfSnake(GameEngine eng, int x, int y, int width, int height, String file, int h, int v) {
        super(eng, x, y);
        this.width = width;
        this.height = height;
        drawable = new ImageSetDrawable(file, x, y, h, v);
        geometry = new BoxedGeometry(x, y, width, height);
        health = initialHealth;
        speed = 30;
    }

    private void setType(int t) {
        type = t;
    }

    private void selectSnakeImg() {
        int base = orientation == -1 ? 0 : 4;
        long et = engine.getGameTime();
        if (!stunned)
            ((ImageSetDrawable)drawable).setImg((int)((et / 250000000) % 4 + base));
        else
            ((ImageSetDrawable)drawable).setImg(base);
    }

    private void selectWolfImg() {
        int base = orientation == -1 ? 0 : 5;
        long et = engine.getGameTime();
        if (!stunned)
            ((ImageSetDrawable)drawable).setImg((int)((et / 200000000) % 5 + base));
        else
            ((ImageSetDrawable)drawable).setImg(base);
    }

    public static WolfSnake createWolf(GameEngine eng, int x, int y) {
        WolfSnake res = new WolfSnake(eng, x, y, wolfWidth, wolfHeight, wolf_fn, wolfH, wolfV);
        res.setType(wolf);
        return res;
    }

    public static WolfSnake createSnake(GameEngine eng, int x, int y) {
        WolfSnake res = new WolfSnake(eng, x, y, snakeWidth, snakeHeight, snake_fn, snakeH, snakeV);
        res.setType(snake);
        return res;
    }

    public static WolfSnake createSnowWolf(GameEngine eng, int x, int y) {
        WolfSnake res = new WolfSnake(eng, x, y, snowWolfWidth, snowWolfHeight, snowWolf_fn, wolfH, wolfV);
        res.setType(swolf);
        return res;
    }

    public void performAttack(long delta) {
        if (!stunned) {
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
        multiplier = orientation = -1;
        if(type == snake)
            selectSnakeImg();
        else
            selectWolfImg();
        if(stunned)
            multiplier = 0;
        move(delta);
        performAttack(delta);
        super.update(delta, g);
    }
}
