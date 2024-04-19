import java.awt.*;
import java.util.Objects;

public class Edge extends Tile {
    private int rayCounterTop;
    private int rayCounterBottom;
    private boolean clickedTopHalf;
    public static int globalRayCounter = 0;
    private Point topSquarePosition;
    private Point topSquarePositionStart;
    private Point topSquarePositionEnd;
    private Point bottomSquarePosition;
    private Point bottomSquarePositionStart;
    private Point bottomSquarePositionEnd;
    public static int locationOnHex;
    public static boolean rightHalf = false;
    public static boolean bottomRight = false;
    public boolean rayAbsorbed = false;

    public static boolean betweenTopAndBot = false;
    public static boolean bottomOfHex = false;
    public String topColourBox;
    public String botColourBox;

    public static Edge startTile;
    public static Edge endTile = null;

    public Edge(int x, int y, int z) {
        super(x, y, z);
        this.rayCounterTop = 0;
        this.rayCounterBottom = 0;
        this.topSquarePosition = null;
        this.bottomSquarePosition = null;
        this.topSquarePositionStart = null;
        this.bottomSquarePositionStart = null;
        this.topSquarePositionEnd = null;
        this.bottomSquarePositionEnd = null;
        this.topColourBox = null;
        this.botColourBox = null;
    }



