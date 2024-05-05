import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Board extends JPanel {
    final static int BSIZE = 11; // board size. controls the number hexagons. Only works with odd numbers
    final static Color COLOURBACK = Color.BLACK;
    final static Color COLOURCELL = Color.ORANGE;
    final static Color COLOURGRID = Color.WHITE;
    final static Color COLOURONE = new Color(255, 255, 255, 200);
    final static Color COLOURONETXT = Color.BLUE;
    final static Color COLOURTWO = new Color(0, 0, 0, 200);
    final static Color COLOURTWOTXT = new Color(255, 100, 255);
    final static int HEXSIZE = 60; // hex size in pixels
    final static int BORDERS = 15;
    final static int SCRSIZE = HEXSIZE * (BSIZE + 1) + BORDERS * 3; // screen size (vertical dimension).
    int[] atomArray = new int[6];
    // do we show the circles of influence? currently on true since atoms show by
    // default too
    public static boolean showCircles = false;
    Tile[][] hBoard;
    private JFrame frame;
    private int wrongGuesses;

    public Board() {
        // We start with 6 wrong guesses as we have not guessed any atoms yet, therefore we have not guessed 6 of them
        wrongGuesses = 6;
        Board.showCircles = false;
    }

    void initGame() {

        Hexmech.setXYasVertex(false); // RECOMMENDED: leave this as FALSE.

        Hexmech.setHeight(HEXSIZE); // Either setHeight or setSize must be run to initialize the hex
        Hexmech.setBorders(BORDERS);
        createBoard();
        placeRandomAtoms();
        
        wrongGuesses = 6;
        Board.showCircles = false;
        Edge.globalRayCounter = 0;
    }

    private void createBoard() {
        hBoard = new Tile[BSIZE][BSIZE];
        // temporary way to save board to link hexagons together
        Tile[][][] hexagonalBoard = new Tile[BSIZE][BSIZE][BSIZE];
        int depth = 0;
        // Create a center tile
        hBoard[BSIZE / 2][BSIZE / 2] = new Hexagon(0, 0, 0);
        hexagonalBoard[BSIZE / 2][BSIZE / 2][BSIZE / 2] = hBoard[BSIZE / 2][BSIZE / 2];
        // Cube coordinates for each direction
        // https://www.redblobgames.com/grids/hexagons/
        final int[][] sides = { { 1, 0, -1 }, { 0, 1, -1 }, { -1, 1, 0 }, { -1, 0, 1 }, { 0, -1, 1 }, { 1, -1, 0 } };
        // Layer by layer
        for (depth = 1; depth <= BSIZE / 2; depth++) {
            // Side by side(n -> ne -> ... -> nw)
            for (int side = 1; side <= 6; side++) {
                // For each tile on that side
                for (int tile = 1; tile <= depth; tile++) {
                    // Get its cube coordinates
                    int x = sides[side - 1][0] * depth + sides[(side + 1) % 6][0] * (tile - 1);
                    int y = sides[side - 1][1] * depth + sides[(side + 1) % 6][1] * (tile - 1);
                    int z = sides[side - 1][2] * depth + sides[(side + 1) % 6][2] * (tile - 1);
                    Tile h = depth == BSIZE / 2 ? new Edge(x, y, z) : new Hexagon(x, y, z);
                    // Convert those cube coordinates to offset coordinates
                    Point p = h.getPosition();
                    if (hBoard[p.x][p.y] != null) {
                        Tile g = hBoard[p.x][p.y];
                        System.err.println(String.format("%d %d %d and %d %d %d have the same point %d %d", x, y, z, g.x, g.y, g.z, p.x, p.y));
                    }
                    // Add it to the board
                    hBoard[p.x][p.y] = h;
                    hexagonalBoard[x + BSIZE / 2][y + BSIZE / 2][z + BSIZE / 2] = h;
                    // for each side
                    for (int i = 0; i < 6; i++) {
                        int newx = x + BSIZE / 2 + sides[i][0];
                        int newy = y + BSIZE / 2 + sides[i][1];
                        int newz = z + BSIZE / 2 + sides[i][2];
                        // out of bounds
                        if (newx < 0 || newy < 0 || newz < 0 || newx >= BSIZE || newy >= BSIZE || newz >= BSIZE)
                            continue;
                        Tile newh = hexagonalBoard[newx][newy][newz];
                        if (newh != null) {
                            h.setAdjacent(newh, (i + 3) % 6);
                            newh.setAdjacent(h, i);
                        }
                    }
                }
            }
        }
    }

    public void createAndShowGUI() {
        DrawingPanel panel = new DrawingPanel();
        frame = new JFrame("Hex Testing 4");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
        Container content = frame.getContentPane();
        content.add(panel);
        frame.setSize((int) (SCRSIZE / 1.23), SCRSIZE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    void placeRandomAtoms() {
        Random random = new Random();
        int placedAtoms = 0;
        while (placedAtoms < 6) {
            // Pick a random hexagon(even from the ones not drawn)
            int placement = random.nextInt(BSIZE * BSIZE);
            int x = placement % BSIZE;
            int y = placement / BSIZE;
            Tile tile = hBoard[x][y];
            // is the hexagon drawn, and is it still inactive?
            if (tile instanceof Hexagon && !((Hexagon) tile).getActive()) {
                // activate it
                ((Hexagon) tile).setActive(true);
                atomArray[placedAtoms] = placement;
                // Temporary reveal generated atoms
                //tile.clicked();
                placedAtoms += 1;
            }
        }
    }

    void checkAtoms() {
        showCircles = !showCircles;
        for (int i = 0; i < atomArray.length; i++) {
            int x = atomArray[i] % BSIZE;
            int y = atomArray[i] / BSIZE;
            Tile tile = hBoard[x][y];
            tile.clicked();
        }
    }

    class DrawingPanel extends JPanel {
        public DrawingPanel() {
            setBackground(COLOURBACK);

            MyMouseListener ml = new MyMouseListener();
            addMouseListener(ml);
        }

        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            super.paintComponent(g2);
            for (Tile[] i : hBoard) {
                for (Tile tile : i) {
                    if(tile != null) tile.drawBottom(g2, showCircles);
                }
            }

            for (Tile[] i : hBoard) {
                for (Tile tile : i) {
                    if(tile != null) tile.drawTop(g2, showCircles);
                }
            }

//            for (Tile[] i : hBoard) {
//                for (Tile tile : i) {
//                    if(Edge.startTile != null && tile instanceof Edge) {
//                        Edge.startTile.drawRayMarker(g2);
//                    }
//                }
//            }

            repaint();
        }

        class MyMouseListener extends MouseAdapter { // inner class inside DrawingPanel
            public void mouseClicked(MouseEvent e) {
                Point p = new Point(Hexmech.pxtoHex(e.getX(), e.getY()));
                if (p.x < 0 || p.y < 0 || p.x >= BSIZE || p.y >= BSIZE)
                    return;

                // Update locationOnHex variable before invoking clicked() method
                int clickX = e.getX(); // X-coordinate of the click relative to this panel
                int clickY = e.getY(); // Y-coordinate of the click relative to this panel
                //USE THIS LATER **************************************************************
                int hexMidY = Hexmech.hexToPixel(p.x, p.y).y + 60;


                // Compare clickY with the midpoint Y-coordinate of the hexagon
                if (clickY < hexMidY) {
                    // Click is above the midpoint
                    Edge.locationOnHex = 0;
                } else {
                    // Click is below the midpoint
                    Edge.locationOnHex = 1;
                }
                //let boolean right half be true if X coord is > 322
                Edge.rightHalf= clickX > 322;
//                System.out.println("Y direction:" + clickY);
//                System.out.println("X direction"+ clickX);
               // Edge.locationOnFullHex = y > 255  x< 80 y<494
                //left side
                Edge.betweenTopAndBot = (clickX < 80 || clickX>530&& (clickY > 255 && clickY <494));
                //right side
                Edge.bottomOfHex = (clickY > 525);
                Edge.bottomRight = (clickY>526);
                //xcord > 530
                // Call clicked() method after updating locationOnHex
                Tile currenTile = hBoard[p.x][p.y];
                if (currenTile != null) {
                    currenTile.clicked();
                    if(currenTile instanceof Hexagon)
                    wrongGuesses -= ((Hexagon) currenTile).correctlySelected();
                }

                repaint();
            }
        } // end of MyMouseListener class
    } // end of DrawingPanel class


    public void exit() {
        this.frame.dispose();
    }

    public int calculateScore() {
        return Edge.globalRayCounter + this.wrongGuesses * 5;
    }
}
