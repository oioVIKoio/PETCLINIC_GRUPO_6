package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.PetCountByTypeDTO;
import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.services.TypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST de tipos de mascota (Grupo 6)
 */
@RestController
@Slf4j
public class TypeController {

	private TypeService typeService;

	public TypeController(TypeService typeService) {
		this.typeService = typeService;
	}

	/**
	 * Get all types (optional filter by active)
	 */
	@GetMapping(value = "/types")
	public ResponseEntity<List<TypeDTO>> findAllTypes(@RequestParam(required = false) Boolean active) {

		List<TypeDTO> typesTO = (active == null)
				? typeService.findAll()
				: typeService.findByActive(active);

		return ResponseEntity.ok(typesTO);
	}

	/**
	 * Find type by id
	 */
	@GetMapping(value = "/types/{id}")
	ResponseEntity<TypeDTO> findById(@PathVariable Integer id) {

		try {
			return ResponseEntity.ok(typeService.findById(id));
		} catch (TypeNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Create type
	 */
	@PostMapping(value = "/types")
	ResponseEntity<TypeDTO> create(@RequestBody TypeDTO typeTO) {

		TypeDTO newTypeTO = typeService.create(typeTO);

		return ResponseEntity.status(HttpStatus.CREATED).body(newTypeTO);
	}

	/**
	 * Delete type by id
	 */
	@DeleteMapping(value = "/types/{id}")
	ResponseEntity<String> delete(@PathVariable Integer id) {

		try {
			typeService.delete(id);
			return ResponseEntity.ok(" Delete ID :" + id);
		} catch (TypeNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Pet count report by type (only active types)
	 */
	@GetMapping(value = "/types/report/pet-count")
	ResponseEntity<List<PetCountByTypeDTO>> getPetCountByType() {

		return ResponseEntity.ok(typeService.getPetCountByType());
	}

}
