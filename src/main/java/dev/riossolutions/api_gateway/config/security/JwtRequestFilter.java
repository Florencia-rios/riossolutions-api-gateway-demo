package dev.riossolutions.api_gateway.config.security;

import dev.riossolutions.api_gateway.service.IJwtService;
import dev.riossolutions.api_gateway.service.IUserService;
import dev.riossolutions.api_gateway.util.ApiConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * JWT authentication filter for incoming HTTP requests.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final IJwtService jwtService;

    private final IUserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /*
         * JWT authentication logic intentionally omitted.
         * Full implementation available in private version.
         */

        filterChain.doFilter(request, response);
    }
}