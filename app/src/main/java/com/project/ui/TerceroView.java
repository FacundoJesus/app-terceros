package com.project.ui;



import org.springframework.data.domain.Sort;

import com.project.models.Facultad;
import com.project.models.Tercero;
import com.project.models.enums.SituacionIVA;
import com.project.models.enums.TipoSaldo;
import com.project.repositories.TerceroRepository;
import com.project.ui.base.BaseView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
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
import com.vaadin.flow.component.orderedlayout.FlexComponent;

import jakarta.annotation.security.RolesAllowed;


@RolesAllowed({"USER","ADMIN"})
@Route(value = "terceros", layout = MainLayout.class)
@PageTitle("Terceros")
@Menu(order = 2, icon = "vaadin:users")
public class TerceroView extends BaseView {

    private final TerceroRepository terceroRepository;
    
    private final BeanValidationBinder<Tercero> binder = new BeanValidationBinder<>(Tercero.class);
    private Grid<Tercero> grid = new Grid(Tercero.class, false);
    
    private Tercero nuevoTercero = new Tercero();

    // ================= FORM =================
    private TextField tfNombre = new TextField("Nombre");
    private TextField tfCuitl = new TextField("CUIT/L");
    private ComboBox <SituacionIVA> cbSituacionIva = new ComboBox<>("Condición IVA");
    private TextField tfDireccion = new TextField("Dirección");
    private TextField tfLocalidad = new TextField("Localidad");
    private TextField tfProvincia = new TextField("Provincia");
    private TextField tfTelefonos = new TextField("Teléfonos");
    private BigDecimalField bdfSaldoApertura = new BigDecimalField("Saldo Apertura");
    private ComboBox <TipoSaldo> cbTipoSaldo = new ComboBox<>("Tipo Saldo");

    private TextField tfBuscar = new TextField();

    public TerceroView(TerceroRepository repository) {
    	
    	setSizeFull();
    	
        this.terceroRepository = repository;

        configurarBinder();
        // ================= HEADER =================
        H1 titulo = new H1("Gestión de Terceros");
        titulo.setWidthFull();
        titulo.getStyle().set("text-align", "center");

        add(titulo);
        // ================= BUSCADOR =================
        
        configurarBuscador();
        
        // ================= GRID =================
        
        configurarGrid();

        // ================= FORM =================
        
        configurarFormulario();
        
        // ================= BOTONES =================
        
        configurarBotones();  
        
        limpiarFormulario();
    }

    
    // ================= CRUD =================

    private void configurarBinder() {
		binder.forField(tfNombre).bind("nombre");
		binder.forField(tfCuitl).bind("cuitl");
		binder.forField(cbSituacionIva).bind("sitiva");
		binder.forField(tfDireccion).bind("direccion");
		binder.forField(tfLocalidad).bind("localidad");
		binder.forField(tfProvincia).bind("provincia");
		binder.forField(tfTelefonos).bind("telefonos");
		binder.forField(bdfSaldoApertura).bind("saldoApertura");
		binder.forField(cbTipoSaldo).bind("tipoSaldo");
	}


	private void agregarTercero() {
  	
    	if(!binder.validate().isOk()) return;
    	
    	Tercero nuevoTercero = new Tercero();
    	
    	try {
    		binder.writeBean(nuevoTercero);
    		
    		if (terceroRepository.existsByCuitl(nuevoTercero.getCuitl())) {
                mostrarNotificacion("Ya existe un tercero con ese CUIT", NotificationVariant.LUMO_ERROR);
                return;
            }

            terceroRepository.save(nuevoTercero);
            
            actualizarGrid(tfBuscar.getValue());
            mostrarNotificacion("Tercero agregado", NotificationVariant.LUMO_SUCCESS);
            limpiarFormulario(); 
            
    	} catch (ValidationException e) {
        	mostrarNotificacion("Error al llenar el Formulario",NotificationVariant.LUMO_ERROR);
        }
        
    		
	}

    private void actualizarTercero() {

        if (nuevoTercero == null || nuevoTercero.getId() == null) {
            mostrarNotificacion("Seleccione un tercero", NotificationVariant.LUMO_WARNING);
            return;
        }
        
        if(!binder.validate().isOk()) return;
    	
        terceroRepository.save(nuevoTercero);

        actualizarGrid(tfBuscar.getValue());
        mostrarNotificacion("Tercero actualizado", NotificationVariant.LUMO_SUCCESS);
        limpiarFormulario();     	
  
    }

