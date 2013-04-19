public abstract class GameObject {
    Drawable drawable;
    GeometricObject geometry;

    public Drawable getDrawable() {
        return drawable;
    }

    public GeometricObject getGeometry() {
        return geometry;
    }

    public abstract void update(long delta);
}
