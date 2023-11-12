package com.Test.DockerTest;

import com.Test.DockerTest.Controller.DockerTestController;
import com.Test.DockerTest.Model.Card;
import com.Test.DockerTest.ServiceImpl.DockerTestImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class DockerTestApplicationTests {



	@Autowired
	private DockerTestController dockerTestController;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;
	@Test
	void contextLoads() {

	}


	@Test
	void registerAndLogin() throws Exception {
		// Delay for 60 seconds


		// Perform registration
		String registrationResponse = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8009/register")
						.param("userName", "raytest")
						.param("password", "testPassword")
						.param("email", "gamatest@example.com"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		// Perform login using the ID from registration
		mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8009/login")
						.param("userName", "raytest")
						.param("password", "testPassword"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Cookie is set"));

	}
	@Test
	void testPostCardEndpoint() throws Exception {
		// Create a sample Card object
		Card card = new Card();
		card.setLongNum("1234567890123456");
		card.setExpires("12/25");
		card.setCcv("123");
		card.setUserID("user123567");

		// Perform the POST request and validate the response
		mockMvc.perform(post("/postCards")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(card)))
				.andExpect(MockMvcResultMatchers.status().isOk());
		// Add more assertions based on the expected behavior and response
	}
}