package com.project.ui;


import org.springframework.data.domain.Sort;

import com.project.models.Facultad;
import com.project.repositories.FacultadRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import com.vaadin.flow.data.binder.BeanValidationBinder;

@RolesAllowed({"USER","ADMIN"})
@Route(value = "facultades", layout = MainLayout.class)
@PageTitle("Facultades")
@Menu(order = 1, icon = "vaadin:academy-cap")
public class FacultadView extends VerticalLayout {
	
    private final FacultadRepository facultadRepository;

    private final BeanValidationBinder<Facultad> binder = new BeanValidationBinder<>(Facultad.class);
    private Grid<Facultad> grid = new Grid<>(Facultad.class, false);

    private Facultad facultadActual;

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

        H1 titulo = new H1("Gestión de Facultades");
        titulo.setWidthFull();
        titulo.getStyle().set("text-align", "center");
        add(titulo);

        configurarBuscador();
        
        configurarGrid();
        
        cargarFormulario();
        
        configurarBotones();

        limpiarFormulario();

    }
    
    
    // ================= CRUD =================
    
    private void agregarFacultad() {
    	
    	Facultad nuevaFacultad = new Facultad();
  
    	if (!binder.validate().isOk()) return;
    	
        try {
            binder.writeBean(nuevaFacultad);
            
            if (facultadRepository.existsByCuit(nuevaFacultad.getCuit())) {
                mostrarNotificacion("Ya existe una Facultad con ese CUIT", NotificationVariant.LUMO_ERROR);
                return;
            }
            
            facultadRepository.save(nuevaFacultad);

            actualizarGrid(tfBuscar.getValue());

            mostrarNotificacion("Facultad agregada", NotificationVariant.LUMO_SUCCESS);
            limpiarFormulario();
            
        } catch (ValidationException e) {
        	e.printStackTrace();
        }
    }
    
    private void actualizarFacultad() {

        if (facultadActual == null || facultadActual.getId() == null) {
            mostrarNotificacion("Seleccione una facultad",NotificationVariant.LUMO_WARNING);
            return;
        }
        
        try {
            binder.writeBean(facultadActual);
            
            facultadRepository.save(facultadActual);
            
            actualizarGrid(tfBuscar.getValue());
            mostrarNotificacion("Facultad actualizada",NotificationVariant.LUMO_SUCCESS);
            limpiarFormulario();

        }catch (ValidationException e) {
        	e.printStackTrace();
        }
        
    }
    
    private void eliminarFacultad() {

        if (facultadActual == null || facultadActual.getId() == null) {
            mostrarNotificacion("Seleccione una facultad", NotificationVariant.LUMO_WARNING);
            return;
        }

        facultadRepository.delete(facultadActual);
        
        actualizarGrid(tfBuscar.getValue());
        mostrarNotificacion("Facultad eliminada", NotificationVariant.LUMO_SUCCESS);
        limpiarFormulario();
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
        grid.addColumn(Facultad::getId).setHeader("ID");
        grid.addColumn(Facultad::getNombre).setHeader("Nombre");
        grid.addColumn(Facultad::getDireccion).setHeader("Dirección");
        grid.addColumn(Facultad::getCuit).setHeader("CUIT");
        grid.addColumn(Facultad::getSucursal).setHeader("Sucursal");
        grid.addColumn(Facultad::getTelefonos).setHeader("Teléfonos");
        grid.addColumn(Facultad::getCorreos).setHeader("Correo");
        grid.addColumn(Facultad::getDefecto).setHeader("Por Defecto");

        actualizarGrid(null);

        grid.asSingleSelect().addValueChangeListener(e -> {
            facultadActual = e.getValue();
            if (facultadActual != null) {
                binder.setBean(facultadActual); // Binder llena el formulario, cuando el usuario seleccioa una fila.
            }
        });
        add(grid);
    }  
    
    private void configurarBuscador() {
        tfBuscar.setPlaceholder("Buscar Facultad por nombre...");
        tfBuscar.setClearButtonVisible(true);
        tfBuscar.setWidth("33%");
        
        tfBuscar.addValueChangeListener(e -> actualizarGrid(e.getValue()));

        HorizontalLayout buscadorLayout = new HorizontalLayout(tfBuscar);
        buscadorLayout.setWidthFull();
        buscadorLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        buscadorLayout.setAlignItems(FlexComponent.Alignment.START);

        add(buscadorLayout);
    }
    
    private void cargarFormulario() {
        FormLayout form = new FormLayout();
        form.add(
                tfNombre,
                tfDireccion,
                tfCuit,
                ifSucursal,
                tfTelefonos,
                tfCorreos,
                cbDefecto
        );
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("800px", 3)
        );
        add(form);
    }
    
    private void limpiarFormulario() {
        facultadActual = new Facultad();
        facultadActual.setDefecto(false);
        binder.setBean(facultadActual);
        grid.deselectAll();
    }
    
    private void configurarBotones() {
        Button btnAgregar = new Button("Agregar", VaadinIcon.PLUS.create(), e -> agregarFacultad());
        btnAgregar.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);

        Button btnActualizar = new Button("Actualizar", VaadinIcon.EDIT.create(), e -> actualizarFacultad());
        btnActualizar.addClassName("btn-actualizar");

        Button btnEliminar = new Button("Eliminar", VaadinIcon.TRASH.create(), e -> eliminarFacultad());
        btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);

        Button btnLimpiarFormulario = new Button("Limpiar Formulario", VaadinIcon.ERASER.create(), e -> limpiarFormulario());
        btnLimpiarFormulario.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnLimpiarFormulario.getStyle().set("margin-left", "30px");

        HorizontalLayout acciones = new HorizontalLayout(btnAgregar, btnActualizar,btnEliminar,btnLimpiarFormulario);

        add(acciones);
    }

    private void actualizarGrid(String filtroNombre) {
        if (filtroNombre == null || filtroNombre.isBlank()) {
            grid.setItems(facultadRepository.findAll(Sort.by("id").ascending()));
            return;
        }
        grid.setItems(facultadRepository.findByNombreContainingIgnoreCaseOrderByIdAsc(filtroNombre));
    }

    private void mostrarNotificacion(String msg, NotificationVariant variant) {

        Icon icon;

        if (variant == NotificationVariant.LUMO_SUCCESS) {
            icon = VaadinIcon.CHECK_CIRCLE.create();
            icon.setColor("green");
        } else if (variant == NotificationVariant.LUMO_ERROR) {
            icon = VaadinIcon.CLOSE_CIRCLE.create();
            icon.setColor("red");
        } else {
        	//WARNING
            icon = VaadinIcon.WARNING.create();
            icon.setColor("yellow");
        }

        Div texto = new Div();
        texto.getElement().setProperty("innerHTML", msg);

        HorizontalLayout contenido = new HorizontalLayout(icon, texto);

        contenido.setAlignItems(Alignment.CENTER);

        Notification n = new Notification(contenido);

        n.addThemeVariants(variant);
        n.setDuration(5000);
        n.setPosition(Notification.Position.MIDDLE);

        n.open();
    }
    
	
}
