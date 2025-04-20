package stepDefinitions;

import au.com.telstra.simcardactivator.SimCardActivator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.internal.runners.statements.Fail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = SimCardActivator.class, loader = SpringBootContextLoader.class)
public class SimCardActivatorStepDefinitions {
    //@Autowired
    //private TestRestTemplate restTemplate;

    String iccidFail;
    String actualResponse;
    String iccidPass;

    @Given("iccid fail {string}")
    public void iccidFail(String arg0) {
        iccidFail = arg0;//"8944500102198304826";
    }

    @Given("iccid pass {string}")
    public void iccidPass(String arg0) {
        iccidPass = arg0; //"8944500102198304826";
    }

    @When("request to activate sim card")
    public void ActivateSimCard(){
        actualResponse = Activation.activation(iccidFail);
    }

    @Then("should return activation failed {string}")
    public void shouldReturnActivationFailed(String expectedResponse) {
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }


    @Then("should return activation success {string}")
    public void shouldReturnActivationSuccess(String expectedResponse) {
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }


}

//Pass the ICCID to the API and creates all the neccessary connections.
class Activation{
    public static String activation(String iccid){
        String response = APIConnection.APIconnection(iccid);
        return Split.split(response);           // return true or false
    }
}


//Method to convert the Raw responseBody into the needed format
class Split{
    public static String split(String str){
        String[] raw = str.split(",")[1].split(":");
        return raw[1].substring(0, raw[1].indexOf("}"));
    };
}
