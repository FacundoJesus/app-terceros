package com.project.ui;

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

@Layout
public final class MainLayout extends AppLayout {

    MainLayout() {
        setPrimarySection(Section.DRAWER);
        addToDrawer(createApplicationHeader(), createApplicationDrawer(), createApplicationFooter());
    }

    private Component createApplicationHeader() {

        var appLogo = new Avatar("Aplicación Terceros");
        appLogo.addClassName("app-logo");
        appLogo.addThemeVariants(
                AvatarVariant.AURA_FILLED,
                AvatarVariant.XSMALL
        );

        var appName = new Span("App Terceros");
        appName.addClassName("app-name");

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

        header.add(appLogo, appName);

        // Empuja el botón a la derecha
        header.expand(appName);

        header.add(btnTema);

        return header;
    }

    private Component createApplicationDrawer() {
        var scroller = new Scroller(createSideNav());
        scroller.addThemeVariants(ScrollerVariant.OVERFLOW_INDICATORS);
        return scroller;
    }

    private Component createApplicationFooter() {
        var footer = new VerticalLayout(new Span("Citera, Facundo Jesús"));
        footer.setAlignItems(FlexComponent.Alignment.CENTER);
        footer.addClassName("app-footer");
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
