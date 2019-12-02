package com.distributivo.controller.distributivo;

import com.distributivo.ejb.business.CursoFacade;
import com.distributivo.model.business.Curso;
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

@Named(value = "cursoController")
@ViewScoped
public class CursoController extends AbstractController<Curso> implements Serializable {

    @EJB
    private CursoFacade facade;

    private LazyDataModelAdvance<Curso> tablaLazy;

    public CursoController() {
        super(Curso.class);
    }

    @PostConstruct
    public void inicializar() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.setCurrentUser(auth.getName());
        this.setFacade(facade);
        this.table = new Table<>(facade);
        this.setClasePK(Long.class);
        this.setTitle("Curso");
        this.table.load();
        
    }

    @Override
    public void changeView(String view, String id) {
        this.typeOfView = view;
        switch (view) {
            case "list":
                this.setPersistence(null);
                this.setTitle("Curso");
                break;
            case "create":
                this.title = "Curso / Nuevo";
                this.prepararCrear();
                break;
            case "edit":
                this.title = "Curso / Editar / " + this.getEntidad().getIdCurso();
                this.prepararEditar(id);
                break;
            case "delete":
                this.title = "Curso / Eliminar / " + this.getEntidad().getIdCurso();
                this.prepararAnular(id);
                break;
            case "view":
                this.prepararView(id);
                this.title = "Curso / " + this.getEntidad().getIdCurso();
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
            this.title = "Curso / Nuevo";
        } else {
            this.title = "Curso / " + this.getEntidad().getIdCurso();
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
            Mensaje.SUCESO("Se ha modificado el Curso: " + this.getEntidad().getIdCurso());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar modificar: " + this.getEntidad().getIdCurso() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void createEntity() {
        try {
            this.getEntidad().setFechaCreacion(new Date());
            //this.getEntidad().setUsuarioCreacionAudi(this.getCurrentUser());
            this.getEntidad().setEstado(StateEntity.ACTIVO.name());
            this.facade.guardar(this.getEntidad());
            Mensaje.SUCESO("Se ha guardado el Curso: " + this.getEntidad().getIdCurso());
            this.setEntidad(new Curso());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar guardar: " + this.getEntidad().getIdCurso() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void disableEntity() {
        try {
            this.getEntidad().setFechaModificacion(new Date());
            //this.getEntidad().setUsuarioModificacionAudi(this.getCurrentUser());
            this.getEntidad().setEstado(StateEntity.ANULADO.name());
            this.facade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha anulado la Curso: " + this.getEntidad().getIdCurso());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar anular: " + this.getEntidad().getIdCurso() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void enableEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Curso getParalelo(Long id) {
        return this.facade.buscarPorCodigo(id);
    }

    public List<Curso> complete(String query) {
        return facade.buscarPorCoincidencia(query);
    }

    public LazyDataModelAdvance<Curso> getTablaLazy() {
        return tablaLazy;
    }

    public void setTablaLazy(LazyDataModelAdvance<Curso> tablaLazy) {
        this.tablaLazy = tablaLazy;
    }

}
