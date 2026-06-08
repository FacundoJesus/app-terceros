package com.project.ui;

import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.spring.security.AuthenticationContext;

import jakarta.annotation.security.PermitAll;


@Layout
@PermitAll
public final class MainLayout extends AppLayout {

    private final AuthenticationContext authenticationContext;

    public MainLayout(AuthenticationContext authenticationContext) {

        this.authenticationContext = authenticationContext;

        setPrimarySection(Section.DRAWER);
        addToDrawer(
            createApplicationHeader(),
            createApplicationDrawer(),
            createApplicationFooter()
        );
    }

    private Component createApplicationHeader() {

    	// LOGO
        var appLogo = new Avatar("Aplicación Terceros");
        appLogo.addClassName("app-logo");
        appLogo.addThemeVariants(
                AvatarVariant.AURA_FILLED,
                AvatarVariant.XSMALL
        );

        // NOMBRE APLICACION
        Span appName = new Span("App Terceros");
        appName.addClassName("app-name");

        // USUARIO LOGUEADO
        Span usuarioActual = new Span();
        authenticationContext
                .getAuthenticatedUser(UserDetails.class)
                .ifPresent(user ->
                        usuarioActual.setText("Usuario: " + user.getUsername())
                );
        usuarioActual.getStyle()
                .set("font-size", "var(--lumo-font-size-s)")
                .set("color", "var(--lumo-secondary-text-color)");

        // NOMBRE + USUARIO
        VerticalLayout appInfo = new VerticalLayout(
                appName,
                usuarioActual
        );

        appInfo.setSpacing(false);
        appInfo.setPadding(false);

        // BOTON TEMA OSCURO-CLARO
        Button btnTema = new Button(VaadinIcon.MOON.create());

        btnTema.addClickListener(e -> {

            UI.getCurrent().getPage().executeJs("""
                const html = document.documentElement;

                if (html.getAttribute('theme') === 'light') {
                    html.removeAttribute('theme');
                    return false;
                } else {
                    html.setAttribute('theme', 'light');
                    return true;
                }
            """).then(Boolean.class, dark -> {

                if (dark) {
                    btnTema.setIcon(VaadinIcon.SUN_O.create());
                } else {
                    btnTema.setIcon(VaadinIcon.MOON.create());
                }
            });
        });

        HorizontalLayout header = new HorizontalLayout();

        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setPadding(true);

        header.add(appLogo, appInfo);

        // Empuja el botón hacia la derecha
        header.expand(appInfo);

        header.add(btnTema);

        return header;
    }

    private Component createApplicationDrawer() {

        boolean esAdmin = authenticationContext
                .getAuthenticatedUser(UserDetails.class)
                .map(user -> user.getAuthorities()
                        .stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
                .orElse(false);

        SideNav navPrincipal = new SideNav();
        SideNav navAdmin = new SideNav();

        MenuConfiguration.getMenuEntries().forEach(entry -> {

            if ("Usuarios".equals(entry.title())) {
                navAdmin.addItem(createSideNavItem(entry));
            } else {
                navPrincipal.addItem(createSideNavItem(entry));
            }
        });

        VerticalLayout menu = new VerticalLayout();
        menu.add(navPrincipal);

        // 👇 SOLO ADMIN VE TODO ESTO
        if (esAdmin && !navAdmin.getItems().isEmpty()) {

            Span adminLabel = new Span("ADMINISTRACIÓN");
            adminLabel.getStyle()
                    .set("font-size", "var(--lumo-font-size-xs)")
                    .set("font-weight", "600")
                    .set("color", "var(--lumo-secondary-text-color)")
                    .set("margin-top", "1rem")
                    .set("margin-left", "0.5rem");

            menu.add(adminLabel, navAdmin);
        }

        menu.setSpacing(false);
        menu.setPadding(false);

        return new Scroller(menu);
    }
    private Component createApplicationFooter() {

        Button btnLogout = new Button("Cerrar sesión");

        btnLogout.addClickListener(event ->
            authenticationContext.logout()
        );

        VerticalLayout footer = new VerticalLayout(btnLogout);

        footer.setAlignItems(FlexComponent.Alignment.CENTER);

        return footer;
    }

    private SideNav createSideNav() {
        var nav = new SideNav();
        nav.setMinWidth(200, Unit.PIXELS);
        MenuConfiguration.getMenuEntries().forEach(entry -> nav.addItem(createSideNavItem(entry)));
        return nav;
    }

    private SideNavItem createSideNavItem(MenuEntry menuEntry) {
        if (menuEntry.icon() != null) {
            Component icon = null;
            if (menuEntry.icon().contains(".svg")) {
                icon = new SvgIcon(menuEntry.icon());
                
            } else {
                icon = new Icon(menuEntry.icon());
            }
            return new SideNavItem(menuEntry.title(), menuEntry.path(), icon);
        } else {
            return new SideNavItem(menuEntry.title(), menuEntry.path());
        }
    }
    
}
