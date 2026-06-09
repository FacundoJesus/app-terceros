package com.project.ui;

import java.util.Collections;

import com.project.models.Factura;
import com.project.models.Pago;
import com.project.models.PagoDetalle;
import com.project.models.Tercero;
import com.project.models.enums.ModoPago;
import com.project.repositories.PagoRepository;
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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Validator;

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

		// ================= HEADER =================
		H1 titulo = new H1("Gestión de Pagos");
		titulo.setWidthFull();
		titulo.getStyle().set("text-align", "center");
		add(titulo);
		
		// ================= BUSCADOR =================
        configurarBuscador();
        
        // ================= GRID PAGOS =================
        configurarGridPagos();
        
        // ================= GRID PAGO-DETALLES =================
        H3 titulo2 = new H3("Pago Detalles");
        titulo2.setWidthFull();
        titulo2.getStyle().set("text-align", "center");
        add(titulo2);
        
        configurarGridPagoDetalles();
        
        // ================= FORM CABECERA =================
        cargarFormulario();

        // ================= BOTONES =================
        configurarBotones();
        
        limpiarFormulario();
	}

    
    private void configurarBinder() {
    	binderPago.forField(dpFecha).bind("fechaPago");
    	binderPago.forField(bdfMontoPago).bind("montoPago");
    	binderPago.forField(cbModoPago).bind("modoPago");
    	binderPago.forField(cbTercero).bind("tercero");
	}


	// ================= CRUD =================
	private void agregarPago() {
		
		if(!binderPago.validate().isOk()) return;

		pagoRepository.save(pagoActual);
		
		actualizarGridPagos(tfBuscar.getValue());
        mostrarNotificacion("Pago agregado", NotificationVariant.LUMO_SUCCESS);
        limpiarFormulario();	
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
		
		pagoRepository.delete(pagoActual);
		
		actualizarGridPagos(tfBuscar.getValue());
        mostrarNotificacion("Pago eliminado", NotificationVariant.LUMO_SUCCESS);
        limpiarFormulario();
	}

	// ================= HELPERS =================
	private void configurarBuscador() {	
		tfBuscar.setPlaceholder("Buscar Pago por nombre...");
        tfBuscar.setClearButtonVisible(true);

        tfBuscar.setWidth("44%");

        tfBuscar.addValueChangeListener(e -> actualizarGridPagos(e.getValue()));

        HorizontalLayout buscadorLayout = new HorizontalLayout(tfBuscar);
        buscadorLayout.setWidthFull();
        buscadorLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        buscadorLayout.setAlignItems(FlexComponent.Alignment.START);

        add(buscadorLayout);
	}

	private void configurarGridPagos() {
		gridPagos.addColumn(Pago::getId).setHeader("ID");
		gridPagos.addColumn(Pago::getFechaPago).setHeader("Fecha Pago");
		gridPagos.addColumn(Pago::getMontoPago).setHeader("Monto Pago");
		gridPagos.addColumn(Pago::getModoPago).setHeader("Modo Pago");
		gridPagos.addColumn(pago -> pago.getTercero().getNombre()).setHeader("Tercero");
		
		actualizarGridPagos(null);
		gridPagos.asSingleSelect().addValueChangeListener(evento -> {
			pagoActual = evento.getValue();
			if(pagoActual != null) {
				binderPago.setBean(pagoActual);
				cargarPagoDetalle(pagoActual);
			}
		});		
		add(gridPagos);
	}
	
	
	private void cargarPagoDetalle(Pago p) {
		gridPagoDetalles.setItems(p.getPagosDetalles());
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
	
	private void configurarBotones() {
		Button btnAgregar = new Button("Agregar", VaadinIcon.PLUS.create(), e -> agregarPago());
		btnAgregar.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.PRIMARY);
		
		Button btnActualizar = new Button("Actualizar", VaadinIcon.EDIT.create(), e -> actualizarPago());
		btnActualizar.addClassName("btn-actualizar");
		
		Button btnEliminar= new Button("Eliminar", VaadinIcon.TRASH.create(), e -> eliminarPago());
		btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.PRIMARY);
	
		Button btnLimpiarForm = new Button("Limpiar Formulario",  VaadinIcon.ERASER.create(), e -> limpiarFormulario());
		btnLimpiarForm.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
		btnLimpiarForm.getStyle().set("margin-left","30px");
		
        HorizontalLayout acciones = new HorizontalLayout(btnAgregar, btnActualizar, btnEliminar, btnLimpiarForm);

        acciones.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        acciones.setAlignItems(FlexComponent.Alignment.CENTER);
        add(acciones);
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
