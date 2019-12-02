package com.distributivo.controller.security;


import com.distributivo.ejb.security.PermisoFacade;
import com.distributivo.model.security.Permiso;
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
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Toshiba
 */
@Named(value = "permisoBean")
@ViewScoped
public class PermisoBean extends AbstractController<Permiso> implements Serializable {

    @EJB
    private PermisoFacade  permisoFacade;

    private List<Permiso> permisoCombo;

    public PermisoBean() {
        super(Permiso.class);
    }

    @PostConstruct
    public void inicializar() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.setCurrentUser(auth.getName());
        this.setFacade(permisoFacade);
        this.table = new Table<>(permisoFacade);
        this.setClasePK(Integer.class);
        this.setTitle("Permiso");
        this.table.load();
    }

    @Override
    public void changeView(String view, String id) {
        this.typeOfView = view;
        switch (view) {
            case "list":
                this.table.load();
                this.setPersistence(null);
                this.setTitle("Permiso");
                break;
            case "create":
                this.title = "Permiso / Nuevo";
                this.prepararCrear();
                break;
            case "edit":
                this.title = "Permiso / Editar / " + this.getEntidad().getNombrePerm();
                this.prepararEditar(id);
                break;
            case "delete":
                this.title = "Permiso / Eliminar / " + this.getEntidad().getNombrePerm();
                this.prepararAnular(id);
                break;
            case "view":
                this.prepararView(id);
                this.title = "Permiso / " + this.getEntidad().getNombrePerm();
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
            this.title = "Permiso / Nuevo";
        } else {
            this.title = "Permiso / " + this.getEntidad().getNombrePerm();
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
            this.permisoFacade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha modificado el permiso: " + this.getEntidad().getNombrePerm());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar modificar: " + this.getEntidad().getNombrePerm() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void createEntity() {
        try {
            this.getEntidad().setFechaCreacion(new Date());
            this.getEntidad().setUsuarioCreacion(this.getCurrentUser());
            this.getEntidad().setEstado(StateEntity.ACTIVO.name());
            this.permisoFacade.guardar(this.getEntidad());
            Mensaje.SUCESO("Se ha guardado el permiso: " + this.getEntidad().getNombrePerm());
            this.setEntidad(new Permiso());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar guardar: " + this.getEntidad().getNombrePerm() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void disableEntity() {
        try {
            this.getEntidad().setUsuarioModificacion(this.getCurrentUser());
            this.getEntidad().setFechaModificacion(new Date());
            this.getEntidad().setEstado(StateEntity.ANULADO.name());
            this.permisoFacade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha anulado el permiso: " + this.getEntidad().getNombrePerm());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar anular: " + this.getEntidad().getNombrePerm() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void enableEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    /**
     * @return the permisoCombo
     */
    public List<Permiso> getPermisoCombo() {
        permisoCombo = this.permisoFacade.getPermisosPadre();
        return permisoCombo;
    }

    /**
     * @param permisoCombo the permisoCombo to set
     */
    public void setPermisoCombo(List<Permiso> permisoCombo) {
        this.permisoCombo = permisoCombo;
    }

    public PermisoFacade getPermisoFacade() {
        return permisoFacade;
    }

}
