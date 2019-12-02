package com.distributivo.controller.distributivo;

import com.distributivo.ejb.business.CursoFacade;
import com.distributivo.ejb.business.MallaCabeceraFacade;
import com.distributivo.ejb.business.MallaDetalleFacade;
import com.distributivo.ejb.business.MateriaFacade;
import com.distributivo.model.business.MallaDetalle;
import com.distributivo.model.business.Curso;
import com.distributivo.model.business.Materia;
import com.distributivo.model.business.MallaCabecera;
import com.distributivo.utilidadbase.controller.Mensaje;
import com.tesis.utilidadbase.facade.StateEntity;
import com.tesis.utilidadbase.validacion.Validaciones;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Named(value = "asignacionMateriasController")
@ViewScoped
public class AsignacionMateriasController implements Serializable {

    @EJB
    private MallaDetalleFacade mallaDetalleFacade;

    private Integer idMalla;
    //variables para recuperar las claves de los combos
    private Integer idCurso;
    private Integer idMateria;
    private Integer numHoras;
    private String observaciones;

    @EJB
    private MallaCabeceraFacade mallafacade;

    @EJB
    private CursoFacade cursofacade;
    @EJB
    private MateriaFacade materiafacade;
    //variables para llenar los combos
    private List<MallaDetalle> listaMallas;
    private List<Curso> listaCursos;
    private List<Materia> listaMaterias;

    private List<MallaCabecera> mallaCombo;
    private List<Curso> cursoCombo;
    private List<Materia> materiaCombo;

    private MallaDetalle detallemalla;

    @PostConstruct
    public void inicializar() {

    }

