package au.com.telstra.simcardactivator;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.RequestPayload;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
@RequestMapping("/")
public class Controller {

    @Autowired
    RestTemplate restTemplate;

    ActivationRequest activationRequest;

    @PostMapping("/activate")
    public ResponseEntity<?> activate(@RequestBody JsonNode request) {

        // Creating a activation request which will be fed to actuate.
        activationRequest = new ActivationRequest(request.get("iccid").asInt());

        // Creating a connection to actuate
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<ActivationRequest> entity = new HttpEntity<>(activationRequest, headers);

        return restTemplate.exchange("http://localhost:8444/actuate", HttpMethod.POST, entity, String.class);
    }
}
