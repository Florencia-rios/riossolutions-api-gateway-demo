package dev.riossolutions.api_gateway.exception.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "error", "errors"})
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
    /**
     * The unique and fine-grained application-level error code.
     */
    @NotNull ErrorCode code;

    /**
     * The human-readable description for an issue. Provide non-standard more granular error message.
     */
    @NotNull String error;

    /**
     * The human-readable descriptions for several issues.
     */
    List<String> errors;

}
