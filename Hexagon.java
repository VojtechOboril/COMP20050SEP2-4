import java.awt.*;
import javax.swing.*;
import java.awt.event.*;



public class Hexagon
{
    // adjacent hexagons
    // north, north east, south east, south, south west, north west
    private Hexagon n, ne, se, s, sw, nw;
    // How far from center of the board is it? used for generation of a board
    private final int depth;
    // Coordinates of this hexagon
    private Point p;
    // 3 number coordinates of this hexagon - used for generation
    private final int x, y, z;

    private int value;

    public Hexagon(int depth, int x, int y, int z) {
        this.depth = depth;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Hexagon getAdjacent() {
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

    public Point getPosition() {
        return this.p;
    }

    public void convertPosition(int bsize) {
        int yOffset;
        if(this.z < this.x) {
            yOffset = (this.x-this.z)/2;
        } else {
            // -1 here acts as ceiling function, because the board is a bit biased to higher numbers(bottom right y coordinates are 6 6 7 7 8)
            yOffset = (this.x-this.z - 1)/2;
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
            this.value = (int)'•';
        } else {
            this.value = 0;
        }
    }
}