    @Override
    public void clicked() {
        this.endTile = null;
        this.startTile = null;

        if (locationOnHex == 0) {
            this.clickedTopHalf = true;
        } else {
            this.clickedTopHalf = false;
        }


        //set these to be the 2 adj locations if there is more than 2 locations.
        int[] directions = new int[2];
        //check how many adjacents there are, then save them into the array 'adjs'
        int counter = 0;
        for (int i = 0; i < 6; i++) {
            Tile adj = this.getAdjacent(i);
            if (adj instanceof Hexagon) {
                directions[counter] = i;
                counter++;
            }
        }

        for (int i = 0; i < 6; i++) {
            Tile adj = this.getAdjacent(i);
            Ray r = new Ray(this, i);
            if (adj instanceof Hexagon) {

                //if has 2 adjacent hexagons
                if (counter == 2 && (!rightHalf || bottomRight)){
                    if (locationOnHex == 0) {
                        r.setDirection(directions[0]);
                    } else if (locationOnHex == 1) {
                        r.setDirection(directions[1]);
                    }
                } else if (counter == 2 && rightHalf) {
                    if (locationOnHex == 0) {
                        r.setDirection(directions[1]);
                    } else if (locationOnHex == 1) {
                        r.setDirection(directions[0]);
                    }
                }
                if(!(!clickedTopHalf&&counter==1)) {
                    //System.out.println("Ray started at " + this + " with the direction of " + r.getDirection());
                }
                this.startTile = this;
                this.endTile = this;
                // check if there is an atom right next to it, resulting in a reflection or absorbtion
                boolean reflected = false;
                boolean absorbed = false;
                for (int j = r.getDirection() - 1; j <= r.getDirection() + 1; j++) {
                    // if one of the tiles in the direction of the beam has an atom
                    if (this.getAdjacent((j + 6) % 6) instanceof Hexagon && ((Hexagon) this.getAdjacent((j + 6) % 6)).getActive()) {
                        if (j == r.getDirection()) absorbed = true;
                        this.endTile = null;
                            reflected = true;
                    }
                }
                if(!(!clickedTopHalf&&counter==1)) {
                    if (absorbed) {
                        r.setEnd(this.getAdjacent(r.getDirection()));
                        r.setResult(Result.ABSORBED);
                    } else if (reflected) {
                        this.receiveRay(r);
                    } else {
                        this.getAdjacent(r.getDirection()).receiveRay(r);
                    }
                }

                if(r.getResult()==Result.ABSORBED) {
                    rayAbsorbed = true;
                    this.endTile = null;
                }
                //startTile = r.getStart();
                //this prevents function from running twice when we have more than one adjacent hexagon
                break;
            }
        }


        int hexMidStartY = Hexmech.hexToPixel(this.startTile.p.x, this.startTile.p.y).y;
        int hexMidStartX = Hexmech.hexToPixel(this.startTile.p.x, this.startTile.p.y).x;


        if(this.endTile == null){
            this.endTile = this.startTile;
        }


        int hexMidEndY = Hexmech.hexToPixel(this.endTile.p.x, this.endTile.p.y).y;
        int hexMidEndX = Hexmech.hexToPixel(this.endTile.p.x, this.endTile.p.y).x;


        if (this.clickedTopHalf) {
            if(rayCounterTop == 0) {
                this.rayCounterTop = ++globalRayCounter;
                if ((this.topSquarePosition == null) && (this.topSquarePositionStart == null) && (this.topSquarePositionEnd == null)) {
                    if(startTile == endTile && rayAbsorbed){
                        this.topColourBox = "Red";
                    }else{
                        this.topColourBox = "Blue";
                    }
                    this.topSquarePositionStart = new Point(hexMidStartX + 55, hexMidStartY + 40);
                    if (this.endTile != null) {
                        //IF STATEMENT TO CHECK IF END IS SUPPOSED TO BE TOP OR BOTTOM
                        this.topSquarePositionEnd = new Point(hexMidEndX + 55, hexMidEndY + 40);
                    }
                    this.topSquarePosition = new Point(1, 1);
                }
            }
        }else {
            if (rayCounterBottom == 0 && counter == 2) {
                this.rayCounterBottom = ++globalRayCounter;
                if (this.bottomSquarePosition == null && (this.bottomSquarePositionStart == null) && (this.bottomSquarePositionEnd == null)) {
                    if(startTile == endTile && rayAbsorbed){
                        this.botColourBox = "Red";
                    }
                    else{
                        this.botColourBox = "Blue";
                    }
                    this.bottomSquarePositionStart = new Point(hexMidStartX + 55, hexMidStartY + 70);
                    //IF STATEMENT TO CHECK IF END IS SUPPOSED TO BE TOP OR BOTTOM
                    if (this.endTile != null) {
                        this.bottomSquarePositionEnd = new Point(hexMidEndX + 55, hexMidEndY + 70);
                    }

                    this.bottomSquarePosition = new Point(1, 1);
                }
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

    @Override
    public void drawBottom(Graphics2D g2, boolean showCircles) {
        // showCircles unused here, but needs to stay due to it extending tile
        // TODO change this to draw it in 2 halves
        Hexmech.drawHex(this.p.x, this.p.y, g2);
        Hexmech.fillHex(this.p.x, this.p.y, this.value, g2);
    }
    @Override
    void drawTop(Graphics2D g2, boolean showCircles) {
        if (this.startTile != null && this.endTile != null) {
            drawRayMarker(g2);
        }
    }



    @Override
    void drawRayMarker(Graphics2D g2) {
        g2.setColor(Color.blue);

        // Draw the top square if it exists
        if (this.topSquarePosition != null) {
            if (Objects.equals(this.topColourBox, "Blue")) {
                g2.setColor(Color.blue);
            } else  if (Objects.equals(this.topColourBox, "Red")){
                g2.setColor(Color.red);
            }
            drawSquare(g2, this.topSquarePositionStart, this.rayCounterTop);
            if(this.endTile!=null) {
                if (Objects.equals(this.topColourBox, "Blue")) {
                    g2.setColor(Color.blue);
                } else if (Objects.equals(this.topColourBox, "Red")) {
                    g2.setColor(Color.red);
                }
                drawSquare(g2, this.topSquarePositionEnd, this.rayCounterTop);
            }
        }

        // Draw the bottom square if it exists
        if (this.bottomSquarePosition != null) {
            if (Objects.equals(this.botColourBox, "Blue")) {
                g2.setColor(Color.blue);
            } else if (Objects.equals(this.botColourBox, "Red")) {
                g2.setColor(Color.red);
            }
            drawSquare(g2, this.bottomSquarePositionStart, this.rayCounterBottom);
            if(this.endTile!=null) {
                if (Objects.equals(this.botColourBox, "Blue")) {
                    g2.setColor(Color.blue);
                } else if (Objects.equals(this.botColourBox, "Red")){
                    g2.setColor(Color.red);
                }
                drawSquare(g2, this.bottomSquarePositionEnd, this.rayCounterBottom);
            }
        }
    }

    private void drawSquare(Graphics2D g2, Point position, int label) {
        g2.fillRect(position.x, position.y, 10, 10);
        g2.setColor(Color.WHITE);
        g2.drawRect(position.x, position.y, 10, 10);
        g2.setFont(new Font("Arial", Font.BOLD, 10));
        g2.drawString(String.valueOf(label), position.x + 2, position.y + 8);
    }
}