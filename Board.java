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
    boolean showCircles = true;
    Tile[][] hBoard;
    //Ray[] sentRays;

    public Board() {
        JButton newGameButton = new JButton("Board implementation");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initGame();
                createAndShowGUI();
            }
        });
        this.add(newGameButton);

        JButton revealAtomsButton = new JButton("reveal atoms");
        revealAtomsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAtoms();
                //showRayPaths
            }
        });
        this.add(revealAtomsButton);
    }

    void initGame() {

        Hexmech.setXYasVertex(false); // RECOMMENDED: leave this as FALSE.

        Hexmech.setHeight(HEXSIZE); // Either setHeight or setSize must be run to initialize the hex
        Hexmech.setBorders(BORDERS);
        createBoard();
        placeRandomAtoms();
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
                    h.convertPosition(BSIZE);
                    Point p = h.getPosition();
                    if (hBoard[p.x][p.y] != null) {
                        Tile g = hBoard[p.x][p.y];
                        System.err.println(String.format("%d %d %d and %d %d %d have the same point %d %d", x, y, z,
                                g.x, g.y, g.z, p.x, p.y));
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

    private void createAndShowGUI() {
        DrawingPanel panel = new DrawingPanel();
        JFrame frame = new JFrame("Hex Testing 4");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
                tile.clicked();
                placedAtoms += 1;
                System.out.println(placement);
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
            System.out.println(atomArray[i]);
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

            for (int i = 0; i < BSIZE; i++) {
                for (int j = 0; j < BSIZE; j++) {
                    if (hBoard[i][j] != null) {
                        // draw grid
                        Hexmech.drawHex(i, j, g2);
                        // fill in hexes
                        Hexmech.fillHex(i, j, hBoard[i][j].getValue(), g2);
                    }
                }
            }

            if (showCircles) {
                drawCircles(g2);
            }
            repaint();
        }

        private void drawCircles(Graphics2D g2) {
            g2.setColor(Color.RED);
            int radius = 60; // adjust the radius as needed
            for (int i = 0; i < BSIZE; i++) {
                for (int j = 0; j < BSIZE; j++) {
                    if (hBoard[i][j] instanceof Hexagon && ((Hexagon) hBoard[i][j]).getActive()) {
                        Point center = Hexmech.hexToPixel(i, j);
                        Stroke oldStroke = g2.getStroke();
                        // Set a dashed stroke
                        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,
                                new float[] { 5 }, 0));
                        g2.drawOval(center.x, center.y, 2 * radius, 2 * radius);
                        // Reset the stroke
                        g2.setStroke(oldStroke);
                    }

                }
            }
        }

        class MyMouseListener extends MouseAdapter { // inner class inside DrawingPanel
            public void mouseClicked(MouseEvent e) {
                Point p = new Point(Hexmech.pxtoHex(e.getX(), e.getY()));
                if (p.x < 0 || p.y < 0 || p.x >= BSIZE || p.y >= BSIZE)
                    return;
                // What do you want to do when a hexagon is clicked?
                if (hBoard[p.x][p.y] != null) {
                    hBoard[p.x][p.y].clicked();
                }
                repaint();

                //show coordinates
                int clickX = e.getX(); // X-coordinate of the click relative to this panel
                int clickY = e.getY(); // Y-coordinate of the click relative to this panel
                System.out.println("Xcord=" + clickX + "\nYcord="+ clickY);
                int hexMidY = Hexmech.hexToPixel(p.x, p.y).y + 60;
                System.out.println("the mid point y is " + hexMidY);

                // Compare clickY with the midpoint Y-coordinate of the hexagon
                if (clickY < hexMidY) {
                    // Click is above the midpoint
                    System.out.println("Clicked above the midpoint of the hexagon.");
                    Tile.locationOnHex=0;
                } else if (clickY > hexMidY) {
                    // Click is below the midpoint
                    System.out.println("Clicked below the midpoint of the hexagon.");
                    Tile.locationOnHex=1;
                } else {
                    // Click is exactly at the midpoint
                    System.out.println("Clicked exactly at the midpoint of the hexagon.");
                    Tile.locationOnHex=1;
                }
            }

        } // end of MyMouseListener class
    } // end of DrawingPanel class


}
