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

                // check if there is an atom right next to it, resulting in a reflection or absorbtion
                boolean reflected = false;
                boolean absorbed = false;
                for(int j = r.getDirection() - 1; j <= r.getDirection()+1; j++) {
                    // if one of the tiles in the direction of the beam has an atom
                    if(this.getAdjacent((j + 6)%6) instanceof Hexagon && ((Hexagon) this.getAdjacent((j + 6)%6)).getActive()) {
                        if(j == r.getDirection()) absorbed = true;
                        reflected = true;
                    }
                }
                if(absorbed) {
                    r.setEnd(this.getAdjacent(r.getDirection()));
                    r.setResult(Result.ABSORBED);
                } else if(reflected) {
                    this.receiveRay(r);
                } else {
                    this.getAdjacent(r.getDirection()).receiveRay(r);
                }
            }
        }
    }

    @Override
    public void receiveRay(Ray r) {
        r.setEnd(this);
        if(r.getStart() == r.getEnd()) {
            r.setResult(Result.REFLECTION);
        } else {
            r.setResult(Result.DETOUR);
        }
    }
}
