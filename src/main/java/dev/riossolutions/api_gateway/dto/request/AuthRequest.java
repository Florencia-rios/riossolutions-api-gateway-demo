package dev.riossolutions.api_gateway.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Authentication request payload.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {

    @NotBlank(message = "The username must not be empty")
    private String username;
    @NotBlank(message = "The password must not be empty")
    private String password;
}