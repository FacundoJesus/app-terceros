package com.project.ui;

import com.project.models.Pago;
import com.project.models.PagoDetalle;
import com.project.models.Tercero;
import com.project.models.enums.ModoPago;
import com.project.repositories.PagoRepository;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.validation.Validator;

@Route(value = "pagos", layout = MainLayout.class)
@PageTitle("Pagos")
@Menu(order = 4, icon = "vaadin:money")
public class PagoView extends VerticalLayout {
	
	private final PagoRepository pagoRepository;
	private final Validator validator;
	
	private Grid<Pago> gridPagos = new Grid<>(Pago.class,false);
	private Grid<PagoDetalle> gridPagoDetalles = new Grid<>(PagoDetalle.class,false);
	
	private Pago pagoActual = new Pago();
	
	// ================= FORM =================
	private DatePicker dpFecha = new DatePicker("Fecha");
	private BigDecimalField bdfMontoPago = new BigDecimalField("Monto Pago");
	private ComboBox<ModoPago> cbModoPago = new ComboBox<>();
	private ComboBox<Tercero> cbTercero = new ComboBox<>();
	
	private TextField tfBuscar = new TextField();
	
	
	public PagoView(PagoRepository pagoRepository, Validator validator) {
		
		this.pagoRepository = pagoRepository;
		this.validator = validator;
		
		setSizeFull();

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
		
	}
	

	// HELPERS
	private void configurarBuscador() {	
		tfBuscar.setPlaceholder("Buscar Pago por nombre...");
        tfBuscar.setClearButtonVisible(true);

        tfBuscar.setWidth("33%");

        //tfBuscar.addValueChangeListener(e -> actualizarGridPagos(e.getValue()));

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
		
		//actualizarGridPagos(null);
		gridPagos.asSingleSelect().addValueChangeListener(evento -> {
			pagoActual = evento.getValue();
			if(pagoActual != null) {
				//cargarPago(pagoActual);
				//cargarPagoDetalles(pagoActual);
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
	
	private void configurarBotones() {
		
		
	}


	private void cargarFormulario() {
		
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
