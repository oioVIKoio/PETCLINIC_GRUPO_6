package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.TypeDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
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

	/**
	 * @throws Exception
	 */
	@Test
	public void testGetPetCountByType() throws Exception {

		String TYPE_NAME = "cat";      // con los datos semilla: 4 mascotas
		int TYPE_ID = 1;

		// ------------ Expected: cats registered in /pets ---------------

		String pets = mockMvc.perform(get("/pets"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		List<Integer> catPets = JsonPath.parse(pets).read("$[?(@.typeId == " + TYPE_ID + ")].id");
		int PET_COUNT_EXPECTED = catPets.size();

		// ------------ Report ---------------

		mockMvc.perform(get("/types/report/pet-count"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[*].typeName", hasItem(TYPE_NAME)))
				.andExpect(jsonPath("$[?(@.typeName == 'cat')].petCount", hasItem(PET_COUNT_EXPECTED)));
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testDeleteType() throws Exception {

		String TYPE_NAME = "chinchilla";
		String DESCRIPTION = "Small rodent";

		TypeDTO newTypeTO = TypeDTO.builder()
				.name(TYPE_NAME)
				.description(DESCRIPTION)
				.active(true)
				.build();

		// ------------ Create ---------------

		ResultActions mvcActions = mockMvc.perform(post("/types")
						.content(om.writeValueAsString(newTypeTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();

		Integer id = JsonPath.parse(response).read("$.id");

		// ------------ Delete ---------------

		mockMvc.perform(delete("/types/" + id))
				.andExpect(status().isOk());

		// ------------ Validation ---------------

		mockMvc.perform(get("/types/" + id))
				.andExpect(status().isNotFound());
	}

}
