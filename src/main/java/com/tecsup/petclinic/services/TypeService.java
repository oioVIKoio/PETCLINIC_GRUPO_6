package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.PetCountByTypeDTO;
import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;

import java.util.List;

public interface TypeService {

	/**
	 *
	 * @param typeDTO
	 * @return
	 */
	TypeDTO create(TypeDTO typeDTO);

	/**
	 *
	 * @param typeDTO
	 * @return
	 */
	TypeDTO update(TypeDTO typeDTO);

	/**
	 *
	 * @param id
	 * @throws TypeNotFoundException
	 */
	void delete(Integer id) throws TypeNotFoundException;

	/**
	 *
	 * @param id
	 * @return
	 */
	TypeDTO findById(Integer id) throws TypeNotFoundException;

	/**
	 *
	 * @return
	 */
	List<TypeDTO> findAll();

	/**
	 * Pet count report by type (only active types)
	 * @return
	 */
	List<PetCountByTypeDTO> getPetCountByType();
}
