package dev.riossolutions.api_gateway.restcontroller;

import dev.riossolutions.api_gateway.util.AbstractClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * This class is used for testing purposes to consume the API as a proxy service.
 * It can be removed once it is no longer needed.
 */
@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET})
public class PruebaRestController extends AbstractClient {

    public PruebaRestController(RestTemplateBuilder restTemplate) {
        super(restTemplate);
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {

        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8089/health")
                .build()
                .toUri();

        ResponseEntity<String> response = this.restTemplate.getForEntity(uri, String.class);

        return ResponseEntity.ok(response.getBody());
    }
}
