package dev.riossolutions.api_gateway.service;

import dev.riossolutions.api_gateway.dto.response.AuthResponse;
import dev.riossolutions.api_gateway.dto.response.UsersResponse;
import dev.riossolutions.api_gateway.dto.response.ValidateTokenResponse;
import dev.riossolutions.api_gateway.repository.IUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service responsible for retrieving users and their associated roles from the repository.
 */
@Service
@Slf4j
public class UserService implements IUserService {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private IUserRepository userRepository;

    /**
     * Authenticates a user and generates a JWT access token.
     *
     * @param username the user's username
     * @param password the user's password
     * @return the generated JWT access token
     */
    @Override
    public AuthResponse login(String username, String password) {

        try {
            AuthResponse tokenResponse = new AuthResponse();

            // Primero tiene que validar que exista ese user y pass
            Map<String, Object> roles = userRepository.validateUserPass(username, password);

            List<String> rolesInMap = (List<String>) roles.get("roles");

            // Y, si está bien, luego generar el token
            if (!rolesInMap.isEmpty()) {
                String token = jwtService.generateToken(roles, username);
                tokenResponse.setAccessToken(token);
            } else {
                throw new BadCredentialsException("User is not valid because do not have any roles");
            }

            return tokenResponse;

        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    /**
     * Validates the given token and returns its information if it is valid.
     *
     * @param token the JWT token to validate
     * @return the token information
     * @throws BadCredentialsException if the token is invalid
     */
    @Override
    public ValidateTokenResponse validate(String token) {
        // Validar roles del token

        try {
            if (isValidToken(token)) {
                ValidateTokenResponse response = new ValidateTokenResponse();
                String username = jwtService.extractUsername(token);
                response.setValid(true);
                response.setUsername(username);
                response.setRoles(jwtService.getRolesFromToken(token));
                log.info("Validate token successful for user: {}", username);
                return response;
            } else {
                throw new BadCredentialsException("Invalid or Expired Token");
            }
        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    private boolean isValidToken(String token) {
        return jwtService.validateToken(token);
    }

    /**
     * Retrieves the usernames of all registered users.
     *
     * @return a list of registered usernames
     */
    @Override
    public UsersResponse getUsers() {

        try {
            UsersResponse usersResponse = new UsersResponse();

            List<String> users = userRepository.getUsers();

            usersResponse.setUsers(users);

            return usersResponse;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Extracts the user details by its username
     * @param username the username of
     * @return the user details
     */
    @Override
    public UserDetails getUser(String username) {
        try {
            log.info("Searching details for user {}", username);

            return userRepository.getUser(username);

        } catch (Exception e) {
            log.error("Authentication failed for ", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
