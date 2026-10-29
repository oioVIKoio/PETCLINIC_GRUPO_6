package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.PetCountByTypeDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class TypeServiceTest {

    @Autowired
    private TypeService typeService ;

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
}
