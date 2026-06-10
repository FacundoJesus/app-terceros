package com.project.ui.utils;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

public class NotificationUtil {
	
	private NotificationUtil() {}

    public static void mostrar(String msg, NotificationVariant variant) {

        Icon icon;

        if (variant == NotificationVariant.LUMO_SUCCESS) {
            icon = VaadinIcon.CHECK_CIRCLE.create();
            icon.setColor("green");
        } else if (variant == NotificationVariant.LUMO_ERROR) {
            icon = VaadinIcon.CLOSE_CIRCLE.create();
            icon.setColor("red");
        } else {
            icon = VaadinIcon.WARNING.create();
            icon.setColor("orange");
        }

        Div text = new Div();
        text.setText(msg);

        HorizontalLayout layout = new HorizontalLayout(icon, text);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        Notification notification = new Notification(layout);
        notification.addThemeVariants(variant);
        notification.setDuration(4000);
        notification.setPosition(Notification.Position.MIDDLE);

        notification.open();
    }

}
