package com.distributivo.controller.distributivo;

import com.distributivo.ejb.business.TipoActividadFacade;
import com.distributivo.model.business.TipoActividad;
import com.distributivo.utilidadbase.controller.AbstractController;
import com.distributivo.utilidadbase.controller.LazyDataModelAdvance;
import com.distributivo.utilidadbase.controller.Mensaje;
import com.distributivo.utilidadbase.controller.Table;
import com.tesis.utilidadbase.facade.StateEntity;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Named(value = "tipoActividadController")
@ViewScoped
public class TipoActividadController extends AbstractController<TipoActividad> implements Serializable {

    @EJB
    private TipoActividadFacade facade;

    private LazyDataModelAdvance<TipoActividad> tablaLazy;

    public TipoActividadController() {
        super(TipoActividad.class);
    }

    @PostConstruct
    public void inicializar() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.setCurrentUser(auth.getName());
        this.setFacade(facade);
        this.table = new Table<>(facade);
        this.setClasePK(Long.class);
        this.setTitle("Tipo de actividades");
        this.table.load();
        
    }

    @Override
    public void changeView(String view, String id) {
        this.typeOfView = view;
        switch (view) {
            case "list":
                this.setPersistence(null);
                this.setTitle("Tipo de actividad");
                break;
            case "create":
                this.title = "Tipo de actividad / Nuevo";
                this.prepararCrear();
                break;
            case "edit":
                this.title = "Tipo de actividad / Editar / " + this.getEntidad().getIdTipoActividad();
                this.prepararEditar(id);
                break;
            case "delete":
                this.title = "Tipo de actividad / Eliminar / " + this.getEntidad().getIdTipoActividad();
                this.prepararAnular(id);
                break;
            case "view":
                this.prepararView(id);
                this.title = "Tipo de actividad / " + this.getEntidad().getIdTipoActividad();
                break;
        }
    }

    @Override
    public void executeAction(ActionEvent evt) {
        switch (this.typeOfView) {
            case "create":
                this.createEntity();
                break;
            case "edit":
                this.editEntity();
                break;
            case "delete":
                this.disableEntity();
                break;
        }
        if (this.typeOfView.equals("create")) {
            this.title = "Tipo de actividad / Nuevo";
        } else {
            this.title = "Tipo de actividad / " + this.getEntidad().getIdTipoActividad();
            this.setPersistence(null);
            this.typeOfView = "view";
        }
    }
    
    @Override
    protected void editEntity() {
        try {
            this.facade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha modificado el Tipo de actividad: " + this.getEntidad().getIdTipoActividad());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar modificar: " + this.getEntidad().getIdTipoActividad() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void createEntity() {
        try {
            //this.getEntidad().setUsuarioCreacionAudi(this.getCurrentUser());
            this.getEntidad().setEstado(StateEntity.ACTIVO.name());
            this.facade.guardar(this.getEntidad());
            Mensaje.SUCESO("Se ha guardado el Tipo de actividad: " + this.getEntidad().getIdTipoActividad());
            this.setEntidad(new TipoActividad());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar guardar: " + this.getEntidad().getIdTipoActividad() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void disableEntity() {
        try {
            //this.getEntidad().setUsuarioModificacionAudi(this.getCurrentUser());
            this.getEntidad().setEstado(StateEntity.ANULADO.name());
            this.facade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha anulado el Tipo de actividad: " + this.getEntidad().getIdTipoActividad());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar anular: " + this.getEntidad().getIdTipoActividad() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void enableEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public LazyDataModelAdvance<TipoActividad> getTablaLazy() {
        return tablaLazy;
    }

    public void setTablaLazy(LazyDataModelAdvance<TipoActividad> tablaLazy) {
        this.tablaLazy = tablaLazy;
    }
    
}
