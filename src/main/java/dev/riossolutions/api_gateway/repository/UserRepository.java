package dev.riossolutions.api_gateway.repository;

import dev.riossolutions.api_gateway.config.security.CustomUserDetails;
import dev.riossolutions.api_gateway.util.ApiConstants;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demo implementation of {@link IUserRepository}.
 *
 * <p>
 * This implementation is intended for development and testing purposes only.
 * It uses in-memory data and mock responses.
 * </p>
 *
 * <p>
 * In a real-world scenario, this repository can be replaced with:
 * </p>
 * <ul>
 *     <li>Active Directory / LDAP</li>
 *     <li>Relational or NoSQL database</li>
 *     <li>External IAM or Identity Provider</li>
 * </ul>
 */
@Component
public class UserRepository implements IUserRepository {

    @Override
    public List<String> getUsers() {
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> validateUserPass(String username, String password) {
        Map<String, Object> response = new HashMap<>();

        List<String> roles = new ArrayList<>();
        roles.add(ApiConstants.ADMIN_ROLE);

        response.put("roles", roles);
        return response;
    }

    @Override
    public UserDetails getUser(String username) {
        return new CustomUserDetails(username);
    }
}