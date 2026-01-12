package dev.riossolutions.api_gateway.util;

/**
 * Centralized API constants used for endpoints, roles, headers, and security configuration.
 */
public interface ApiConstants {

    String AUTH_URI = "/api/auth";
    String AUTH_LOGIN = "/login";
    String AUTH_VALIDATE = "/validate";
    String AUTH_USERS = "/users";
    String AUTH_USER = "/user/{username}";

    String ADMIN_ROLE = "ADMIN";
    String EDITOR_ROLE = "EDITOR";
    String READ_ONLY_ROLE = "READ_ONLY";

    String HEADER_AUTHORIZATION = "Authorization";
    String MESSAGE_AUTHENTICATION_FAILED = "Authentication failed.";
    String HEADER_AUTHORIZATION_PREFIX = "Bearer ";
    String[] WHITE_LIST_URL = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html/**",
            AUTH_URI+AUTH_VALIDATE,
            AUTH_URI+AUTH_LOGIN,
            AUTH_URI+AUTH_USERS,
            AUTH_URI+AUTH_USER
    };
}
