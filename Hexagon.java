import java.awt.*;



public class Hexagon
{
    // adjacent hexagons
    private boolean active = false;
    // north, north east, south east, south, south west, north west
    private Hexagon n, ne, se, s, sw, nw;
    // Coordinates of this hexagon
    private Point p;
    // 3 number coordinates of this hexagon - used for generation
    public final int x, y, z;
    // What should be displayed when clicked?
    private int value;

    public Hexagon(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Hexagon getAdjacent() {
        // TODO
        return null;
    }

    /**
     * @param adj Hexagon to put as adjacent
     * @param direction 1 = north, and then clockwise up to 6 = nortwest
     */
    public void setAdjacent(Hexagon adj, int direction) {
        switch (direction) {
            case 1:
                this.n = adj;
                break;
            case 2:
                this.ne = adj;
                break;
            case 3:
                this.se = adj;
                break;
            case 4:
                this.s = adj;
                break;
            case 5:
                this.sw = adj;
                break;
            case 6:
                this.nw = adj;
                break;
        }
    }

    /**
     * @return Position in offset coordinates
     */
    public Point getPosition() {
        return this.p;
    }

    public void convertPosition(int bsize) {
        int yOffset;
        if(this.z > this.x && (bsize/2)%2 == 0) {
            // -1 here acts as ceiling function, because the board is a bit biased to higher numbers(bottom right y coordinates are 6 6 7 7 8)
            yOffset = (this.x-this.z - 1)/2;
        } else if (this.z < this.x && (bsize/2)%2 == 1) {
            // +1 here is again a floor-ish function, and it is divided like this, because 4n+1 sized boards behave a bit differently than 4n+3 sized boards
            yOffset = (this.x-this.z + 1)/2;
        } else {
            yOffset = (this.x-this.z)/2;
        }
        this.p = new Point(bsize/2 + this.y, bsize/2 + yOffset);
    }
    
    public int getValue() {
        return this.value;
    }

    public void setValue(int v) {
        this.value = v;
    }

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
}