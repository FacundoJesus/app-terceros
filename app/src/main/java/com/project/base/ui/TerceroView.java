package com.project.base.ui;

import java.math.BigDecimal;

import org.springframework.data.domain.Sort;

import com.project.models.SituacionIVA;
import com.project.models.Tercero;
import com.project.models.TipoSaldo;
import com.project.repositories.TerceroRepository;
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
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

import jakarta.validation.Validator;

@Route(value = "terceros", layout = MainLayout.class)
@PageTitle("Terceros")
@Menu(order = 2, icon = "vaadin:users")
public class TerceroView extends VerticalLayout {

    private final TerceroRepository terceroRepository;
    private final Validator validator;
    
    private Grid<Tercero> grid = new Grid(Tercero.class, false);

    private Tercero terceroActual = new Tercero();

    // ================= FORM =================
    private TextField tfNombre = new TextField("Nombre");
    private TextField tfCuitl = new TextField("CUIT/L");
    private ComboBox <SituacionIVA> cbSituacionIva = new ComboBox<>("Condición IVA");
    private TextField tfDireccion = new TextField("Dirección");
    private TextField tfLocalidad = new TextField("Localidad");
    private TextField tfProvincia = new TextField("Provincia");
    private TextField tfTelefonos = new TextField("Teléfonos");
    private IntegerField tfSaldo = new IntegerField("Saldo Apertura");
    private ComboBox <TipoSaldo> cbTipoSaldo = new ComboBox<>("Tipo Saldo");

    private TextField tfBuscar = new TextField();

    public TerceroView(TerceroRepository repository, Validator validator) {
    	
    	setSizeFull();
    	
        this.terceroRepository = repository;
        this.validator = validator;

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
    }

    
    // ================= CRUD =================

    private void agregarTercero() {

    	Tercero nuevoTercero = new Tercero();
    	
        nuevoTercero.setNombre(tfNombre.getValue());
        nuevoTercero.setCuitl(tfCuitl.getValue());
        nuevoTercero.setSitiva(cbSituacionIva.getValue());
        nuevoTercero.setDireccion(tfDireccion.getValue());
        nuevoTercero.setLocalidad(tfLocalidad.getValue());
        nuevoTercero.setProvincia(tfProvincia.getValue());
        nuevoTercero.setTelefonos(tfTelefonos.getValue());
        nuevoTercero.setSaldoApertura(tfSaldo.getValue() != null ? BigDecimal.valueOf(tfSaldo.getValue()): null);
        nuevoTercero.setTipoSaldo(cbTipoSaldo.getValue());
    	
        if (!validarTercero(terceroActual)) return;

        if (terceroRepository.existsByCuitl(nuevoTercero.getCuitl())) {
            showNotificacion("Ya existe un tercero con ese CUIT", NotificationVariant.LUMO_ERROR);
            return;
        }

        terceroRepository.save(nuevoTercero);

        actualizarGrid(tfBuscar.getValue());
        showNotificacion("Tercero agregado", NotificationVariant.LUMO_SUCCESS);
        limpiarFormulario();         
    }

    private void actualizarTercero() {

        if (terceroActual == null || terceroActual.getId() == null) {
            showNotificacion("Seleccione un tercero", NotificationVariant.LUMO_WARNING);
            return;
        }
      
        terceroActual.setNombre(tfNombre.getValue());
        terceroActual.setCuitl(tfCuitl.getValue());
        terceroActual.setSitiva(cbSituacionIva.getValue());
        terceroActual.setDireccion(tfDireccion.getValue());
        terceroActual.setLocalidad(tfLocalidad.getValue());
        terceroActual.setProvincia(tfProvincia.getValue());
        terceroActual.setTelefonos(tfTelefonos.getValue());
        terceroActual.setSaldoApertura(tfSaldo.getValue() != null ? BigDecimal.valueOf(tfSaldo.getValue()): null);
        terceroActual.setTipoSaldo(cbTipoSaldo.getValue());
        
        if (!validarTercero(terceroActual)) return;
        
        terceroRepository.save(terceroActual);

        actualizarGrid(tfBuscar.getValue());
        showNotificacion("Tercero actualizado", NotificationVariant.LUMO_SUCCESS);
        limpiarFormulario();   
    }

