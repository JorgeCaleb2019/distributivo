package com.distributivo.controller.security;

import com.distributivo.ejb.security.RolFacade;
import com.distributivo.model.security.Rol;
import com.distributivo.utilidadbase.controller.Mensaje;
import com.distributivo.utilidadbase.controller.AbstractController;
import com.distributivo.utilidadbase.controller.Table;
import com.tesis.utilidadbase.facade.StateEntity;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Toshiba
 */
@Named(value = "rolBean")
@ViewScoped
public class RolBean extends AbstractController<Rol> implements Serializable {

    @EJB
    private RolFacade rolFacade;

    public RolBean() {
        super(Rol.class);
    }

    @PostConstruct
    public void inicializar() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.setCurrentUser(auth.getName());
        this.setFacade(rolFacade);
        this.table = new Table<>(rolFacade);
        this.setClasePK(Integer.class);
        this.setTitle("Rol");
        this.table.load();
    }

    @Override
    public void changeView(String view, String id) {
        this.typeOfView = view;
        switch (view) {
            case "list":
                this.table.load();
                this.setPersistence(null);
                this.setTitle("Rol");
                break;
            case "create":
                this.title = "Rol / Nuevo";
                this.prepararCrear();
                break;
            case "edit":
                this.title = "Rol / Editar / " + this.getEntidad().getNombreRol();
                this.prepararEditar(id);
                break;
            case "delete":
                this.title = "Rol / Eliminar / " + this.getEntidad().getNombreRol();
                this.prepararAnular(id);
                break;
            case "view":
                this.prepararView(id);
                this.title = "Rol / " + this.getEntidad().getNombreRol();
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
            this.title = "Rol / Nuevo";
        } else {
            this.title = "Rol / " + this.getEntidad().getNombreRol();
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
            this.rolFacade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha modificado el rol: " + this.getEntidad().getNombreRol());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar modificar: " + this.getEntidad().getNombreRol() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void createEntity() {
        try {
            this.getEntidad().setFechaCreacion(new Date());
            this.getEntidad().setUsuarioCreacion(this.getCurrentUser());
            this.getEntidad().setEstado(StateEntity.ACTIVO.name());
            this.rolFacade.guardar(this.getEntidad());
            Mensaje.SUCESO("Se ha guardado el rol: " + this.getEntidad().getNombreRol());
            this.setEntidad(new Rol());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar guardar: " + this.getEntidad().getNombreRol() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void disableEntity() {
        try {
            this.getEntidad().setFechaModificacion(new Date());
            this.getEntidad().setUsuarioModificacion(this.getCurrentUser());
            this.getEntidad().setEstado(StateEntity.ANULADO.name());
            this.rolFacade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha anulado el rol: " + this.getEntidad().getNombreRol());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar anular: " + this.getEntidad().getNombreRol() + ".ERROR=" + ex.getMessage());
        }
    }
    @Override
    protected void enableEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
