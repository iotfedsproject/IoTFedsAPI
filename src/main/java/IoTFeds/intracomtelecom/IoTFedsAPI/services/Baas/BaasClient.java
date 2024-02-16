package IoTFeds.intracomtelecom.IoTFedsAPI.services.baas;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashMap;

@Component
@Slf4j
public class BaasClient {

    private RestTemplate restTemplate;

    @Autowired
    public BaasClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public ResponseEntity<String> makeBaasHttpRequest(String baasBaseUrl, String baasPrefix, HttpMethod method, HashMap<String, String> body, MultiValueMap<String, String> parameters) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        headers.setContentType(MediaType.APPLICATION_JSON);

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(baasBaseUrl + "/" + baasPrefix).queryParams(parameters);
        HttpEntity<?> entity = new HttpEntity<>(body, headers);

        log.debug("Sending request to: " + uriComponentsBuilder.toUriString() + " using: " + method);
        log.debug("With body: " + entity.getBody());

        try {

            return restTemplate.exchange(
                    uriComponentsBuilder.toUriString(),
                    method,
                    entity,
                    String.class
            );
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders()).body(e.getResponseBodyAsString());
        }
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Do any additional configuration here
        return builder.build();
    }
}
