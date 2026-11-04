package com.tecsup.petclinic.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetCountByTypeDTO {

    private Integer typeId;
    private String typeName;
    private Long petCount;
}
