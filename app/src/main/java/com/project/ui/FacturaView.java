package com.project.ui;

import java.util.Collections;
import com.project.models.Factura;
import com.project.models.FacturaItem;
import com.project.models.Tercero;
import com.project.repositories.FacturaRepository;
import com.project.repositories.TerceroRepository;
import com.project.ui.base.BaseView;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"USER","ADMIN"})
@Route(value = "facturas", layout = MainLayout.class)
@PageTitle("Facturas")
@Menu(order = 3, icon = "vaadin:invoice")
public class FacturaView extends BaseView {

    private final FacturaRepository facturaRepository;
    private final TerceroRepository terceroRepository;
    private final BeanValidationBinder<Factura> binderFactura = new BeanValidationBinder<>(Factura.class);
    
    private Grid<Factura> gridFacturas = new Grid<>(Factura.class, false);
    private Grid<FacturaItem> gridItems = new Grid<>(FacturaItem.class, false);
    private Factura facturaActual = new Factura();

    private DatePicker dpFecha = new DatePicker("Fecha");
    private IntegerField ifNumero = new IntegerField("Número");
    private ComboBox<Tercero> cbTercero = new ComboBox<>("Tercero");
    private TextField tfBuscar = new TextField();
    
    public FacturaView(FacturaRepository facturaRepository, TerceroRepository terceroRepository) {
    	
    	setSizeFull();
        this.facturaRepository = facturaRepository;
        this.terceroRepository = terceroRepository;

        configurarBinderFactura();
        
        // ================= TITULO =================
        add(crearTitulo("Gestion de Facturas"));

        // ================= BUSCADOR =================
        configurarBuscador();

        // ================= GRID FACTURAS =================
        configurarGridFacturas();

        // ================= FORMULARIO =================
        cargarFormulario();
        
        // ================= BOTONES =================
        add(crearBotonesCrud(
        		e -> agregarFactura(),
		        e -> actualizarFactura(),
		        e -> eliminarFactura(),
		        e -> limpiarFormulario()
		        ));

        // ================= GRID ITEMS DE LA FACTURA =================
        add(crearSubtitulo("Items de la Factura Nº:"));
        configurarGridFacturaItems();

     
        limpiarFormulario();
    }

	// ================= CRUD =================
    private void agregarFactura() {

    	if(!binderFactura.validate().isOk()) return;
    	
    	Factura nuevaFactura = new Factura();
    	
    	try {
    		binderFactura.writeBean(nuevaFactura);
    		
            facturaRepository.save(nuevaFactura);
            
            actualizarGridFacturas(tfBuscar.getValue());
            mostrarNotificacion("Factura agregada", NotificationVariant.LUMO_SUCCESS);
            limpiarFormulario();
            
    	} catch(ValidationException ex) {
    		mostrarNotificacion("Error al llenar el Formulario",NotificationVariant.LUMO_ERROR);
    	}
     
    }
    
    private void actualizarFactura() {

    	if (facturaActual == null || facturaActual.getId() == null) {
    	    mostrarNotificacion("Debes seleccionar una Factura", NotificationVariant.LUMO_WARNING);
    	    return;
    	}
    	
    	if(!binderFactura.validate().isOk()) return;

        facturaRepository.save(facturaActual);
        
        actualizarGridFacturas(tfBuscar.getValue());
        mostrarNotificacion("Factura actualizada", NotificationVariant.LUMO_SUCCESS);
        limpiarFormulario();
    }
    
    private void eliminarFactura() {
    	
        if (facturaActual == null || facturaActual.getId() == null) {
        	mostrarNotificacion("Debes seleccionar una Factura",NotificationVariant.LUMO_WARNING);
        	return;
        }
        
        mostrarVentanaDialogo("Se eliminarán las Facturas del Tercero asociado.",
        		() -> {
        			facturaRepository.delete(facturaActual);

        	        actualizarGridFacturas(tfBuscar.getValue());
        	        mostrarNotificacion("Factura eliminada",NotificationVariant.LUMO_SUCCESS);
        	        limpiarFormulario();  
        		});
    }  
    

    // ================= HELPERS =================
    private void configurarBuscador() {
        tfBuscar.addValueChangeListener(e -> actualizarGridFacturas(e.getValue()));
        add(crearBuscador(tfBuscar));
    }
    
    private void configurarBinderFactura() {
    	binderFactura.forField(dpFecha).bind("fechaFactura");
    	binderFactura.forField(ifNumero).bind("numeroFactura");
    	binderFactura.forField(cbTercero).bind("tercero");
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
                binderFactura.setBean(facturaActual);
                gridItems.setItems(facturaActual.getItems());
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
    
    private void cargarFormulario() {
        cbTercero.setItems(terceroRepository.findAll());
        cbTercero.setItemLabelGenerator(Tercero::getNombre);

        HorizontalLayout form = new HorizontalLayout(dpFecha,ifNumero,cbTercero);

        form.setWidthFull();
        add(form);
    }
          
    private void limpiarFormulario() {
    	facturaActual = new Factura();
 
    	binderFactura.setBean(facturaActual);
    	
        gridFacturas.deselectAll();
        gridItems.setItems(Collections.emptyList());
    }
    
}
    
    