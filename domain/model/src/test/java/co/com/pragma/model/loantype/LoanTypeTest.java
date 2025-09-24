package co.com.pragma.model.loantype;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoanTypeTest {

    @Test
    void testLoanTypeBuilder() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Personal Loan";
        double interestRate = 15.5;
        boolean isAutomaticValidation = true;

        // Act
        LoanType loanType = LoanType.builder()
                .id(id)
                .name(name)
                .interestRate(interestRate)
                .isAutomaticValidation(isAutomaticValidation)
                .build();

        // Assert
        assertEquals(id, loanType.getId());
        assertEquals(name, loanType.getName());
        assertEquals(interestRate, loanType.getInterestRate());
        assertTrue(loanType.isAutomaticValidation());
    }

    @Test
    void testLoanTypeNoArgsConstructor() {
        // Act
        LoanType loanType = new LoanType();

        // Assert
        assertNotNull(loanType);
    }

    @Test
    void testLoanTypeAllArgsConstructor() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Vehicle Loan";
        double interestRate = 12.0;
        boolean isAutomaticValidation = false;

        // Act
        LoanType loanType = new LoanType(id, name, interestRate, isAutomaticValidation);

        // Assert
        assertEquals(id, loanType.getId());
        assertEquals(name, loanType.getName());
        assertEquals(interestRate, loanType.getInterestRate());
        assertFalse(loanType.isAutomaticValidation());
    }

    @Test
    void testLoanTypeSetters() {
        // Arrange
        LoanType loanType = new LoanType();
        UUID id = UUID.randomUUID();
        String name = "Mortgage Loan";
        double interestRate = 8.5;
        boolean isAutomaticValidation = true;

        // Act
        loanType.setId(id);
        loanType.setName(name);
        loanType.setInterestRate(interestRate);
        loanType.setAutomaticValidation(isAutomaticValidation);

        // Assert
        assertEquals(id, loanType.getId());
        assertEquals(name, loanType.getName());
        assertEquals(interestRate, loanType.getInterestRate());
        assertTrue(loanType.isAutomaticValidation());
    }

    @Test
    void testToBuilder() {
        // Arrange
        UUID originalId = UUID.randomUUID();
        LoanType originalLoanType = LoanType.builder()
                .id(originalId)
                .name("Original Loan")
                .interestRate(10.0)
                .isAutomaticValidation(false)
                .build();

        UUID newId = UUID.randomUUID();

        // Act
        LoanType modifiedLoanType = originalLoanType.toBuilder()
                .id(newId)
                .name("Modified Loan")
                .build();

        // Assert
        assertEquals(newId, modifiedLoanType.getId());
        assertEquals("Modified Loan", modifiedLoanType.getName());
        assertEquals(10.0, modifiedLoanType.getInterestRate());
        assertFalse(modifiedLoanType.isAutomaticValidation());
        
        // Original should be unchanged
        assertEquals(originalId, originalLoanType.getId());
        assertEquals("Original Loan", originalLoanType.getName());
    }

    @Test
    void testLoanTypeWithZeroInterestRate() {
        // Act
        LoanType loanType = LoanType.builder()
                .id(UUID.randomUUID())
                .name("Zero Interest Loan")
                .interestRate(0.0)
                .isAutomaticValidation(true)
                .build();

        // Assert
        assertEquals(0.0, loanType.getInterestRate());
        assertTrue(loanType.isAutomaticValidation());
    }
}