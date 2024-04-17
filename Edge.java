import java.awt.*;

public class Edge extends Tile {
    private int rayCounter;
    private boolean clickedTopHalf;
    private static int globalRayCounter = 0;
    private Point topSquarePosition;
    private Point bottomSquarePosition;

    public Edge(int x, int y, int z) {
        super(x, y, z);
        this.rayCounter = 0;
        this.topSquarePosition = null;
        this.bottomSquarePosition = null;
    }


    public static int locationOnHex;
    public static boolean rightHalf = false;

    public static int locationOnFullHex;
    public static boolean betweenTopAndBot = false;
    public static boolean bottomOfHex = false;

    public static Tile startTile;


    private Point startRayPoint;
    private Point endRayPoint;


    //TODO if there is more than one adjacent hexagon then use locationOnHex variable (0/1)
    //have variable Tile adj1, adj2
    //e.g if locationOnHex == 0 then send ray to adj1 else adj2.


    @Override
    public void clicked() {
        System.out.println("this is an edge");
        int hexMidY = Hexmech.hexToPixel(this.p.x, this.p.y).y;
        if (locationOnHex == 1) {
            this.clickedTopHalf = true;
        } else {
            this.clickedTopHalf = false;
        }


        this.rayCounter = ++globalRayCounter;

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
            if (adj instanceof Hexagon) {
                Ray r = new Ray(this, i);
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
                } else if (reflected) {
                    this.receiveRay(r);
                } else {
                    this.getAdjacent(r.getDirection()).receiveRay(r);
                }

                if (this instanceof Edge) {
                    startTile = r.getStart();
                }
                //startTile = r.getStart();
                //this prevents function from running twice when we have more than one adjacent hexagon
                break;
            }
        }
        startRayPoint = new Point(this.getPosition().x, this.getPosition().y);
        endRayPoint = new Point(this.getPosition().x, this.getPosition().y);

        if (this.clickedTopHalf) {
            if (this.topSquarePosition == null) {
                this.topSquarePosition = new Point(this.p.x + 55, hexMidY - 20);
            } else {
                // Place a new top square at a fixed distance (e.g., 30 pixels) above the existing one
                this.topSquarePosition = new Point(this.topSquarePosition.x, this.topSquarePosition.y - 30);
            }
        } else {
            if (this.bottomSquarePosition == null) {
                this.bottomSquarePosition = new Point(this.p.x + 55, hexMidY + 70);
            } else {
                // Place a new bottom square at a fixed distance (e.g., 30 pixels) below the existing one
                this.bottomSquarePosition = new Point(this.bottomSquarePosition.x, this.bottomSquarePosition.y + 30);
            }
        }
    }

    @Override
    public void receiveRay(Ray r) {
        r.setEnd(this);
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
        //System.out.println("EDGE*************");
        int hexMidY = Hexmech.hexToPixel(p.x, p.y).y;
        int hexMidX = Hexmech.hexToPixel(p.x, p.y).x;
        g2.setColor(Color.blue);
        //g2.drawRect(hexMidX, hexMidY, 5, 5);
        //g2.fillRect(hexMidX, hexMidY, 5, 5);
        //System.out.println("Hexmid coordinates from edge: " + hexMidX + hexMidY);
        //ON LEFT
        if (!Edge.rightHalf) {
            if (locationOnHex == 0) {
                // Draw in top half
                g2.drawRect(hexMidX + 55, hexMidY - 20, 10, 10);
                g2.fillRect(hexMidX + 55, hexMidY - 20, 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 10));
                g2.drawString(String.valueOf(this.rayCounter), hexMidX + 57, hexMidY - 12);
            } else {
                // Draw in bottom half
                g2.drawRect(hexMidX + 55, hexMidY + 70, 10, 10);
                g2.fillRect(hexMidX + 55, hexMidY + 70, 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 10));
                g2.drawString(String.valueOf(this.rayCounter), hexMidX + 57, hexMidY + 78);
            }

        } else if (Edge.rightHalf) {
        if (locationOnHex == 0) {
            // Draw in top half
            g2.drawRect(hexMidX + 55, hexMidY - 20, 10, 10);
            g2.fillRect(hexMidX + 55, hexMidY - 20, 10, 10);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 10));
            g2.drawString(String.valueOf(this.rayCounter), hexMidX + 57, hexMidY - 12);
        } else {
            // Draw in bottom half
            g2.drawRect(hexMidX + 55, hexMidY + 70, 10, 10);
            g2.fillRect(hexMidX + 55, hexMidY + 70, 10, 10);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 10));
            g2.drawString(String.valueOf(this.rayCounter), hexMidX + 57, hexMidY + 78);
        }
    }
            }

        }

 //   @Override
//    void drawRayMarker(Graphics2D g2) {
//        int hexMidY = Hexmech.hexToPixel(this.p.x, this.p.y).y;
//        int hexMidX = Hexmech.hexToPixel(this.p.x, this.p.y).x;
//        g2.setColor(Color.blue);
//
//        // Draw the top square if it exists
//        if (this.topSquarePosition != null) {
//            drawSquare(g2, this.topSquarePosition, this.rayCounter);
//        }
//
//        // Draw the bottom square if it exists
//        if (this.bottomSquarePosition != null) {
//            drawSquare(g2, this.bottomSquarePosition, this.rayCounter);
//        }
//    }
//
//    private void drawSquare(Graphics2D g2, Point position, int label) {
//        g2.setColor(Color.BLUE);
//        g2.drawRect(position.x, position.y, 10, 10);
//        g2.fillRect(position.x, position.y, 10, 10);
//        g2.setColor(Color.WHITE);
//        g2.setFont(new Font("Arial", Font.BOLD, 10));
//        g2.drawString(String.valueOf(label), position.x + 2, position.y + 8);
//    }
//}