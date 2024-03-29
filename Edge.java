public class Edge extends Tile {
    public Edge(int x, int y, int z) {
        super(x, y, z);
    }

    //TODO if there is more than one adjacent hexagon then use locationOnHex variable (0/1)
    //have variable Tile adj1, adj2
    //e.g if locationOnHex == 0 then send ray to adj1 else adj2.

    @Override
    public void clicked() {
        //set these to be the 2 adj locations if there is more than 2 locations.
        Tile[] adjs = new Tile[2];
        int[] directions= new int[2];
        //check how many adjacents there are, then save them into the array 'adjs'
        int counter=0;
        for(int i=0;i<6;i++){
            Tile adj = this.getAdjacent(i);
            if(adj instanceof Hexagon){
                adjs[counter]= adj;
                directions[counter] = i;
                counter++;
            }
        }

        for(int i = 0;  i < 6; i++) {
            Tile adj = this.getAdjacent(i);
            if(adj instanceof Hexagon) {
                Ray r = new Ray(this,i);
                if(counter==2){
                    System.out.println(locationOnHex);
                    if(locationOnHex==0){
                        r.setStart(adjs[0]);
                        r.setDirection(directions[0]);
                        System.out.println("direction="+directions[0]);
                    }
                    else if(locationOnHex==1){
                        r.setStart(adjs[1]);
                        r.setDirection(directions[1]);
                        System.out.println("direction="+directions[1]);
                    }
                }

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
