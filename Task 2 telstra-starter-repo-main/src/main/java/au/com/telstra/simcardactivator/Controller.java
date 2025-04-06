package au.com.telstra.simcardactivator;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/")
public class Controller {

    String iccid;
    String customerEmail;
    Boolean active;
    SimCards simCards;


    private final SimCardInfoRepo repo;

    @Autowired
    RestTemplate restTemplate;

    ActivationRequest activationRequest;
    @Autowired
    private RestOperations restOperations;

    public Controller(SimCardInfoRepo repo) {
        this.repo = repo;
    }

    @GetMapping("/test")
    public String test() {
        SimCards s = new SimCards("1234", "J@gmail.com", true);
        repo.save(s);
        System.out.println("SimCard with id: "+s.getId());
        return "Saved";
    }

    // Getting a post request and try to activate the sim card by communicating with another micro-service.
    // Storing the transactions.
    @PostMapping("/activate")
    public ResponseEntity<?> activate(@RequestBody JsonNode request) {

        // Will be stored in H2 database.
        // Will be fed to SimCardActivator
        iccid = request.get("iccid").asText();
        customerEmail = request.get("customerEmail").asText();

        activationRequest = new ActivationRequest(iccid);

        // Creating a connection to actuate
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<ActivationRequest> entity = new HttpEntity<>(activationRequest, headers);

        // Extracting the results received from the other micro-service.
        String responseBody = restTemplate.exchange("http://localhost:8444/actuate", HttpMethod.POST, entity, String.class).getBody();
        assert responseBody != null;
        active = Boolean.valueOf(responseBody.substring(responseBody.indexOf(":")+1, responseBody.lastIndexOf("}")));

        // Adding to DB, H2
        simCards = new SimCards(iccid, customerEmail, active);
        repo.save(simCards);
        System.out.println("SimCard: "+simCards+" \ncreated");

        //
        //return active ? ResponseEntity.ok("SimCard Activated") : ResponseEntity.badRequest().body("SimCard Activation Failed");
        return restTemplate.exchange("http://localhost:8444/actuate", HttpMethod.POST, entity, String.class);
    }

    //This method gets the sim id data from the database.
    @GetMapping("/getSimCardId")
    public String getSimCardID(@RequestParam("simCardId") Long simCardId){

        SimCards sim = repo.findByid(simCardId);
        if (repo.findByid(simCardId) == null){
            return "Not found";
        }
        else{
            return sim.toString();
        }
    }

    // Still fixing.
    @GetMapping("/getAllSimCards")
    public List<String> getAllSimCards(){
        List<String> sims = new ArrayList<>();
        for (SimCards simCard : repo.findAll()) {
            sims.add(simCard.toString());
        }
        return sims;
    }

}
