package dev.riossolutions.api_gateway.restcontroller;

import dev.riossolutions.api_gateway.dto.request.AuthRequest;
import dev.riossolutions.api_gateway.dto.request.ValidateTokenRequest;
import dev.riossolutions.api_gateway.restcontroller.Api.AuthApi;
import dev.riossolutions.api_gateway.service.IUserService;
import dev.riossolutions.api_gateway.util.ApiConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication and user management operations.
 *
 * Provides endpoints for login, token validation,
 * and user information retrieval.
 *
 * Authentication is based on JWT and all logic
 * is delegated to the service layer.
 */
@RestController
@RequestMapping(ApiConstants.AUTH_URI)
@CrossOrigin(origins = "*", methods= {RequestMethod.POST, RequestMethod.GET})
public class AuthRestController implements AuthApi {

    @Autowired
    private IUserService userService;

    @PostMapping(ApiConstants.AUTH_LOGIN)
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(userService.login(request.getUsername(), request.getPassword()));
    }

    @PostMapping(ApiConstants.AUTH_VALIDATE)
    public ResponseEntity<?> validate(@Valid @RequestBody ValidateTokenRequest request) {
        return ResponseEntity.ok(userService.validate(request.getToken()));
    }

    @GetMapping(ApiConstants.AUTH_USERS)
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }


    @GetMapping(ApiConstants.AUTH_USER)
    public ResponseEntity<?> getUser(@Valid @PathVariable String username) {
        return ResponseEntity.ok(userService.getUser(username));
    }
}
