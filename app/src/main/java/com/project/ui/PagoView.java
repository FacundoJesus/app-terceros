package com.project.ui;

import java.util.Collections;
import com.project.models.Pago;
import com.project.models.PagoDetalle;
import com.project.models.Tercero;
import com.project.models.enums.ModoPago;
import com.project.repositories.PagoRepository;
import com.project.repositories.TerceroRepository;
import com.project.ui.base.BaseView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

	private static final long serialVersionUID = 1L;
	
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
	
    // BOTONES ITEM
    private Button btnAgregarDetalle = new Button("Agregar Detalle");
    private Button btnModificarDetalle = new Button("Modificar Detalle");
    private Button btnEliminarDetalle = new Button("Eliminar Detalle");
    private PagoDetalle detalleActual = new PagoDetalle();
    
	private H3 subtDetalleP;
	
	public PagoView(PagoRepository pagoRepository, TerceroRepository terceroRepository) {
		
		this.terceroRepository = terceroRepository;
		this.pagoRepository = pagoRepository;
		setSizeFull();
			
		configurarBinder();

		// ================= TITULO =================
		add(crearTitulo("Gestión de Pagos"));
		
		// ================= BUSCADOR =================
		configurarBuscador();
        
        // ================= GRID PAGOS =================
        configurarGridPagos();
       
        // ================= FORMULARIO =================
        cargarFormulario();

        // ================= BOTONES =================
        add(crearBotonesCrud(
                e -> agregarPago(),
                e -> actualizarPago(),
                e -> eliminarPago(),
                e -> limpiarFormulario()));
        
        // ================= GRID DETALLES DEL PAGO =================
        subtDetalleP = crearSubtitulo("Detalles del Pago");
        add(subtDetalleP);
        configurarGridPagoDetalles();
        
        // ================= BOTONES ITEMS =================
        btnAgregarDetalle.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnModificarDetalle.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnEliminarDetalle.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        HorizontalLayout accionesItems = new HorizontalLayout(btnAgregarDetalle,btnModificarDetalle,btnEliminarDetalle);
        
        btnAgregarDetalle.addClickListener(e -> abrirDialogoDetalle(null));
        btnModificarDetalle.addClickListener(e -> abrirDialogoDetalle(detalleActual));
        btnEliminarDetalle.addClickListener(e -> eliminarDetalle());

        add(accionesItems);
        
        
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
	
    private void eliminarDetalle() {

        //if (itemActual == null) return;

        mostrarVentanaDialogo("¿Eliminar item seleccionado?", () -> {
        	        pagoActual.getPagosDetalles().remove(pagoActual);
        	        
        	        pagoActual = pagoRepository.save(pagoActual);    
        	        
        	        gridPagoDetalles.setItems(pagoActual.getPagosDetalles());
        	        
        	        detalleActual = null;

        	        btnModificarDetalle.setEnabled(false);
        	        btnEliminarDetalle.setEnabled(false);

        	        mostrarNotificacion("Item eliminado", NotificationVariant.LUMO_SUCCESS);
        	    }
        	);
    }

	// ================= HELPERS =================
    private void configurarBuscador() {
        tfBuscar.addValueChangeListener(e -> actualizarGridPagos(e.getValue()));
        add(crearBuscador(tfBuscar));
    }
    
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
				subtDetalleP.setText("Detalle del Pago: " + pagoActual.getTercero().getNombre());
				btnAgregarDetalle.setEnabled(true);
			}
		});		
		add(gridPagos);
	}
	
	private void configurarGridPagoDetalles() {
		gridPagoDetalles.addColumn(PagoDetalle::getInstrumentNumber).setHeader("Número de instrumento");
		gridPagoDetalles.addColumn(PagoDetalle::getInstrumentDate).setHeader("Fecha de instrumento");
		gridPagoDetalles.addColumn(PagoDetalle::getBanco).setHeader("Banco");
		gridPagoDetalles.addColumn(PagoDetalle::getPagoRealizado).setHeader("Pago realizado");
		
        gridPagoDetalles.setHeight("250px");
        gridPagoDetalles.setAllRowsVisible(false);
        
        gridPagoDetalles.asSingleSelect().addValueChangeListener(e -> {

            detalleActual = e.getValue();

            boolean seleccionado = pagoActual != null;
            btnModificarDetalle.setEnabled(seleccionado);
            btnEliminarDetalle.setEnabled(seleccionado);
        });
        
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
		
        subtDetalleP.setText("Detalles del Pago");
		gridPagos.deselectAll();
		gridPagoDetalles.setItems(Collections.emptyList());	
		
        btnAgregarDetalle.setEnabled(false);
        btnModificarDetalle.setEnabled(false);
        btnEliminarDetalle.setEnabled(false);
	}
	
	private void abrirDialogoDetalle(PagoDetalle detalle) {

        Dialog dialog = new Dialog();

        TextField tfNinstrumento= new TextField("Nº Instrumento");
        DatePicker dpFecha = new DatePicker("Fecha de Instrumento");
        TextField tfBanco = new TextField("Banco");
        Checkbox chbPRealizado = new Checkbox("Pago Realizado");

        BeanValidationBinder<PagoDetalle> binder = new BeanValidationBinder<>(PagoDetalle.class);

        binder.forField(tfNinstrumento).bind("instrumentNumber");
        binder.forField(dpFecha).bind("instrumentDate");
        binder.forField(tfBanco).bind("banco");
        binder.forField(chbPRealizado).bind("pagoRealizado");


        PagoDetalle pagoEditando;

        if (detalle == null) {
            pagoEditando = new PagoDetalle();
        } else {
            pagoEditando = detalle;
        }

        binder.setBean(pagoEditando);

        Button btnGuardar = new Button("Guardar", e -> {
        	
            if (!binder.validate().isOk()) return;

            if (detalle == null) {
                pagoEditando.setPago(pagoActual);
                pagoActual.getPagosDetalles().add(pagoEditando);
            }

            pagoRepository.save(pagoActual);
            gridPagoDetalles.setItems(pagoActual.getPagosDetalles());

            dialog.close();

            mostrarNotificacion("Pago guardado", NotificationVariant.LUMO_SUCCESS);
        });

        Button btnCancelar = new Button("Cancelar", e -> dialog.close());

        dialog.add(new VerticalLayout(tfNinstrumento,dpFecha,tfBanco,chbPRealizado,
        			new HorizontalLayout(btnGuardar,btnCancelar)));

        dialog.open();
    }
	
}
