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
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "pagos_detalle")
public class PagoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_pagosdetalle")
    private Long id;

    @NotBlank
    @Column(name = "instrumentnumber", nullable = false, length = 15)
    private String instrumentNumber;

    @NotNull
    @Column(name = "instrumentdate", nullable = false)
    private LocalDate instrumentDate;

    @Column(name = "banco", length = 100)
    private String banco;

    @Column(name = "pagorealizado")
    private Boolean pagoRealizado = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pagos", nullable = false)
    @ToString.Exclude
    private Pago pago;
}
