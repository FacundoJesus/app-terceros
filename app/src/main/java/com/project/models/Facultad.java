package com.project.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="facultad")
public class Facultad {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Column(name= "id_facultad")
	private Long id;
	
	@NotBlank(message = "El nombre de la Facultad es obligatorio")
	@Size(max=70, message = "El nombre de la Facultad no debe superar los 70 caracteres")
	@Column(name="nombre", nullable = false)
	private String nombre;
	
	@NotBlank(message="La dirección de la Facultad es obligatoria")
	@Size(max=100, message="La dirección de la Facultad no debe superar los 100 caracteres")
	@Column(name="direccion", nullable = false)
	private String direccion;
	
	@NotBlank(message="El CUIT de la Facultad es obligatorio")
	@Size(max=15, message="El CUIT Facultad no debe superar los 15 caracteres")
	@Column(name="cuit", nullable = false)
	private String cuit;
	
	@NotNull(message = "La sucursal de la Facultad es obligatoria")
	@Column(name = "sucursal",nullable = false)
	private Integer sucursal;
	
	@Column(name="telefonos")
	@Size(max=120, message="Los teléfonos de la Facultad no debe superar los 120 caracteres")
	private String telefonos;
	
	@Column(name="email")
	@Email(message = "Email inválido")
	@Size(max=70, message="El email de la Facultad no debe superar los 70 caracteres")
	private String correos;
	

	@Column(name = "defecto")
	private Boolean defecto;
	
}

