package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.TypeDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class TypeServiceTest {

    @Autowired
    private TypeService typeService;

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
}
