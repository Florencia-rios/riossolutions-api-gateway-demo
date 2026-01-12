package dev.riossolutions.api_gateway.service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IJwtService {

    String generateToken(Map<String, Object> othersClaims, String subject);

    String extractUsername(String token);

    List<String> getRolesFromToken(String token);

    boolean validateToken(final String token);

    Optional<String> getTokenWithoutBearer(HttpServletRequest request);
}
