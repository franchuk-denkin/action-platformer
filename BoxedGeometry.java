public class BoxedGeometry implements GeometricObject {
    private int x, y, width, height;

    BoxedGeometry(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean checkCollision(GeometricObject g) {
        return false;
    }

    @Override
    public int intersectWithDownRay(int x, int y) {
        if (x >= this.x && x < this.x + width) {
            if (y <= this.y)
                return y;
            if (y <= this.y + height)
                return this.y + height;
        }
        return Integer.MAX_VALUE;
    }

    void setCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
