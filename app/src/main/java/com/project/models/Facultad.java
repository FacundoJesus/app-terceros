package com.project.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name="facultad")
public class Facultad {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_facultad")
	private Long id;
	
	@NotBlank(message="El nombre de la Facultad es obligatorio")
	@Column(name="nombre")
	private String nombre;
	
	@NotBlank(message="La dirección de la Facultad es obligatoria")
	@Column(name="direccion")
	private String direccion;
	
	@NotBlank(message="El CUIT de la Facultad es obligatorio")
	@Column(name="cuit")
	private String cuit;
	
	@NotNull(message = "La sucursal de la Facultad es obligatoria")
	@Column(name = "sucursal")
	private Integer sucursal;
	
	@Column(name="telefonos")
	private String telefonos;
	
	@Column(name="email")
	private String correos;
	

	@Column(name = "defecto")
	private Boolean defecto;
	

}

