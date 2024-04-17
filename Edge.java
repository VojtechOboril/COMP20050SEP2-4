import java.awt.*;

public class Edge extends Tile {
    private int rayCounter;
    private boolean clickedTopHalf;
    private static int globalRayCounter = 0;
    private Point topSquarePosition;
    private Point topSquarePositionStart;
    private Point topSquarePositionEnd;
    private Point bottomSquarePosition;
    private Point bottomSquarePositionStart;
    private Point bottomSquarePositionEnd;

    public Edge(int x, int y, int z) {
        super(x, y, z);
        this.rayCounter = 0;
        this.topSquarePosition = null;
        this.bottomSquarePosition = null;
        this.topSquarePositionStart = null;
        this.bottomSquarePositionStart = null;
        this.topSquarePositionEnd = null;
        this.bottomSquarePositionEnd = null;
        this.endTile = null;
    }


    public static int locationOnHex;
    public static boolean rightHalf = false;

    public static int locationOnFullHex;
    public static boolean betweenTopAndBot = false;
    public static boolean bottomOfHex = false;

    public static Tile startTile;
    public static Tile endTile = null;


    private Point startRayPoint;
    private Point endRayPoint;


    //TODO if there is more than one adjacent hexagon then use locationOnHex variable (0/1)
    //have variable Tile adj1, adj2
    //e.g if locationOnHex == 0 then send ray to adj1 else adj2.


    @Override
    public void clicked() {
        System.out.println("this is an edge");
        if (locationOnHex == 0) {
            this.clickedTopHalf = true;
        } else {
            this.clickedTopHalf = false;
        }


        //set these to be the 2 adj locations if there is more than 2 locations.
        Tile[] adjs = new Tile[2];
        int[] directions = new int[2];
        //check how many adjacents there are, then save them into the array 'adjs'
        int counter = 0;
        for (int i = 0; i < 6; i++) {
            Tile adj = this.getAdjacent(i);
            if (adj instanceof Hexagon) {
                adjs[counter] = adj;
                directions[counter] = i;
                counter++;
            }
        }

        for (int i = 0; i < 6; i++) {
            Tile adj = this.getAdjacent(i);
            Ray r = new Ray(this, i);
            if (adj instanceof Hexagon) {

                //if has 2 adjacent hexagons
                if (counter == 2 && !rightHalf) {
                    if (locationOnHex == 0) {
                        r.setStart(adjs[0]);
                        r.setDirection(directions[0]);
                        System.out.println("direction=" + directions[0]);
                    } else if (locationOnHex == 1) {
                        r.setStart(adjs[1]);
                        r.setDirection(directions[1]);
                        System.out.println("direction=" + directions[1]);
                    }
                } else if (counter == 2 && rightHalf) {
                    if (locationOnHex == 0) {
                        r.setStart(adjs[1]);
                        r.setDirection(directions[1]);
                        System.out.println("direction=" + directions[1]);
                    } else if (locationOnHex == 1) {
                        r.setStart(adjs[0]);
                        r.setDirection(directions[0]);
                        System.out.println("direction=" + directions[0]);
                    }
                }

                System.out.println("Ray started at " + this + " with the direction of " + r.getDirection());
                startTile = this;
                // check if there is an atom right next to it, resulting in a reflection or absorbtion
                boolean reflected = false;
                boolean absorbed = false;
                for (int j = r.getDirection() - 1; j <= r.getDirection() + 1; j++) {
                    // if one of the tiles in the direction of the beam has an atom
                    if (this.getAdjacent((j + 6) % 6) instanceof Hexagon && ((Hexagon) this.getAdjacent((j + 6) % 6)).getActive()) {
                        if (j == r.getDirection()) absorbed = true;
                        reflected = true;
                    }
                }
                if (absorbed) {
                    r.setEnd(this.getAdjacent(r.getDirection()));
                    r.setResult(Result.ABSORBED);
                    this.endTile = null;
                } else if (reflected) {
                    this.receiveRay(r);
                } else {
                    this.getAdjacent(r.getDirection()).receiveRay(r);
                }

                if (this instanceof Edge) {
                    //startTile = r.getStart();
                }
                //startTile = r.getStart();
                //this prevents function from running twice when we have more than one adjacent hexagon
                break;
            }
        }

        startRayPoint = new Point(this.getPosition().x, this.getPosition().y);
        endRayPoint = new Point(this.getPosition().x, this.getPosition().y);

        int hexMidStartY = Hexmech.hexToPixel(this.startTile.p.x, this.startTile.p.y).y;
        int hexMidStartX = Hexmech.hexToPixel(this.startTile.p.x, this.startTile.p.y).x;

        int hexMidEndY = Hexmech.hexToPixel(this.endTile.p.x, this.endTile.p.y).y;
        int hexMidEndX = Hexmech.hexToPixel(this.endTile.p.x, this.endTile.p.y).x;

        this.rayCounter = ++globalRayCounter;
        if (this.clickedTopHalf) {
            if ((this.topSquarePosition == null)&&(this.topSquarePositionStart == null)&&(this.topSquarePositionEnd == null)) {
                this.topSquarePositionStart = new Point(hexMidStartX + 55, hexMidStartY  + 40);
                if(endTile!=null) {
                    this.topSquarePositionEnd = new Point(hexMidEndX + 55, hexMidEndY + 40);
                }
                this.topSquarePosition = new Point(1,1);
            } else {
                globalRayCounter--;
            }
        } else {
            if (this.bottomSquarePosition == null&&(this.bottomSquarePositionStart == null)&&(this.bottomSquarePositionEnd == null)) {
                this.bottomSquarePositionStart = new Point(hexMidStartX + 55, hexMidStartY + 70);
                if(this.endTile!=null) {
                    this.bottomSquarePositionEnd = new Point(hexMidEndX + 55, hexMidEndY + 70);
                }
                this.bottomSquarePosition = new Point(1,1);
            } else {
                globalRayCounter--;
            }
        }
    }

