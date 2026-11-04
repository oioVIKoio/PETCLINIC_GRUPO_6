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

    /**
     *
     */
    @Test
    public void testFindTypeById() {

        final String NAME_EXPECTED = "cat";

        Integer ID = 1;

        TypeDTO type = null;

        try {
            type = this.typeService.findById(ID);
        } catch (TypeNotFoundException e) {
            fail(e.getMessage());
        }
        assertEquals(NAME_EXPECTED, type.getName());
    }

    /**
     *
     */
    @Test
    public void testCreateType() {

        String TYPE_NAME = "ferret";
        String DESCRIPTION = "Small mustelid";
        Boolean ACTIVE = true;
        String SIZE_CATEGORY = "small";
        int AVERAGE_LIFESPAN = 7;
        String CARE_LEVEL = "high";

        TypeDTO typeDTO = TypeDTO.builder()
                .name(TYPE_NAME)
                .description(DESCRIPTION)
                .active(ACTIVE)
                .sizeCategory(SIZE_CATEGORY)
                .averageLifespan(AVERAGE_LIFESPAN)
                .careLevel(CARE_LEVEL)
                .build();

        TypeDTO newTypeDTO = this.typeService.create(typeDTO);

        log.info("TYPE CREATED :" + newTypeDTO.toString());

        assertNotNull(newTypeDTO.getId());
        assertEquals(TYPE_NAME, newTypeDTO.getName());
        assertEquals(DESCRIPTION, newTypeDTO.getDescription());
        assertEquals(ACTIVE, newTypeDTO.getActive());
        assertEquals(SIZE_CATEGORY, newTypeDTO.getSizeCategory());
        assertEquals(AVERAGE_LIFESPAN, newTypeDTO.getAverageLifespan());
        assertEquals(CARE_LEVEL, newTypeDTO.getCareLevel());

    }

    /**
     *
     */
    @Test
    public void testUpdateType() {

        String TYPE_NAME = "parrot";
        String DESCRIPTION = "Tropical bird";
        Boolean ACTIVE = true;
        String SIZE_CATEGORY = "small";
        int AVERAGE_LIFESPAN = 40;
        String CARE_LEVEL = "medium";

        String UP_TYPE_NAME = "parrot2";
        String UP_DESCRIPTION = "Large tropical bird";
        Boolean UP_ACTIVE = false;
        String UP_SIZE_CATEGORY = "medium";
        int UP_AVERAGE_LIFESPAN = 50;
        String UP_CARE_LEVEL = "high";

        TypeDTO typeDTO = TypeDTO.builder()
                .name(TYPE_NAME)
                .description(DESCRIPTION)
                .active(ACTIVE)
                .sizeCategory(SIZE_CATEGORY)
                .averageLifespan(AVERAGE_LIFESPAN)
                .careLevel(CARE_LEVEL)
                .build();

        // ------------ Create ---------------

        log.info(">" + typeDTO);
        TypeDTO typeDTOCreated = this.typeService.create(typeDTO);
        log.info(">>" + typeDTOCreated);

        // ------------ Update ---------------

        // Prepare data for update
        typeDTOCreated.setName(UP_TYPE_NAME);
        typeDTOCreated.setDescription(UP_DESCRIPTION);
        typeDTOCreated.setActive(UP_ACTIVE);
        typeDTOCreated.setSizeCategory(UP_SIZE_CATEGORY);
        typeDTOCreated.setAverageLifespan(UP_AVERAGE_LIFESPAN);
        typeDTOCreated.setCareLevel(UP_CARE_LEVEL);

        // Execute update
        TypeDTO upgradeTypeDTO = this.typeService.update(typeDTOCreated);
        log.info(">>>>" + upgradeTypeDTO);

        //            EXPECTED        ACTUAL
        assertEquals(typeDTOCreated.getId(), upgradeTypeDTO.getId());
        assertEquals(UP_TYPE_NAME, upgradeTypeDTO.getName());
        assertEquals(UP_DESCRIPTION, upgradeTypeDTO.getDescription());
        assertEquals(UP_ACTIVE, upgradeTypeDTO.getActive());
        assertEquals(UP_SIZE_CATEGORY, upgradeTypeDTO.getSizeCategory());
        assertEquals(UP_AVERAGE_LIFESPAN, upgradeTypeDTO.getAverageLifespan());
        assertEquals(UP_CARE_LEVEL, upgradeTypeDTO.getCareLevel());
    }
}
