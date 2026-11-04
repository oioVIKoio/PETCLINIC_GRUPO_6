package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.TypeDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integracion de TypeController (Grupo 6 - Integrante B)
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class TypeControllerTest {

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	/**
	 * @throws Exception
	 */
	@Test
	public void testCreateType() throws Exception {

		String TYPE_NAME = "ferret";
		String DESCRIPTION = "Small mustelid";
		Boolean ACTIVE = true;

		TypeDTO newTypeTO = TypeDTO.builder()
				.name(TYPE_NAME)
				.description(DESCRIPTION)
				.active(ACTIVE)
				.build();

		mockMvc.perform(post("/types")
						.content(om.writeValueAsString(newTypeTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.name", is(TYPE_NAME)))
				.andExpect(jsonPath("$.description", is(DESCRIPTION)))
				.andExpect(jsonPath("$.active", is(ACTIVE)));
	}

}
