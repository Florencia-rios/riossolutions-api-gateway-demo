package dev.riossolutions.api_gateway.util;

import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Utility methods for handling RestTemplate HTTP responses.
 */
public class RestTemplateUtils {
    private RestTemplateUtils() {
        throw new UnsupportedOperationException("Cannot instantiate utility class");
    }

    public static <T> String getBody(ClientHttpResponse httpResponse) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(httpResponse.getBody(), StandardCharsets.UTF_8);
        String body = new BufferedReader(inputStreamReader).lines().collect(Collectors.joining("\n"));
        return body;
    }
}