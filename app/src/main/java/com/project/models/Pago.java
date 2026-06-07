package com.project.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.project.models.enums.ModoPago;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="pagos")
public class Pago {
	
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_pagos")
	private Long id;
	
	@NotNull(message="La fecha del pago es obligatoria")
	@Column(name="fecha_pago", nullable = false)
	private LocalDate fechaPago;
	
	@NotNull(message="El monto del pago es obligatorio")
	@Column(name="monto_pago", nullable = false, scale = 2, precision = 12)
	private BigDecimal montoPago;
	
	@NotNull(message = "El modo de pago es obligatorio")
	@Enumerated(EnumType.STRING)
	@Column(name="modo_pago", nullable = false)
	private ModoPago modoPago;
	
	// RELACION CON PROVEEDOR-TERCERO
	@ManyToOne(fetch = FetchType.LAZY)
	@ToString.Exclude
	@JoinColumn(
	        name = "id_tercero",
	        nullable = false
	    )
	private Tercero tercero;
	
	// RELACION PAGOS_DETALLES
	@OneToMany(
		    mappedBy = "pago",
		    cascade = CascadeType.ALL,
		    orphanRemoval = true
		)
	private List<PagoDetalle> pagosDetalles = new ArrayList<>();

}
