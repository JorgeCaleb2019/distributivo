package com.distributivo.controller.distributivo;

import com.distributivo.ejb.business.DocenteFacade;
//import com.distributivo.ejb.business.PeriodoFacade;
import com.distributivo.model.business.Docente;
//import com.distributivo.model.business.Periodo;
import com.distributivo.utilidadbase.controller.LazyDataModelAdvance;
import com.distributivo.utilidadbase.controller.Mensaje;
import com.distributivo.utilidadbase.controller.AbstractController;
import com.tesis.utilidadbase.facade.StateEntity;
import com.tesis.utilidadbase.validacion.Validaciones;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;


@Named(value = "docenteController")
@ViewScoped
public class DocenteController extends AbstractController<Docente> implements Serializable {

    @EJB
    private DocenteFacade facade;

    private Validaciones ERROR_CEDULA;
    private Validaciones ERROR_RUC;
    private Validaciones ERROR_NO_DEFINIDO;
    private Validaciones IDENTIFICACION_OK;

    private List<Docente> docentes;
    private LazyDataModelAdvance<Docente> tablaLazy;

   /* pr-ivate LocalDate dateValue;
    private LocalDate fechaNacimiento;
*/
    public DocenteController() {
        super(Docente.class);
    }

    @PostConstruct
    public void inicializar() {
        this.setFacade(facade);
        this.setClasePK(Integer.class);
        this.setTitle("Docente");
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
                this.setTitle("Docente");
                break;
            case "create":
                this.title = "Docente / Nuevo";
                this.prepararCrear();
                break;
            case "edit":
                this.title = "Docente / Editar / " + this.getEntidad().getNombre();
                this.prepararEditar(id);
                break;
            case "delete":
                this.title = "Docente / Eliminar / " + this.getEntidad().getNombre();
                this.prepararAnular(id);
                break;
            case "view":
                this.prepararView(id);
                this.title = "Docente / " + this.getEntidad().getNombre();
                break;
        }
    }

    @Override
    public void executeAction(ActionEvent evt) {

        System.out.println("entra a grabar");
        switch (this.typeOfView) {
            case "create":
                this.createEntity();
                System.out.println("entra a crear");
                break;
            case "edit":
                this.editEntity();
                break;
            case "delete":
                this.disableEntity();
                break;
        }
        if (this.typeOfView.equals("create")) {
            this.title = "Docente / Nuevo";
        } else {
            this.title = "Docente / " + this.getEntidad().getNombre();
            this.setPersistence(null);
            this.typeOfView = "view";
        }
    }

    @Override
    protected void editEntity() {
        try {
            this.facade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha modificado el Docente: " + this.getEntidad().getNombre());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar modificar: " + this.getEntidad().getNombre() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void createEntity() {
        try {
            this.getEntidad().setEstado(StateEntity.ACTIVO.name());
            /*   if (1 == 1) {
             return;
             }*/
            System.out.println("tb entro aqui");
            this.facade.guardar(this.getEntidad());
            Mensaje.SUCESO("Se ha guardado el Docente: " + this.getEntidad().getNombre());
            this.setEntidad(new Docente());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar guardar: " + this.getEntidad().getNombre() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void disableEntity() {
        try {
            this.getEntidad().setEstado(StateEntity.ANULADO.name());
            this.facade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha anulado la Docente: " + this.getEntidad().getNombre());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar anular: " + this.getEntidad().getNombre() + ".ERROR=" + ex.getMessage());
        }
    }

    public void validarScript() {
        //logica
        System.out.println("valida");
        System.out.println(this.getEntidad().getCedula());

        if (this.getEntidad().getCedula() != null) {
            if (Validaciones.validarCedula(this.getEntidad().getCedula()) == false) {
                this.getEntidad().setCedula("");
                return;
            }
        }

        //ademas hay q validar si el numero de cedula ya esta registrado
        Docente docente = facade.buscarPorCedula(this.getEntidad().getCedula());
        if (docente != null) {//sii es diferente de null es xq existe el docente
            Mensaje.ERROR("Número de cedula ya se encuentra registrado en la base de datos");
            System.out.println("numero de cedula existe");
            this.getEntidad().setCedula("");
            return;
        } else {
            System.out.println("numero no de cedula existe");
        }
    }

    /*  if (this.getERROR_CEDULA() == 0)
     {
                
     }
     */
    public void validarPersona() {
        //logica
        /*
        System.out.println("valida");
        System.out.println(this.getEntidad().getCedula());

        if (this.getEntidad().getCedula() != null) {
            Integer idCedula = 0;
            List<Docente> docentes = facade.buscarPorCedula(this.getEntidad().getCedula().toString());
            if (docentes.size() > 0) {
                System.out.println("existen en la base");
                Mensaje.ERROR("Ya esta registrado esta persona");
                return;
            }
        }
         */
    }

    /*public void valueChanged(ValueChangeEvent event) {
    Object oldValue = event.getOldValue();
    Object newValue = event.getValue();
    UIComponent component = event.getComponent();
}*/
  
    
    
    public void validarFecha2() {
        System.out.println("valida");
        //logica
    
        
        LocalDate fechaNacimiento2 = this.getEntidad().getFechaNacimiento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            
        
        System.out.println(fechaNacimiento2.toString());

        if (fechaNacimiento2 != null) {
            System.out.println("entro");

            LocalDate ahora = LocalDate.now();
            System.out.println(ahora);
            System.out.println(fechaNacimiento2);

            Period periodo = Period.between(fechaNacimiento2, ahora);
            int anio = (fechaNacimiento2.getYear());

            if (anio >= 10000) {
                Mensaje.ERROR("Seleccione un Año incorrecto");
                System.out.println("Año incorrecto");
                return;

            }
            System.out.printf("Tu edad es: %s años, %s meses y %s días",
                    periodo.getYears(), periodo.getMonths(), periodo.getDays());

            if (periodo.getYears() < 18 || periodo.getYears() > 75) {
                Mensaje.ERROR("La edad no esta permitida");
                System.out.println("La edad no esta permitida");
                return;
            }
        }
    }
    

    @Override
    protected void enableEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Docente getDocente(Long id) {
        return this.facade.buscarPorCodigo(id);
    }

    public LazyDataModelAdvance<Docente> getTablaLazy() {
        return tablaLazy;
    }

    public void setTablaLazy(LazyDataModelAdvance<Docente> tablaLazy) {
        this.tablaLazy = tablaLazy;
    }

    public List<Docente> complete(String filter) {
        return this.facade.buscarPorCoincidencia(filter);
    }

    public Validaciones getERROR_CEDULA() {
        return ERROR_CEDULA;
    }

    public void setERROR_CEDULA(Validaciones ERROR_CEDULA) {
        this.ERROR_CEDULA = ERROR_CEDULA;
    }

    public Validaciones getERROR_RUC() {
        return ERROR_RUC;
    }

    public void setERROR_RUC(Validaciones ERROR_RUC) {
        this.ERROR_RUC = ERROR_RUC;
    }

    public Validaciones getERROR_NO_DEFINIDO() {
        return ERROR_NO_DEFINIDO;
    }

    public void setERROR_NO_DEFINIDO(Validaciones ERROR_NO_DEFINIDO) {
        this.ERROR_NO_DEFINIDO = ERROR_NO_DEFINIDO;
    }

    public Validaciones getIDENTIFICACION_OK() {
        return IDENTIFICACION_OK;
    }

    public void setIDENTIFICACION_OK(Validaciones IDENTIFICACION_OK) {
        this.IDENTIFICACION_OK = IDENTIFICACION_OK;
    }

    
    
}
