
package co.com.pragma.model.client;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

class ClientTest {

    @Test
    void testNoArgsConstructor() {
        Client client = new Client();
        assertNull(client.getId());
        assertNull(client.getNames());
        assertNull(client.getLastNames());
        assertNull(client.getDni());
        assertNull(client.getDob());
        assertNull(client.getPhone());
        assertNull(client.getAddress());
        assertNull(client.getEmail());
        assertNull(client.getSalary());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        LocalDate dob = LocalDate.now();
        BigDecimal salary = new BigDecimal("50000.00");

        Client client = new Client(id, "John", "Doe", "123456789", dob, "1234567890", "123 Main St", "john.doe@example.com", salary);

        assertEquals(id, client.getId());
        assertEquals("John", client.getNames());
        assertEquals("Doe", client.getLastNames());
        assertEquals("123456789", client.getDni());
        assertEquals(dob, client.getDob());
        assertEquals("1234567890", client.getPhone());
        assertEquals("123 Main St", client.getAddress());
        assertEquals("john.doe@example.com", client.getEmail());
        assertEquals(salary, client.getSalary());
    }

    @Test
    void testBuilder() {
        UUID id = UUID.randomUUID();
        LocalDate dob = LocalDate.now();
        BigDecimal salary = new BigDecimal("60000.00");

        Client client = Client.builder()
                .id(id)
                .names("Jane")
                .lastNames("Doe")
                .dni("987654321")
                .dob(dob)
                .phone("0987654321")
                .address("456 Oak Ave")
                .email("jane.doe@example.com")
                .salary(salary)
                .build();

        assertEquals(id, client.getId());
        assertEquals("Jane", client.getNames());
        assertEquals("Doe", client.getLastNames());
        assertEquals("987654321", client.getDni());
        assertEquals(dob, client.getDob());
        assertEquals("0987654321", client.getPhone());
        assertEquals("456 Oak Ave", client.getAddress());
        assertEquals("jane.doe@example.com", client.getEmail());
        assertEquals(salary, client.getSalary());
    }

    @Test
    void testSetters() {
        Client client = new Client();
        UUID id = UUID.randomUUID();
        LocalDate dob = LocalDate.now();
        BigDecimal salary = new BigDecimal("70000.00");

        client.setId(id);
        client.setNames("Jim");
        client.setLastNames("Beam");
        client.setDni("112233445");
        client.setDob(dob);
        client.setPhone("1122334455");
        client.setAddress("789 Pine St");
        client.setEmail("jim.beam@example.com");
        client.setSalary(salary);

        assertEquals(id, client.getId());
        assertEquals("Jim", client.getNames());
        assertEquals("Beam", client.getLastNames());
        assertEquals("112233445", client.getDni());
        assertEquals(dob, client.getDob());
        assertEquals("1122334455", client.getPhone());
        assertEquals("789 Pine St", client.getAddress());
        assertEquals("jim.beam@example.com", client.getEmail());
        assertEquals(salary, client.getSalary());
    }
    
    @Test
    void testToBuilder() {
        UUID id = UUID.randomUUID();
        LocalDate dob = LocalDate.now();
        BigDecimal salary = new BigDecimal("60000.00");

        Client client1 = Client.builder()
                .id(id)
                .names("Jane")
                .lastNames("Doe")
                .dni("987654321")
                .dob(dob)
                .phone("0987654321")
                .address("456 Oak Ave")
                .email("jane.doe@example.com")
                .salary(salary)
                .build();

        Client client2 = client1.toBuilder().names("Janet").build();

        assertEquals(id, client2.getId());
        assertEquals("Janet", client2.getNames());
        assertEquals("Doe", client2.getLastNames());
    }
}
