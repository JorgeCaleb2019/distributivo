package com.distributivo.controller.distributivo;

import com.distributivo.ejb.business.DedicacionFacade;
import com.distributivo.model.business.Dedicacion;
import com.distributivo.utilidadbase.controller.LazyDataModelAdvance;
import com.distributivo.utilidadbase.controller.Mensaje;
import com.distributivo.utilidadbase.controller.AbstractController;
import com.distributivo.utilidadbase.controller.Table;
import com.tesis.utilidadbase.facade.StateEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Named(value = "dedicacionController")
@ViewScoped
public class DedicacionController extends AbstractController<Dedicacion> implements Serializable {

    @EJB
    private DedicacionFacade facade;

    private LazyDataModelAdvance<Dedicacion> tablaLazy;

    public DedicacionController() {
        super(Dedicacion.class);
    }

    @PostConstruct
    public void inicializar() {
       /* this.setFacade(facade);
        this.setClasePK(Integer.class);
        this.setTitle("Dedicacion");
        tablaLazy = new LazyDataModelAdvance<>(facade);
        tablaLazy.setFacade(facade);
        tablaLazy.setClasePK(Long.class);
        this.table.load();
        */
           Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.setCurrentUser(auth.getName());
        this.setFacade(facade);
        this.setClasePK(Long.class);
        this.setTitle("Dedicacion");
        tablaLazy = new LazyDataModelAdvance<>(facade);
        tablaLazy.setFacade(facade);
        tablaLazy.setClasePK(Long.class);
        this.table = new Table<>(facade);
        this.table.load();
    }

    @Override
    public void changeView(String view, String id) {
        this.typeOfView = view;
        switch (view) {
            case "list":
                this.setPersistence(null);
                this.setTitle("Dedicacion");
                break;
            case "create":
                this.title = "Dedicacion / Nuevo";
                this.prepararCrear();
                break;
            case "edit":
                this.title = "Dedicacion / Editar / " + this.getEntidad().getId();
                this.prepararEditar(id);
                break;
            case "delete":
                this.title = "Dedicacion / Eliminar / " + this.getEntidad().getId();
                this.prepararAnular(id);
                break;
            case "view":
                this.prepararView(id);
                this.title = "Dedicacion / " + this.getEntidad().getId();
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
            this.title = "Dedicacion / Nuevo";
        } else {
            this.title = "Dedicacion / " + this.getEntidad().getId();
            this.setPersistence(null);
            this.typeOfView = "view";
        }
    }

    @Override
    protected void editEntity() {
        try {
            this.facade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha modificado el Dedicacion: " + this.getEntidad().getId());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar modificar: " + this.getEntidad().getId() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void createEntity() {
        try {
            this.getEntidad().setEstado(StateEntity.ACTIVO.name());
            this.facade.guardar(this.getEntidad());
            Mensaje.SUCESO("Se ha guardado el Dedicacion: " + this.getEntidad().getId());
            this.setEntidad(new Dedicacion());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar guardar: " + this.getEntidad().getId() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void disableEntity() {
        try {
            this.getEntidad().setEstado(StateEntity.ANULADO.name());
            this.facade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha anulado la Dedicacion: " + this.getEntidad().getId());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar anular: " + this.getEntidad().getId() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void enableEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Dedicacion getDedicacion(Long id){
        return this.facade.buscarPorCodigo(id);
    }
    
    public List<Dedicacion> complete(String query){
        return facade.buscarPorCoincidencia(query);
    }

    public LazyDataModelAdvance<Dedicacion> getTablaLazy() {
        return tablaLazy;
    }

    public void setTablaLazy(LazyDataModelAdvance<Dedicacion> tablaLazy) {
        this.tablaLazy = tablaLazy;
    }

    
}
