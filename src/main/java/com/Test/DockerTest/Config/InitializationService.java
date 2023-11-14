package com.Test.DockerTest.Config;

import com.Test.DockerTest.Model.Address;
import com.Test.DockerTest.Model.Card;
import com.Test.DockerTest.Service.DockerTestService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class InitializationService {

    @Autowired
    private DockerTestService dockerTestService;

    @PostConstruct
    // 60 seconds delay
    public void init() {
        try {
            System.out.println("Inside the test");
            String urlToCheck = "http://edge-router/catalogue";

            if (isServiceUp(urlToCheck)) {
                printColoredMessage("Service is up!", ConsoleColor.GREEN);
            } else {
                printColoredMessage("Service is down!", ConsoleColor.RED);
            }

            String randomUsername = RandomStringUtils.randomAlphanumeric(8);
            String randomPassword = RandomStringUtils.randomAlphanumeric(12);
            String randomString = RandomStringUtils.randomAlphabetic(3);
            String randomEmail = "kvt" + randomString + "@gmail.com";
            String response = null;

            // Invoke the login method

            // Invoke the register method
            try {
                response = dockerTestService.register("springDockerUser", "SpringDockerPassword", "SpringDocker@gmail.com");
                printColoredMessage("Test Registration Success: " , ConsoleColor.GREEN);
            } catch (Exception e) {
                printColoredMessage("Registration failed: " + e.getMessage(), ConsoleColor.RED);
            }
            if (response != null) {
                try {
                    String re = dockerTestService.login("springDockerUser", "SpringDockerPassword");
                    if(re.contains("Cookie is set")){
                        printColoredMessage("Test login Success: " , ConsoleColor.GREEN);
                    }

                } catch (Exception e) {
                    printColoredMessage("Login failed: " + e.getMessage(), ConsoleColor.RED);
                }
            } else {
                printColoredMessage("Registration failed ", ConsoleColor.RED);
            }

            // Create a Card object and invoke postCard method
            try {
                Card card = new Card();
                card.setLongNum("1234567890123456");
                card.setExpires("12/25");
                card.setCcv("123");
                card.setUserID("65518ecfee11cb0001b25970");
                dockerTestService.postCard(card);
                printColoredMessage("Post card succeeded", ConsoleColor.GREEN);
            } catch (Exception e) {
                printColoredMessage("Post card failed: " + e.getMessage(), ConsoleColor.RED);
            }

            String result = null;
            try {
                // Set address fields
                Address address = new Address();
                address.setStreet("Sample Street123");
                address.setNumber("12345");
                address.setCountry("Sample Country12");
                address.setCity("Sample City");
                address.setPostcode("12345");
                address.setUserid("655327a3ee11cb00019db9a6");

                result = dockerTestService.postAddress(address);
                if(result.contains("id")){
                    printColoredMessage("Post Address succeeded", ConsoleColor.GREEN);
                }

            } catch (Exception e) {
                printColoredMessage("Post address failed: " + e.getMessage(), ConsoleColor.RED);
            }
            if (result != null) {
                try {
                    dockerTestService.getAddress(jsonParser(result));
                    printColoredMessage("Get address succeeded", ConsoleColor.GREEN);
                } catch (Exception e) {
                    printColoredMessage("Get address failed: " + e.getMessage(), ConsoleColor.RED);
                }
            } else {
                printColoredMessage("Result of post address is null", ConsoleColor.RED);
            }
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
        }
    }

    private void printColoredMessage(String message, ConsoleColor color) {
        System.out.println(color.getCode() + message + ConsoleColor.RESET.getCode());
    }

    private enum ConsoleColor {
        RESET("\u001B[0m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m");

        private final String code;

        ConsoleColor(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    private String jsonParser(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            // Extract the value associated with the "id" key
            String idValue = jsonNode.get("id").asText();

            System.out.println("Value associated with 'id': " + idValue);
            return idValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isServiceUp(String urlToCheck) {
        try {
            URL url = new URL(urlToCheck);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            // Handle exceptions, e.g., connection errors
            e.printStackTrace();
            return false;
        }
    }

    // Additional methods...
}
