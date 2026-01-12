package dev.riossolutions.api_gateway.service;

import dev.riossolutions.api_gateway.dto.response.AuthResponse;
import dev.riossolutions.api_gateway.dto.response.UsersResponse;
import dev.riossolutions.api_gateway.dto.response.ValidateTokenResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUserService {

    AuthResponse login(String username, String password);

    ValidateTokenResponse validate(String token);

    UsersResponse getUsers();

    UserDetails getUser(String username);
}
