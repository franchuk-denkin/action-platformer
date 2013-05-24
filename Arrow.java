public class Arrow extends Projectile {
    private final int arrowV = 200;

    Arrow(GameEngine eng, int startX, int startY, double direction) {
        super(eng, startX, startY, direction);
        drawable = new ImageSetDrawable("arrow.png", startX, startY, direction);
        ((ImageSetDrawable)drawable).setProj(true);
        sentByPlayer = true;
        setV(arrowV);
        sentByPlayer = true;
    }

}
