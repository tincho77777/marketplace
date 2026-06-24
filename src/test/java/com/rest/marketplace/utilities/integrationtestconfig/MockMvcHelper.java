package com.rest.marketplace.utilities.integrationtestconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class MockMvcHelper {

	private MockMvcHelper() {
	}

	public static MockHttpServletResponse get(MockMvc mockMvc, String path) {
		try {
			return mockMvc.perform(
					MockMvcRequestBuilders
							.get(path)
							.contentType(MediaType.APPLICATION_JSON)
			).andReturn().getResponse();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static MockHttpServletResponse post(MockMvc mockMvc, ObjectMapper objectMapper, String path, Object body) {
		try {
			return mockMvc.perform(
					MockMvcRequestBuilders
							.post(path)
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(body))
			).andReturn().getResponse();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static MockHttpServletResponse put(MockMvc mockMvc, ObjectMapper objectMapper, String path, Object body) {
		try {
			return mockMvc.perform(
					MockMvcRequestBuilders
							.put(path)
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(body))
			).andReturn().getResponse();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static MockHttpServletResponse delete(MockMvc mockMvc, String path) {
		try {
			return mockMvc.perform(
					MockMvcRequestBuilders
							.delete(path)
							.contentType(MediaType.APPLICATION_JSON)
			).andReturn().getResponse();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
