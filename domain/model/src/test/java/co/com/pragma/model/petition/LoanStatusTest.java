
package co.com.pragma.model.petition;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoanStatusTest {

    @Test
    void testEnumValues() {
        assertEquals("Pendiente de revisión", LoanStatus.PENDING_REVIEW.getDescription());
        assertEquals("Aprobado", LoanStatus.APPROVED.getDescription());
        assertEquals("Rechazado", LoanStatus.REJECTED.getDescription());
        assertEquals("Activo", LoanStatus.ACTIVE.getDescription());
        assertEquals("Pagado", LoanStatus.PAID.getDescription());
        assertEquals("En mora", LoanStatus.DEFAULTED.getDescription());
        assertEquals("Cancelado", LoanStatus.CANCELLED.getDescription());
    }

    @Test
    void testEnumNames() {
        assertNotNull(LoanStatus.valueOf("PENDING_REVIEW"));
        assertNotNull(LoanStatus.valueOf("APPROVED"));
        assertNotNull(LoanStatus.valueOf("REJECTED"));
        assertNotNull(LoanStatus.valueOf("ACTIVE"));
        assertNotNull(LoanStatus.valueOf("PAID"));
        assertNotNull(LoanStatus.valueOf("DEFAULTED"));
        assertNotNull(LoanStatus.valueOf("CANCELLED"));
    }
}
