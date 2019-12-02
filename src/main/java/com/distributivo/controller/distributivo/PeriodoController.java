package com.distributivo.controller.distributivo;

import com.distributivo.ejb.business.PeriodoFacade;
import com.distributivo.ejb.business.InstitucionFacade;
import com.distributivo.model.business.Periodo;
import com.distributivo.model.business.Institucion;
import com.distributivo.utilidadbase.controller.LazyDataModelAdvance;
import com.distributivo.utilidadbase.controller.Mensaje;
import com.distributivo.utilidadbase.controller.AbstractController;
import com.tesis.utilidadbase.facade.StateEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Toshiba
 */
@Named(value = "periodoController")
@ViewScoped
public class PeriodoController extends AbstractController<Periodo> implements Serializable {

    @EJB
    private PeriodoFacade facade;
    
    @EJB
    private InstitucionFacade institucionfacade;
    
    private List<Periodo> periodoList;

    private LazyDataModelAdvance<Periodo> tablaLazy;

    public PeriodoController() {
        super(Periodo.class);
    }

    @PostConstruct
    public void inicializar() {
        this.setFacade(facade);
        this.setClasePK(Integer.class);
        this.setTitle("Periodo");
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
                this.setTitle("Periodo");
                break;
            case "create":
                this.title = "Periodo / Nuevo";
                this.prepararCrear();
                break;
            case "edit":
                this.title = "Periodo / Editar / " + this.getEntidad().getId();
                this.prepararEditar(id);
                break;
            case "delete":
                this.title = "Periodo / Eliminar / " + this.getEntidad().getId();
                this.prepararAnular(id);
                break;
            case "view":
                this.prepararView(id);
                this.title = "Periodo / " + this.getEntidad().getId();
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
            this.title = "Periodo / Nuevo";
        } else {
            this.title = "Periodo / " + this.getEntidad().getId();
            this.setPersistence(null);
            this.typeOfView = "view";
        }
    }

    @Override
    protected void editEntity() {
        try {
            this.facade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha modificado el Periodo: " + this.getEntidad().getId());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar modificar: " + this.getEntidad().getId() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void createEntity() {
        try {
            this.getEntidad().setEstado(StateEntity.ACTIVO.name());
            List<Institucion> listado = this.listaInstitucionActiva();
            if(listado.size() > 0){//si hay registrado una institucion
                this.getEntidad().setInstitucion(listado.get(0));
            }
            this.getEntidad().setEstadoDistributivo("GENERADO");
            this.facade.guardar(this.getEntidad());
            Mensaje.SUCESO("Se ha guardado el Periodo: " + this.getEntidad().getId());
            this.setEntidad(new Periodo());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar guardar: " + this.getEntidad().getId() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void disableEntity() {
        try {
            this.getEntidad().setEstado(StateEntity.ANULADO.name());
            this.facade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha anulado la Periodo: " + this.getEntidad().getId());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar anular: " + this.getEntidad().getId() + ".ERROR=" + ex.getMessage());
        }
    }

    private List<Institucion> listaInstitucionActiva(){
        List<Institucion> listado = institucionfacade.buscarPorEstado();
        return listado;
    }
    
    @Override
    protected void enableEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Periodo getPeriodo(Integer id){
        return this.facade.buscarPorCodigo(id);
    }

    public LazyDataModelAdvance<Periodo> getTablaLazy() {
        return tablaLazy;
    }

    public void setTablaLazy(LazyDataModelAdvance<Periodo> tablaLazy) {
        this.tablaLazy = tablaLazy;
    }

    public List<Periodo> getPeriodoList() {
        periodoList=this.facade.listarPorEstado(StateEntity.ACTIVO);
        return periodoList;
    }

    public void setPeriodoList(List<Periodo> periodoList) {
        this.periodoList = periodoList;
    }

    
}
