public interface GeometricObject {
    public boolean checkCollision(GeometricObject g);
    public int intersectWithDownRay(int x, int y);
    public int intersectWithUpRay(int x, int y);
    public boolean checkCoverage(int x, int y);
}