    private void eliminarTercero() {

        if (nuevoTercero == null || nuevoTercero.getId() == null) {
            mostrarNotificacion("Seleccione un tercero", NotificationVariant.LUMO_WARNING);
            return;
        }

        terceroRepository.delete(nuevoTercero);

        actualizarGrid(tfBuscar.getValue());
        mostrarNotificacion("Tercero eliminado", NotificationVariant.LUMO_SUCCESS);
        limpiarFormulario();
    }

    // ================= HELPERS =================
    
    private void configurarBotones() {
    	Button btnAgregar = new Button("Agregar", VaadinIcon.PLUS.create(), e -> agregarTercero());
        btnAgregar.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.PRIMARY);

        Button btnActualizar = new Button("Actualizar", VaadinIcon.EDIT.create(), e -> actualizarTercero());      
        btnActualizar.addClassName("btn-actualizar");
        
        Button btnEliminar = new Button("Eliminar", VaadinIcon.TRASH.create(), e -> eliminarTercero());
        btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.PRIMARY);
        
        Button btnLimpiarForm = new Button("Limpiar Formulario", VaadinIcon.ERASER.create(), e -> limpiarFormulario());
        btnLimpiarForm.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnLimpiarForm.getStyle().set("margin-left", "30px");

        HorizontalLayout acciones = new HorizontalLayout(btnAgregar, btnActualizar, btnEliminar, btnLimpiarForm);

        add(acciones);
    }
    
    private void configurarFormulario() {
    	cbTipoSaldo.setItems(TipoSaldo.values());
        cbSituacionIva.setItems(SituacionIVA.values());
        FormLayout form = new FormLayout();
        form.add(
                tfNombre,
                tfCuitl,
                cbSituacionIva,
                tfDireccion,
                tfLocalidad,
                tfProvincia,
                tfTelefonos,
                bdfSaldoApertura,
                cbTipoSaldo
        );

        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("800px", 3)
        );

        add(form);
    }
    
    private void configurarGrid() {
    	grid.addColumn(Tercero::getId).setHeader("ID");
        grid.addColumn(Tercero::getNombre).setHeader("Nombre");
        grid.addColumn(Tercero::getCuitl).setHeader("CUIT/L");
        grid.addColumn(Tercero::getSitiva).setHeader("IVA");
        grid.addColumn(Tercero::getDireccion).setHeader("Dirección");
        grid.addColumn(Tercero::getLocalidad).setHeader("Localidad");
        grid.addColumn(Tercero::getProvincia).setHeader("Provincia");
        grid.addColumn(Tercero::getTelefonos).setHeader("Teléfonos");
        grid.addColumn(Tercero::getSaldoApertura).setHeader("Saldo");
        grid.addColumn(Tercero::getTipoSaldo).setHeader("Tipo Saldo");
        
        actualizarGrid(null);

        grid.asSingleSelect().addValueChangeListener(e -> {
            nuevoTercero = e.getValue();
            if (nuevoTercero != null) binder.setBean(nuevoTercero);
        });

        add(grid);
    }
    
    private void configurarBuscador() {
    	tfBuscar.setPlaceholder("Buscar Tercero por nombre...");
        tfBuscar.setClearButtonVisible(true);
        tfBuscar.setWidth("33%");

        tfBuscar.addValueChangeListener(e -> actualizarGrid(e.getValue()));

        HorizontalLayout buscadorLayout = new HorizontalLayout(tfBuscar);
        buscadorLayout.setWidthFull();
        buscadorLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        buscadorLayout.setAlignItems(FlexComponent.Alignment.START);

        add(buscadorLayout);
    }
    
    private void limpiarFormulario() {
        nuevoTercero = new Tercero();  
        binder.setBean(nuevoTercero);
        grid.deselectAll();
    }
 
    private void actualizarGrid(String filtroNombre) {
        if (filtroNombre == null || filtroNombre.isBlank()) {
            grid.setItems(terceroRepository.findAll(Sort.by("id").ascending()));
            return;
        }
        grid.setItems(terceroRepository.findByNombreContainingIgnoreCaseOrderByIdAsc(filtroNombre));
    }

}