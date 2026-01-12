package dev.riossolutions.api_gateway.service;

import dev.riossolutions.api_gateway.util.ApiConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

/**
 * Provides functionality for generating and validating JWT tokens.
 */
@Component
@Slf4j
public class JwtService implements IJwtService {

    @Value("${spring.security.jwt.secret-key}")
    private String secretKey;
    @Value("${spring.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${allowed.roles}")
    private Set<String> allowedRoles;
    private static final int BEARER_INDEX = ApiConstants.HEADER_AUTHORIZATION_PREFIX.length();

    /**
     * Generates a new JWT token.
     *
     * @param othersClaims additional claims to include in the token
     * @param subject the username used as the token subject
     * @return the generated JWT token
     */
    @Override
    public String generateToken(Map<String, Object> othersClaims, String subject) {
        return buildToken(othersClaims, subject, jwtExpiration);
    }

    /**
     * Extracts username from token
     * @param token
     * @return the username
     */
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the roles from the given JWT token.
     *
     * @param token the JWT token
     * @return the list of roles
     */
    @Override
    public List<String> getRolesFromToken(String token) {
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }

    /**
     * Validates the given JWT token by checking its expiration date
     * and verifying that its roles are included in the allowed roles list.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid; false otherwise
     */
    @Override
    public boolean validateToken(final String token) {
        try {

            if (isTokenExpired(token)) {
                log.warn("Token has expired: {}", token);
                throw new BadCredentialsException("Token has expired");
            }

            List<String> rolesFromToken = getRolesFromToken(token);

            boolean allRolesValid = rolesFromToken.stream()
                    .allMatch(allowedRoles::contains);

            if (!allRolesValid) {
                log.warn("Token contains invalid roles: {}", rolesFromToken);
                return false;
            }

            return true;
        } catch (ExpiredJwtException e) {
            log.warn("Token has expired: {}", token);
            throw new BadCredentialsException("Token has expired");
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token: {}", token);
            throw new BadCredentialsException("Unsupported JWT token");
        } catch (MalformedJwtException e) {
            log.warn("Malformed JWT token: {}", token);
            throw new BadCredentialsException("Malformed JWT token");
        } catch (SignatureException e) {
            log.warn("Invalid JWT signature: {}", token);
            throw new BadCredentialsException("Invalid JWT signature");
        } catch (IllegalArgumentException e) {
            log.warn("JWT token compact of handler are invalid: {}", token);
            throw new BadCredentialsException("JWT token compact of handler are invalid");
        }
    }

    public Optional<String> getTokenWithoutBearer(HttpServletRequest request) {
        Optional<String> token = Optional.ofNullable(request.getHeader(ApiConstants.HEADER_AUTHORIZATION));
        return getTokenWithoutBearer(token);
    }

    private static Optional<String> getTokenWithoutBearer(Optional<String> tokenWithBearerPrefix) {
        return tokenWithBearerPrefix.map(s -> StringUtils.substring(s, BEARER_INDEX));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            String subject,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
