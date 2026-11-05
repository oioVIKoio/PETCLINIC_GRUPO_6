package com.tecsup.petclinic.util;

import com.tecsup.petclinic.dtos.PetCountByTypeDTO;
import com.tecsup.petclinic.dtos.TypeDTO;

import java.util.List;

public class TObjectCreator {

	public static TypeDTO getTypeTO() {
		return TypeDTO.builder().id(1).name("cat").description("Domestic feline").active(true)
				.sizeCategory("small").averageLifespan(15).careLevel("medium").build();
	}

	public static TypeDTO getNewTypeTO() {
		return TypeDTO.builder().id(100).name("ferret").description("Small mustelid").active(true)
				.sizeCategory("small").averageLifespan(8).careLevel("medium").build();
	}

	public static List<PetCountByTypeDTO> getPetCountTOs() {
		return List.of(
				new PetCountByTypeDTO(1, "cat", 4L),
				new PetCountByTypeDTO(2, "dog", 4L),
				new PetCountByTypeDTO(3, "lizard", 1L));
	}
}
