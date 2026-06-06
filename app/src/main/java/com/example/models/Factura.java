package com.example.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name="facturas")
public class Factura {
	
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_factura")
	private Long id;
	
	@NotNull(message="La fecha de la factura es obligatoria")
	@Column(name="fecha_factura")
	private LocalDate fechaFactura;
	
	@Column(name="numero")
	@NotNull(message="El número de la factura es obligatorio")
	private Integer numeroFactura;

	// PROVEEDOR-TERCERO
	@ToString.Exclude
	@NotNull(message = "Debe seleccionar un tercero")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_tercero",
        nullable = false
    )
    private Tercero tercero;
    
    // ITEMS
    @OneToMany(
            mappedBy = "factura",
            cascade = CascadeType.ALL,
            orphanRemoval = true
        )
    private List<FacturaItem> items = new ArrayList<>();

}
