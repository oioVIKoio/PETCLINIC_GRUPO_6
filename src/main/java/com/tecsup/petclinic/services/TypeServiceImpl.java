package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.PetCountByTypeDTO;
import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.entities.Type;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.mappers.TypeMapper;
import com.tecsup.petclinic.repositories.TypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TypeServiceImpl implements TypeService {

	private final TypeRepository typeRepository;
	private final TypeMapper typeMapper;

	/**
	 *
	 * @param typeDTO
	 * @return
	 */
	@Override
	public TypeDTO create(TypeDTO typeDTO) {

		Type newType = typeRepository.save(typeMapper.mapToEntity(typeDTO));

		return typeMapper.mapToDto(newType);
	}

	/**
	 *
	 * @param typeDTO
	 * @return
	 */
	@Override
	public TypeDTO update(TypeDTO typeDTO) {

		Type newType = typeRepository.save(typeMapper.mapToEntity(typeDTO));

		return typeMapper.mapToDto(newType);
	}

	/**
	 *
	 * @param id
	 * @throws TypeNotFoundException
	 */
	@Override
	public void delete(Integer id) throws TypeNotFoundException {

		TypeDTO type = findById(id);

		typeRepository.delete(this.typeMapper.mapToEntity(type));
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	@Override
	public TypeDTO findById(Integer id) throws TypeNotFoundException {

		Optional<Type> type = typeRepository.findById(id);

		if ( !type.isPresent())
			throw new TypeNotFoundException("Record not found...!");

		return this.typeMapper.mapToDto(type.get());
	}

	/**
	 *
	 * @return
	 */
	@Override
	public List<TypeDTO> findAll() {

		return typeRepository.findAll()
				.stream()
				.map(this.typeMapper::mapToDto)
				.collect(Collectors.toList());
	}

	/**
	 *
	 * @return
	 */
	@Override
	public List<PetCountByTypeDTO> getPetCountByType() {

		List<PetCountByTypeDTO> report = typeRepository.countPetsByType();

		report.forEach(item -> log.info("{}", item));

		return report;
	}
}
