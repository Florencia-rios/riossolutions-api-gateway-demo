package dev.riossolutions.api_gateway.restcontroller.Api;

import dev.riossolutions.api_gateway.dto.request.AuthRequest;
import dev.riossolutions.api_gateway.dto.request.ValidateTokenRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth RestController")
public interface AuthApi {

    @Operation(summary = "Login", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully login", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "Invalid token or token expired", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)
    })
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request);

    @Operation(summary = "Validate", responses = {
            @ApiResponse(responseCode = "200", description = "Valid Token", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "Invalid token or token expired", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)
    })
    public ResponseEntity<?> validate(@Valid @RequestBody ValidateTokenRequest request);

    @Operation(summary = "Users", responses = {
            @ApiResponse(responseCode = "200", description = "Users", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "Invalid token or token expired", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)
    })
    public ResponseEntity<?> getUsers();
}
