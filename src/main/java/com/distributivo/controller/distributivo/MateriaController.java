package com.distributivo.controller.distributivo;

import com.distributivo.ejb.business.MateriaFacade;
import com.distributivo.ejb.business.PeriodoFacade;
import com.distributivo.model.business.Materia;
import com.distributivo.model.business.Periodo;
import com.distributivo.utilidadbase.controller.LazyDataModelAdvance;
import com.distributivo.utilidadbase.controller.Mensaje;
import com.distributivo.utilidadbase.controller.AbstractController;
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

@Named(value = "materiaController")
@ViewScoped
public class MateriaController extends AbstractController<Materia> implements Serializable {

    @EJB
    private MateriaFacade facade;

    private LazyDataModelAdvance<Materia> tablaLazy;

    public MateriaController() {
        super(Materia.class);
    }

    @PostConstruct
    public void inicializar() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.setCurrentUser(auth.getName());

        this.setFacade(facade);
        this.setClasePK(Integer.class);
        this.setTitle("Materia");
        tablaLazy = new LazyDataModelAdvance<>(facade);
        tablaLazy.setFacade(facade);
        tablaLazy.setClasePK(Integer.class);
    }

    @Override
    public void changeView(String view, String id) {
        this.typeOfView = view;
        switch (view) {
            case "list":
                this.setPersistence(null);
                this.setTitle("Materia");
                break;
            case "create":
                this.title = "Materia / Nuevo";
                this.prepararCrear();
                break;
            case "edit":
                this.title = "Materia / Editar / " + this.getEntidad().getNombre();
                this.prepararEditar(id);
                break;
            case "delete":
                this.title = "Materia / Eliminar / " + this.getEntidad().getNombre();
                this.prepararAnular(id);
                break;
            case "view":
                this.prepararView(id);
                this.title = "Materia / " + this.getEntidad().getNombre();
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
            this.title = "Empresa / Nuevo";
        } else {
            this.title = "Empresa / " + this.getEntidad().getNombre();
            this.setPersistence(null);
            this.typeOfView = "view";
        }
    }

    @Override
    protected void editEntity() {
        try {
            this.getEntidad().setFechaModificacionAudi(new Date());
            this.getEntidad().setUsuarioModificacionAudi(this.getCurrentUser());
            this.facade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha modificado la empresa: " + this.getEntidad().getNombre());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar modificar: " + this.getEntidad().getNombre() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void createEntity() {
        try {
            this.getEntidad().setFechaCreacionAudi(new Date());
            this.getEntidad().setUsuarioCreacionAudi(this.getCurrentUser());
            this.getEntidad().setEstado(StateEntity.ACTIVO.name());
            this.facade.guardar(this.getEntidad());
            Mensaje.SUCESO("Se ha guardado la Materia: " + this.getEntidad().getNombre());
            this.setEntidad(new Materia());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar guardar: " + this.getEntidad().getNombre() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void disableEntity() {
        try {
            this.getEntidad().setFechaModificacionAudi(new Date());
            this.getEntidad().setUsuarioModificacionAudi(this.getCurrentUser());
            this.getEntidad().setEstado(StateEntity.ANULADO.name());
            this.facade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha anulado la Materia: " + this.getEntidad().getNombre());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar anular: " + this.getEntidad().getNombre() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void enableEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public LazyDataModelAdvance<Materia> getTablaLazy() {
        return tablaLazy;
    }

    public Materia getMateria(Long id) {
        return this.facade.buscarPorCodigo(id);
    }

    public List<Materia> complete(String query) {
        return facade.buscarPorCoincidencia(query);
    }

    public void setTablaLazy(LazyDataModelAdvance<Materia> tablaLazy) {
        this.tablaLazy = tablaLazy;
    }

}
