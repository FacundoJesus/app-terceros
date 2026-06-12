package com.project.ui;

import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.models.Usuario;
import com.project.models.enums.RolUsuario;
import com.project.repositories.UsuarioRepository;
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

	private static final long serialVersionUID = 1L;
	
	private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private Grid<Usuario> grid = new Grid<>(Usuario.class, false);

    private Usuario usuarioActual = null;

    private TextField tfNombreUsuario = new TextField("Nombre de Usuario");
    private PasswordField pfContraseña = new PasswordField("Contraseña");
    private ComboBox<RolUsuario> cbRolUser = new ComboBox<>("Rol Usuario");
    private TextField tfBuscar = new TextField();

    public UsuarioView(UsuarioRepository userRepository, PasswordEncoder passwordEncoder) {

        setSizeFull();
        this.usuarioRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

        // ================= TITULO =================
        add(crearTitulo("Gestión de Usuarios"));

        // ================= BUSCADOR =================
        configurarBuscador();
        
        // ================= GRID =================
        configurarGrid();
        
        configurarFormulario();
        
        // ================= BOTONES =================
        add(crearBotonesCrud(
        		e -> agregarUsuario(),
		        e -> actualizarUsuario(),
		        e -> eliminarUsuario(),
		        e -> limpiarFormulario()
		        ));
 
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
            mostrarNotificacion("El usuario con ese nombre ya existe", NotificationVariant.LUMO_ERROR);
            return;
        }

        Usuario nuevo = new Usuario();

        nuevo.setNombreUsuario(tfNombreUsuario.getValue());
        nuevo.setPassword(passwordEncoder.encode(pfContraseña.getValue()));
        nuevo.setRolUsuario(cbRolUser.getValue());

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

    private void configurarGrid() {
        grid.addColumn(Usuario::getId).setHeader("ID");
        grid.addColumn(Usuario::getNombreUsuario).setHeader("Usuario");
        grid.addColumn(Usuario::getPassword).setHeader("Contraseña");
        grid.addColumn(Usuario::getRolUsuario).setHeader("Rol");

        actualizarGrid(null);

        grid.asSingleSelect().addValueChangeListener(e -> {

            usuarioActual = e.getValue();

            if (usuarioActual != null) {
                tfNombreUsuario.setValue(usuarioActual.getNombreUsuario());
                cbRolUser.setValue(usuarioActual.getRolUsuario());
                tfNombreUsuario.setValue(usuarioActual.getNombreUsuario());
                pfContraseña.setValue(usuarioActual.getPassword());
                
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
        cbRolUser.setItems(RolUsuario.values());

        form.add(tfNombreUsuario, pfContraseña, cbRolUser);

        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("800px", 3)
        );

        add(form);
    }

    private void limpiarFormulario() {
        usuarioActual = null;

        tfNombreUsuario.clear();
        pfContraseña.clear();
        cbRolUser.clear();
        
        grid.deselectAll();
    }

    private void configurarBuscador() {

        tfBuscar.setPlaceholder("Buscar usuario...");
        tfBuscar.setClearButtonVisible(true);
        tfBuscar.setWidth("33%");

        tfBuscar.addValueChangeListener(e -> actualizarGrid(e.getValue()));

        add(tfBuscar);
    }

}