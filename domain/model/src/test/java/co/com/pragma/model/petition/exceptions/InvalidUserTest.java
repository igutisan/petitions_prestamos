
package co.com.pragma.model.petition.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidUserTest {

    @Test
    void testExceptionMessage() {
        String errorMessage = "This is a test error message.";
        InvalidUser exception = new InvalidUser(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testThrowException() {
        String errorMessage = "Another test message.";
        Exception exception = assertThrows(InvalidUser.class, () -> {
            throw new InvalidUser(errorMessage);
        });

        assertEquals(errorMessage, exception.getMessage());
    }
}
