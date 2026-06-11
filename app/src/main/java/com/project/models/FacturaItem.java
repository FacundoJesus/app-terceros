package com.project.models;

import java.math.BigDecimal;

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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "facturas_items")
public class FacturaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_items")
    private Long id;

    @NotNull(message="El monto es obligatorio")
    @Column(name = "monto", precision = 8, scale = 2)
    private BigDecimal monto;

    @NotNull(message="La cantidad es obligatoria")
    @Column(name = "cantidad", precision = 9, scale = 2)
    private BigDecimal cantidad;

    @NotBlank(message="El detalle es obligatorio")
    @Column(name = "detalle", length = 300)
    private String detalle;

    // RELACIÓN CON FACTURA
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_factura", nullable = false)
    private Factura factura;
}