    private void eliminarTercero() {

        if (terceroActual.getId() == null) {
            showNotificacion("Seleccione un tercero", NotificationVariant.LUMO_WARNING);
            return;
        }

        terceroRepository.delete(terceroActual);

        actualizarGrid(tfBuscar.getValue());
        showNotificacion("Tercero eliminado", NotificationVariant.LUMO_SUCCESS);
        limpiarFormulario();
    }

    // ================= HELPERS =================
    
    private void configurarBotones() {
    	Button btnAgregar = new Button("Agregar", e -> agregarTercero());
        btnAgregar.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.PRIMARY);

        Button btnActualizar = new Button("Actualizar", e -> actualizarTercero());      
        btnActualizar.addClassName("btn-actualizar");
        
        Button btnEliminar = new Button("Eliminar", e -> eliminarTercero());
        btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.PRIMARY);
        
        Button btnLimpiarFormulario = new Button("Limpiar Formulario", e -> limpiarFormulario());
        btnLimpiarFormulario.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnLimpiarFormulario.getStyle().set("margin-left", "30px");

        HorizontalLayout acciones = new HorizontalLayout(btnAgregar, btnActualizar, btnEliminar, btnLimpiarFormulario);

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
                tfSaldo,
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
            terceroActual = e.getValue();
            if (terceroActual != null) cargarFormulario(terceroActual);
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
    
       
    private void cargarFormulario(Tercero t) {
        tfNombre.setValue(t.getNombre() != null ? t.getNombre() : "");
        tfCuitl.setValue(t.getCuitl() != null ? t.getCuitl() : "");
        cbSituacionIva.setValue(t.getSitiva());
        tfDireccion.setValue(t.getDireccion() != null ? t.getDireccion() : "");
        tfLocalidad.setValue(t.getLocalidad() != null ? t.getLocalidad() : "");
        tfProvincia.setValue(t.getProvincia() != null ? t.getProvincia() : "");
        tfTelefonos.setValue(t.getTelefonos() != null ? t.getTelefonos() : "");
        tfSaldo.setValue(t.getSaldoApertura() != null ? t.getSaldoApertura().intValue() : null);
        cbTipoSaldo.setValue(t.getTipoSaldo());
    }

    
    private boolean validarTercero(Tercero t) {
    	
    	var errores = validator.validate(t);

        if (!errores.isEmpty()) {
        	String mensaje = errores.stream()
        	        .map(e -> "<li>" + e.getMessage() + "</li>")
        	        .collect(java.util.stream.Collectors.joining());

        	showNotificacion("<b>Se encontraron errores:</b><ul>" + mensaje + "</ul>", NotificationVariant.LUMO_ERROR);
            return false;
        }
        return true;
    }

    
    private void limpiarFormulario() {
        terceroActual = new Tercero();

        tfNombre.clear();
        tfCuitl.clear();
        cbSituacionIva.clear();
        tfDireccion.clear();
        tfLocalidad.clear();
        tfProvincia.clear();
        tfTelefonos.clear();
        tfSaldo.clear();
        cbTipoSaldo.clear();
        
        grid.deselectAll();
    }

    
    private void actualizarGrid(String filtroNombre) {

        if (filtroNombre == null || filtroNombre.isBlank()) {
            grid.setItems(terceroRepository.findAll(Sort.by("id").ascending()));
            return;
        }
        grid.setItems(terceroRepository.findByNombreContainingIgnoreCaseOrderByIdAsc(filtroNombre));
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

        HorizontalLayout layout = new HorizontalLayout(icon, texto);
        layout.setAlignItems(Alignment.CENTER);

        Notification n = new Notification(layout);
        n.addThemeVariants(variant);
        n.setPosition(Notification.Position.MIDDLE);
        n.setDuration(5000);

        n.open();
    }
}