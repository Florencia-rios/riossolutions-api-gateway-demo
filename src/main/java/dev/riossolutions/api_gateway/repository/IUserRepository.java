package dev.riossolutions.api_gateway.repository;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

/***
 * This class is the repository of users
 */
public interface IUserRepository {

    List<String> getUsers();

    Map<String, Object> validateUserPass(String username, String password);

    UserDetails getUser(String username);
}
