import org.junit.jupiter.api.Test;
import java.awt.Graphics2D;
import java.awt.Point;
import static org.junit.jupiter.api.Assertions.*;

public class TileTests {

    @Test
    void testTileGetPosition() {
        Tile tile = new TestTile(1, 2, -3);
        Point position = tile.getPosition();
        assertEquals(new Point(7, 7), position);
    }

    @Test
    void testTileSetAndGetAdjacent() {
        Tile tile1 = new TestTile(0, 0, 0);
        Tile tile2 = new TestTile(1, 0, -1);
        tile1.setAdjacent(tile2, 0);
        assertEquals(tile2, tile1.getAdjacent(0));
    }

    @Test
    void testTileGetValue() {
        Tile tile = new TestTile(0, 0, 0);
        tile.setValue(5);
        assertEquals(5, tile.getValue());
    }

    // Inner class for testing Tile abstract class
    private static class TestTile extends Tile {
        public TestTile(int x, int y, int z) {
            super(x, y, z);
        }

        // Implementing abstract methods
        @Override
        void receiveRay(Ray r) {
        }

        @Override
        void clicked() {
        }

        @Override
        void drawBottom(Graphics2D g2, boolean showCircles) {
        }

        @Override
        void drawTop(Graphics2D g2, boolean showCircles) {
        }

        @Override
        void drawRayMarker(Graphics2D g2) {
        }
    }
}
