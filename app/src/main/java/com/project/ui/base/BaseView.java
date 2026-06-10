package com.project.ui.base;


import com.project.ui.utils.DialogUtil;
import com.project.ui.utils.NotificationUtil;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
public abstract class BaseView extends VerticalLayout {
	
	// MENSAJE EXITO O ERROR
	protected void mostrarNotificacion(String mensaje, NotificationVariant variant) {
		NotificationUtil.mostrar(mensaje,variant);
	}
	
	// VENTANA DE CONFIRMACION
	protected void mostrarVentanaDialogo(String mensaje, Runnable accionConfirmada) {
		DialogUtil.confirmar(mensaje, accionConfirmada);
	}
	

	//TITULO
	protected H1 crearTitulo(String texto) {

	    H1 titulo = new H1(texto);

	    titulo.setWidthFull();
	    titulo.getStyle().set("text-align", "center");

	    return titulo;
	}
	
	//SUBTITULO
	protected H3 crearSubtitulo(String texto) {

	    H3 titulo = new H3(texto);

	    titulo.setWidthFull();

	    titulo.getStyle().set("text-align", "center");

	    return titulo;
	}
	
	// BUSCADOR
	protected HorizontalLayout crearBuscador(TextField campoBuscar) {

	    campoBuscar.setPlaceholder("Buscar por nombre...");
	    campoBuscar.setClearButtonVisible(true);
	    campoBuscar.setWidth("33%");

	    HorizontalLayout layout = new HorizontalLayout(campoBuscar);

	    layout.setWidthFull();
	    layout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
	    layout.setAlignItems(FlexComponent.Alignment.START);

	    return layout;
	}

	// BOTONES CRUD
	protected HorizontalLayout crearBotonesCrud(
	        ComponentEventListener<ClickEvent<Button>> agregar,
	        ComponentEventListener<ClickEvent<Button>> actualizar,
	        ComponentEventListener<ClickEvent<Button>> eliminar,
	        ComponentEventListener<ClickEvent<Button>> limpiar) {

	    Button btnAgregar = new Button("Agregar", VaadinIcon.PLUS.create(),agregar);
	    btnAgregar.addThemeVariants(ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_PRIMARY);

	    Button btnActualizar = new Button("Actualizar", VaadinIcon.EDIT.create(), actualizar);
	    btnActualizar.addClassName("btn-actualizar");
	    
	    Button btnEliminar = new Button("Eliminar", VaadinIcon.TRASH.create(), eliminar);

	    btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_PRIMARY);

	    Button btnLimpiar = new Button("Limpiar Formulario", VaadinIcon.ERASER.create(), limpiar);
	    btnLimpiar.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
	    btnLimpiar.getStyle().set("margin-left", "30px");

	    return new HorizontalLayout( btnAgregar,btnActualizar,btnEliminar,btnLimpiar);
	}
	
	
	
}
