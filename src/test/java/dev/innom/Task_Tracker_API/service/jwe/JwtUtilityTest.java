package dev.innom.Task_Tracker_API.service.jwe;

import dev.innom.Task_Tracker_API.jwt.JwtUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
public class JwtUtilityTest {

    private JwtUtility jwtUtility;

    @BeforeEach
    void setUp() {
        jwtUtility = new JwtUtility();

        ReflectionTestUtils.setField(jwtUtility, "secret", "mysecretkeymysecretkeymysecretkey12"); // must be 32+ bytes
        jwtUtility.init();
    }

    @Test
    void generateToken_and_getEmail() {
        String email = "test@example.com";

        String token = jwtUtility.generateToke(email);
        String extractedEmail = jwtUtility.getUserEmail(token);

        assertEquals(email, extractedEmail);
    }

    @Test
    void isExpired_ReturnFalsefor_newToken() {
        String token = jwtUtility.generateToke("test@example.com");

        boolean expired = jwtUtility.isExpired(token);

        assertFalse(expired);
    }
}
