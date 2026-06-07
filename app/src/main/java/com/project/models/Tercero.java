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
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="terceros")
public class Tercero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name="id_tercero")
    private Long id;

    @NotBlank(message="El nombre del Tercero es obligatorio")
    @Size(max=20, message="El nombre del Tercero no debe superar los 20 caracteres")
    @Column(name="nombre", nullable = false)
    private String nombre;
    
    @NotBlank(message="El CUIT del Tercero es obligatorio")
    @Size(max=15, message="El CUIT del Tercero no debe superar los 15 caracteres")
    @Column(name="cuitl", nullable = false)
    private String cuitl;

    @NotNull(message="La situación IVA es obligatoria")
    @Column(name="sitiva", nullable = false)
    @Enumerated(EnumType.STRING)
    private SituacionIVA sitiva;

    @NotBlank(message="La dirección del Tercero es obligatoria")
    @Size(max=70, message="La dirección del Tercero no debe superar los 70 caracteres")
    @Column(name="direccion", nullable = false)
    private String direccion;

    @Column(name="localidad")
    @Size(max=70, message="La localidad del Tercero no debe superar los 70 caracteres")
    private String localidad;

    @Column(name="provincia")
    @Size(max=70, message="La provincia del Tercero no debe superar los 70 caracteres")
    private String provincia;

    @Column(name="telefonos")
    @Size(max=120, message="Los teléfonos del Tercero no debe superar los 120 caracteres")
    private String telefonos;

    @Column(name="saldo_apertura", scale=2, precision=10)
    private BigDecimal saldoApertura;
   
    @Enumerated(EnumType.STRING)
    @Column(name="tipo_saldo")
    private TipoSaldo tipoSaldo;
    

}