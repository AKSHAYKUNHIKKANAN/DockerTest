package com.Test.DockerTest.ServiceImpl;

import com.Test.DockerTest.Model.Card;
import com.Test.DockerTest.Model.User;
import com.Test.DockerTest.Model.UserResponse;
import com.Test.DockerTest.Service.DockerTestService;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class DockerTestImpl implements DockerTestService {


    @Override
    public String register(String userName,String password,String email) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(userName);

        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);

        String url = "http://localhost:8085/register";

        ResponseEntity<UserResponse> responseEntity = restTemplate.postForEntity(url, requestEntity, UserResponse.class);

        // Access the response object
        UserResponse userResponse = responseEntity.getBody();

        return userResponse.getId();
    }

    public String login(String userName,String password) throws IOException {
        String url = "http://localhost:8085/login";
        // Create URL object
        URL urlTest = new URL(url);

        // Open a connection to the URL
        HttpURLConnection connection = (HttpURLConnection) urlTest.openConnection();

        // Set the request method to GET
        connection.setRequestMethod("GET");

        // Set up basic authentication
        String credentials = userName + ":" + password;
        String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        connection.setRequestProperty("Authorization", "Basic " + base64Credentials);

        // Get the response code
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        // Read the response
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Get and validate cookies
            Map<String, List<String>> headerFields = connection.getHeaderFields();
            List<String> cookies = headerFields.get("Set-Cookie");

            if (cookies != null) {
                for (String cookie : cookies) {
                    // Validate or process each cookie as needed
                    System.out.println("Cookie: " + cookie);
                    // Add your validation logic here
                }
            } else {
                System.out.println("No cookies in the response.");
            }

            // Continue reading the response content if needed
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                System.out.println("Response Content: " + content.toString());
                return content.toString();

            }
        } else {
            System.out.println("Failed to authenticate. Response Code: " + responseCode);
        }

        // Close the connection
        connection.disconnect();
        return null;
    }

    @Override
    public String postCard(Card card) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        HttpEntity<Card> requestEntity = new HttpEntity<>(card, headers);

        String url = "http://localhost:8085/cards";

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        System.out.println(responseEntity.getBody());
        // Access the response object



        return responseEntity.getBody();//userResponse.getId();
    }

}
