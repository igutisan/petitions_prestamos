
package co.com.pragma.model.petition;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.UUID;

class PetitionTest {

    @Test
    void testNoArgsConstructor() {
        Petition petition = new Petition();
        assertNull(petition.getId());
        assertNull(petition.getUserId());
        assertNull(petition.getLoanAmount());
        assertNull(petition.getTerm());
        assertNull(petition.getLoanType());
        assertNull(petition.getLoanStatus());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        BigDecimal loanAmount = new BigDecimal("10000.00");

        Petition petition = new Petition(id, "user123", loanAmount, "12 Months", Loantypes.PERSONAL, LoanStatus.PENDING_REVIEW);

        assertEquals(id, petition.getId());
        assertEquals("user123", petition.getUserId());
        assertEquals(loanAmount, petition.getLoanAmount());
        assertEquals("12 Months", petition.getTerm());
        assertEquals(Loantypes.PERSONAL, petition.getLoanType());
        assertEquals(LoanStatus.PENDING_REVIEW, petition.getLoanStatus());
    }

    @Test
    void testBuilder() {
        UUID id = UUID.randomUUID();
        BigDecimal loanAmount = new BigDecimal("20000.00");

        Petition petition = Petition.builder()
                .id(id)
                .userId("user456")
                .loanAmount(loanAmount)
                .term("24 Months")
                .loanType(Loantypes.HIPOTECARIO)
                .loanStatus(LoanStatus.APPROVED)
                .build();

        assertEquals(id, petition.getId());
        assertEquals("user456", petition.getUserId());
        assertEquals(loanAmount, petition.getLoanAmount());
        assertEquals("24 Months", petition.getTerm());
        assertEquals(Loantypes.HIPOTECARIO, petition.getLoanType());
        assertEquals(LoanStatus.APPROVED, petition.getLoanStatus());
    }

    @Test
    void testSetters() {
        Petition petition = new Petition();
        UUID id = UUID.randomUUID();
        BigDecimal loanAmount = new BigDecimal("30000.00");

        petition.setId(id);
        petition.setUserId("user789");
        petition.setLoanAmount(loanAmount);
        petition.setTerm("36 Months");
        petition.setLoanType(Loantypes.VEHICULAR);
        petition.setLoanStatus(LoanStatus.REJECTED);

        assertEquals(id, petition.getId());
        assertEquals("user789", petition.getUserId());
        assertEquals(loanAmount, petition.getLoanAmount());
        assertEquals("36 Months", petition.getTerm());
        assertEquals(Loantypes.VEHICULAR, petition.getLoanType());
        assertEquals(LoanStatus.REJECTED, petition.getLoanStatus());
    }

    @Test
    void testToBuilder() {
        UUID id = UUID.randomUUID();
        BigDecimal loanAmount = new BigDecimal("20000.00");

        Petition petition1 = Petition.builder()
                .id(id)
                .userId("user456")
                .loanAmount(loanAmount)
                .term("24 Months")
                .loanType(Loantypes.HIPOTECARIO)
                .loanStatus(LoanStatus.APPROVED)
                .build();

        Petition petition2 = petition1.toBuilder().loanStatus(LoanStatus.PENDING_REVIEW).build();

        assertEquals(id, petition2.getId());
        assertEquals("user456", petition2.getUserId());
        assertEquals(LoanStatus.PENDING_REVIEW, petition2.getLoanStatus());
    }
}
