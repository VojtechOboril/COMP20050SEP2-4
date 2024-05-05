import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EdgeTests {

    @Test
    void testEdgeCreation() {
        Edge edge = new Edge(0, 0, 0);
        assertNotNull(edge);
    }

    @Test
    void testEdgeReceiveRay() {
        Edge edge = new Edge(0, 0, 0);
        Hexagon hexagon = new Hexagon(1, 0, -1);
        edge.setAdjacent(hexagon, 0);
        Ray ray = new Ray(edge, 0);
        edge.receiveRay(ray);
        assertEquals(edge, ray.getEnd());
    }
}