    @Override
    public void receiveRay(Ray r) {
        r.setEnd(this);
        this.endTile = this;
        if (r.getStart() == r.getEnd()) {
            r.setResult(Result.REFLECTION);
        } else {
            r.setResult(Result.DETOUR);
        }
    }

    public void drawBottom(Graphics2D g2, boolean showCircles) {
        // showCircles unused here
        // TODO change this to draw it in 2 halves
        Hexmech.drawHex(this.p.x, this.p.y, g2);
        Hexmech.fillHex(this.p.x, this.p.y, this.value, g2);
    }

    void drawTop(Graphics2D g2, boolean showCircles) {
        //draw any initial lines that went through if showCircles
        if (startRayPoint != null && endRayPoint != null) {
            drawRayMarker(g2);
        }
    }



    @Override
    void drawRayMarker(Graphics2D g2) {
        System.out.println(endTile);
//        int hexMidY = Hexmech.hexToPixel(this.p.x, this.p.y).y;
//        int hexMidX = Hexmech.hexToPixel(this.p.x, this.p.y).x;
        g2.setColor(Color.blue);

        // Draw the top square if it exists
        if (this.topSquarePosition != null) {
            drawSquare(g2, this.topSquarePositionStart, this.rayCounter);
            if(this.endTile!=null) {
                drawSquare(g2, this.topSquarePositionEnd, this.rayCounter);
            }
        }

        // Draw the bottom square if it exists
        if (this.bottomSquarePosition != null) {
            drawSquare(g2, this.bottomSquarePositionStart, this.rayCounter);
            if(this.endTile!=null) {
                drawSquare(g2, this.bottomSquarePositionEnd, this.rayCounter);
            }
        }
    }

    private void drawSquare(Graphics2D g2, Point position, int label) {
       // if(this.endTile != null) {
        //    g2.setColor(Color.RED);
       // }
       // else {
            g2.setColor(Color.BLUE);
        //}
        g2.drawRect(position.x, position.y, 10, 10);
        g2.fillRect(position.x, position.y, 10, 10);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 10));
        g2.drawString(String.valueOf(label), position.x + 2, position.y + 8);
    }
}