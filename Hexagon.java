import java.awt.*;

public class Hexagon extends Tile
{

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
        this.getAdjacent(r.getDirection()).receiveRay(r);
    }
}