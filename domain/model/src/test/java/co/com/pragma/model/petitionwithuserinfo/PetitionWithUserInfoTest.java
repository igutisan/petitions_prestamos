package co.com.pragma.model.petitionwithuserinfo;

import co.com.pragma.model.petition.LoanStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.UUID;

class PetitionWithUserInfoTest {

    @Test
    void testNoArgsConstructor() {
        PetitionWithUserInfo p = new PetitionWithUserInfo();
        assertNull(p.getId());
        assertNull(p.getLoanAmount());
        assertEquals(0, p.getTerm());
        assertNull(p.getLoanTypeName());
        assertNull(p.getLoanStatus());
        assertNull(p.getUserEmail());
        assertNull(p.getUserName());
        assertNull(p.getUserSalary());
    }

    @Test
    void testAllArgsConstructor() {
        String id = UUID.randomUUID().toString();
        BigDecimal loanAmount = new BigDecimal("15000.00");
        BigDecimal userSalary = new BigDecimal("75000.00");

        PetitionWithUserInfo p = new PetitionWithUserInfo(id, loanAmount, 36, "PERSONAL", LoanStatus.APPROVED, "test@user.com", "Test User", 10.0, null, userSalary);

        assertEquals(id, p.getId());
        assertEquals(loanAmount, p.getLoanAmount());
        assertEquals(36, p.getTerm());
        assertEquals("PERSONAL", p.getLoanTypeName());
        assertEquals(LoanStatus.APPROVED, p.getLoanStatus());
        assertEquals("test@user.com", p.getUserEmail());
        assertEquals("Test User", p.getUserName());
        assertEquals(userSalary, p.getUserSalary());
    }

    @Test
    void testBuilder() {
        String id = UUID.randomUUID().toString();
        BigDecimal loanAmount = new BigDecimal("25000.00");
        BigDecimal userSalary = new BigDecimal("85000.00");

        PetitionWithUserInfo p = PetitionWithUserInfo.builder()
                .id(id)
                .loanAmount(loanAmount)
                .term(48)
                .loanTypeName("VEHICULAR")
                .loanStatus(LoanStatus.PENDING_REVIEW)
                .userEmail("builder@user.com")
                .userName("Builder User")
                .userSalary(userSalary)
                .build();

        assertEquals(id, p.getId());
        assertEquals(loanAmount, p.getLoanAmount());
        assertEquals(48, p.getTerm());
        assertEquals("VEHICULAR", p.getLoanTypeName());
        assertEquals(LoanStatus.PENDING_REVIEW, p.getLoanStatus());
        assertEquals("builder@user.com", p.getUserEmail());
        assertEquals("Builder User", p.getUserName());
        assertEquals(userSalary, p.getUserSalary());
    }

    @Test
    void testSetters() {
        PetitionWithUserInfo p = new PetitionWithUserInfo();
        String id = UUID.randomUUID().toString();
        BigDecimal loanAmount = new BigDecimal("5000.00");
        BigDecimal userSalary = new BigDecimal("45000.00");

        p.setId(id);
        p.setLoanAmount(loanAmount);
        p.setTerm(12);
        p.setLoanTypeName("HIPOTECARIO");
        p.setLoanStatus(LoanStatus.REJECTED);
        p.setUserEmail("setter@user.com");
        p.setUserName("Setter User");
        p.setUserSalary(userSalary);

        assertEquals(id, p.getId());
        assertEquals(loanAmount, p.getLoanAmount());
        assertEquals(12, p.getTerm());
        assertEquals("HIPOTECARIO", p.getLoanTypeName());
        assertEquals(LoanStatus.REJECTED, p.getLoanStatus());
        assertEquals("setter@user.com", p.getUserEmail());
        assertEquals("Setter User", p.getUserName());
        assertEquals(userSalary, p.getUserSalary());
    }

    @Test
    void testToBuilder() {
        String id = UUID.randomUUID().toString();
        BigDecimal loanAmount = new BigDecimal("25000.00");
        BigDecimal userSalary = new BigDecimal("85000.00");

        PetitionWithUserInfo p1 = PetitionWithUserInfo.builder()
                .id(id)
                .loanAmount(loanAmount)
                .term(48)
                .loanTypeName("VEHICULAR")
                .loanStatus(LoanStatus.PENDING_REVIEW)
                .userEmail("builder@user.com")
                .userName("Builder User")
                .userSalary(userSalary)
                .build();

        PetitionWithUserInfo p2 = p1.toBuilder().userName("Modified User").build();

        assertEquals(id, p2.getId());
        assertEquals(loanAmount, p2.getLoanAmount());
        assertEquals("Modified User", p2.getUserName());
        assertEquals(userSalary, p2.getUserSalary());
    }
}