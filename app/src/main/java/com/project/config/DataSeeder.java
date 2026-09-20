package com.project.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.project.models.Factura;
import com.project.models.FacturaItem;
import com.project.models.Facultad;
import com.project.models.Pago;
import com.project.models.PagoDetalle;
import com.project.models.Tercero;
import com.project.models.enums.ModoPago;
import com.project.models.enums.SituacionIVA;
import com.project.models.enums.TipoSaldo;
import com.project.repositories.FacturaRepository;
import com.project.repositories.FacultadRepository;
import com.project.repositories.PagoRepository;
import com.project.repositories.TerceroRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final FacultadRepository facultadRepository;
    private final TerceroRepository terceroRepository;
    private final FacturaRepository facturaRepository;
    private final PagoRepository pagoRepository;

    public DataSeeder(FacultadRepository facultadRepository,
            TerceroRepository terceroRepository,
            FacturaRepository facturaRepository,
            PagoRepository pagoRepository) {
        this.facultadRepository = facultadRepository;
        this.terceroRepository = terceroRepository;
        this.facturaRepository = facturaRepository;
        this.pagoRepository = pagoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Solo cargar datos si las tablas están vacías
        if (facultadRepository.count() == 0) {
            cargarFacultades();
        }

        if (terceroRepository.count() == 0) {
            cargarTerceros();
        }

        if (facturaRepository.count() == 0) {
            cargarFacturas();
        }

        if (pagoRepository.count() == 0) {
            cargarPagos();
        }
    }

    private void cargarFacultades() {
        List<Facultad> facultades = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Facultad facultad = new Facultad();
            facultad.setNombre("Facultad " + i);
            facultad.setDireccion("Dirección Facultad " + i);
            facultad.setCuit("30-11111111-" + (i % 10));
            facultad.setSucursal(i);
            facultad.setTelefonos("444-555" + i);
            facultad.setCorreos("facultad" + i + "@utn.edu.ar");
            facultad.setDefecto(i == 1);
            facultades.add(facultad);
        }
        facultadRepository.saveAll(facultades);
    }

    private void cargarTerceros() {
        List<Tercero> terceros = new ArrayList<>();
        SituacionIVA[] situaciones = SituacionIVA.values();
        TipoSaldo[] tiposSaldo = TipoSaldo.values();

        for (int i = 1; i <= 10; i++) {
            Tercero tercero = new Tercero();
            tercero.setNombre("Tercero " + i);
            tercero.setCuitl("20-22222222-" + (i % 10));
            tercero.setSitiva(situaciones[i % situaciones.length]);
            tercero.setDireccion("Dirección Tercero " + i);
            tercero.setLocalidad("Localidad " + i);
            tercero.setProvincia("Provincia " + i);
            tercero.setTelefonos("11-2222-333" + i);
            tercero.setSaldoApertura(new BigDecimal(1000 * i));
            tercero.setTipoSaldo(tiposSaldo[i % tiposSaldo.length]);
            terceros.add(tercero);
        }
        terceroRepository.saveAll(terceros);
    }

    private void cargarFacturas() {
        List<Tercero> terceros = terceroRepository.findAll();
        if (terceros.isEmpty())
            return;

        List<Factura> facturas = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Factura factura = new Factura();
            factura.setFechaFactura(LocalDate.now().minusDays(i));
            factura.setNumeroFactura(1000 + i);

            // Asignar un tercero (circularmente)
            Tercero tercero = terceros.get(i % terceros.size());
            factura.setTercero(tercero);

            // Agregar un par de items a cada factura
            FacturaItem item1 = new FacturaItem();
            item1.setMonto(new BigDecimal("150.00"));
            item1.setCantidad(new BigDecimal("2"));
            item1.setDetalle("Item A " + i);
            item1.setFactura(factura);

            FacturaItem item2 = new FacturaItem();
            item2.setMonto(new BigDecimal("300.00"));
            item2.setCantidad(new BigDecimal("1"));
            item2.setDetalle("Item B " + i);
            item2.setFactura(factura);

            factura.getItems().add(item1);
            factura.getItems().add(item2);

            facturas.add(factura);
        }
        facturaRepository.saveAll(facturas);
    }

    private void cargarPagos() {
        List<Tercero> terceros = terceroRepository.findAll();
        if (terceros.isEmpty())
            return;

        List<Pago> pagos = new ArrayList<>();
        ModoPago[] modos = ModoPago.values();

        for (int i = 1; i <= 10; i++) {
            Pago pago = new Pago();
            pago.setFechaPago(LocalDate.now().minusDays(i - 1));
            pago.setMontoPago(new BigDecimal(500 * i));
            pago.setModoPago(modos[i % modos.length]);

            // Asignar un tercero (circularmente)
            Tercero tercero = terceros.get(i % terceros.size());
            pago.setTercero(tercero);

            // Agregar un detalle al pago
            PagoDetalle detalle = new PagoDetalle();
            detalle.setInstrumentNumber("INST-" + i);
            detalle.setInstrumentDate(LocalDate.now());
            detalle.setBanco("Banco " + i);
            detalle.setPagoRealizado(true);
            detalle.setPago(pago);

            pago.getPagosDetalles().add(detalle);

            pagos.add(pago);
        }
        pagoRepository.saveAll(pagos);
    }
}