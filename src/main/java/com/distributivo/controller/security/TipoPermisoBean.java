/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.controller.security;

import com.distributivo.ejb.security.TipoPermisoFacade;
import com.distributivo.model.security.TipoPermiso;
import com.distributivo.utilidadbase.controller.Mensaje;
import com.distributivo.utilidadbase.controller.AbstractController;
import com.distributivo.utilidadbase.controller.Table;
import com.tesis.utilidadbase.facade.StateEntity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Toshiba
 */
@Named(value = "tipoPermisoBean")
@SessionScoped
public class TipoPermisoBean extends AbstractController<TipoPermiso> implements Serializable {

    @EJB
    private TipoPermisoFacade tipoPermisoFacade;

    private List<TipoPermiso> tipoPermisoCombo;

    public TipoPermisoBean() {
        super(TipoPermiso.class);
    }

    @PostConstruct
    public void inicializar() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.setCurrentUser(auth.getName());
        this.setFacade(tipoPermisoFacade);
        this.table = new Table<>(tipoPermisoFacade);
        this.setClasePK(Integer.class);
        this.table.load();
    }

    @Override
    public void changeView(String view, String id) {
        this.typeOfView = view;
        switch (view) {
            case "list":
                this.table.load();
                this.setPersistence(null);
                break;
            case "create":
                this.title = "Tipo de Permiso / Nuevo";
                this.prepararCrear();
                break;
            case "edit":
                this.title = "Tipo de Permiso / Editar / " + this.getEntidad().getNombreTiper();
                this.prepararEditar(id);
                break;
            case "delete":
                this.title = "Tipo de Permiso / Eliminar / " + this.getEntidad().getNombreTiper();
                this.prepararAnular(id);
                break;
            case "view":
                this.prepararView(id);
                this.title = "Tipo de Permiso / " + this.getEntidad().getNombreTiper();
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
            this.title = "Tipo de Permiso / Nuevo";
        } else {
            this.title = "Tipo de Permiso / " + this.getEntidad().getNombreTiper();
            this.setPersistence(null);
            this.typeOfView = "view";
        }

        this.table.load();
    }

    @Override
    protected void editEntity() {
        try {
            this.getEntidad().setFechaModificacion(new Date());
            this.getEntidad().setUsuarioModificacion(this.getCurrentUser());
            this.tipoPermisoFacade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha modificado el tipo de permiso: " + this.getEntidad().getNombreTiper());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar modificar: " + this.getEntidad().getNombreTiper() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void createEntity() {
        try {
            this.getEntidad().setFechaCreacion(new Date());
            this.getEntidad().setUsuarioCreacion(this.getCurrentUser());
            this.getEntidad().setEstado(StateEntity.ACTIVO.name());
            this.tipoPermisoFacade.guardar(this.getEntidad());
            Mensaje.SUCESO("Se ha guardado el tipo de permiso: " + this.getEntidad().getNombreTiper());
            this.setEntidad(new TipoPermiso());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar guardar: " + this.getEntidad().getNombreTiper() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void disableEntity() {
        try {
            this.getEntidad().setFechaModificacion(new Date());
            this.getEntidad().setUsuarioModificacion(this.getCurrentUser());
            this.getEntidad().setEstado(StateEntity.ANULADO.name());
            this.tipoPermisoFacade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha anulado el tipo de permiso: " + this.getEntidad().getNombreTiper());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar anular: " + this.getEntidad().getNombreTiper() + ".ERROR=" + ex.getMessage());
        }
    }
    @Override
    protected void enableEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the tipoPermisoCombo
     */
    public List<TipoPermiso> getTipoPermisoCombo() {
        this.tipoPermisoCombo = this.tipoPermisoFacade.listarPorEstado(StateEntity.ACTIVO);
        return tipoPermisoCombo;
    }

    /**
     * @param tipoPermisoCombo the tipoPermisoCombo to set
     */
    public void setTipoPermisoCombo(List<TipoPermiso> tipoPermisoCombo) {
        this.tipoPermisoCombo = tipoPermisoCombo;
    }

    public TipoPermisoFacade getTipoPermisoFacade() {
        return tipoPermisoFacade;
    }

}
