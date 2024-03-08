public class Ray {
    private Tile start, end;
    private int direction;
    //path
    // TODO

    public Ray(Tile start, int direction) {
        this.start = start;
        this.direction = direction;
    }

    public void setEnd(Tile end) {
        this.end = end;
    }

    public Tile getEnd() {
        return this.end;
    }

    public Tile getStart() {
        return this.start;
    }

    public int getDirection() {
        return this.direction;
    }

    public void setDirection(int d) {
        this.direction = d;
    }
}
