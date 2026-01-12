package dev.riossolutions.api_gateway.config.resttemplate;

import dev.riossolutions.api_gateway.exception.handler.RestTemplateResponseErrorHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.web.client.RestTemplate;

/**
 * Configures RestTemplate with common interceptors and error handling.
 */
public class CustomRestTemplateCustomizer implements RestTemplateCustomizer {

    private final HttpServletRequest httpServletRequest;
    private final RestTemplateResponseErrorHandler errorHandler;

    public CustomRestTemplateCustomizer(HttpServletRequest httpServletRequest, RestTemplateResponseErrorHandler errorHandler) {
        this.httpServletRequest = httpServletRequest;
        this.errorHandler = errorHandler;
    }

    @Override
    public void customize(RestTemplate restTemplate) {
        /*
         * Interceptors and error handling configuration
         * are applied in the full implementation.
         */
    }
}