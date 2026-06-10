package com.project.ui.utils;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;

// PARA VENTANA DE DIALOGO
public class DialogUtil {

    private DialogUtil() {}

    public static void confirmar(String mensaje, Runnable accionConfirmada) {

        ConfirmDialog dialog = new ConfirmDialog();

        dialog.setHeader("Confirmación");
        dialog.setText(mensaje);

        dialog.setCancelable(true);
        dialog.setCancelText("Cancelar");

        dialog.setConfirmText("Aceptar");
        dialog.setConfirmButtonTheme("error primary");

        dialog.addConfirmListener(e -> accionConfirmada.run());

        dialog.open();
    }
    
}