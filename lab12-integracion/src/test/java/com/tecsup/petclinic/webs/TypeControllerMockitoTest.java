package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.PetCountByTypeDTO;
import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.services.TypeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de TypeController con el servicio mockeado (Grupo 6 - Integrante C)
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class TypeControllerMockitoTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private TypeService typeService;

	/**
	 * GET /types?active=true -> 7 tipos activos (sin snake).
	 *
	 * @throws Exception
	 */
	@Test
	public void testFindActiveTypes() throws Exception {

		int NRO_RECORD = 7;

		List<TypeDTO> activeTypes = List.of(
				TypeDTO.builder().id(1).name("cat").description("Domestic feline").active(true).build(),
				TypeDTO.builder().id(2).name("dog").description("Domestic canine").active(true).build(),
				TypeDTO.builder().id(3).name("lizard").description("Reptile").active(true).build(),
				TypeDTO.builder().id(4).name("bird").description("Avian").active(true).build(),
				TypeDTO.builder().id(5).name("hamster").description("Small rodent").active(true).build(),
				TypeDTO.builder().id(7).name("rabbit").description("Domestic rabbit").active(true).build(),
				TypeDTO.builder().id(8).name("turtle").description("Turtle or tortoise").active(true).build());

		Mockito.when(typeService.findByActive(true)).thenReturn(activeTypes);

		mockMvc.perform(get("/types").param("active", "true"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(NRO_RECORD)))
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[*].name", not(hasItem("snake"))))
				.andExpect(jsonPath("$[?(@.active == false)]", hasSize(0)));

		Mockito.verify(typeService).findByActive(true);
	}

	/**
	 * El reporte devuelto por el servicio no trae tipos inactivos.
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetPetCountByType_ExcludeInactive() throws Exception {

		List<PetCountByTypeDTO> report = List.of(
				new PetCountByTypeDTO(1, "cat", 4L),
				new PetCountByTypeDTO(2, "dog", 4L),
				new PetCountByTypeDTO(3, "lizard", 1L),
				new PetCountByTypeDTO(4, "bird", 1L),
				new PetCountByTypeDTO(5, "hamster", 2L),
				new PetCountByTypeDTO(7, "rabbit", 0L),
				new PetCountByTypeDTO(8, "turtle", 0L));

		Mockito.when(typeService.getPetCountByType()).thenReturn(report);

		mockMvc.perform(get("/types/report/pet-count"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(7)))
				.andExpect(jsonPath("$[*].typeName", hasItem("cat")))
				.andExpect(jsonPath("$[*].typeName", not(hasItem("snake"))));
	}

	/**
	 * DELETE /types/1000 -> el servicio lanza TypeNotFoundException -> 404.
	 *
	 * @throws Exception
	 */
	@Test
	public void testDeleteTypeKO() throws Exception {

		Integer ID_NOT_EXIST = 1000;

		Mockito.doThrow(new TypeNotFoundException("Record not found...!"))
				.when(typeService).delete(ID_NOT_EXIST);

		mockMvc.perform(delete("/types/" + ID_NOT_EXIST))
				.andDo(print())
				.andExpect(status().isNotFound());

		Mockito.verify(typeService).delete(ID_NOT_EXIST);
	}

}
