public class Edge extends Tile {
    public Edge(int x, int y, int z) {
        super(x, y, z);
    }

    @Override
    public void clicked() {
        // TODO change 0 to reflect the correct direction
        Ray r = new Ray(this, 0);
        this.getAdjacent(0).receiveRay(r);
    }

    @Override
    public void receiveRay(Ray r) {
        r.setEnd(this);
        //System.out.println("Ray started at " + r.getStart() + " and ended at " + r.getEnd());
    }
}
