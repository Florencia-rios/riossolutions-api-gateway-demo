package dev.riossolutions.api_gateway.service;

import dev.riossolutions.api_gateway.config.security.CustomUserDetails;
import dev.riossolutions.api_gateway.dto.response.AuthResponse;
import dev.riossolutions.api_gateway.dto.response.UsersResponse;
import dev.riossolutions.api_gateway.dto.response.ValidateTokenResponse;
import dev.riossolutions.api_gateway.repository.UserRepository;
import dev.riossolutions.api_gateway.util.ApiConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(value = MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void whenLoginThenReturnToken() {
        // set up
        String username = "username";
        String password = "pass";
        String token = "token";

        AuthResponse tokenResponse = new AuthResponse(token);

        Map<String, Object> roles = new HashMap<String, Object>();
        List<String> rolesInMap = new ArrayList<>();
        rolesInMap.add(ApiConstants.ADMIN_ROLE);
        roles.put("roles", rolesInMap);

        // mock
        Mockito.doReturn(roles).when(userRepository).validateUserPass(username, password);
        Mockito.doReturn(token).when(jwtService).generateToken(roles, username);

        // execute
        AuthResponse response = userService.login(username, password);

        // assertion
        Assertions.assertEquals(tokenResponse.getAccessToken(), response.getAccessToken());
    }

    @Test
    void whenTryToLoginAndDoNotHaveAnyRolesThenThrowsBadCredentialsException() {
        // set up
        String username = "username";
        String password = "pass";

        Map<String, Object> roles = new HashMap<String, Object>();
        List<String> rolesInMap = new ArrayList<>();
        roles.put("roles", rolesInMap);

        // mock
        Mockito.doReturn(roles).when(userRepository).validateUserPass(username, password);

        // execute and assertion
        BadCredentialsException exception =
                Assertions.assertThrows(BadCredentialsException.class,
                        () -> userService.login(username, password));

        Assertions.assertEquals("User is not valid because do not have any roles",
                exception.getMessage());
    }

    @Test
    void whenValidateThenReturnUserDetails() {
        // set up
        String username = "username";

        Map<String, Object> roles = new HashMap<>();
        List<String> rolesInMap = new ArrayList<>();
        rolesInMap.add(ApiConstants.ADMIN_ROLE);
        roles.put("roles", rolesInMap);

        String token = "token";

        ValidateTokenResponse expected = new ValidateTokenResponse(true, username, rolesInMap);

        // mock
        Mockito.doReturn(username).when(jwtService).extractUsername(token);
        Mockito.doReturn(rolesInMap).when(jwtService).getRolesFromToken(token);
        Mockito.doReturn(true).when(jwtService).validateToken(token);

        // execute
        ValidateTokenResponse response = userService.validate(token);

        // assertion
        Assertions.assertEquals(expected.getRoles().get(0), response.getRoles().get(0));
        Assertions.assertTrue(response.isValid());
        Assertions.assertEquals(expected.getUsername(), response.getUsername());
    }

    @Test
    void whenValidateThenThrowsBadCredentialsException() {
        // set up
        Map<String, Object> roles = new HashMap<>();
        List<String> rolesInMap = new ArrayList<>();
        rolesInMap.add(ApiConstants.ADMIN_ROLE);
        roles.put("roles", rolesInMap);

        String token = "token";

        // mock
        Mockito.doReturn(false).when(jwtService).validateToken(token);

        // execute and assertion
        BadCredentialsException exception =
                Assertions.assertThrows(BadCredentialsException.class,
                        () -> userService.validate(token));

        Assertions.assertEquals("Invalid or Expired Token",
                exception.getMessage());

    }

    @Test
    void whenValidateButThrowsAnExceptionThenThrowsBadCredentialsException() {
        // set up
        Map<String, Object> roles = new HashMap<>();
        List<String> rolesInMap = new ArrayList<>();
        rolesInMap.add(ApiConstants.ADMIN_ROLE);
        roles.put("roles", rolesInMap);

        String token = "token";

        // mock
        Mockito.doThrow(BadCredentialsException.class).when(jwtService).validateToken(token);

        // execute and assertion
        Assertions.assertThrows(BadCredentialsException.class,
                () -> userService.validate(token));
    }

    @Test
    void whenGetUsersThenReturnUsers() {
        // set up
        List<String> users = new ArrayList<>();
        users.add("user1");
        UsersResponse usersResponse = new UsersResponse();
        usersResponse.setUsers(users);

        // mock
        Mockito.doReturn(users).when(userRepository).getUsers();

        // execute
        UsersResponse response = userService.getUsers();

        // assertion
        Assertions.assertEquals(usersResponse.getUsers().get(0), response.getUsers().get(0));
        Assertions.assertEquals(usersResponse.getUsers().size(), response.getUsers().size());
    }

    @Test
    void whenGetUsersButThrowsAnExceptionThenThrowsRuntimeException() {
        // mock
        Mockito.doThrow(RuntimeException.class).when(userRepository).getUsers();

        // execute and assertion
        Assertions.assertThrows(RuntimeException.class,
                () -> userService.getUsers());
    }

    @Test
    void whenGetUserThenReturnUser() {
        // set up
        String username = "username";

        UserDetails userDetailsResponse = new CustomUserDetails(username);

        // mock
        Mockito.doReturn(userDetailsResponse).when(userRepository).getUser(username);

        // execute
        UserDetails response = userService.getUser(username);

        // assertion
        Assertions.assertEquals(userDetailsResponse.getUsername(), response.getUsername());
    }

    @Test
    void whenGetUserButThrowsAnExceptionThenThrowsRuntimeException() {
        // set up
        String username = "username";

        // mock
        Mockito.doThrow(RuntimeException.class).when(userRepository).getUser(username);

        // execute and assertion
        Assertions.assertThrows(RuntimeException.class,
                () -> userService.getUser(username));
    }
}
