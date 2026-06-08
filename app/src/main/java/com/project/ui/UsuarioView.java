package com.project.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"ADMIN"})
@Route(value = "usuarios", layout = MainLayout.class)
@PageTitle("Usuarios")
@Menu(order = 5, icon = "vaadin:user-star")
public class UsuarioView extends VerticalLayout {
	
	public UsuarioView() {
		
	}
	
}