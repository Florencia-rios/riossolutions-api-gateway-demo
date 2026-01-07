package dev.riossolutions.api_gateway.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Token validation response payload.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ValidateTokenResponse {

    private boolean valid;
    private String username;
    private List<String> roles;
}
