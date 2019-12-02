package com.distributivo.controller.distributivo;

import com.distributivo.ejb.business.TitularidadFacade;
import com.distributivo.model.business.Titularidad;
import com.distributivo.utilidadbase.controller.LazyDataModelAdvance;
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
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Named(value = "titularidadController")
@ViewScoped
public class TitularidadController extends AbstractController<Titularidad> implements Serializable{
    
    @EJB
    private TitularidadFacade facade;

    private LazyDataModelAdvance<Titularidad> tablaLazy;
    
    public TitularidadController() {
       super(Titularidad.class);    
    }

    @PostConstruct
    public void inicializar() {
      /* Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.setCurrentUser(auth.getName());
        this.setFacade(facade);
        this.setClasePK(Integer.class);
        this.setTitle("Titularidad");
        tablaLazy = new LazyDataModelAdvance<>(facade);
        tablaLazy.setFacade(facade);
        tablaLazy.setClasePK(Integer.class);
        this.table.load();
        */
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.setCurrentUser(auth.getName());
      /*  this.setFacade(facade);
        this.table = new Table<>(facade);
        this.setClasePK(Integer.class);
        this.setTitle("Titularidad");
        this.table.load();*/
      
       this.setFacade(facade);
        this.setClasePK(Long.class);
        this.setTitle("Titularidad");
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
                this.setTitle("Titularidad");
                break;
            case "create":
                this.title = "Titularidad / Nuevo";
                this.prepararCrear();
                break;
            case "edit":
                this.title = "Titularidad / Editar / " + this.getEntidad().getIdTitularidad();
                this.prepararEditar(id);
                break;
            case "delete":
                this.title = "Titularidad / Eliminar / " + this.getEntidad().getIdTitularidad();
                this.prepararAnular(id);
                break;
            case "view":
                this.prepararView(id);
                this.title = "Titularidad / " + this.getEntidad().getIdTitularidad();
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
            this.title = "Titularidad / Nuevo";
        } else {
            this.title = "Titularidad / " + this.getEntidad().getIdTitularidad();
            this.setPersistence(null);
            this.typeOfView = "view";
        }
    }

    @Override
    protected void editEntity() {
        try {
            //this.getEntidad().setFecha(new Date());
            //this.getEntidad().setUsuario(this.getCurrentUser());
            this.facade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha modificado el Curso: " + this.getEntidad().getIdTitularidad());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar modificar: " + this.getEntidad().getIdTitularidad() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void createEntity() {
        try {
            this.getEntidad().setFechaCreacion(new Date());
            //this.getEntidad().setUsuarioCreacionAudi(this.getCurrentUser());
            this.getEntidad().setEstado(StateEntity.ACTIVO.name());
            
            this.facade.guardar(this.getEntidad());
            Mensaje.SUCESO("Se ha guardado la titularidad: " + this.getEntidad().getIdTitularidad());
            this.setEntidad(new Titularidad());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar guardar: " + this.getEntidad().getIdTitularidad() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void disableEntity() {
        try {
            //this.getEntidad().setFechaModificacion(new Date());
            //this.getEntidad().setUsuarioModificacionAudi(this.getCurrentUser());
            this.getEntidad().setEstado(StateEntity.ANULADO.name());
            this.facade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha anulado la titularidad: " + this.getEntidad().getIdTitularidad());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar anular: " + this.getEntidad().getIdTitularidad() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void enableEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Titularidad getParalelo(Long id) {
        return this.facade.buscarPorCodigo(id);
    }

    public List<Titularidad> complete(String query) {
        return facade.buscarPorCoincidencia(query);
    }

    public LazyDataModelAdvance<Titularidad> getTablaLazy() {
        return tablaLazy;
    }

    public void setTablaLazy(LazyDataModelAdvance<Titularidad> tablaLazy) {
        this.tablaLazy = tablaLazy;
    }    
}
