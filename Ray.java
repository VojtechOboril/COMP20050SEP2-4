public class Ray {
    private Tile start, end;
    private int direction;
    private Result result;

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
    public void setStart(Tile start) {
        this.start=start;
    }

    public void setResult(Result r) {
        this.result = r;
        //System.out.println("Ray started at " + this.getStart() + " and ended at " + this.getEnd() + " with the result of " + this.getResult());
    }

    public Result getResult() {
        return this.result;
    }
}