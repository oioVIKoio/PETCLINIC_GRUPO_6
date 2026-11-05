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

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integracion de TypeController (Grupo 6 - Integrante C: filtros y errores)
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class TypeControllerTest {

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	/**
	 * GET /types?active=true -> solo tipos activos (excluye snake).
	 * No se fija el total exacto porque otras pruebas pueden crear tipos activos.
	 *
	 * @throws Exception
	 */
	@Test
	public void testFindActiveTypes() throws Exception {

		mockMvc.perform(get("/types").param("active", "true"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// los 7 tipos activos de data.sql estan presentes
				.andExpect(jsonPath("$[*].name",
						hasItems("cat", "dog", "lizard", "bird", "hamster", "rabbit", "turtle")))
				// snake (inactivo) no aparece
				.andExpect(jsonPath("$[*].name", not(hasItem("snake"))))
				// ningun elemento inactivo
				.andExpect(jsonPath("$[?(@.active == false)]", hasSize(0)));
	}

	/**
	 * El reporte excluye tipos con active = false.
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetPetCountByType_ExcludeInactive() throws Exception {

		String INACTIVE_SEED = "snake";       // inactivo en data.sql
		String INACTIVE_NEW = "axolotl";      // se crea inactivo en la prueba

		// ------------ Create an inactive type ---------------

		TypeDTO inactiveTO = TypeDTO.builder()
				.name(INACTIVE_NEW)
				.description("Aquatic salamander")
				.active(false)
				.build();

		mockMvc.perform(post("/types")
						.content(om.writeValueAsString(inactiveTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		// ------------ Report ---------------

		mockMvc.perform(get("/types/report/pet-count"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[*].typeName", hasItem("cat")))
				.andExpect(jsonPath("$[*].typeName", not(hasItem(INACTIVE_SEED))))
				.andExpect(jsonPath("$[*].typeName", not(hasItem(INACTIVE_NEW))));
	}

	/**
	 * DELETE de un id inexistente -> 404.
	 *
	 * @throws Exception
	 */
	@Test
	public void testDeleteTypeKO() throws Exception {

		int ID_NOT_EXIST = 1000;

		mockMvc.perform(delete("/types/" + ID_NOT_EXIST))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

}
