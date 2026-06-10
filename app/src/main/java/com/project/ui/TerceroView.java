package com.project.ui;

import org.springframework.data.domain.Sort;
import com.project.models.Tercero;
import com.project.models.enums.SituacionIVA;
import com.project.models.enums.TipoSaldo;
import com.project.repositories.TerceroRepository;
import com.project.ui.base.BaseView;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;


@RolesAllowed({"USER","ADMIN"})
@Route(value = "terceros", layout = MainLayout.class)
@PageTitle("Terceros")
@Menu(order = 2, icon = "vaadin:users")
public class TerceroView extends BaseView {

    private final TerceroRepository terceroRepository;
    
    private final BeanValidationBinder<Tercero> binder = new BeanValidationBinder<>(Tercero.class);
    private Grid<Tercero> gridTercero = new Grid(Tercero.class, false);
    
    private Tercero nuevoTercero = new Tercero();

    private TextField tfNombre = new TextField("Nombre");
    private TextField tfCuitl = new TextField("CUIT/L");
    private ComboBox <SituacionIVA> cbSituacionIva = new ComboBox<>("Condición IVA");
    private TextField tfDireccion = new TextField("Dirección");
    private TextField tfLocalidad = new TextField("Localidad");
    private TextField tfProvincia = new TextField("Provincia");
    private TextField tfTelefonos = new TextField("Teléfonos");
    private BigDecimalField bdfSaldoApertura = new BigDecimalField("Saldo Apertura");
    private ComboBox <TipoSaldo> cbTipoSaldo = new ComboBox<>("Tipo Saldo");
    private TextField tfBuscar = new TextField();

    public TerceroView(TerceroRepository repository) {
    	
    	setSizeFull();
        this.terceroRepository = repository;

        configurarBinder();
        
        // ================= TITULO =================
        add(crearTitulo("Gestion de Terceros"));
        // ================= BUSCADOR =================
        add(crearBuscador(tfBuscar));
        
        // ================= GRID TERCEROS =================        
        configurarGrid();

        // ================= FORMULARIO =================
        configurarFormulario();
        
        // ================= BOTONES =================
        add(crearBotonesCrud(
                e -> agregarTercero(),
                e -> actualizarTercero(),
                e -> eliminarTercero(),
                e -> limpiarFormulario()));
         
        limpiarFormulario();
    }

    
    // ================= CRUD =================
	private void agregarTercero() {
  	
    	if(!binder.validate().isOk()) return;
    	
    	Tercero nuevoTercero = new Tercero();
    	
    	try {
    		binder.writeBean(nuevoTercero);
    		
    		if (terceroRepository.existsByCuitl(nuevoTercero.getCuitl())) {
                mostrarNotificacion("Ya existe un tercero con ese CUIT", NotificationVariant.LUMO_ERROR);
                return;
            }

            terceroRepository.save(nuevoTercero);
            
            actualizarGrid(tfBuscar.getValue());
            mostrarNotificacion("Tercero agregado", NotificationVariant.LUMO_SUCCESS);
            limpiarFormulario(); 
            
    	} catch (ValidationException e) {
        	mostrarNotificacion("Error al llenar el Formulario",NotificationVariant.LUMO_ERROR);
        }	
	}

    private void actualizarTercero() {

        if (nuevoTercero == null || nuevoTercero.getId() == null) {
            mostrarNotificacion("Seleccione un tercero", NotificationVariant.LUMO_WARNING);
            return;
        }
        
        if(!binder.validate().isOk()) return;
    	
        terceroRepository.save(nuevoTercero);

        actualizarGrid(tfBuscar.getValue());
        mostrarNotificacion("Tercero actualizado", NotificationVariant.LUMO_SUCCESS);
        limpiarFormulario();     	
    }

    private void eliminarTercero() {

        if (nuevoTercero == null || nuevoTercero.getId() == null) {
            mostrarNotificacion("Seleccione un tercero", NotificationVariant.LUMO_WARNING);
            return;
        }
        
        mostrarVentanaDialogo("También se eliminarán las facturas y pagos asociados.",
                () -> {
                    terceroRepository.delete(nuevoTercero);

                    actualizarGrid(tfBuscar.getValue());
                    mostrarNotificacion("Tercero eliminado", NotificationVariant.LUMO_SUCCESS);
                    limpiarFormulario();
                }
            );
    }

    // ================= HELPERS =================
    private void configurarBinder() {
		binder.forField(tfNombre).bind("nombre");
		binder.forField(tfCuitl).bind("cuitl");
		binder.forField(cbSituacionIva).bind("sitiva");
		binder.forField(tfDireccion).bind("direccion");
		binder.forField(tfLocalidad).bind("localidad");
		binder.forField(tfProvincia).bind("provincia");
		binder.forField(tfTelefonos).bind("telefonos");
		binder.forField(bdfSaldoApertura).bind("saldoApertura");
		binder.forField(cbTipoSaldo).bind("tipoSaldo");
	}
    
    private void configurarGrid() {
    	gridTercero.addColumn(Tercero::getId).setHeader("ID");
        gridTercero.addColumn(Tercero::getNombre).setHeader("Nombre");
        gridTercero.addColumn(Tercero::getCuitl).setHeader("CUIT/L");
        gridTercero.addColumn(Tercero::getSitiva).setHeader("IVA");
        gridTercero.addColumn(Tercero::getDireccion).setHeader("Dirección");
        gridTercero.addColumn(Tercero::getLocalidad).setHeader("Localidad");
        gridTercero.addColumn(Tercero::getProvincia).setHeader("Provincia");
        gridTercero.addColumn(Tercero::getTelefonos).setHeader("Teléfonos");
        gridTercero.addColumn(Tercero::getSaldoApertura).setHeader("Saldo");
        gridTercero.addColumn(Tercero::getTipoSaldo).setHeader("Tipo Saldo");
        
        actualizarGrid(null);

        gridTercero.asSingleSelect().addValueChangeListener(e -> {
            nuevoTercero = e.getValue();
            if (nuevoTercero != null) binder.setBean(nuevoTercero);
        });

        add(gridTercero);
    }
    
    private void actualizarGrid(String filtroNombre) {
        if (filtroNombre == null || filtroNombre.isBlank()) {
            gridTercero.setItems(terceroRepository.findAll(Sort.by("id").ascending()));
            return;
        }
        gridTercero.setItems(terceroRepository.findByNombreContainingIgnoreCaseOrderByIdAsc(filtroNombre));
    }
    
    private void configurarFormulario() {
    	cbTipoSaldo.setItems(TipoSaldo.values());
        cbSituacionIva.setItems(SituacionIVA.values());
        
        FormLayout form = new FormLayout();
        form.add(tfNombre,tfCuitl,cbSituacionIva,tfDireccion,tfLocalidad,tfProvincia,tfTelefonos,bdfSaldoApertura,cbTipoSaldo);
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),new FormLayout.ResponsiveStep("800px", 3));
        
        add(form);
    }

    private void limpiarFormulario() {
        nuevoTercero = new Tercero();  
        binder.setBean(nuevoTercero);
        gridTercero.deselectAll();
    }

}