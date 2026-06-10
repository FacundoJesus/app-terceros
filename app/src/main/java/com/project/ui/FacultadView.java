package com.project.ui;


import org.springframework.data.domain.Sort;

import com.project.models.Facultad;
import com.project.repositories.FacultadRepository;
import com.project.ui.base.BaseView;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;

@RolesAllowed({"USER","ADMIN"})
@Route(value = "facultades", layout = MainLayout.class)
@PageTitle("Facultades")
@Menu(order = 1, icon = "vaadin:academy-cap")
public class FacultadView extends BaseView {
	
	private static final long serialVersionUID = 1L;

	private final FacultadRepository facultadRepository;

    private final BeanValidationBinder<Facultad> binder = new BeanValidationBinder<>(Facultad.class);
    private Grid<Facultad> gridFacultad = new Grid<>(Facultad.class, false);

    private Facultad facultadActual = new Facultad();

    private TextField tfNombre = new TextField("Nombre");
    private TextField tfDireccion = new TextField("Dirección");
    private TextField tfCuit = new TextField("CUIT");
    private IntegerField ifSucursal = new IntegerField("Sucursal");
    private TextField tfTelefonos = new TextField("Teléfonos");
    private TextField tfCorreos = new TextField("Correo");
    private Checkbox cbDefecto = new Checkbox("Por defecto");
    private TextField tfBuscar = new TextField();

    

	public FacultadView(FacultadRepository repository) {
    	
    	setSizeFull();
        this.facultadRepository = repository;

        configurarBinder();

        // ================= TITULO =================
        add(crearTitulo("Gestión de Facultades"));
        // ================= BUSCADOR =================
        add(crearBuscador(tfBuscar));
        // ================= GRID FACULTADES =================
        configurarGrid();
        // ================= FORMULARIO =================
        configurarFormulario();
        // ================= BOTONES =================
        add(crearBotonesCrud(
                e -> agregarFacultad(),
                e -> actualizarFacultad(),
                e -> eliminarFacultad(),
                e -> limpiarFormulario()));
        
        limpiarFormulario();
    }
    
    // ================= CRUD =================
    private void agregarFacultad() {
    	
        if (!binder.validate().isOk()) return;

        Facultad nuevaFacultad = new Facultad();

        try {
            binder.writeBean(nuevaFacultad);

            if (facultadRepository.existsByCuit(nuevaFacultad.getCuit())) {
                mostrarNotificacion("Ya existe una Facultad con ese CUIT",NotificationVariant.LUMO_ERROR);
                return;
            }

            facultadRepository.save(nuevaFacultad);

            actualizarGrid(tfBuscar.getValue());
            mostrarNotificacion("Facultad agregada",NotificationVariant.LUMO_SUCCESS);
            limpiarFormulario();

        } catch (ValidationException e) {
        	mostrarNotificacion("Error al llenar el Formulario",NotificationVariant.LUMO_ERROR);
        }
    }
    
    private void actualizarFacultad() {

        if (facultadActual == null || facultadActual.getId() == null) {
            mostrarNotificacion("Seleccione una facultad",NotificationVariant.LUMO_WARNING);
            return;
        }
        
        if (!binder.validate().isOk()) return;
            
        facultadRepository.save(facultadActual);
        
        actualizarGrid(tfBuscar.getValue());
        mostrarNotificacion("Facultad actualizada",NotificationVariant.LUMO_SUCCESS);
        limpiarFormulario();  
    }
    
    private void eliminarFacultad() {

        if (facultadActual == null || facultadActual.getId() == null) {
            mostrarNotificacion("Seleccione una facultad", NotificationVariant.LUMO_WARNING);
            return;
        }
        
        mostrarVentanaDialogo("Eliminarás de la lista la Facultad seleccionada.",
        		() -> {
        	        facultadRepository.delete(facultadActual);
        	        
        	        actualizarGrid(tfBuscar.getValue());
        	        mostrarNotificacion("Facultad eliminada", NotificationVariant.LUMO_SUCCESS);
        	        limpiarFormulario();
        		});
    }
    
    // ================= HELPERS =================
    private void configurarBinder() {
    	binder.forField(tfNombre).bind("nombre");
		binder.forField(tfDireccion).bind("direccion");
		binder.forField(tfCuit).bind("cuit");
		binder.forField(ifSucursal).bind("sucursal");
		binder.forField(tfTelefonos).bind("telefonos");
		binder.forField(tfCorreos).bind("correos");
		binder.forField(cbDefecto).bind("defecto");
    }
    
    private void configurarGrid() {
        gridFacultad.addColumn(Facultad::getId).setHeader("ID");
        gridFacultad.addColumn(Facultad::getNombre).setHeader("Nombre");
        gridFacultad.addColumn(Facultad::getDireccion).setHeader("Dirección");
        gridFacultad.addColumn(Facultad::getCuit).setHeader("CUIT");
        gridFacultad.addColumn(Facultad::getSucursal).setHeader("Sucursal");
        gridFacultad.addColumn(Facultad::getTelefonos).setHeader("Teléfonos");
        gridFacultad.addColumn(Facultad::getCorreos).setHeader("Correo");
        gridFacultad.addColumn(Facultad::getDefecto).setHeader("Por Defecto");

        actualizarGrid(null);

        gridFacultad.asSingleSelect().addValueChangeListener(e -> {
            facultadActual = e.getValue();
            if (facultadActual != null) {
                binder.setBean(facultadActual); // Binder llena el formulario, cuando el usuario seleccioa una fila.
            }
        });
        add(gridFacultad);
    }  
    
    private void actualizarGrid(String filtroNombre) {
        if (filtroNombre == null || filtroNombre.isBlank()) {
            gridFacultad.setItems(facultadRepository.findAll(Sort.by("id").ascending()));
            return;
        }
        gridFacultad.setItems(facultadRepository.findByNombreContainingIgnoreCaseOrderByIdAsc(filtroNombre));
    } 
    
    private void configurarFormulario() {
        FormLayout form = new FormLayout();
        form.add(tfNombre,tfDireccion,tfCuit,ifSucursal,tfTelefonos,tfCorreos,cbDefecto);
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("800px", 3));
        add(form);
    }
    
    private void limpiarFormulario() {
        facultadActual = new Facultad();
        facultadActual.setDefecto(false);
        binder.setBean(facultadActual);
        gridFacultad.deselectAll();
    }

}