    public void mostrarDialogo() {
        //logica
        if (this.getIdMalla() == null) {

            FacesMessage msg = new FacesMessage("Debe seleccionar primero la Malla", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            return;
        }
        System.out.println("Refresca los datos del dialogo");
        materiaCombo = new ArrayList<>();
        this.idCurso = 0;
        this.idMateria = 0;
        this.numHoras = 0;
        this.observaciones = "";
        System.out.println("Limpio los datos");
        RequestContext req = RequestContext.getCurrentInstance();
        req.execute("PF('wdialogo').show();");
    }

    public void cargarMalla() {
        /*  FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "titulo", "mensaje");
         FacesContext.getCurrentInstance().addMessage(null, facesMsg);*/
        if (this.getIdMalla() == null) {
            FacesMessage msg = new FacesMessage("Debe seleccionar primero la Malla", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            return;
            /*RequestContext.getCurrentInstance()
             .execute("mensajear('Debe seleccionar primero la Malla');");
             return;*/
        }

        System.out.println("malla selecciponada: " + this.getIdMalla());
        listaMallas = mallaDetalleFacade.buscarPorCoincidencia(this.getIdMalla());
    }

    public void cargarMaterias() {
        System.out.println("Curso: " + this.getIdCurso());
        System.out.println("Id malla: " + this.getIdMalla());
        List<Materia> listadoMateriaLibres = new ArrayList<>();
        List<Materia> listadoMateriaAsignados = materiafacade.buscarPorEstado();
        List<MallaDetalle> listaMateriaOcupado = mallaDetalleFacade.buscarMateriaMalla(this.getIdCurso(), this.getIdMalla());
        boolean bandera = false;
        for (Materia mat : listadoMateriaAsignados) {
            bandera = false;
            for (MallaDetalle mall : listaMateriaOcupado) {
                if (mall.getMateria().getId() == mat.getId()) {
                    bandera = true;
                }
            }
            if (bandera == false) {
                listadoMateriaLibres.add(mat);
            }
        }
        materiaCombo = listadoMateriaLibres;

    }

    public void validarNumero(){
        //logica
        System.out.println("valida numero");
        System.out.println(this.getNumHoras());

        if (this.getNumHoras() != null) {
            if(this.getNumHoras() > 0 && this.getNumHoras() <= 10){
                System.out.println("esta entre 0 - 10");
            }else{
                Mensaje.ADVERTENCIA("Número de horas debe de estar entre 0 - 10");
                this.setNumHoras(0);
            }
        }
    }
    public void registrarAsignacion() {
        System.out.println("entra grabar malla");
        //Integer horas = 0;
        MallaDetalle detalleGrabar = new MallaDetalle();

        //recuperar el curso seleccionado a partir del id
        List<Curso> listaC = this.listaCursos(this.getIdCurso());

        //System.out.println("estuve aqui");
        try {
            System.out.println("codigo de curso" + (this.getIdCurso()));
            if (this.getIdCurso() == null || this.getIdCurso() == 0) {
                Mensaje.ADVERTENCIA("Debe seleccionar el curso!");
                RequestContext.getCurrentInstance()
                        .execute("mensajear('Debe seleccionar el curso!');");
                System.out.println("entro a validar el curso");
                return;

            }
            System.out.println("codigo de materia " + (this.getIdMateria()));
            if (this.getIdMateria() == null || this.getIdMateria() == 0) {
                Mensaje.ADVERTENCIA("Debe seleccionar la materia!");
                RequestContext.getCurrentInstance()
                        .execute("mensajear('Debe seleccionar la materia!');");
                System.out.println("entro a validar la materia");
                return;
            }

            if (this.getNumHoras() == null || this.getNumHoras() == 0) {
                Mensaje.ADVERTENCIA("Debe registrar el número de horas");
                RequestContext.getCurrentInstance()
                        .execute("mensajear('Debe registrar el número de horas!');");
                System.out.println("entro a validar las horas");
                return;
            }

            List<Materia> listaM = this.listaMateria(this.getIdMateria());
            List<MallaCabecera> listaCab = this.listaMalla(this.getIdMalla());
            if (listaC.size() > 0) {
                detalleGrabar.setCurso(listaC.get(0));
            }
            if (listaM.size() > 0) {
                detalleGrabar.setMateria(listaM.get(0));
            }
            if (listaCab.size() > 0) {
                detalleGrabar.setMallaCabecera(listaCab.get(0));
            }

            detalleGrabar.setEstado("ACTIVO");
            detalleGrabar.setFechaIngreso(new Date());
            detalleGrabar.setNumHoras(this.getNumHoras());
            detalleGrabar.setObservacion(this.getObservaciones());

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            detalleGrabar.setUsuario(auth.getName());
            this.mallaDetalleFacade.guardar(detalleGrabar);
        //listaMallas = facade.buscarPorCoincidencia(this.getIdMalla());
            //mandare a cerrar la ventana desde aqui..
            System.out.println("deberia grabar correctamente");
            RequestContext.getCurrentInstance().execute("PF('wdialogo').hide()");
            Mensaje.SUCESO("Registrado con éxito!");
            cargarMalla();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private List<Curso> listaCursos(Integer codigo) {
        List<Curso> listado = cursofacade.buscarPorCodigo(codigo);
        return listado;
    }

    private List<Materia> listaMateria(Integer codigo) {
        List<Materia> listado = materiafacade.buscarPorCodigo(codigo);
        return listado;
    }

    private List<MallaCabecera> listaMalla(Integer codigo) {
        List<MallaCabecera> listado = mallafacade.buscarPorCodigo(codigo);
        return listado;
    }

    public MallaDetalle getParalelo(Long id) {
        return this.mallaDetalleFacade.buscarPorCodigo(id);
    }

    public List<MallaDetalle> complete(Integer query) {
        return mallaDetalleFacade.buscarPorCoincidencia(query);
    }

    public List<MallaCabecera> getMallaCombo() {
        this.mallaCombo = this.mallafacade.listarPorEstado(StateEntity.ACTIVO);
        return mallaCombo;
    }

    public void setMallaCombo(List<MallaCabecera> mallaCombo) {
        this.mallaCombo = mallaCombo;
    }

    public Integer getIdMalla() {
        return idMalla;
    }

    public void setIdMalla(Integer idMalla) {
        this.idMalla = idMalla;
    }

    public List<MallaDetalle> getListaMallas() {
        //listaMallas = facade.buscarPorCoincidencia("");
        //System.out.println("mallas encontradas: " + listaMallas.size());
        return listaMallas;
    }

    public void setListaMallas(List<MallaDetalle> listaMallas) {
        this.listaMallas = listaMallas;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public Integer getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }

    public List<Curso> getListaCursos() {
        listaCursos = cursofacade.buscarPorEstado();
        return listaCursos;
    }

    public void setListaCursos(List<Curso> listaCursos) {
        this.listaCursos = listaCursos;
    }

    public List<Materia> getListaMaterias() {
        listaMaterias = materiafacade.buscarPorEstado();
        return listaMaterias;
    }

    public void setListaMaterias(List<Materia> listaMaterias) {
        this.listaMaterias = listaMaterias;
    }

    public List<Curso> getCursoCombo() {
        cursoCombo = cursofacade.buscarPorEstado();
        return cursoCombo;
    }

    public void setCursoCombo(List<Curso> cursoCombo) {
        this.cursoCombo = cursoCombo;
    }

    public List<Materia> getMateriaCombo() {
        return materiaCombo;
    }

    public void setMateriaCombo(List<Materia> materiaCombo) {
        this.materiaCombo = materiaCombo;
    }

    public Integer getNumHoras() {
        return numHoras;
    }

    public void setNumHoras(Integer numHoras) {
        this.numHoras = numHoras;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /*public void leer (MallaDetalle mallaSeleccionada) {
     listaMallas = mallaSeleccionada; 
     }*/
    public void onRowEdit(RowEditEvent event) {
        System.out.println("editara");
        System.out.println(((MallaDetalle) event.getObject()).getIdDetalle());

        MallaDetalle editar = ((MallaDetalle) event.getObject());
        mallaDetalleFacade.modificar(editar);

        Mensaje.SUCESO("Se ha modificado la malla");
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edicion cancelada", ((MallaDetalle) event.getObject()).getIdDetalle() + "");
        //FacesContext.getCurrentInstance().addMessage(null, msg);
        Mensaje.SUCESO("Edicion cancelada");
    }
}
