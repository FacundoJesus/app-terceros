package com.project.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.project.models.enums.SituacionIVA;
import com.project.models.enums.TipoSaldo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name="terceros")
public class Tercero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_tercero")
    private Long id;

    @NotBlank(message="El nombre del Tercero es obligatorio")
    @Column(name="nombre")
    private String nombre;
    
    @NotBlank(message="El CUIT del Tercero es obligatorio")
    @Column(name="cuitl")
    private String cuitl;

    @NotNull(message="La situación IVA es obligatoria")
    @Column(name="sitiva")
    @Enumerated(EnumType.STRING)
    private SituacionIVA sitiva;

    @NotBlank(message="La dirección del Tercero es obligatoria")
    @Column(name="direccion")
    private String direccion;

    @Column(name="localidad")
    private String localidad;

    @Column(name="provincia")
    private String provincia;

    @Column(name="telefonos")
    private String telefonos;

    @Column(name="saldo_apertura")
    private BigDecimal saldoApertura;
   
    @Enumerated(EnumType.STRING)
    @Column(name="tipo_saldo")
    private TipoSaldo tipoSaldo;
    

}