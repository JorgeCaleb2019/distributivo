package com.distributivo.controller.security;

import com.distributivo.ejb.security.GrupoFacade;
import com.distributivo.model.security.Grupo;
import com.distributivo.utilidadbase.controller.Mensaje;
import com.distributivo.utilidadbase.controller.AbstractController;
import com.distributivo.utilidadbase.controller.Table;
import com.tesis.utilidadbase.facade.StateEntity;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Named(value = "grupoBean")
@ViewScoped
public class GrupoBean extends AbstractController<Grupo> implements Serializable {

    @EJB
    private GrupoFacade grupoFacade;

    public GrupoBean() {
        super(Grupo.class);
    }

    @PostConstruct
    public void inicializar() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.setCurrentUser(auth.getName());
        this.setFacade(grupoFacade);
        this.table = new Table<>(grupoFacade);
        this.setClasePK(Integer.class);
        this.setTitle("Grupo");
        this.table.load();
    }

    @Override
    public void changeView(String view, String id) {
        this.typeOfView = view;
        switch (view) {
            case "list":
                this.table.load();
                this.setPersistence(null);
                this.setTitle("Grupo");
                break;
            case "create":
                this.title = "Grupo / Nuevo";
                this.prepararCrear();
                break;
            case "edit":
                this.title = "Grupo / Editar / " + this.getEntidad().getNombreGrup();
                this.prepararEditar(id);
                break;
            case "delete":
                this.title = "Grupo / Eliminar / " + this.getEntidad().getNombreGrup();
                this.prepararAnular(id);
                break;
            case "view":
                this.prepararView(id);
                this.title = "Grupo / " + this.getEntidad().getNombreGrup();
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
            this.title = "Grupo / Nuevo";
        } else {
            this.title = "Grupo / " + this.getEntidad().getNombreGrup();
            this.typeOfView = "view";
            this.setPersistence(null);
        }
        
        this.table.load();
    }

    @Override
    protected void editEntity() {
        try {
            this.getEntidad().setFechaModificacion(new Date());
            this.getEntidad().setUsuarioModificacion(this.getCurrentUser());
            this.grupoFacade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha modificado el grupo: " + this.getEntidad().getNombreGrup());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar modificar: " + this.getEntidad().getNombreGrup() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void createEntity() {
        try {
            this.getEntidad().setFechaCreacion(new Date());
            this.getEntidad().setUsuarioCreacion(this.getCurrentUser());
            this.getEntidad().setEstado(StateEntity.ACTIVO.name());
            this.grupoFacade.guardar(this.getEntidad());
            Mensaje.SUCESO("Se ha guardado el grupo: " + this.getEntidad().getNombreGrup());
            this.setEntidad(new Grupo());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar guardar: " + this.getEntidad().getNombreGrup() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void disableEntity() {
        try {
            this.getEntidad().setUsuarioModificacion(this.getCurrentUser());
            this.getEntidad().setFechaModificacion(new Date());
            this.getEntidad().setEstado(StateEntity.ANULADO.name());
            this.grupoFacade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha anulado el grupo: " + this.getEntidad().getNombreGrup());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar anular: " + this.getEntidad().getNombreGrup() + ".ERROR=" + ex.getMessage());
        }
    }
@Override
    protected void enableEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
