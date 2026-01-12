package dev.riossolutions.api_gateway.util;

import dev.riossolutions.api_gateway.exception.handler.RestTemplateResponseErrorHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

/**
 * Base HTTP client with common RestTemplate configuration.
 */
public abstract class AbstractClient {

    protected final RestTemplate restTemplate;

    protected AbstractClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    protected HttpHeaders buildAuthToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        return headers;
    }
}
