package com.project.base.ui;


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
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.validation.Validator;

@Route(value = "facultades", layout = MainLayout.class)
@PageTitle("Facultades")
@Menu(order = 1, icon = "vaadin:building")
public class FacultadView extends VerticalLayout {
	
    private final FacultadRepository facultadRepository;
    private final Validator validator;

    private Grid<Facultad> grid = new Grid<>(Facultad.class, false);

    private Facultad facultadActual = new Facultad();

    // ================= FORM =================

    private TextField tfNombre = new TextField("Nombre");
    private TextField tfDireccion = new TextField("Dirección");
    private TextField tfCuit = new TextField("CUIT");
    private IntegerField ifSucursal = new IntegerField("Sucursal");
    private TextField tfTelefonos = new TextField("Teléfonos");
    private TextField tfCorreos = new TextField("Correo");
    private Checkbox cbDefecto = new Checkbox("Facultad por defecto");

    private TextField tfBuscar = new TextField();

    public FacultadView(FacultadRepository repository, Validator validator) {
    	
    	setSizeFull();
        this.facultadRepository = repository;
        this.validator = validator;

        // ================= HEADER =================

        H1 titulo = new H1("Gestión de Facultades");
        titulo.setWidthFull();
        titulo.getStyle().set("text-align", "center");

        add(titulo);

        // ================= BUSCADOR =================

        configurarBuscador();

        // ================= GRID =================
        configurarGrid();

        // ================= FORM =================
        cargarFormulario();

        // ================= BOTONES =================
        configurarBotones();

    }
    
    
    
    // ================= CRUD =================
    private void agregarFacultad() {
    	
        Facultad nuevaFacultad = new Facultad();

        nuevaFacultad.setNombre(tfNombre.getValue());
        nuevaFacultad.setDireccion(tfDireccion.getValue());
        nuevaFacultad.setCuit(tfCuit.getValue());
        nuevaFacultad.setSucursal(ifSucursal.getValue());
        nuevaFacultad.setTelefonos(tfTelefonos.getValue());
        nuevaFacultad.setCorreos(tfCorreos.getValue());
        nuevaFacultad.setDefecto(cbDefecto.getValue());

        if (!validarFacultad(nuevaFacultad)) return;
        
        if (facultadRepository.existsByCuit(tfCuit.getValue())) {
            showNotificacion("Ya existe una facultad con ese CUIT", NotificationVariant.LUMO_ERROR);
            return;
        }
       
        facultadRepository.save(nuevaFacultad);
        
        actualizarGrid(tfBuscar.getValue());
        showNotificacion("Facultad agregada correctamente",NotificationVariant.LUMO_SUCCESS);
        limpiarFormulario();    
    }
    

    private void actualizarFacultad() {

        if ( facultadActual == null || facultadActual.getId() == null) {
            showNotificacion("Seleccione una facultad", NotificationVariant.LUMO_WARNING);
            return;
        }
        
        facultadActual.setNombre(tfNombre.getValue());
        facultadActual.setDireccion(tfDireccion.getValue());
        facultadActual.setCuit(tfCuit.getValue());
        facultadActual.setSucursal(ifSucursal.getValue());
        facultadActual.setTelefonos(tfTelefonos.getValue());
        facultadActual.setCorreos(tfCorreos.getValue());
        facultadActual.setDefecto(cbDefecto.getValue());

        if (!validarFacultad(facultadActual)) return;

        facultadRepository.save(facultadActual);

        actualizarGrid(tfBuscar.getValue());
        showNotificacion("Facultad actualizada correctamente", NotificationVariant.LUMO_SUCCESS);
        limpiarFormulario();
    }

    private void eliminarFacultad() {

        if (facultadActual == null || facultadActual.getId() == null) {
            showNotificacion("Seleccione una facultad", NotificationVariant.LUMO_WARNING);
            return;
        }

        facultadRepository.delete(facultadActual);
        
        actualizarGrid(tfBuscar.getValue());
        showNotificacion("Facultad eliminada correctamente", NotificationVariant.LUMO_SUCCESS);
        limpiarFormulario();
    }
    


    // ================= HELPERS =================
    
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
            if (facultadActual != null) cargarFormulario(facultadActual);
        });
        
        add(grid);
    }
    
    private void configurarBotones() {
        Button btnAgregar = new Button("Agregar", e -> agregarFacultad());
        btnAgregar.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);

        Button btnActualizar = new Button("Actualizar", e -> actualizarFacultad());
        btnActualizar.addClassName("btn-actualizar");

        Button btnEliminar = new Button("Eliminar", e -> eliminarFacultad());
        btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);

        Button btnLimpiarFormulario = new Button("Limpiar Formulario",e -> limpiarFormulario());
        btnLimpiarFormulario.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnLimpiarFormulario.getStyle().set("margin-left", "30px");

        HorizontalLayout acciones = new HorizontalLayout(btnAgregar, btnActualizar,btnEliminar,btnLimpiarFormulario);

        add(acciones);
    }


    private void cargarFormulario(Facultad f) {
        tfNombre.setValue(f.getNombre() != null ? f.getNombre() : "");
        tfDireccion.setValue(f.getDireccion() != null ? f.getDireccion() : "");
        tfCuit.setValue(f.getCuit() != null ? f.getCuit() : "");
        ifSucursal.setValue(f.getSucursal());
        tfTelefonos.setValue(f.getTelefonos() != null ? f.getTelefonos() : "");
        tfCorreos.setValue(f.getCorreos() != null ? f.getCorreos() : "");
        cbDefecto.setValue(f.getDefecto() != null ? f.getDefecto() : false);
    }

    private boolean validarFacultad(Facultad f) {
        var errores = validator.validate(f);
        if (!errores.isEmpty()) {
            String mensaje = errores.stream()
                    .map(e -> "<li>" + e.getMessage() + "</li>")
                    .collect(java.util.stream.Collectors.joining());
            showNotificacion("<b>Se encontraron errores:</b><ul>" + mensaje + "</ul>",NotificationVariant.LUMO_ERROR);
            return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        facultadActual = new Facultad();

        tfNombre.clear();
        tfDireccion.clear();
        tfCuit.clear();
        ifSucursal.clear();
        tfTelefonos.clear();
        tfCorreos.clear();
        cbDefecto.clear();

        grid.deselectAll();
    }

    private void actualizarGrid(String filtro) {

        if (filtro == null || filtro.isBlank()) {
            grid.setItems(facultadRepository.findAll(Sort.by("id").ascending()));
            return;
        }
        grid.setItems(facultadRepository.findByNombreContainingIgnoreCaseOrderByIdAsc(filtro));
    }

    
    private void showNotificacion(String msg, NotificationVariant variant) {

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

        HorizontalLayout contenido = new HorizontalLayout(icon, texto);

        contenido.setAlignItems(Alignment.CENTER);

        Notification n = new Notification(contenido);

        n.addThemeVariants(variant);
        n.setDuration(5000);
        n.setPosition(Notification.Position.MIDDLE);

        n.open();
    }
    
	
}
