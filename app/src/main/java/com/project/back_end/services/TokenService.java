package com.project.back_end.services;

import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

// 1. @Component Annotation
@Component
public class TokenService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    // 2. Constructor Injection for Dependencies
    @Autowired
    public TokenService(AdminRepository adminRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    /**
     * 3. getSigningKey Method
     * Generates the HMAC SHA key for signing and verifying the JWT.
     * @return A SecretKey object.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 4. generateToken Method
     * Creates a new JWT for a given user email.
     * @param email The user's email, which will be the subject of the token.
     * @return A signed JWT string.
     */
    public String generateToken(String email) {
        long expirationTime = 1000L * 60 * 60 * 24 * 7; // 7 days in milliseconds
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 5. extractEmail Method
     * Extracts the email (subject) from a JWT.
     * @param token The JWT string.
     * @return The email address contained in the token.
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * A generic method to extract any claim from a token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        // The "Bearer " prefix must be removed before parsing.
        String cleanToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(cleanToken)
                .getBody();
    }

    /**
     * 6. validateToken Method
     * Checks if a token is valid for a given role by verifying its signature and checking if the user exists.
     * @param token The JWT string.
     * @param role The role to validate against ("admin", "doctor", "patient").
     * @return true if the token is valid for the role, false otherwise.
     */
    public boolean validateToken(String token, String role) {
        try {
            final String email = extractEmail(token);
            switch (role.toLowerCase()) {
                case "admin":
                    return adminRepository.findByEmail(email).isPresent() && !isTokenExpired(token);
                case "doctor":
                    return doctorRepository.findByEmail(email).isPresent() && !isTokenExpired(token);
                case "patient":
                    return patientRepository.findByEmail(email).isPresent() && !isTokenExpired(token);
                default:
                    return false;
            }
        } catch (Exception e) {
            // Catches any exception during parsing (e.g., ExpiredJwtException, MalformedJwtException)
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // --- Additional Helper Methods used in Controllers ---

    /**
     * A practical method to get the user's role directly from the token.
     * @param token The JWT string (including "Bearer " prefix).
     * @return The role as a string ("admin", "doctor", "patient") or null if not found.
     */
    public String getRoleFromToken(String token) {
        try {
            final String email = extractEmail(token);
            if (adminRepository.findByEmail(email).isPresent()) return "admin";
            if (doctorRepository.findByEmail(email).isPresent()) return "doctor";
            if (patientRepository.findByEmail(email).isPresent()) return "patient";
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * A simple validation check used by controllers before processing a request.
     * @param token The JWT string (including "Bearer " prefix).
     * @return true if the token is valid and not expired, false otherwise.
     */
    public boolean isTokenValid(String token) {
        try {
            return getRoleFromToken(token) != null && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}