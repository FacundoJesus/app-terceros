package com.project.ui;

import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.models.Usuario;
import com.project.models.enums.RolUsuario;
import com.project.repositories.UsuarioRepository;
import com.project.ui.base.BaseView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"ADMIN"})
@Route(value = "usuarios", layout = MainLayout.class)
@PageTitle("Usuarios")
@Menu(order = 5, icon = "vaadin:user-star")
public class UsuarioView extends BaseView {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private Grid<Usuario> grid = new Grid<>(Usuario.class, false);

    private Usuario usuarioActual = null;

    private TextField tfNombreUsuario = new TextField("Nombre de Usuario");
    private PasswordField pfContraseña = new PasswordField("Contraseña");
    private TextField tfBuscar = new TextField();

    public UsuarioView(UsuarioRepository repository, PasswordEncoder passwordEncoder) {

        setSizeFull();

        this.usuarioRepository = repository;
        this.passwordEncoder = passwordEncoder;

        H1 titulo = new H1("Gestión de Usuarios");
        titulo.setWidthFull();
        titulo.getStyle().set("text-align", "center");
        add(titulo);

        configurarBuscador();
        
        configurarGrid();
        
        configurarFormulario();
        
        configurarBotones();

        limpiarFormulario();
    }

    // ================= CRUD =================

    private void agregarUsuario() {
        if (tfNombreUsuario.isEmpty()) {
            mostrarNotificacion("El nombre es obligatorio", NotificationVariant.LUMO_ERROR);
            return;
        }

        if (pfContraseña.isEmpty()) {
            mostrarNotificacion("La contraseña es obligatoria", NotificationVariant.LUMO_ERROR);
            return;
        }

        if (usuarioRepository.existsByNombreUsuario(tfNombreUsuario.getValue())) {
            mostrarNotificacion("El usuario ya existe", NotificationVariant.LUMO_ERROR);
            return;
        }

        Usuario nuevo = new Usuario();

        nuevo.setNombreUsuario(tfNombreUsuario.getValue());
        nuevo.setPassword(passwordEncoder.encode(pfContraseña.getValue()));
        nuevo.setRolUsuario(RolUsuario.USER);

        usuarioRepository.save(nuevo);

        actualizarGrid(tfBuscar.getValue());
        limpiarFormulario();
    }

    private void actualizarUsuario() {
        if (usuarioActual == null || usuarioActual.getId() == null) {
            mostrarNotificacion("Seleccione un usuario", NotificationVariant.LUMO_WARNING);
            return;
        }

        if (tfNombreUsuario.isEmpty()) {
            mostrarNotificacion("El nombre es obligatorio", NotificationVariant.LUMO_ERROR);
            return;
        }

        usuarioActual.setNombreUsuario(tfNombreUsuario.getValue());

        if (!pfContraseña.isEmpty()) {
            usuarioActual.setPassword(passwordEncoder.encode(pfContraseña.getValue()));
        }

        usuarioRepository.save(usuarioActual);

        actualizarGrid(tfBuscar.getValue());
        mostrarNotificacion("Usuario actualizado", NotificationVariant.LUMO_SUCCESS);
        limpiarFormulario();
    }

    private void eliminarUsuario() {
        if (usuarioActual == null || usuarioActual.getId() == null) {
            mostrarNotificacion("Seleccione un usuario", NotificationVariant.LUMO_WARNING);
            return;
        }

        usuarioRepository.delete(usuarioActual);

        actualizarGrid(tfBuscar.getValue());
        mostrarNotificacion("Usuario eliminado", NotificationVariant.LUMO_SUCCESS);
        limpiarFormulario();
    }

    // ================= GRID =================

    private void configurarGrid() {
        grid.addColumn(Usuario::getId).setHeader("ID");
        grid.addColumn(Usuario::getNombreUsuario).setHeader("Usuario");
        grid.addColumn(Usuario::getRolUsuario).setHeader("Rol");

        actualizarGrid(null);

        grid.asSingleSelect().addValueChangeListener(e -> {

            usuarioActual = e.getValue();

            if (usuarioActual != null) {
                tfNombreUsuario.setValue(usuarioActual.getNombreUsuario());
                pfContraseña.clear();
            } else {
                limpiarFormulario();
            }
        });

        add(grid);
    }

    private void actualizarGrid(String filtro) {

        if (filtro == null || filtro.isBlank()) {
            grid.setItems(usuarioRepository.findAll(Sort.by("id").ascending()));
        } else {
            grid.setItems(
                usuarioRepository.findByNombreUsuarioContainingIgnoreCase(filtro)
            );
        }
    }

    // ================= FORM =================

    private void configurarFormulario() {

        FormLayout form = new FormLayout();

        form.add(tfNombreUsuario, pfContraseña);

        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("800px", 2)
        );

        add(form);
    }

    private void limpiarFormulario() {

        usuarioActual = null;

        tfNombreUsuario.clear();
        pfContraseña.clear();

        grid.deselectAll();
    }

    // ================= BOTONES =================

    private void configurarBotones() {

        Button btnAgregar = new Button("Agregar", VaadinIcon.PLUS.create(), e -> agregarUsuario());
        btnAgregar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        Button btnActualizar = new Button("Actualizar", VaadinIcon.EDIT.create(), e -> actualizarUsuario());
        btnActualizar.addClassName("btn-actualizar");
        
        Button btnEliminar = new Button("Eliminar", VaadinIcon.TRASH.create(), e -> eliminarUsuario());
        btnEliminar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        Button btnLimpiar = new Button("Limpiar Formulario", VaadinIcon.ERASER.create(), e -> limpiarFormulario());
        btnLimpiar.getStyle().set("margin-left", "30px");
        
        HorizontalLayout acciones = new HorizontalLayout(
                btnAgregar,
                btnActualizar,
                btnEliminar,
                btnLimpiar
        );

        add(acciones);
    }

    // ================= BUSCADOR =================

    private void configurarBuscador() {

        tfBuscar.setPlaceholder("Buscar usuario...");
        tfBuscar.setClearButtonVisible(true);
        tfBuscar.setWidth("33%");

        tfBuscar.addValueChangeListener(e -> actualizarGrid(e.getValue()));

        add(tfBuscar);
    }

}