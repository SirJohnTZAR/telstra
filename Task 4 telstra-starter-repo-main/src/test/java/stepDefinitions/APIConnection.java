package stepDefinitions;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class APIConnection {

    public String email = "jin@gmail.com";


    public static String APIconnection(String iccid){
        String responseBody = "";
        String url = "http://localhost:8080/activate";
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        // --- Prepare the JSON request body ---
        // TODO: Replace this with the actual JSON data you need to send.
        // For complex objects, using a JSON library (like Jackson or Gson)
        // to serialize a Java object to a JSON string is recommended.
        String jsonRequestBody = "{\"iccid\": \""+iccid+"\", \"customerEmail\": \"email@gmail1.com\"}";
        // -------------------------------------

        try {
            // Create an HttpRequest for a POST request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json") // Set Content-Type header for JSON body
                    .header("Accept", "application/json")       // Indicate we accept JSON response
                    // Specify the POST method and provide the request body
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody))
                    .build();

            System.out.println("Sending POST request with body: " + jsonRequestBody);

            // Send the request and receive the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the HTTP status code
            int statusCode = response.statusCode();
            System.out.println("Response Status Code: " + statusCode);
            responseBody = response.body();
            System.out.println("Raw Response Body: " + responseBody);


            // Check for common success codes for POST (e.g., 201 Created or 200 OK)
            if (statusCode == 201 || statusCode == 200) {
                System.out.println("\nPOST request successful!");
                // TODO: Add specific parsing logic here if you need to extract data
                // from the response body (e.g., ID of the created item).
                // Example: String status = parseFieldFromJson(responseBody, "status");
            } else {
                // Handle non-successful status codes
                System.err.println("\nAPI POST request failed with status code: " + statusCode);
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseBody;
    }
}
