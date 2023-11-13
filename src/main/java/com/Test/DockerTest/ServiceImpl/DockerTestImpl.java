package com.Test.DockerTest.ServiceImpl;

import com.Test.DockerTest.Model.Address;
import com.Test.DockerTest.Model.Card;
import com.Test.DockerTest.Model.User;
import com.Test.DockerTest.Model.UserResponse;
import com.Test.DockerTest.Service.DockerTestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.client.HttpServerErrorException;
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
    @Autowired
    private ObjectMapper objectMapper;


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

        String url = "http://edge-router/register";

        ResponseEntity<UserResponse> responseEntity = restTemplate.postForEntity(url, requestEntity, UserResponse.class);


        UserResponse userResponse = responseEntity.getBody();

        return userResponse.getId();
    }

    public String login(String userName,String password) throws IOException {
        String url = "http://edge-router/login";


        URL urlTest = new URL(url);


        HttpURLConnection connection = (HttpURLConnection) urlTest.openConnection();


        connection.setRequestMethod("GET");


        String credentials = userName + ":" + password;
        String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        connection.setRequestProperty("Authorization", "Basic " + base64Credentials);


        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);


        if (responseCode == HttpURLConnection.HTTP_OK) {

            Map<String, List<String>> headerFields = connection.getHeaderFields();
            List<String> cookies = headerFields.get("Set-Cookie");

            if (cookies != null) {
                for (String cookie : cookies) {

                    System.out.println("Cookie: " + cookie);

                }
            } else {
                System.out.println("No cookies in the response.");
            }


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


        connection.disconnect();
        return null;
    }

    @Override
    public String postCard(Card card) {

        String url = "http://edge-router/cards";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", "_TRAEFIK_BACKEND=http://front-end:8079; logged_in=iy6Zx28OuZ9OlWWkqw1u9ZakDYotYV5W; md.sid=s%3Aiy6Zx28OuZ9OlWWkqw1u9ZakDYotYV5W.%2BcAy7QqTlqOJKuB8%2F9iNHL88Hd2NLAMa0tpF3fDD20g");



        String jsonBody;
        try {
            jsonBody = objectMapper.writeValueAsString(card);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Address to JSON", e);
        }

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            System.out.println("Response: " + responseEntity.getBody());
            return responseEntity.getBody();
        } catch (HttpServerErrorException e) {
            System.err.println("Error Response: " + e.getResponseBodyAsString());
            throw new RuntimeException("Error during POST request", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected error during POST request", e);
        }
    }

    @Override
    public String postAddress(Address address) {
        String url = "http://edge-router/addresses";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", "_TRAEFIK_BACKEND=http://front-end:8079; logged_in=iy6Zx28OuZ9OlWWkqw1u9ZakDYotYV5W; md.sid=s%3Aiy6Zx28OuZ9OlWWkqw1u9ZakDYotYV5W.%2BcAy7QqTlqOJKuB8%2F9iNHL88Hd2NLAMa0tpF3fDD20g");



        String jsonBody;
        try {
            jsonBody = objectMapper.writeValueAsString(address);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Address to JSON", e);
        }

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            System.out.println("Response: " + responseEntity.getBody());
            return responseEntity.getBody();
        } catch (HttpServerErrorException e) {
            System.err.println("Error Response: " + e.getResponseBodyAsString());
            throw new RuntimeException("Error during POST request", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected error during POST request", e);
        }
    }

    @Override
    public String getAddress(String id) {

        String endpoint = "http://edge-router/addresses";


        try {

            URL url = new URL(endpoint + "?id=" + id);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");


            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            StringBuilder response = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                System.out.println("Response Content: " + response.toString());



            }


            connection.disconnect();
            return response.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
