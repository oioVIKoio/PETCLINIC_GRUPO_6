package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.PetCountByTypeDTO;
import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.entities.Pet;
import com.tecsup.petclinic.exceptions.PetNotFoundException;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class TypeServiceTest {

    @Autowired
    private TypeService typeService ;

    @Autowired
    private PetService petService ;

    /**
     *
     */
    @Test
    public void testGetPetCountByType() {

        int TYPE_ID = 5;          // hamster
        long COUNT_EXPECTED = 2;

        List<PetCountByTypeDTO> report = this.typeService.getPetCountByType();

        PetCountByTypeDTO hamster = report.stream()
                .filter(item -> item.getTypeId() == TYPE_ID)
                .findFirst()
                .orElse(null);

        assertNotNull(hamster);
        assertEquals(COUNT_EXPECTED, hamster.getPetCount());
    }

    /**
     *
     */
    @Test
    @Transactional
    public void testGetPetCountByType_Empty() {

        int SIZE_EXPECTED = 0;

        // ------------ Delete pets and types (rollback at the end) ---------------

        try {
            for (Pet pet : this.petService.findAll())
                this.petService.delete(pet.getId());

            for (TypeDTO type : this.typeService.findAll())
                this.typeService.delete(type.getId());
        } catch (PetNotFoundException | TypeNotFoundException e) {
            fail(e.getMessage());
        }

        // ------------ Validation ---------------

        List<PetCountByTypeDTO> report = this.typeService.getPetCountByType();

        assertEquals(SIZE_EXPECTED, report.size());
    }

    /**
     *
     */
    @Test
    public void testDeleteType() {

        String TYPE_NAME = "ferret";
        String DESCRIPTION = "Small mustelid";

        // ------------ Create ---------------

        TypeDTO typeDTO = TypeDTO.builder()
                .name(TYPE_NAME)
                .description(DESCRIPTION)
                .active(true)
                .build();

        TypeDTO newTypeDTO = this.typeService.create(typeDTO);
        log.info("" + newTypeDTO);

        // ------------ Delete ---------------

        try {
            this.typeService.delete(newTypeDTO.getId());
        } catch (TypeNotFoundException e) {
            fail(e.getMessage());
        }

        // ------------ Validation ---------------

        try {
            this.typeService.findById(newTypeDTO.getId());
            assertTrue(false);
        } catch (TypeNotFoundException e) {
            assertTrue(true);
        }
    }
}
