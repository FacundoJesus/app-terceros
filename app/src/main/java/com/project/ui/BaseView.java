package com.project.ui;


import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class BaseView extends VerticalLayout {
	
	protected void mostrarNotificacion(String mensaje, NotificationVariant variant) {
		NotificationUtil.mostrar(mensaje,variant);
	}

}
