package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.dtos.PetCountByTypeDTO;
import com.tecsup.petclinic.entities.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeRepository
	extends JpaRepository<Type, Integer> {

	// Count pets by type (only active types)
	@Query("SELECT new com.tecsup.petclinic.dtos.PetCountByTypeDTO(t.id, t.name, COUNT(p.id)) "
			+ "FROM types t LEFT JOIN pets p ON p.typeId = t.id "
			+ "WHERE t.active = true "
			+ "GROUP BY t.id, t.name ORDER BY t.id")
	List<PetCountByTypeDTO> countPetsByType();

	@Override
	List<Type> findAll();

}
