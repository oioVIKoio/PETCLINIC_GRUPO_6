package com.tecsup.petclinic.webs;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integracion de TypeController (Grupo 6 - Integrante A)
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class TypeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * @throws Exception
	 */
	@Test
	public void testFindAllTypes() throws Exception {

		final int ID_FIRST_RECORD = 1;
		final String NAME_FIRST_RECORD = "cat";

		this.mockMvc.perform(get("/types"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)))
				.andExpect(jsonPath("$[0].name", is(NAME_FIRST_RECORD)));
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testFindTypeOK() throws Exception {

		String TYPE_NAME = "cat";
		String DESCRIPTION = "Domestic feline";
		Boolean ACTIVE = true;
		String SIZE_CATEGORY = "small";
		int AVERAGE_LIFESPAN = 15;
		String CARE_LEVEL = "medium";

		this.mockMvc.perform(get("/types/1"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is(TYPE_NAME)))
				.andExpect(jsonPath("$.description", is(DESCRIPTION)))
				.andExpect(jsonPath("$.active", is(ACTIVE)))
				.andExpect(jsonPath("$.sizeCategory", is(SIZE_CATEGORY)))
				.andExpect(jsonPath("$.averageLifespan", is(AVERAGE_LIFESPAN)))
				.andExpect(jsonPath("$.careLevel", is(CARE_LEVEL)));
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testFindTypeKO() throws Exception {

		mockMvc.perform(get("/types/666"))
				.andExpect(status().isNotFound());

	}

}
