import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HexagonTests {

    @Test
    void testHexagonClicked() {
        Hexagon hexagon = new Hexagon(0, 0, 0);
        hexagon.clicked();
        assertEquals(120, hexagon.getValue());

        hexagon.clicked();
        assertEquals(0, hexagon.getValue());
    }

    @Test
    void testHexagonCorrectlySelected() {
        Hexagon hexagon = new Hexagon(0, 0, 0);
        assertEquals(1, hexagon.correctlySelected());

        hexagon.setActive(true);
        assertEquals(-1, hexagon.correctlySelected());
    }

    @Test
    void testHexagonSetAndGetActive() {
        Hexagon hexagon = new Hexagon(0, 0, 0);
        hexagon.setActive(true);
        assertTrue(hexagon.getActive());

        hexagon.setActive(false);
        assertFalse(hexagon.getActive());
    }

    
}