package com.SuperToni.SuperToni.configuration.jwt;

import java.time.Duration;
import java.time.Instant;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtService {
    private final String issuer;
    private final Duration ttl;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    // Make sure you have EXACTLY this constructor
    public JwtService(String issuer, Duration ttl, JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.issuer = issuer;
        this.ttl = ttl;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateToken(final String username) {
        try {
            logger.debug("Generating JWT token for username: {}", username);
            
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be null or empty");
            }
            
            final var claimsSet = JwtClaimsSet.builder()
                    .subject(username.trim())
                    .issuer(issuer)
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plus(ttl))
                    .claim("username", username.trim()) // Add username as a claim
                    .build();

            String token = jwtEncoder.encode(JwtEncoderParameters.from(claimsSet))
                    .getTokenValue();
                    
            logger.debug("JWT token generated successfully for username: {}", username);
            return token;
            
        } catch (Exception e) {
            logger.error("Failed to generate JWT token for username: {}", username, e);
            throw new RuntimeException("JWT token generation failed", e);
        }
    }

    public Boolean validateJwtToken(final String token) {
        try {
            logger.debug("Validating JWT token");
            
            if (token == null || token.trim().isEmpty()) {
                logger.warn("Token validation failed: Token is null or empty");
                return false;
            }
            
            // Parse and validate the token
            Jwt jwt = jwtDecoder.decode(token.trim());
            
            // Validate issuer
            if (!issuer.equals(jwt.getIssuer().toString())) {
                logger.warn("Token validation failed: Invalid issuer. Expected: {}, Found: {}", issuer, jwt.getIssuer());
                return false;
            }
            
            // Validate expiration
            Instant expiration = jwt.getExpiresAt();
            if (expiration == null || expiration.isBefore(Instant.now())) {
                logger.warn("Token validation failed: Token is expired");
                return false;
            }
            
            // Validate issued at time (optional - ensure token wasn't issued in the future)
            Instant issuedAt = jwt.getIssuedAt();
            if (issuedAt != null && issuedAt.isAfter(Instant.now().plus(Duration.ofMinutes(5)))) { // Allow 5 minutes clock skew
                logger.warn("Token validation failed: Token issued in the future");
                return false;
            }
            
            // Validate subject exists
            if (jwt.getSubject() == null || jwt.getSubject().isEmpty()) {
                logger.warn("Token validation failed: Token has no subject");
                return false;
            }
            
            logger.debug("Token validation successful");
            return true;
            
        } catch (JwtException e) {
            logger.warn("Token validation failed: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error during token validation", e);
            return false;
        }
    }

    // Additional helper method to extract username from token
    public String extractUsername(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("Token cannot be null or empty");
            }
            
            Jwt jwt = jwtDecoder.decode(token.trim());
            String username = jwt.getSubject();
            
            if (username == null || username.isEmpty()) {
                throw new JwtException("Token has no valid subject");
            }
            
            return username;
        } catch (JwtException e) {
            logger.error("Failed to extract username from token: {}", e.getMessage());
            throw new JwtException("Invalid token", e);
        }
    }

    // Additional helper method to check if token is about to expire
    public Boolean isTokenExpiringSoon(String token, Duration threshold) {
        try {
            if (token == null || token.trim().isEmpty()) {
                return true; // Consider null/empty tokens as expired
            }
            
            Jwt jwt = jwtDecoder.decode(token.trim());
            Instant expiration = jwt.getExpiresAt();
            if (expiration == null) {
                return true; // No expiration means expired
            }
            return expiration.minus(threshold).isBefore(Instant.now());
        } catch (JwtException e) {
            logger.debug("Token expiration check failed: {}", e.getMessage());
            return true; // Invalid tokens are considered expired
        }
    }
    
    // Method to get token claims
    public Jwt parseToken(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("Token cannot be null or empty");
            }
            
            return jwtDecoder.decode(token.trim());
        } catch (JwtException e) {
            logger.error("Failed to parse token: {}", e.getMessage());
            throw new JwtException("Token parsing failed", e);
        }
    }
}