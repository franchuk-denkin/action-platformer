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
                return this.y;
            if (y <= this.y + height)
                return this.y + height;
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public int intersectWithUpRay(int x, int y) {
        if (x >= this.x && x < this.x + width) {
            if (y >= this.y + height)
                return this.y + height;
            if (y >= this.y)
                return this.y;
        }
        return Integer.MIN_VALUE;
    }

    void setCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean checkCoverage(int x, int y) {
        return x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height;
    }
}
