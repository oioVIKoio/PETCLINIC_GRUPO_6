package com.tecsup.petclinic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "types")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Type {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String description;
	private Boolean active;
	@Column(name = "size_category")
	private String sizeCategory;
	@Column(name = "average_lifespan")
	private Integer averageLifespan;
	@Column(name = "care_level")
	private String careLevel;

}
