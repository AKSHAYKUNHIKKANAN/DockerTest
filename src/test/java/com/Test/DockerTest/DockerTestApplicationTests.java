package com.Test.DockerTest;

import com.Test.DockerTest.Controller.DockerTestController;
import com.Test.DockerTest.Model.Address;
import com.Test.DockerTest.Model.Card;
import com.Test.DockerTest.ServiceImpl.DockerTestImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
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




		String registrationResponse = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8009/register")
						.param("userName", "kvtest")
						.param("password", "testPassword")
						.param("email", "kvtest@example.com"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();


		mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8009/login")
						.param("userName", "kvtest")
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
		card.setUserID("65518ecfee11cb0001b25970");


		mockMvc.perform(post("/postCards")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(card)))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}
	@Test
	void testPostCardA() throws Exception {
		// Create a sample Card object
		Card card = new Card();
		card.setLongNum("1234567890123456");
		card.setExpires("12/25");
		card.setCcv("123");
		card.setUserID("65518ecfee11cb0001b25970");


		mockMvc.perform(post("/postCards")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(card)))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}
	@Test
	void testAddress() throws Exception {

		Address address = new Address();
		address.setStreet("Sample Street");
		address.setNumber("123");
		address.setCountry("Sample Country");
		address.setCity("Sample City");
		address.setPostcode("12345");
		address.setUserid("65518ecfee11cb0001b25970");


		ResultActions postResult =  mockMvc.perform(post("/address")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(address)))
				.andExpect(MockMvcResultMatchers.status().isOk());

		String id = jsonParser(postResult.andReturn().getResponse().getContentAsString());
		System.out.println(id);


		mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8009/getAddress")
						.param("id", id))
				.andExpect(MockMvcResultMatchers.status().isOk());


	}

	public String jsonParser(String jsonString) {

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
}