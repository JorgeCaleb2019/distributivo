package com.distributivo.controller.distributivo;
import com.distributivo.ejb.business.ParaleloFacade;
import com.distributivo.model.business.Paralelo;
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

@Named(value = "paraleloController")
@ViewScoped
public class ParaleloController extends AbstractController<Paralelo> implements Serializable{
    
    @EJB
    private ParaleloFacade facade;

    private LazyDataModelAdvance<Paralelo> tablaLazy;

    public ParaleloController() {
        super(Paralelo.class);
    }

    @PostConstruct
    public void inicializar() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.setCurrentUser(auth.getName());
        this.setFacade(facade);
        this.setClasePK(Integer.class);
        this.setTitle("Paralelo");
        tablaLazy = new LazyDataModelAdvance<>(facade);
        tablaLazy.setFacade(facade);
        tablaLazy.setClasePK(Integer.class);
        this.table = new Table<>(facade);
        this.table.load();
    }

    @Override
    public void changeView(String view, String id) {
        this.typeOfView = view;
        switch (view) {
            case "list":
                this.setPersistence(null);
                this.setTitle("Paralelo");
                break;
            case "create":
                this.title = "Paralelo / Nuevo";
                this.prepararCrear();
                break;
            case "edit":
                this.title = "Paralelo / Editar / " + this.getEntidad().getIdParalelo();
                this.prepararEditar(id);
                break;
            case "delete":
                this.title = "Paralelo / Eliminar / " + this.getEntidad().getIdParalelo();
                this.prepararAnular(id);
                break;
            case "view":
                this.prepararView(id);
                this.title = "Paralelo / " + this.getEntidad().getIdParalelo();
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
            this.title = "Paralelo / Nuevo";
        } else {
            this.title = "Paralelo / " + this.getEntidad().getIdParalelo();
            this.setPersistence(null);
            this.typeOfView = "view";
        }
    }

    @Override
    protected void editEntity() {
        try {
            this.getEntidad().setFechaModificacion(new Date());
            //this.getEntidad().setUsuario(this.getCurrentUser());
            this.facade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha modificado el Paralelo: " + this.getEntidad().getIdParalelo());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar modificar: " + this.getEntidad().getIdParalelo() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void createEntity() {
        try {
            this.getEntidad().setFechaCreacion(new Date());
            //this.getEntidad().setUsuarioCreacionAudi(this.getCurrentUser());
            this.getEntidad().setEstado(StateEntity.ACTIVO.name());
            this.facade.guardar(this.getEntidad());
            Mensaje.SUCESO("Se ha guardado el Paralelo: " + this.getEntidad().getIdParalelo());
            this.setEntidad(new Paralelo());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar guardar: " + this.getEntidad().getIdParalelo() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void disableEntity() {
        try {
            this.getEntidad().setFechaModificacion(new Date());
            //this.getEntidad().setUsuarioModificacionAudi(this.getCurrentUser());
            this.getEntidad().setEstado(StateEntity.ANULADO.name());
            this.facade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha anulado la Paralelo: " + this.getEntidad().getIdParalelo());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar anular: " + this.getEntidad().getIdParalelo() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void enableEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Paralelo getParalelo(Long id) {
        return this.facade.buscarPorCodigo(id);
    }

    public List<Paralelo> complete(String query) {
        return facade.buscarPorCoincidencia(query);
    }

    public LazyDataModelAdvance<Paralelo> getTablaLazy() {
        return tablaLazy;
    }

    public void setTablaLazy(LazyDataModelAdvance<Paralelo> tablaLazy) {
        this.tablaLazy = tablaLazy;
    }
}
