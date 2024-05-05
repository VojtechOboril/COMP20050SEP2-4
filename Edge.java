import java.awt.*;
import java.util.Objects;

/**
 * This class deals with the edge hexagons in the game ie. the hexagons on the perimeter
 * It deals with where the rays start and end along with drawing ray markers in the according places.
 */

public class Edge extends Tile {
    private int rayCounterTop;
    private int rayCounterBottom;
    boolean clickedTopHalf;
    public static int globalRayCounter = 0;
    Point topSquarePosition;
    private Point topSquarePositionStart;
    private Point topSquarePositionEnd;
    private Point bottomSquarePosition;
    private Point bottomSquarePositionStart;
    Point bottomSquarePositionEnd;
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

    /**
     * Constructs an Edge object with specified coordinates.
     * Sets a lot of variables to a default value
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
     */
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


    /**
     * This method handles what happens when an edge it clicked
     * Firstly checks which half of the hexagon we click (upper/lower half)
     * Then checks how many adjacent hexagons (not edges) the pressed hexagon has (it will be either 2 or 1(if it is an edge in a corner))
     * Then sets the direction of the ray and its end status (eg if the ray got absorbed) We use this later when creating ray markers
     * Afterwards we calculate and change the variables to get the start position and end position of where the ray marker will be placed
     */
    @Override
    public void clicked() {
        Edge.endTile = null;
        Edge.startTile = null;

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
                Edge.startTile = this;
                Edge.endTile = this;
                // check if there is an atom right next to it, resulting in a reflection or absorbtion
                boolean reflected = false;
                boolean absorbed = false;
                for (int j = r.getDirection() - 1; j <= r.getDirection() + 1; j++) {
                    // if one of the tiles in the direction of the beam has an atom
                    if (this.getAdjacent((j + 6) % 6) instanceof Hexagon && ((Hexagon) this.getAdjacent((j + 6) % 6)).getActive()) {
                        if (j == r.getDirection()) absorbed = true;
                        Edge.endTile = null;
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
                    Edge.endTile = null;
                }
                //this prevents function from running twice when we have more than one adjacent hexagon
                break;
            }
        }


        int hexMidStartY = Hexmech.hexToPixel(Edge.startTile.p.x, Edge.startTile.p.y).y;
        int hexMidStartX = Hexmech.hexToPixel(Edge.startTile.p.x, Edge.startTile.p.y).x;


        if(Edge.endTile == null){
            Edge.endTile = Edge.startTile;
        }


        int hexMidEndY = Hexmech.hexToPixel(Edge.endTile.p.x, Edge.endTile.p.y).y;
        int hexMidEndX = Hexmech.hexToPixel(Edge.endTile.p.x, Edge.endTile.p.y).x;


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
                    if (Edge.endTile != null) {
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
                    if (Edge.endTile != null) {
                        this.bottomSquarePositionEnd = new Point(hexMidEndX + 55, hexMidEndY + 70);
                    }

                    this.bottomSquarePosition = new Point(1, 1);
                }
            }
        }
    }

    /**
     * Receives a ray and updates the end tile accordingly.
     * checks where the end tile is and its status, which is used when creating ray markers
     *
     * @param r The incoming ray.
     */
    @Override
    public void receiveRay(Ray r) {
        r.setEnd(this);
        Edge.endTile = this;
        if (r.getStart() == r.getEnd()) {
            r.setResult(Result.REFLECTION);
        } else {
            r.setResult(Result.DETOUR);
        }
    }
    /**
     * Method used to draw the edge hexagons.
     *
     * @param g2           The graphics context.
     * @param showCircles  Indicates whether to show circles or not.
     */
    @Override
    public void drawBottom(Graphics2D g2, boolean showCircles) {
        // showCircles unused here, but needs to stay due to it extending tile
        Hexmech.drawHex(this.p.x, this.p.y, g2);
        Hexmech.fillHex(this.p.x, this.p.y, this.value, g2);
    }

    /**
     * Method to draw ray markers
     *
     * @param g2           The graphics context.
     * @param showCircles  Indicates whether to show circles or not.
     */
    @Override
    public void drawTop(Graphics2D g2, boolean showCircles) {
        // showCircles once again unused here, but needs to stay due to it extending tile
        if (Edge.startTile != null && Edge.endTile != null) {
            drawRayMarker(g2);
        }
    }
    /**
     * Draws ray markers on the edge tile.
     * Changes colour accordingly if ray got absorbed or reflected
     *
     * @param g2 The graphics context.
     */
    @Override
    public void drawRayMarker(Graphics2D g2) {
        g2.setColor(Color.blue);

        // Draw the top square if it exists
        if (this.topSquarePosition != null) {
            if (Objects.equals(this.topColourBox, "Blue")) {
                g2.setColor(Color.blue);
            } else  if (Objects.equals(this.topColourBox, "Red")){
                g2.setColor(Color.red);
            }
            drawSquare(g2, this.topSquarePositionStart, this.rayCounterTop);
            if(Edge.endTile!=null) {
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
            if(Edge.endTile!=null) {
                if (Objects.equals(this.botColourBox, "Blue")) {
                    g2.setColor(Color.blue);
                } else if (Objects.equals(this.botColourBox, "Red")){
                    g2.setColor(Color.red);
                }
                drawSquare(g2, this.bottomSquarePositionEnd, this.rayCounterBottom);
            }
        }
    }
    /**
     * Draws a ray marker with the given label in a given position
     *
     * @param g2       The graphics context.
     * @param position The position of the square.
     * @param label    The label for the square.
     */
    public void drawSquare(Graphics2D g2, Point position, int label) {
        g2.fillRect(position.x, position.y, 10, 10);
        g2.setColor(Color.WHITE);
        g2.drawRect(position.x, position.y, 10, 10);
        g2.setFont(new Font("Arial", Font.BOLD, 10));
        g2.drawString(String.valueOf(label), position.x + 2, position.y + 8);
    }
}