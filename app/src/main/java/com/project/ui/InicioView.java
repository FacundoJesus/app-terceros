package com.project.ui;


import com.project.repositories.FacturaRepository;
import com.project.repositories.FacultadRepository;
import com.project.repositories.PagoRepository;
import com.project.repositories.TerceroRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"USER","ADMIN"})
@Route(value = "/", layout = MainLayout.class)
@PageTitle("Inicio")
@Menu(order = 0, icon = "vaadin:home")
public class InicioView extends VerticalLayout {

    public InicioView(FacultadRepository facultadRepository, TerceroRepository terceroRepository,
    				  FacturaRepository facturaRepository, PagoRepository pagoRepository) {

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        getStyle().set("padding", "40px");

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        // ================= TITULO =================

        H1 titulo = new H1("Panel");

        Span subtitulo = new Span("Bienvenido al Sistema de Gestión");

        subtitulo.getStyle()
                .set("opacity", "0.7")
                .set("font-size", "1rem");

        add(titulo, subtitulo);

        // ================= KPI =================

        HorizontalLayout indicadores = new HorizontalLayout();

        indicadores.setWidthFull();
        indicadores.setSpacing(true);

        VerticalLayout cardFacultades = crearCard(
                "Facultades",
                String.valueOf(facultadRepository.count()),
                VaadinIcon.ACADEMY_CAP,
                "facultades"
        );

        VerticalLayout cardTerceros = crearCard(
                "Terceros",
                String.valueOf(terceroRepository.count()),
                VaadinIcon.USERS,
                "terceros"
        );

        VerticalLayout cardFacturas = crearCard(
                "Facturas",
                String.valueOf(facturaRepository.count()),
                VaadinIcon.INVOICE,
                "facturas"
        );

        VerticalLayout cardPagos = crearCard(
                "Pagos",
                String.valueOf(pagoRepository.count()),
                VaadinIcon.MONEY,
                "pagos"
        );

        indicadores.add(
                cardFacultades,
                cardTerceros,
                cardFacturas,
                cardPagos
        );

        indicadores.expand(
                cardFacultades,
                cardTerceros,
                cardFacturas,
                cardPagos
        );

        add(indicadores);

        // ================= ACCESOS RAPIDOS =================

        H3 accesosTitulo = new H3("Accesos rápidos");

        Button btnFacultades = new Button("Facultades",VaadinIcon.BUILDING.create());

        Button btnTerceros = new Button("Terceros",VaadinIcon.USERS.create());

        Button btnFacturas = new Button("Facturas",VaadinIcon.FILE_TEXT.create());

        Button btnPagos = new Button("Pagos",VaadinIcon.MONEY.create());

        btnFacultades.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnTerceros.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnFacturas.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnPagos.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        btnFacultades.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("facultades")));

        btnTerceros.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("terceros")));

        btnFacturas.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("facturas")));

        btnPagos.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("pagos")));

        HorizontalLayout accesos = new HorizontalLayout(btnFacultades, btnTerceros, btnFacturas, btnPagos);

        accesos.setJustifyContentMode(JustifyContentMode.CENTER);
        
        accesos.setSpacing(true);
        accesos.getStyle().set("gap", "40px");
        add(accesosTitulo, accesos);

        // ================= MENSAJE =================

        Span mensaje = new Span("Utilice los accesos rápidos para administrar facultades, terceros, facturas y pagos.");

        mensaje.getStyle()
                .set("margin-top", "20px")
                .set("font-size", "1rem")
                .set("opacity", "0.8");

        add(mensaje);
    }

    private VerticalLayout crearCard(String titulo,String valor, VaadinIcon icono, String ruta) {

    	Icon icon = icono.create();

    	icon.setSize("60px");

        icon.getStyle()
                .set("font-size", "60px")
                .set("margin-bottom", "10px");

        
        H3 tituloCard = new H3(titulo);

        tituloCard.getStyle()
                .set("margin", "0")
        		.set("font-size", "20px");
        

        Span valorCard = new Span(valor);

        valorCard.getStyle()
                .set("font-size", "40px")
                .set("font-weight", "700")
                .set("margin-top", "20px");

        VerticalLayout card = new VerticalLayout(
                icon,
                tituloCard,
                valorCard
        );

        card.setWidthFull();

        card.setAlignItems(
                FlexComponent.Alignment.CENTER);

        card.setJustifyContentMode(
                FlexComponent.JustifyContentMode.CENTER);

        card.setSpacing(false);

        card.addClassName("dashboard-card");

        card.addClickListener(e ->
                getUI().ifPresent(ui -> ui.navigate(ruta)));

        return card;
    }
    
    
}