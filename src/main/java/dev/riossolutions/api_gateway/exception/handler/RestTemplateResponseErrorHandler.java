package dev.riossolutions.api_gateway.exception.handler;

import dev.riossolutions.api_gateway.exception.BadRequestCustomException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.net.URI;

import static dev.riossolutions.api_gateway.util.RestTemplateUtils.getBody;

/**
 * Custom error handler for RestTemplate responses.
 */
@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
            throws IOException {
        return (httpResponse
                .getStatusCode().is4xxClientError() || httpResponse
                .getStatusCode().is5xxServerError());
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse httpResponse) throws IOException {
        if (httpResponse.getStatusCode().is5xxServerError()) {
            //Handle SERVER_ERROR
            throw new HttpServerErrorException(httpResponse.getStatusCode());
        } else if (httpResponse.getStatusCode().is4xxClientError()) {
            //Handle CLIENT_ERROR
            if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                String body = getBody(httpResponse);
                throw new NotFoundException(body);
            }
            if (httpResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
                String body = getBody(httpResponse);
                throw new BadRequestCustomException(body);
            }
            else {
                throw new HttpClientErrorException(httpResponse.getStatusCode());
            }
        }
    }
}