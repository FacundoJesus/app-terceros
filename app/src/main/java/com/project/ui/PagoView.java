package com.project.ui;

import java.util.Collections;
import com.project.models.Pago;
import com.project.models.PagoDetalle;
import com.project.models.Tercero;
import com.project.models.enums.ModoPago;
import com.project.repositories.PagoRepository;
import com.project.repositories.TerceroRepository;
import com.project.ui.base.BaseView;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;


@RolesAllowed({"USER","ADMIN"})
@Route(value = "pagos", layout = MainLayout.class)
@PageTitle("Pagos")
@Menu(order = 4, icon = "vaadin:money")
public class PagoView extends BaseView {
	
	private final PagoRepository pagoRepository;
	private final TerceroRepository terceroRepository;
	private final BeanValidationBinder<Pago> binderPago = new BeanValidationBinder<>(Pago.class);
	
	private Grid<Pago> gridPagos = new Grid<>(Pago.class,false);
	private Grid<PagoDetalle> gridPagoDetalles = new Grid<>(PagoDetalle.class,false);
	private Pago pagoActual = new Pago();

	private DatePicker dpFecha = new DatePicker("Fecha");
	private BigDecimalField bdfMontoPago = new BigDecimalField("Monto Pago");
	private ComboBox<ModoPago> cbModoPago = new ComboBox<>("Modo Pago");
	private ComboBox<Tercero> cbTercero = new ComboBox<>("Tercero");
	private TextField tfBuscar = new TextField();
	
	public PagoView(PagoRepository pagoRepository, TerceroRepository terceroRepository) {
		
		this.terceroRepository = terceroRepository;
		this.pagoRepository = pagoRepository;
		setSizeFull();
		
		configurarBinder();

		// ================= TITULO =================
		add(crearTitulo("Gestión de Pagos"));
		
		// ================= BUSCADOR =================
		add(crearBuscador(tfBuscar));
        
        // ================= GRID PAGOS =================
        configurarGridPagos();
        
        // ================= GRID DETALLES DEL PAGO =================
        add(crearSubtitulo("Detalles del Pago"));
        configurarGridPagoDetalles();
        
        // ================= FORMULARIO =================
        cargarFormulario();

        // ================= BOTONES =================
        add(crearBotonesCrud(
                e -> agregarPago(),
                e -> actualizarPago(),
                e -> eliminarPago(),
                e -> limpiarFormulario()));
        
        limpiarFormulario();
	}

	// ================= CRUD =================
	private void agregarPago() {
		
		if(!binderPago.validate().isOk()) return;
		
		Pago nuevoPago = new Pago();
		
		try {
			binderPago.writeBean(nuevoPago);
			
			pagoRepository.save(nuevoPago);
			
			actualizarGridPagos(tfBuscar.getValue());
	        mostrarNotificacion("Pago agregado", NotificationVariant.LUMO_SUCCESS);
	        limpiarFormulario();	
	        
		} catch(ValidationException ex) {
			mostrarNotificacion("Error al llenar el Formulario",NotificationVariant.LUMO_ERROR);
		}
	}
	
	private void actualizarPago() {
		
		if(pagoActual == null || pagoActual.getId() == null) {
			mostrarNotificacion("Debes seleccionar un Pago", NotificationVariant.LUMO_WARNING);
			return;
		}
		
		if(!binderPago.validate().isOk()) return;
		
		pagoRepository.save(pagoActual);
		
		actualizarGridPagos(tfBuscar.getValue());
        mostrarNotificacion("Pago actualizado", NotificationVariant.LUMO_SUCCESS);
        limpiarFormulario();
	}
	
	private void eliminarPago() {
		
		if(pagoActual == null || pagoActual.getId() == null) {
			mostrarNotificacion("Debes seleccionar un Pago", NotificationVariant.LUMO_WARNING);
			return;
		}
		
		mostrarVentanaDialogo("Se eliminarán los Pagos del Tercero asociado.",(
				) -> {
					pagoRepository.delete(pagoActual);
					
					actualizarGridPagos(tfBuscar.getValue());
			        mostrarNotificacion("Pago eliminado", NotificationVariant.LUMO_SUCCESS);
			        limpiarFormulario();
				});
	}

	// ================= HELPERS =================
    private void configurarBinder() {
    	binderPago.forField(dpFecha).bind("fechaPago");
    	binderPago.forField(bdfMontoPago).bind("montoPago");
    	binderPago.forField(cbModoPago).bind("modoPago");
    	binderPago.forField(cbTercero).bind("tercero");
	}
    
	private void configurarGridPagos() {
		gridPagos.addColumn(Pago::getId).setHeader("ID");
		gridPagos.addColumn(Pago::getFechaPago).setHeader("Fecha Pago");
		gridPagos.addColumn(Pago::getMontoPago).setHeader("Monto Pago");
		gridPagos.addColumn(Pago::getModoPago).setHeader("Modo Pago");
		gridPagos.addColumn(pago -> pago.getTercero().getNombre()).setHeader("Tercero");
		
		actualizarGridPagos(null);
		
		gridPagos.asSingleSelect().addValueChangeListener(e-> {
			pagoActual = e.getValue();
			if(pagoActual != null) {
				binderPago.setBean(pagoActual);
				gridPagoDetalles.setItems(pagoActual.getPagosDetalles());
			}
		});		
		add(gridPagos);
	}
	
	private void configurarGridPagoDetalles() {
		gridPagoDetalles.addColumn(PagoDetalle::getInstrumentNumber).setHeader("Número de instrumento");
		gridPagoDetalles.addColumn(PagoDetalle::getInstrumentDate).setHeader("Fecha de instrumento");
		gridPagoDetalles.addColumn(PagoDetalle::getBanco).setHeader("Banco");
		gridPagoDetalles.addColumn(PagoDetalle::getPagoRealizado).setHeader("Pago realizado");
		
        gridPagoDetalles.setHeight("200px");
        gridPagoDetalles.setAllRowsVisible(false);
        
        add(gridPagoDetalles);	
	}
	

	private void cargarFormulario() {
		cbModoPago.setItems(ModoPago.values());
		
		cbTercero.setItems(terceroRepository.findAll());
		cbTercero.setItemLabelGenerator(Tercero::getNombre);
		
		HorizontalLayout formulario = new HorizontalLayout(dpFecha,bdfMontoPago,cbModoPago,cbTercero );
		
		add(formulario);
	}
	
	private void actualizarGridPagos(String filtroNombre) {

	    if (filtroNombre == null || filtroNombre.isBlank()) {
	        gridPagos.setItems(pagoRepository.buscarTodosConDetalles());
	    } else {
	        gridPagos.setItems(pagoRepository.buscarPorNombreTercero(filtroNombre));
	    }

	    pagoActual = null;
	    gridPagos.deselectAll();
	    gridPagoDetalles.setItems(Collections.emptyList());
	}
	
	private void limpiarFormulario() {
		pagoActual = new Pago();
		
		binderPago.setBean(pagoActual);
		
		gridPagos.deselectAll();
		gridPagoDetalles.setItems(Collections.emptyList());	
	}
	
}
