
package co.com.pragma.model.petitionwithuserinfo;

import co.com.pragma.model.petition.LoanStatus;
import co.com.pragma.model.petition.Loantypes;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

class PetitionWithUserInfoTest {

    @Test
    void testNoArgsConstructor() {
        PetitionWithUserInfo p = new PetitionWithUserInfo();
        assertNull(p.getLoanAmount());
        assertNull(p.getTerm());
        assertNull(p.getLoanType());
        assertNull(p.getLoanStatus());
        assertNull(p.getUserEmail());
        assertNull(p.getUserName());
        assertNull(p.getUserSalary());
    }

    @Test
    void testAllArgsConstructor() {
        BigDecimal loanAmount = new BigDecimal("15000.00");
        BigDecimal userSalary = new BigDecimal("75000.00");

        PetitionWithUserInfo p = new PetitionWithUserInfo(loanAmount, "36 Months", Loantypes.PERSONAL, LoanStatus.APPROVED, "test@user.com", "Test User", userSalary);

        assertEquals(loanAmount, p.getLoanAmount());
        assertEquals("36 Months", p.getTerm());
        assertEquals(Loantypes.PERSONAL, p.getLoanType());
        assertEquals(LoanStatus.APPROVED, p.getLoanStatus());
        assertEquals("test@user.com", p.getUserEmail());
        assertEquals("Test User", p.getUserName());
        assertEquals(userSalary, p.getUserSalary());
    }

    @Test
    void testBuilder() {
        BigDecimal loanAmount = new BigDecimal("25000.00");
        BigDecimal userSalary = new BigDecimal("85000.00");

        PetitionWithUserInfo p = PetitionWithUserInfo.builder()
                .loanAmount(loanAmount)
                .term("48 Months")
                .loanType(Loantypes.VEHICULAR)
                .loanStatus(LoanStatus.PENDING_REVIEW)
                .userEmail("builder@user.com")
                .userName("Builder User")
                .userSalary(userSalary)
                .build();

        assertEquals(loanAmount, p.getLoanAmount());
        assertEquals("48 Months", p.getTerm());
        assertEquals(Loantypes.VEHICULAR, p.getLoanType());
        assertEquals(LoanStatus.PENDING_REVIEW, p.getLoanStatus());
        assertEquals("builder@user.com", p.getUserEmail());
        assertEquals("Builder User", p.getUserName());
        assertEquals(userSalary, p.getUserSalary());
    }

    @Test
    void testSetters() {
        PetitionWithUserInfo p = new PetitionWithUserInfo();
        BigDecimal loanAmount = new BigDecimal("5000.00");
        BigDecimal userSalary = new BigDecimal("45000.00");

        p.setLoanAmount(loanAmount);
        p.setTerm("12 Months");
        p.setLoanType(Loantypes.HIPOTECARIO);
        p.setLoanStatus(LoanStatus.REJECTED);
        p.setUserEmail("setter@user.com");
        p.setUserName("Setter User");
        p.setUserSalary(userSalary);

        assertEquals(loanAmount, p.getLoanAmount());
        assertEquals("12 Months", p.getTerm());
        assertEquals(Loantypes.HIPOTECARIO, p.getLoanType());
        assertEquals(LoanStatus.REJECTED, p.getLoanStatus());
        assertEquals("setter@user.com", p.getUserEmail());
        assertEquals("Setter User", p.getUserName());
        assertEquals(userSalary, p.getUserSalary());
    }

    @Test
    void testToBuilder() {
        BigDecimal loanAmount = new BigDecimal("25000.00");
        BigDecimal userSalary = new BigDecimal("85000.00");

        PetitionWithUserInfo p1 = PetitionWithUserInfo.builder()
                .loanAmount(loanAmount)
                .term("48 Months")
                .loanType(Loantypes.VEHICULAR)
                .loanStatus(LoanStatus.PENDING_REVIEW)
                .userEmail("builder@user.com")
                .userName("Builder User")
                .userSalary(userSalary)
                .build();

        PetitionWithUserInfo p2 = p1.toBuilder().userName("Modified User").build();

        assertEquals(loanAmount, p2.getLoanAmount());
        assertEquals("Modified User", p2.getUserName());
        assertEquals(userSalary, p2.getUserSalary());
    }
}
