package com.project.models;

import com.project.models.enums.RolUsuario;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="usuarios")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name= "id_usuario")
	private Long id;
	
	@NotBlank(message = "El nombre de usuario es obligatorio")
	@Size(max=20)
	@Column(name= "nombre_usuario", nullable=false, unique = true, length=20)
	private String nombreUsuario;
	
	@NotBlank(message = "El password es obligatorio")
	@Size(max=60)
	@Column(name= "password_usuario", nullable=false, length=60)
	private String password;
	
	@NotNull(message = "El Rol es obligatorio")
	@Enumerated(EnumType.STRING) 
	@Column(name= "rol_usuario", nullable=false)
	private RolUsuario rolUsuario;

}
