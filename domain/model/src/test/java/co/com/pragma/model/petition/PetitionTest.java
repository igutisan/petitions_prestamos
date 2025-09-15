
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
        assertEquals(0, petition.getTerm());
        assertNull(petition.getLoanTypeId());
        assertNull(petition.getLoanStatus());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID loanTypeId = UUID.randomUUID();
        BigDecimal loanAmount = new BigDecimal("10000.00");

        Petition petition = new Petition(id, userId, loanAmount, 12, loanTypeId, LoanStatus.PENDING_REVIEW);

        assertEquals(id, petition.getId());
        assertEquals(userId, petition.getUserId());
        assertEquals(loanAmount, petition.getLoanAmount());
        assertEquals(12, petition.getTerm());
        assertEquals(loanTypeId, petition.getLoanTypeId());
        assertEquals(LoanStatus.PENDING_REVIEW, petition.getLoanStatus());
    }

    @Test
    void testBuilder() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID loanTypeId = UUID.randomUUID();
        BigDecimal loanAmount = new BigDecimal("20000.00");

        Petition petition = Petition.builder()
                .id(id)
                .userId(userId)
                .loanAmount(loanAmount)
                .term(24)
                .loanTypeId(loanTypeId)
                .loanStatus(LoanStatus.APPROVED)
                .build();

        assertEquals(id, petition.getId());
        assertEquals(userId, petition.getUserId());
        assertEquals(loanAmount, petition.getLoanAmount());
        assertEquals(24, petition.getTerm());
        assertEquals(loanTypeId, petition.getLoanTypeId());
        assertEquals(LoanStatus.APPROVED, petition.getLoanStatus());
    }

    @Test
    void testSetters() {
        Petition petition = new Petition();
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID loanTypeId = UUID.randomUUID();
        BigDecimal loanAmount = new BigDecimal("30000.00");

        petition.setId(id);
        petition.setUserId(userId);
        petition.setLoanAmount(loanAmount);
        petition.setTerm(36);
        petition.setLoanTypeId(loanTypeId);
        petition.setLoanStatus(LoanStatus.REJECTED);

        assertEquals(id, petition.getId());
        assertEquals(userId, petition.getUserId());
        assertEquals(loanAmount, petition.getLoanAmount());
        assertEquals(36, petition.getTerm());
        assertEquals(loanTypeId, petition.getLoanTypeId());
        assertEquals(LoanStatus.REJECTED, petition.getLoanStatus());
    }

    @Test
    void testToBuilder() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID loanTypeId = UUID.randomUUID();
        BigDecimal loanAmount = new BigDecimal("20000.00");

        Petition petition1 = Petition.builder()
                .id(id)
                .userId(userId)
                .loanAmount(loanAmount)
                .term(24)
                .loanTypeId(loanTypeId)
                .loanStatus(LoanStatus.APPROVED)
                .build();

        Petition petition2 = petition1.toBuilder().loanStatus(LoanStatus.PENDING_REVIEW).build();

        assertEquals(id, petition2.getId());
        assertEquals(userId, petition2.getUserId());
        assertEquals(LoanStatus.PENDING_REVIEW, petition2.getLoanStatus());
    }
}
