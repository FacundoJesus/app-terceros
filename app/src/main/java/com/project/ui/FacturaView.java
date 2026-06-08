package com.project.ui;

import java.util.Collections;

import com.project.models.Factura;
import com.project.models.FacturaItem;
import com.project.models.Tercero;
import com.project.repositories.FacturaRepository;
import com.project.repositories.TerceroRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Validator;

@RolesAllowed({"ADMIN"})
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

    // ================= FORM =================
    private DatePicker dpFecha = new DatePicker("Fecha");
    private IntegerField ifNumero = new IntegerField("Número");
    private ComboBox<Tercero> cbTercero = new ComboBox<>("Tercero");

    private TextField tfBuscar = new TextField();
    
    public FacturaView(FacturaRepository facturaRepository, TerceroRepository terceroRepository, Validator validator) {
    	
    	setSizeFull();
        this.facturaRepository = facturaRepository;
        this.terceroRepository = terceroRepository;
        this.validator = validator;

        // ================= HEADER =================
        H1 titulo = new H1("Gestión de Facturas");
        titulo.setWidthFull();
        titulo.getStyle().set("text-align", "center");
        add(titulo);


        // ================= BUSCADOR =================
        configurarBuscador();


        // ================= GRID FACTURAS =================
        configurarGridFacturas();

        // ================= GRID ITEMS =================
        H3 titulo2 = new H3("Facturas Items");
        titulo2.setWidthFull();
        titulo2.getStyle().set("text-align", "center");
        add(titulo2);
        
        configurarGridFacturaItems();
        
        // ================= FORM CABECERA =================
        cargarFormulario();

        // ================= BOTONES =================
        configurarBotones();

    }

    // ================= CRUD =================
    private void agregarFactura() {

        Factura nuevaFactura = new Factura();

        nuevaFactura.setNumeroFactura(ifNumero.getValue());
        nuevaFactura.setFechaFactura(dpFecha.getValue());
        nuevaFactura.setTercero(cbTercero.getValue());

        if (!validarFactura(nuevaFactura)) return;
        
        facturaRepository.save(nuevaFactura);

        actualizarGridFacturas(tfBuscar.getValue());
        mostrarNotificacion("Factura agregada", NotificationVariant.LUMO_SUCCESS);
        limpiarFormulario();
    }
    
    private void actualizarFactura() {

    	if (facturaActual == null || facturaActual.getId() == null) {
    	    mostrarNotificacion("Debes seleccionar una Factura", NotificationVariant.LUMO_WARNING);
    	    return;
    	}

        facturaActual.setNumeroFactura(ifNumero.getValue());
        facturaActual.setFechaFactura(dpFecha.getValue());
        facturaActual.setTercero(cbTercero.getValue());

        if (!validarFactura(facturaActual)) return;

        facturaRepository.save(facturaActual);

        
        mostrarNotificacion("Factura actualizada", NotificationVariant.LUMO_SUCCESS);
        limpiarFormulario();
        
    }
    
    
    private void eliminarFactura() {
        if (facturaActual == null || facturaActual.getId() == null) {
        	mostrarNotificacion("Debes seleccionar una Factura",NotificationVariant.LUMO_WARNING);
        	return;
        }

        facturaRepository.delete(facturaActual);

        actualizarGridFacturas(tfBuscar.getValue());
        mostrarNotificacion("Factura eliminada",NotificationVariant.LUMO_SUCCESS);
        limpiarFormulario();
        
    }  
    
    
    // ================= HELPERS =================
    
    private void configurarBuscador() {
        tfBuscar.setPlaceholder("Buscar Factura por nombre...");
        tfBuscar.setClearButtonVisible(true);

        tfBuscar.setWidth("33%");

        tfBuscar.addValueChangeListener(e -> actualizarGridFacturas(e.getValue()));

        HorizontalLayout buscadorLayout = new HorizontalLayout(tfBuscar);
        buscadorLayout.setWidthFull();
        buscadorLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        buscadorLayout.setAlignItems(FlexComponent.Alignment.START);

        add(buscadorLayout);
    }
    
    private void cargarFormulario() {
        cbTercero.setItems(terceroRepository.findAll());
        cbTercero.setItemLabelGenerator(Tercero::getNombre);

        HorizontalLayout form = new HorizontalLayout(dpFecha,ifNumero,cbTercero);

        form.setWidthFull();
        add(form);
    }
    
    private void configurarGridFacturas() {
        gridFacturas.addColumn(Factura::getId).setHeader("ID");
        gridFacturas.addColumn(Factura::getFechaFactura).setHeader("Fecha");
        gridFacturas.addColumn(Factura::getNumeroFactura).setHeader("Número");
        gridFacturas.addColumn(f -> f.getTercero().getNombre()).setHeader("Tercero");

        actualizarGridFacturas(null);
        
        gridFacturas.asSingleSelect().addValueChangeListener(e -> {
            facturaActual = e.getValue();
            if (facturaActual != null) {
                cargarFactura(facturaActual);
                cargarItems(facturaActual);
            }
        });

        add(gridFacturas);
    } 
    
    private void configurarGridFacturaItems() {
        gridItems.addColumn(FacturaItem::getDetalle).setHeader("Detalle");
        gridItems.addColumn(FacturaItem::getCantidad).setHeader("Cantidad");
        gridItems.addColumn(FacturaItem::getMonto).setHeader("Monto");
        
        gridItems.setHeight("200px");
        gridItems.setAllRowsVisible(false);
        
        add(gridItems);
    }
    
    private void actualizarGridFacturas(String filtroNombre) {
        if (filtroNombre == null || filtroNombre.isBlank()) {
            gridFacturas.setItems(facturaRepository.buscarTodasConItems());
        } else {
            gridFacturas.setItems(facturaRepository.buscarPorNombreTercero(filtroNombre));
        }
        facturaActual = null;
        gridFacturas.deselectAll();
        gridItems.setItems(Collections.emptyList());
    }
    
    private void cargarFactura(Factura f) {
        dpFecha.setValue(f.getFechaFactura());  
        ifNumero.setValue(f.getNumeroFactura());
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

        Button btnLimpiarForm = new Button("Limpiar Formulario", e -> limpiarFormulario());
        btnLimpiarForm.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnLimpiarForm.getStyle().set("margin-left", "30px");
        HorizontalLayout acciones = new HorizontalLayout(btnAgregar,btnActualizar,btnEliminar,btnLimpiarForm);

        acciones.setWidthFull();
        acciones.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        acciones.setAlignItems(FlexComponent.Alignment.CENTER);

        add(acciones);
    }
    
    private void limpiarFormulario() {
        facturaActual = new Factura();

        dpFecha.clear();
        ifNumero.clear();
        cbTercero.clear();

        gridFacturas.deselectAll();
        gridItems.setItems(Collections.emptyList());
    }
    
    private void mostrarNotificacion(String msg, NotificationVariant variant) {

        Icon icon;

        if (variant == NotificationVariant.LUMO_SUCCESS) {
            icon = VaadinIcon.CHECK_CIRCLE.create();
            icon.setColor("green");
        } else if (variant == NotificationVariant.LUMO_ERROR) {
            icon = VaadinIcon.CLOSE_CIRCLE.create();
            icon.setColor("red");
        } else if (variant == NotificationVariant.LUMO_WARNING) {
            icon = VaadinIcon.WARNING.create();
            icon.setColor("orange");
        } else {
            icon = VaadinIcon.INFO_CIRCLE.create();
            icon.setColor("blue");
        }

        Div texto = new Div();
        texto.getElement().setProperty("innerHTML", msg);

        HorizontalLayout layout = new HorizontalLayout(icon, texto);
        layout.setAlignItems(Alignment.CENTER);

        Notification n = new Notification(layout);
        n.addThemeVariants(variant);
        n.setPosition(Notification.Position.MIDDLE);
        n.setDuration(5000);

        n.open();
    }
    
    private boolean validarFactura(Factura f) {
        var errores = validator.validate(f);
        if (!errores.isEmpty()) {
            String mensaje = errores.stream()
                    .map(e -> "<li>" + e.getMessage() + "</li>")
                    .collect(java.util.stream.Collectors.joining());
            mostrarNotificacion("<b>Errores:</b><ul>" + mensaje + "</ul>",NotificationVariant.LUMO_ERROR);
            return false;
        }
        return true;
    }
    
     
}
    
    