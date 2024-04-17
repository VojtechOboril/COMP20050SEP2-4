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
    protected final Polygon hexagonPoints;

    public Tile(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.convertPosition();

        int x0 = p.x * (Hexmech.s+Hexmech.t);
        int y0 = p.y * Hexmech.h + (p.x%2) * Hexmech.h/2;
        this.hexagonPoints = Hexmech.hex(x0, y0);
    }
    abstract void receiveRay(Ray r);
    abstract void clicked();
    abstract void drawBottom(Graphics2D g2, boolean showCircles);
    abstract void drawTop(Graphics2D g2, boolean showCircles);
    abstract void drawRayMarker(Graphics2D g2);
    /**
     * @param adj Hexagon to put as adjacent
     * @param direction 0 = north, and then counterclockwise up to 5 = northeast
     */
    public void setAdjacent(Tile adj, int direction) {
        switch (direction) {
            case 0:
                this.n = adj;
                break;
            case 5:
                this.ne = adj;
                break;
            case 4:
                this.se = adj;
                break;
            case 3:
                this.s = adj;
                break;
            case 2:
                this.sw = adj;
                break;
            case 1:
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

    // suppressing warnings because bsize is final, and it never expects it to hit the first condition
    @SuppressWarnings("all")
    public void convertPosition() {
        int yOffset;
        if(this.z > this.x && (Board.BSIZE/2)%2 == 0) {
            // -1 here acts as ceiling function, because the board is a bit biased to higher numbers(bottom right y coordinates are 6 6 7 7 8)
            yOffset = (this.x-this.z - 1)/2;
        } else if (this.z < this.x && (Board.BSIZE/2)%2 == 1) {
            // +1 here is again a floor-ish function, and it is divided like this, because 4n+1 sized boards behave a bit differently than 4n+3 sized boards
            yOffset = (this.x-this.z + 1)/2;
        } else {
            yOffset = (this.x-this.z)/2;
        }
        this.p = new Point(Board.BSIZE/2 + this.y, Board.BSIZE/2 + yOffset);
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
