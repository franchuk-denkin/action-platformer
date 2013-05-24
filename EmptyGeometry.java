public class EmptyGeometry implements GeometricObject {

    @Override
    public boolean checkCollision(GeometricObject g) {
        return false;
    }

    @Override
    public int intersectWithDownRay(int x, int y) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean checkCoverage(int x, int y) {
        return false;
    }
}
