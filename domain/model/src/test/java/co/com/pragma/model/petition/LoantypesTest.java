
package co.com.pragma.model.petition;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoantypesTest {

    @Test
    void testEnumValues() {
        assertNotNull(Loantypes.valueOf("PERSONAL"));
        assertNotNull(Loantypes.valueOf("HIPOTECARIO"));
        assertNotNull(Loantypes.valueOf("EMPRESARIAL"));
        assertNotNull(Loantypes.valueOf("VEHICULAR"));
    }
}
