import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.Set;
import java.util.SortedSet;

public class Player extends MovableObject {
    public final int defaultWidth = 30, defaultHeight = 50;


    private final AffineTransform identityTransform = new AffineTransform();

    private int health = 3;

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    Player(int x, int y, GameEngine engine) {
        super(x, y, engine);
        engine.setPlayer(this);
        width = defaultWidth;
        height = defaultHeight;
        drawable = new ImageSetDrawable(new String[]{"player.png", "player_1.png"}, x, y);
        geometry = new BoxedGeometry(x, y, width, height);
        this.x = x;
        this.y = y;
        this.engine = engine;
        priority = 100;
    }

    private void performMove(long delta) {
        multiplier = 0;
        if (!engine.getKeys().contains(KeyEvent.VK_SPACE))
            if (engine.getKeys().contains(KeyEvent.VK_A))
                orientation = multiplier = -1;
            else if (engine.getKeys().contains(KeyEvent.VK_D))
                orientation = multiplier = 1;
        jumpStartCondition = engine.getKeys().contains(KeyEvent.VK_W) && !engine.getKeys().contains(KeyEvent.VK_SPACE);
        move(delta);
    }

    boolean attacking = false;
    long attackStartTime;
    final long attackDuration = 400000000; // 0.4s
    final int swordAttack = 5;
    final int attackRange = 10;

    private void performSwordAttack(long delta) {
        if(engine.getGameTime() - attackStartTime > attackDuration && attacking)
            attacking = false;
        if(!attacking && engine.getKeys().contains(KeyEvent.VK_K)) {
            attacking = true;
            attackStartTime = engine.getGameTime();
            for (GameObject obj : engine.getObjList()) {
                if(obj instanceof Enemy) {
                    if ((obj.getGeometry().checkCoverage(x + width + attackRange, y + height / 2) && orientation == 1) // атака вправо
                            || (obj.getGeometry().checkCoverage(x - attackRange, y + height / 2) && orientation == -1)) // атака вліво
                        ((Enemy)obj).attackIt(swordAttack);
                }
            }
        }
    }

    private void performBowAttack(long delta) {
        if(engine.getGameTime() - attackStartTime > attackDuration && attacking)
            attacking = false;
        Set<Integer> keys = engine.getKeys();
        if (!attacking &&engine.getKeys().contains(KeyEvent.VK_SPACE)) {
            int ax = 0, ay = 0;
            if (keys.contains(KeyEvent.VK_W)) {
                ax = 0;
                ay = -1;
            }
            if (keys.contains(KeyEvent.VK_Q)) {
                ax = -1;
                ay = -1;
            }
            if (keys.contains(KeyEvent.VK_E)) {
                ax = 1;
                ay = -1;
            }
            if (keys.contains(KeyEvent.VK_A)) {
                ax = -1;
                ay = 0;
            }
            if (keys.contains(KeyEvent.VK_D)) {
                ax = 1;
                ay = 0;
            }
            if (ax != 0 || ay != 0) {
                attacking = true;
                attackStartTime = engine.getGameTime();
                Arrow a = new Arrow(engine, x + width / 2, y + height / 2, Math.atan2(ay, ax));
                engine.addObject(a);
            }
        }
    }

    private boolean blocked = false;
    private long blockStartT = 0;
    private final long blockTime = 1000000000;

    private void block() {
        if (!blocked && engine.getKeys().contains(KeyEvent.VK_SHIFT)) {
            blocked = true;
            blockStartT = engine.getGameTime();
        }
        if (blocked && engine.getGameTime() - blockStartT >= blockTime)
            blocked = false;
    }

    private void performAttack(long delta) {
        performSwordAttack(delta);
        performBowAttack(delta);
    }

    private boolean isSquat = false;

    private void squat() {
        if(isSquat && !engine.getKeys().contains(KeyEvent.VK_S)) {
            ((ImageSetDrawable)drawable).setImg(0);
            isSquat = false;
            height = defaultHeight;
            y -= height / 2;
        }
        if(!isSquat && engine.getKeys().contains(KeyEvent.VK_S)) {
            ((ImageSetDrawable)drawable).setImg(1);
            isSquat  = true;
            height = defaultHeight / 2;
            y += defaultHeight / 2;
        }
    }

    @Override
    public void update(long delta, Graphics2D g) {
        squat();
        block();
        performMove(delta);
        performAttack(delta);
        displayHealth(g);
        super.update(delta, g);
    }

    public void attackIt(GameObject object) {
        if (object instanceof Enemy && blocked)
            ((Enemy)object).stun();
        if (!blocked) {
            health--;
            if (health < 0)
                engine.gameOver();
        }
    }

    public void displayHealth(Graphics2D g) {
        AffineTransform t = g.getTransform();
        g.setTransform(identityTransform);
        g.setColor(Color.red);
        for (int i = 0; i < health; i++)
            g.fillRect(10 + 15 * i, 10, 10, 10);
        g.setTransform(t);
    }

    public boolean isBlocked() {
        return blocked;
    }
}
