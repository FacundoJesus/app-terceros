package com.example.base.ui;

import java.util.Collections;
import com.example.models.Factura;
import com.example.models.FacturaItem;

import com.example.models.Tercero;
import com.example.repositories.TerceroRepository;
import com.example.repositories.FacturaRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

import jakarta.validation.Validator;


@Route(value = "facturas", layout = MainLayout.class)
@PageTitle("Facturas")
@Menu(order = 3, icon = "vaadin:invoice")
public class FacturaView extends VerticalLayout {

    private final FacturaRepository facturaRepository;
    private final TerceroRepository terceroRepository;
    private final Validator validator;

    private Grid<Factura> gridFacturas = new Grid<>(Factura.class, false);
    private Grid<FacturaItem> gridItems = new Grid<>(FacturaItem.class, false);

    private Factura facturaActual = new Factura();

    // ================= FORM CABECERA =================
    private DatePicker dpFecha = new DatePicker("Fecha");
    private TextField tfNumero = new TextField("Número");
    private ComboBox<Tercero> cbTercero = new ComboBox<>("Tercero");

    public FacturaView(FacturaRepository facturaRepository, TerceroRepository terceroRepository, Validator validator) {

        this.facturaRepository = facturaRepository;
        this.terceroRepository = terceroRepository;
        this.validator = validator;

        // ================= HEADER =================
        H1 titulo = new H1("Gestión de Facturas");
        titulo.setWidthFull();
        titulo.getStyle().set("text-align", "center");
        add(titulo);


        // ================= GRID FACTURAS =================
        configurarGridFacturas();

        // ================= GRID ITEMS =================
        configurarGridItems();
        
        // ================= FORM CABECERA =================
        cargarFormulario();

        // ================= BOTONES =================
        configurarBotones();

        actualizarGridFacturas();
    }
    
    private void cargarFormulario() {

        cbTercero.setItems(terceroRepository.findAll());
        cbTercero.setItemLabelGenerator(Tercero::getNombre);

        HorizontalLayout form = new HorizontalLayout(
                dpFecha,
                tfNumero,
                cbTercero
        );

        form.setWidthFull();
        add(form);
    }
    
    private void configurarGridFacturas() {

        gridFacturas.addColumn(Factura::getId).setHeader("ID");
        gridFacturas.addColumn(Factura::getFechaFactura).setHeader("Fecha");
        gridFacturas.addColumn(Factura::getNumeroFactura).setHeader("Número");
        gridFacturas.addColumn(f -> f.getTercero().getNombre()).setHeader("Tercero");

        gridFacturas.asSingleSelect().addValueChangeListener(e -> {
            facturaActual = e.getValue();
            if (facturaActual != null) {
                cargarFactura(facturaActual);
                cargarItems(facturaActual);
            }
        });

        add(gridFacturas);
    } 
    
    private void configurarGridItems() {

        gridItems.addColumn(FacturaItem::getDetalle).setHeader("Detalle");
        gridItems.addColumn(FacturaItem::getCantidad).setHeader("Cantidad");
        gridItems.addColumn(FacturaItem::getMonto).setHeader("Monto");

        add(gridItems);
    }
    
    private void actualizarGridFacturas() {
    	gridFacturas.setItems(facturaRepository.findAllWithDetails());
    }
    
    private void cargarFactura(Factura f) {
        dpFecha.setValue(f.getFechaFactura());
        tfNumero.setValue(f.getNumeroFactura() != null ? f.getNumeroFactura().toString() : "");
        cbTercero.setValue(f.getTercero());
    }

    private void cargarItems(Factura f) {
        gridItems.setItems(f.getItems());
    }
    
    private void configurarBotones() {

        Button btnAgregar = new Button("Agregar", e -> agregarFactura());
        btnAgregar.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.PRIMARY);

        Button btnActualizar = new Button("Actualizar", e -> actualizarFactura());
        btnActualizar.addClassName("btn-actualizar");

        Button btnEliminar = new Button("Eliminar", e -> eliminarFactura());
        btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.PRIMARY);

        Button btnLimpiar = new Button("Limpiar", e -> limpiarFormulario());
        btnLimpiar.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        HorizontalLayout acciones = new HorizontalLayout(
                btnAgregar,
                btnActualizar,
                btnEliminar,
                btnLimpiar
        );

        acciones.setWidthFull();
        acciones.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        acciones.setAlignItems(FlexComponent.Alignment.CENTER);

        add(acciones);
    }
    
    private void limpiarFormulario() {
        facturaActual = new Factura();

        dpFecha.clear();
        tfNumero.clear();
        cbTercero.clear();

        gridItems.setItems(Collections.emptyList());
        gridFacturas.deselectAll();
    }
    
    // ================= CRUD =================
    private void agregarFactura() {
        Factura f = new Factura();

        f.setFechaFactura(dpFecha.getValue());
        f.setNumeroFactura(
                tfNumero.getValue().isBlank() ? null : Integer.parseInt(tfNumero.getValue())
        );
        f.setTercero(cbTercero.getValue());

        facturaRepository.save(f);

        limpiarFormulario();
        actualizarGridFacturas();
    }
    
    private void actualizarFactura() {
        if (facturaActual.getId() == null) return;

        facturaActual.setFechaFactura(dpFecha.getValue());
        facturaActual.setNumeroFactura(
                tfNumero.getValue().isBlank() ? null : Integer.parseInt(tfNumero.getValue())
        );
        facturaActual.setTercero(cbTercero.getValue());

        facturaRepository.save(facturaActual);

        limpiarFormulario();
        actualizarGridFacturas();
    }
    
    private void eliminarFactura() {
        if (facturaActual.getId() == null) return;

        facturaRepository.delete(facturaActual);

        limpiarFormulario();
        actualizarGridFacturas();
    }
     
}
    
    