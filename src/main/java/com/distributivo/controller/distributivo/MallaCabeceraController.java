package com.distributivo.controller.distributivo;

import com.distributivo.ejb.business.CursoFacade;
import com.distributivo.ejb.business.MallaCabeceraFacade;
import com.distributivo.ejb.business.ParaleloFacade;
import com.distributivo.model.business.Curso;
import com.distributivo.model.business.MallaCabecera;
import com.distributivo.model.business.Paralelo;
import com.distributivo.utilidadbase.controller.AbstractController;
import com.distributivo.utilidadbase.controller.LazyDataModelAdvance;
import com.distributivo.utilidadbase.controller.Mensaje;
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


@Named(value = "mallaCabeceraController")
@ViewScoped
public class MallaCabeceraController extends AbstractController<MallaCabecera> implements Serializable {

    @EJB
    private MallaCabeceraFacade facade;

    private LazyDataModelAdvance<MallaCabecera> tablaLazy;

    public MallaCabeceraController() {
        super(MallaCabecera.class);
    }

    @PostConstruct
    public void inicializar() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.setCurrentUser(auth.getName());
        this.setFacade(facade);
        this.setClasePK(Integer.class);
        this.setTitle("Malla");
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
                this.setTitle("Malla");
                break;
            case "create":
                this.title = "Malla / Nuevo";
                this.prepararCrear();
                break;
            case "edit":
                this.title = "Malla / Editar / " + this.getEntidad().getDescripcion();
                this.prepararEditar(id);
                break;
            case "delete":
                this.title = "Malla / Eliminar / " + this.getEntidad().getDescripcion();
                this.prepararAnular(id);
                break;
            case "view":
                this.prepararView(id);
                this.title = "Malla / " + this.getEntidad().getDescripcion();
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
            this.title = "Malla / Nuevo";
        } else {
            this.title = "Malla / " + this.getEntidad().getDescripcion();
            this.setPersistence(null);
            this.typeOfView = "view";
        }
    }

    @Override
    protected void editEntity() {
        try {
            //this.getEntidad().setFechaModificacion(new Date());
            this.getEntidad().setUsuario(this.getCurrentUser());
            this.facade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha modificado la malla: " + this.getEntidad().getDescripcion());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar modificar: " + this.getEntidad().getDescripcion() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void createEntity() {
        try {
            this.getEntidad().setFechaIngreso(new Date());
            this.getEntidad().setUsuario(this.getCurrentUser());
            this.getEntidad().setEstado(StateEntity.ACTIVO.name());
            this.facade.guardar(this.getEntidad());
            Mensaje.SUCESO("Se ha guardado la malla: " + this.getEntidad().getDescripcion());
            this.setEntidad(new MallaCabecera());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar guardar: " + this.getEntidad().getDescripcion() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void disableEntity() {
        try {
            //this.getEntidad().setFechaModificacion(new Date());
            this.getEntidad().setUsuario(this.getCurrentUser());
            this.getEntidad().setEstado(StateEntity.ANULADO.name());
            this.facade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha anulado la malla: " + this.getEntidad().getDescripcion());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar anular: " + this.getEntidad().getDescripcion() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void enableEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public MallaCabecera getMallacabecera(Long id) {
        return this.facade.buscarPorCodigo(id);
    }

    public List<MallaCabecera> complete(String query) {
        return facade.buscarPorCoincidencia(query);
    }

    public LazyDataModelAdvance<MallaCabecera> getTablaLazy() {
        return tablaLazy;
    }

    public void setTablaLazy(LazyDataModelAdvance<MallaCabecera> tablaLazy) {
        this.tablaLazy = tablaLazy;
    }
}
