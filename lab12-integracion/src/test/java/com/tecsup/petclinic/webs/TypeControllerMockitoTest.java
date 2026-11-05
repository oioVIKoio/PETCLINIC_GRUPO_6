package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.PetCountByTypeDTO;
import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.services.TypeService;
import com.tecsup.petclinic.util.TObjectCreator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integrante B (Grupo 6): creación y reporte, con TypeService mockeado
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class TypeControllerMockitoTest {

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private TypeService typeService;

	@Test
	public void testCreateType() throws Exception {

		TypeDTO newTypeTO = TObjectCreator.getNewTypeTO();

		Mockito.when(typeService.create(newTypeTO))
				.thenReturn(newTypeTO);

		mockMvc.perform(post("/types")
				.content(om.writeValueAsString(newTypeTO))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name", is(newTypeTO.getName())))
				.andExpect(jsonPath("$.active", is(newTypeTO.getActive())))
				.andExpect(jsonPath("$.sizeCategory", is(newTypeTO.getSizeCategory())));
	}

	@Test
	public void testGetPetCountByType() throws Exception {

		List<PetCountByTypeDTO> countTOs = TObjectCreator.getPetCountTOs();

		Mockito.when(typeService.getPetCountByType())
				.thenReturn(countTOs);

		mockMvc.perform(get("/types/report/pet-count"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.size()", is(countTOs.size())))
				.andExpect(jsonPath("$[0].typeName", is("cat")))
				.andExpect(jsonPath("$[0].petCount", is(4)));
	}

	@Test
	public void testDeleteType() throws Exception {

		TypeDTO newTypeTO = TObjectCreator.getNewTypeTO();

		Mockito.when(typeService.create(newTypeTO))
				.thenReturn(newTypeTO);

		ResultActions mvcActions = mockMvc.perform(post("/types")
				.content(om.writeValueAsString(newTypeTO))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");

		Mockito.doNothing().when(typeService).delete(id);

		mockMvc.perform(delete("/types/" + id))
				.andExpect(status().isOk());

		Mockito.verify(typeService).delete(id);
	}

}
