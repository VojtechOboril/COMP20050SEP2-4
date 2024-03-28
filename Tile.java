import java.awt.*;
abstract class Tile {
    // adjacent hexagons
    // north, north east, south east, south, south west, north west
    protected Tile n, ne, se, s, sw, nw;
    // Coordinates of this hexagon
    protected Point p;
    // 3 number coordinates of this hexagon - used for generation
    protected final int x, y, z;
    // What should be displayed when clicked?
    protected int value;
    //where did we click on the hexagon? (in respect to the midpoint) 0=above, 1=below.
    public static int locationOnHex;

    public Tile(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    abstract void receiveRay(Ray r);
    abstract void clicked();
    /**
     * @param adj Hexagon to put as adjacent
     * @param direction 0 = north, and then clockwise up to 5 = nortwest
     */
    public void setAdjacent(Tile adj, int direction) {
        switch (direction) {
            case 0:
                this.n = adj;
                break;
            case 1:
                this.ne = adj;
                break;
            case 2:
                this.se = adj;
                break;
            case 3:
                this.s = adj;
                break;
            case 4:
                this.sw = adj;
                break;
            case 5:
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

    public Tile getAdjacent(int direction) {
        switch (direction) {
            case 0:
                return this.n;
            case 1:
                return this.ne;
            case 2:
                return this.se;
            case 3:
                return this.s;
            case 4:
                return this.sw;
            case 5:
                return this.nw;
            default:
                return null;
        }
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int v) {
        this.value = v;
    }
}
