package au.com.telstra.simcardactivator;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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
    public Map<String, Object> test() {
        Map<String, Object> data = new HashMap<>();
        data.put("key1", "value1");
        data.put("key2", "value2");
        return new ResponseEntity<>(data, HttpStatus.OK).getBody();
        /*
        SimCards s = new SimCards("1234", "J@gmail.com", true);
        repo.save(s);
        System.out.println("SimCard with id: "+s.getId());
        return "Saved";

         */
    }

    @PostMapping("/activate")
    public Map<String, Object> activate2(@RequestBody JsonNode requestBody){

        // Will be stored in H2 database.
        // Will be fed to SimCardActivator
        iccid = requestBody.get("iccid").asText();
        customerEmail = requestBody.get("customerEmail").asText();

        // JSOn to be passed to the microservice
        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("iccid", iccid);

        // Creating a connection to actuate
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(jsonData, headers);

        // Extracting the results received from the other micro-service.
        String responseBody = restTemplate.exchange("http://localhost:8444/actuate", HttpMethod.POST, entity, String.class).getBody();
        assert responseBody != null;
        active = Boolean.valueOf(responseBody.substring(responseBody.indexOf(":")+1, responseBody.lastIndexOf("}")));

        // Adding to DB, H2
        simCards = new SimCards(iccid, customerEmail, active);
        repo.save(simCards);
        System.out.println("SimCard: "+simCards+"\ncreated");

        //Adding to JSON
        jsonData.put("active", active);

        return new ResponseEntity<>(jsonData, HttpStatus.OK).getBody();
    }

    //This method gets the sim id data from the database.
    @GetMapping("/getSimCardId")
    public Map<String, Object> getSimCardID(@RequestParam("simCardId") Long simCardId){

        // Creting the json object
        Map<String, Object> jsonData = new HashMap<>();

        SimCards sim = repo.findByid(simCardId);
        if (repo.findByid(simCardId) == null){
            jsonData.put("message", "Not found");
            return new ResponseEntity<>(jsonData, HttpStatus.NOT_FOUND).getBody();
        }
        else{
            jsonData.put("iccid", sim.getIccid());
            jsonData.put("customerEmail", sim.getCustomerEmail());
            jsonData.put("active", sim.getActive());
            return new ResponseEntity<>(jsonData, HttpStatus.OK).getBody();
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


/* APPENDIX

// Getting a post request and try to activate the sim card by communicating with another micro-service.
    // Storing the transactions.
    @PostMapping("/activate")
    public ResponseEntity<?> activate(@RequestBody JsonNode request) {

        // Will be stored in H2 database.
        // Will be fed to SimCardActivator
        iccid = request.get("iccid").asText();
        customerEmail = request.get("customerEmail").asText();

        activationRequest = new ActivationRequest(iccid);

        System.out.println("SimCard: "+iccid+" \nactivated");

        // Creating a connection to actuate
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(activationRequest.getIccidiccid(), headers);

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

    -----------------------------------------------------------------------------------------------------------

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

 */