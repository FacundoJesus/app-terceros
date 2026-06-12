package com.project.ui;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.server.auth.AnonymousAllowed;


@Route(value = "login", autoLayout = false)
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends Main implements BeforeEnterObserver {

	private static final long serialVersionUID = 1L;
	private final LoginForm login;

    public LoginView() {

        login = new LoginForm();
        login.setAction("login");

        H2 titulo = new H2("App Terceros");
        titulo.getStyle()
                .set("margin-top", "0")
                .set("text-align", "center");

        Span subtitulo = new Span("Inicio Sesión");
        subtitulo.getStyle()
                .set("color", "var(--lumo-secondary-text-color)");
        
        Avatar avatar = new Avatar("A T");
        avatar.addThemeVariants(
            AvatarVariant.AURA_FILLED,
            AvatarVariant.LARGE
        );

        VerticalLayout card = new VerticalLayout(
        		avatar,
                titulo,
                subtitulo,
                login
        );

        card.setAlignItems(FlexComponent.Alignment.CENTER);
        card.setPadding(true);
        card.setSpacing(true);

        card.getStyle()
        .set("border", "1px solid rgb(128, 128, 255)")
        .set("border-radius", "12px")
        .set("padding", "20px");

        card.setWidth("450px");
        VerticalLayout layout = new VerticalLayout(card);

        layout.setSizeFull();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        add(layout);
        setSizeFull();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {

            login.setError(true);
        }
    }
}