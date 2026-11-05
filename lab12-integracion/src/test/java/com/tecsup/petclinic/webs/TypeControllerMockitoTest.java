package com.tecsup.petclinic.webs;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de TypeController con el servicio mockeado (Grupo 6 - Integrante A)
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
	 * @throws Exception
	 */
	@Test
	public void testFindAllTypes() throws Exception {

		int NRO_RECORD = 3;
		int ID_FIRST_RECORD = 1;

		List<TypeDTO> typeTOs = List.of(
				TypeDTO.builder().id(1).name("cat").description("Domestic feline").active(true).build(),
				TypeDTO.builder().id(2).name("dog").description("Domestic canine").active(true).build(),
				TypeDTO.builder().id(3).name("lizard").description("Reptile").active(true).build());

		Mockito.when(typeService.findAll())
				.thenReturn(typeTOs);

		this.mockMvc.perform(get("/types"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.size()", is(NRO_RECORD)))
				.andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testFindTypeOK() throws Exception {

		TypeDTO typeTO = TypeDTO.builder().id(1).name("cat").description("Domestic feline").active(true)
				.sizeCategory("small").averageLifespan(15).careLevel("medium").build();

		Mockito.when(typeService.findById(typeTO.getId()))
				.thenReturn(typeTO);

		mockMvc.perform(get("/types/1"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(typeTO.getId())))
				.andExpect(jsonPath("$.name", is(typeTO.getName())))
				.andExpect(jsonPath("$.description", is(typeTO.getDescription())))
				.andExpect(jsonPath("$.active", is(typeTO.getActive())));
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testFindTypeKO() throws Exception {

		Integer ID_NOT_EXIST = 666;

		Mockito.when(this.typeService.findById(ID_NOT_EXIST))
				.thenThrow(new TypeNotFoundException("Record not found...!"));

		mockMvc.perform(get("/types/666"))
				.andExpect(status().isNotFound());
	}

}
