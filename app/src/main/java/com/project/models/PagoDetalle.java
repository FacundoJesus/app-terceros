package com.project.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "pagos_detalle")
public class PagoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_pagosdetalle")
    private Long id;

    @NotBlank(message="N° Instrumento obligatorio")
    @Column(name = "instrumentnumber", nullable = false, length = 15)
    private String instrumentNumber;

    @NotNull(message="Fecha Instrumento obligatorio")
    @Column(name = "instrumentdate", nullable = false)
    private LocalDate instrumentDate;

    @NotBlank(message="El Banco es obligatorio")
    @Column(name = "banco", length = 100, nullable=false)
    private String banco;

    @NotNull(message="Pago Realizado. SI o NO")
    @Column(name = "pagorealizado",nullable=false)
    private Boolean pagoRealizado = false;

    //RELACION CON PAGO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
    		name = "id_pagos",
    		nullable = false)
    private Pago pago;
}
