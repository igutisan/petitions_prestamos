package co.com.pragma.model.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class NotFoundExceptionTest {

    @Test
    void testNotFoundExceptionWithMessage() {
        // Arrange
        String errorMessage = "Entity not found";
        
        // Act
        NotFoundException exception = new NotFoundException(errorMessage);
        
        // Assert
        assertEquals(errorMessage, exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception);
    }
    
    @Test
    void testNotFoundExceptionIsRuntimeException() {
        // Arrange
        String errorMessage = "Resource not found";
        
        // Act
        NotFoundException exception = new NotFoundException(errorMessage);
        
        // Assert
        assertInstanceOf(RuntimeException.class, exception);
        assertEquals(errorMessage, exception.getMessage());
    }
}