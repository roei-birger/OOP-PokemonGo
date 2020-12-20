import api.location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class locationTest {

    location l = new location(1,1,1);

    @Test
    void x() {
        assertEquals(1, l.x(), "getX didn't return the correct X");
    }

    @Test
    void y() {
        assertEquals(1, l.y(), "getY didn't return the correct Y");
    }

    @Test
    void z() {
        assertEquals(1, l.z(), "getZ didn't return the correct Z");
    }

    @Test
    void distance() {
        location t = new location(2,2,2);

        assertEquals(1.7320508075688772, l.distance(t), "distance didn't return the correct distance");
    }
}