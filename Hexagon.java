public class Hexagon extends Tile
{
    private static int[] deflectionTable = {0, 1, 0, 2, 5, 3, 4, 3};
    private boolean active = false;
    public Hexagon(int x, int y, int z) {
        super(x, y, z);
    }

    @Override
    public void clicked() {
        if(this.value == 0) {
            if (this.active) {
                this.value = 8226; //(int)'•'
            } else {
                this.value = 120; //(int)'x'
            }
        } else {
            this.value = 0;
        }
    }

    //allow to change "active" of hexagon
    void setActive(Boolean x){
        active = x;
    }
    Boolean getActive(){
        return active;
    }

    public void receiveRay(Ray r) {
        // temporarily show the path of the ray
        this.clicked();
        // handle deflections, absorbtions
        int exponent = 1;
        int index = 0;
        for(int i = r.getDirection() - 1; i <= r.getDirection() + 1; i++) {
            // there were cases where i = -1, and -1%6 = -1, so just making sure this is all positive
            if(this.getAdjacent((i + 6)%6) instanceof Hexagon && ((Hexagon) this.getAdjacent((i + 6)%6)).getActive()) {
                index += exponent;
            }
            exponent *= 2;
        }
        if(index == 2) {
            //absorbed by an atom
            r.setEnd(this);
            r.setResult(Result.ABSORBED);
        } else {
            //deflected
            r.setDirection((r.getDirection() + deflectionTable[index]) % 6);
            this.getAdjacent(r.getDirection()).receiveRay(r);
        }
    }

}
