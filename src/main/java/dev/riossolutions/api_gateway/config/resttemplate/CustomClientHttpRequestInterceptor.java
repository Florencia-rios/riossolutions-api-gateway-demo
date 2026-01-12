package dev.riossolutions.api_gateway.config.resttemplate;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * RestTemplate interceptor used to propagate authentication context
 * across downstream services.
 */
public class CustomClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(CustomClientHttpRequestInterceptor.class);

    private final HttpServletRequest request;

    public CustomClientHttpRequestInterceptor(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body);

        /*
         * JWT propagation logic intentionally omitted.
         */

        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);

        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) {
        logger.info("Sending request: [{}] {} {}",
                request.getMethod(),
                request.getURI(),
                new String(body, StandardCharsets.UTF_8));
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        logger.info("Received response: {} {}",
                response.getStatusCode(),
                response.getStatusText());
    }
}
