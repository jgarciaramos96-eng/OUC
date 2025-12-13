package edu.uoc.epcsd.user.config;

import edu.uoc.epcsd.user.domain.User;
import org.junit.jupiter.api.Test;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @Test
    void testGenerateToken() {
        JwtUtil jwtUtil = new JwtUtil();

        User user = new User();
        user.setEmail("test@example.com");
        user.setFullName("Nombre Completo");

        String token = jwtUtil.generateToken(user, "ADMIN");

        assertNotNull(token);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey("GRUPO8_GRUPO8_GRUPO8_GRUPO8_GRUPO8".getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("test@example.com", claims.getSubject());
        assertEquals("ADMIN", claims.get("role"));
        assertEquals("Nombre Completo", claims.get("fullName"));
        assertEquals(user.getId(), claims.get("userId"));
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }
}
