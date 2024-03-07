public class Edge extends Tile {
    public Edge(int x, int y, int z) {
        super(x, y, z);
    }

    @Override
    public void clicked() {
        for(int i = 0;  i < 6; i++) {
            Tile adj = this.getAdjacent(i);
            if(adj instanceof Hexagon) {
                // TODO only send 1
                Ray r = new Ray(this, i);
                System.out.println("Ray started at " + this + " with the direction of " + r.getDirection());
                this.getAdjacent(r.getDirection()).receiveRay(r);
            }
        }
    }

    @Override
    public void receiveRay(Ray r) {
        r.setEnd(this);
        System.out.println("Ray started at " + r.getStart() + " and ended at " + r.getEnd());
    }
}
