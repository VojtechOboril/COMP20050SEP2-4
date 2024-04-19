import java.awt.*;

public class Hexagon extends Tile {
    private static int[] deflectionTable = {0, 1, 0, 2, 5, 3, 4, 3};
    private static int circleRadius = 60;
    private static int topLeftToCenter = (int) Math.sqrt((double) (Board.HEXSIZE * Board.HEXSIZE));
    private boolean active = false;
    private boolean[] passedRays = {false, false, false, false, false, false};

    public Hexagon(int x, int y, int z) {
        super(x, y, z);
    }

    @Override
    public void clicked() {
        if (this.value == 0) {
            if (this.active && Board.showCircles) {
                this.value = 8226; //(int)'•'
            } else {
                this.value = 120; //(int)'x'
            }
        } else {
            this.value = 0;
        }
    }

    //allow to change "active" of hexagon
    void setActive(Boolean x) {
        active = x;
    }

    Boolean getActive() {
        return active;
    }

    public void receiveRay(Ray r) {
        // temporarily show the path of the ray
        // this.clicked();
        // handle deflections, absorbtions
        int startDirection = r.getDirection();
        int endDirection;
        int exponent = 1;
        int index = 0;
        for (int i = startDirection - 1; i <= startDirection + 1; i++) {
            // there were cases where i = -1, and -1%6 = -1, so just making sure this is all positive
            if (this.getAdjacent((i + 6) % 6) instanceof Hexagon && ((Hexagon) this.getAdjacent((i + 6) % 6)).getActive()) {
                index += exponent;
            }
            exponent *= 2;
        }
        endDirection = (startDirection + deflectionTable[index]) % 6;

        if (index != 2) passedRays[endDirection] = true; // it doesn't go anywhere
        else passedRays[startDirection] = true;
        passedRays[(startDirection + 3) % 6] = true;

        if (index == 2) {
            //absorbed by an atom
            r.setEnd(this);
            r.setResult(Result.ABSORBED);
        } else {
            //deflected
            r.setDirection(endDirection);
            this.getAdjacent(endDirection).receiveRay(r);
        }
    }

    public void drawBottom(Graphics2D g2, boolean showCircles) {
        Hexmech.drawHex(this.p.x, this.p.y, g2);
        Hexmech.fillHex(this.p.x, this.p.y, this.value, g2);
    }

    public void drawTop(Graphics2D g2, boolean showCircles) {
        if (showCircles) {
            Point topLeft = Hexmech.hexToPixel(this.p.x, this.p.y);
            Point center = new Point(topLeft.x + topLeftToCenter, topLeft.y + topLeftToCenter);
            if (this.active) {
                g2.setColor(Color.RED);
                Stroke oldStroke = g2.getStroke();
                // Set a dashed stroke
                g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,
                        new float[]{5}, 0));
                g2.drawOval(topLeft.x, topLeft.y, 2 * circleRadius, 2 * circleRadius);
                // Reset the stroke
                g2.setStroke(oldStroke);

                g2.fillOval(center.x - 15, center.y - 15, 30, 30);
            }


            g2.setColor(Color.black);
            for (int i = 0; i < 6; i++) {
                if (passedRays[i]) {
                    int x = (hexagonPoints.xpoints[i] + hexagonPoints.xpoints[(i + 1) % 6]) / 2;
                    int y = (hexagonPoints.ypoints[i] + hexagonPoints.ypoints[(i + 1) % 6]) / 2;
                    g2.setColor(Color.red);
                    g2.drawLine(center.x, center.y, x, y);
                }
            }
        }
    }

    public int correctlySelected() {
        return (this.value == 0 && !this.active) || (this.value == 120 && this.active) ? 1 : -1;
    }

    // just so that Edge can inherit it from Tile
    void drawRayMarker(Graphics2D g2) {

    }
}
