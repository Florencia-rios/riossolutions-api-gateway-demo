package dev.riossolutions.api_gateway.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Response payload containing a list of usernames.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersResponse {

    private List<String> users;
}
