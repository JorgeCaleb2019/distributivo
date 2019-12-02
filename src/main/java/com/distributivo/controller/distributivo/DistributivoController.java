package com.distributivo.controller.distributivo;

import com.distributivo.ejb.business.DistributivoCabeceraFacade;
import com.distributivo.ejb.business.DistributivoDetalleFacade;
import com.distributivo.ejb.business.PeriodoFacade;
import com.distributivo.ejb.business.DocenteFacade;
import com.distributivo.ejb.business.MateriaFacade;
import com.distributivo.ejb.business.DedicacionFacade;
import com.distributivo.ejb.business.MallaDetalleFacade;
import com.distributivo.ejb.business.MallaCabeceraFacade;
import com.distributivo.ejb.business.ParaleloCursoFacade;
import com.distributivo.ejb.business.CursoFacade;
import com.distributivo.model.business.Adicionale;
import com.distributivo.model.business.DistributivoCabecera;
import com.distributivo.model.business.DistributivoDetalle;
import com.distributivo.model.business.TitularidadDedicacion;
import com.distributivo.model.business.Periodo;
import com.distributivo.model.business.Curso;
import com.distributivo.model.business.Materia;
import com.distributivo.model.business.Docente;
import com.distributivo.model.business.Dedicacion;
import com.distributivo.model.business.MallaDetalle;
import com.distributivo.model.business.MallaCabecera;
import com.distributivo.model.business.ParaleloCurso;
import com.distributivo.utilidadbase.controller.Mensaje;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;

@Named(value = "distributivoController")
@ViewScoped
public class DistributivoController implements Serializable {

    @EJB
    private DistributivoCabeceraFacade distributivoFacade;
    @EJB
    private DistributivoDetalleFacade distributivoDetalleFacade;
    @EJB
    private PeriodoFacade periodoFacade;
    @EJB
    private DocenteFacade docenteFacade;
    @EJB
    private MateriaFacade materiaFacade;
    @EJB
    private DedicacionFacade dedicacionFacade;
    @EJB
    private CursoFacade cursoFacade;
    @EJB
    private MallaDetalleFacade mallaDetalleFacade;
    @EJB
    private MallaCabeceraFacade mallaCabeceraFacade;
    @EJB
    private ParaleloCursoFacade paraleloFacade;

    private Periodo periodo;

    //variables para capturar las id
    private Integer idDocente;
    private Integer idDedicacion;
    private Integer idMateria;
    private Integer idCurso;
    private Integer idParalelo;
    private Integer numHoras;
    private Integer numHorasActividades;
    private Integer numHorasFaltantes;

    private String observaciones;
    private Integer numeroHoras;
    //detalle seleccionado
    private DistributivoDetalle detalleSeleccionado;

    //listas para llenar los combos
    private List<Docente> docenteCombo;
    private List<Dedicacion> dedicacionCombo;
    private List<MateriaMostrar> materiaCombo;
    private List<Curso> cursoCombo;
    private List<ParaleloCurso> paraleloCombo;
    //lista para el datatable
    private List<Asignaciones> listaMateriasAsignadas;

    @PostConstruct
    public void inicializar() {

    }

    public void cargarMaterias() {
        System.out.println("Codigo docente " + this.getIdDocente());
        System.out.println("Codigo periodo  " + this.getPeriodo().getId());
        if (this.getIdDocente() == null) {
            Mensaje.ADVERTENCIA("Debe seleccionar primero el Docente");
            return;
        }
        DistributivoCabecera distributivo = distributivoFacade.buscarPorDocentePeriodo(this.getIdDocente(), this.getPeriodo().getId());
        if (distributivo != null) {
            System.out.println("Listado de detalles: " + distributivo.getDistributivoDetalles().size());
            if (distributivo.getDistributivoDetalles().size() > 0) {
                List<Asignaciones> listado = new ArrayList<>();
                for (DistributivoDetalle det : distributivo.getDistributivoDetalles()) {
                    if (det.getEstado().equals("A")) {
                        Asignaciones asig = new Asignaciones();
                        asig.setCedula(distributivo.getDocente().getCedula());
                        asig.setCurso(det.getCurso().getNombre());
                        asig.setEstado(det.getEstado());
                        asig.setIdCurso(det.getCurso().getIdCurso());
                        asig.setIdDetalleDistributivo(det.getIdDetalle());
                        asig.setIdDistributivo(distributivo.getIdDistributivo());
                        asig.setIdDocente(distributivo.getDocente().getId());
                        asig.setIdMateria(det.getMateria().getId());
                        asig.setIdMallaDetalle(det.getIdMallaDetalle());
                        asig.setMateria(det.getMateria().getNombre());
                        asig.setNombreDocente(distributivo.getDocente().getNombre());
                        asig.setObservaciones(det.getObservacion());
                        asig.setParalelo(det.getParalelo().getNombre());
                        asig.setDistributivoCabecera(distributivo);
                        asig.setDocente(det.getDistributivoCabecera().getDocente());
                        asig.setDistributivoDetalle(det);
                        //recuperar las horas
                        List<MallaDetalle> mallaDetalle = mallaDetalleFacade.buscarCodigo(det.getIdMallaDetalle());
                        if (mallaDetalle.size() > 0) {
                            asig.setNumHoras(mallaDetalle.get(0).getNumHoras());
                        } else {
                            asig.setNumHoras(0);
                        }
                        listado.add(asig);
                    }
                }
                listaMateriasAsignadas = listado;
            } else {
                listaMateriasAsignadas = new ArrayList<Asignaciones>();
            }
        } else {
            System.out.println("No hay datos");
            listaMateriasAsignadas = new ArrayList<Asignaciones>();
        }

        //ademas se cargan las dedicaciones que cuenta el docente
        Docente docente = docenteFacade.buscarDocentePorId(this.getIdDocente());
        if (docente != null) {
            List<Dedicacion> dedicacionDisponibles = new ArrayList<Dedicacion>();
            if (docente.getTitularidad().getTitularidadDedicacions().size() > 0) {
                for (TitularidadDedicacion tit : docente.getTitularidad().getTitularidadDedicacions()) {
                    dedicacionDisponibles.add(tit.getDedicacion());
                }
            }
            this.dedicacionCombo = dedicacionDisponibles;
        }

        //realizar el conteo de horas de docencia
        Integer numero = 0;
        Integer totalHoras = 0;
        Long idDed = null;
        for (Asignaciones det : listaMateriasAsignadas) {
            idDed = det.getDistributivoCabecera().getDedicacion().getId();
            numero = numero + det.getNumHoras();
        }

        Dedicacion de = new Dedicacion();
        if (idDed == null) {
            this.setIdDedicacion(0);
        } else {
            this.setIdDedicacion(Integer.parseInt(String.valueOf(idDed)));
            de = dedicacionFacade.buscarPorCodigoDedicacion(Integer.parseInt(String.valueOf(idDed)));
        }

        System.out.println("numero de horas: " + numero);
        numeroHoras = numero;

        if (idDed != null) {
            totalHoras = de.getTotal();
        }
        //realizar el conteo de las horas de actividades adicionales
        Integer numActividad = 0;
        if (distributivo != null) {
            if (distributivo.getAdicionales() != null) {
                for (Adicionale ac : distributivo.getAdicionales()) {
                    if (ac.getEstado().equals("ACTIVO")) {
                        numActividad = numActividad + ac.getHoras();
                    }
                }
            }
        }
        numHorasActividades = numActividad;
        Integer restantes = totalHoras - (numActividad + numero);
        numHorasFaltantes = restantes;
    }

    public void grabarAsignacion() {
        try {
            System.out.println("Entra a grabar el distributivo");
            System.out.println("codigo de curso" + (this.getIdCurso()));
            if (this.getIdCurso() == null || this.getIdCurso() <= 0) {
                RequestContext.getCurrentInstance()
                        .execute("mensajear('Ingrese el curso');");
                System.out.println("entro");
                return;
            }
            if (this.getIdParalelo() == null || this.getIdParalelo() <= 0) {
                RequestContext.getCurrentInstance()
                        .execute("mensajear('Ingrese el Paralelo');");
                return;
            }
            if (this.getIdMateria() == null || this.getIdMateria() <= 0) {
                RequestContext.getCurrentInstance()
                        .execute("mensajear('Ingrese la materia ');");
                return;
            }
            System.out.println("Observaciones " + this.getObservaciones());
            System.out.println("Materia seleccionada " + this.getIdMateria());

            Integer maximoHorasClases = 0;
            Integer minimoHorasClases = 0;
            Integer maximoGlobal = 0;
            Integer horasAsignados = 0;
            System.out.println("Dedicacion seleccionado " + this.getIdDedicacion());
            List<Dedicacion> dedicaciones = dedicacionFacade.buscarPorCodigo(this.getIdDedicacion());
            for (Dedicacion ded : dedicaciones) {
                maximoHorasClases = ded.getHorasMax();
                minimoHorasClases = ded.getHorasMin();
                maximoGlobal = ded.getTotal();
            }
            System.out.println("Horas maximo: " + maximoHorasClases + " Minimo: " + minimoHorasClases);
            System.out.println("Listado de materias id detalle");
            for (Asignaciones det : listaMateriasAsignadas) {
                horasAsignados = horasAsignados + det.getNumHoras();
            }
            System.out.println("Horas asignados de clases " + horasAsignados);

            List<MallaDetalle> buscarMateria = mallaDetalleFacade.buscarPorMateriaCurso(this.getIdCurso(), this.getIdMateria());
            for (MallaDetalle ded : buscarMateria) {
                horasAsignados = horasAsignados + ded.getNumHoras();
            }
            System.out.println("Horas actuales: " + horasAsignados);

            Integer numActividad = 0;
            DistributivoCabecera distributivoC = distributivoFacade.buscarPorDocentePeriodo(this.getIdDocente(), this.getPeriodo().getId());
            if (distributivoC != null) {
                if (distributivoC.getAdicionales() != null) {
                    for (Adicionale ac : distributivoC.getAdicionales()) {
                        if (ac.getEstado().equals("ACTIVO")) {
                            numActividad = numActividad + 1;
                        }
                    }
                }
            }
            System.out.println("Horas asignados de actividades adicionales " + numActividad);
            Integer total = horasAsignados + numActividad;

            if (horasAsignados > maximoHorasClases) {//para preguntar por las hpras clases
                RequestContext.getCurrentInstance()
                        .execute("mensajear('No se puede registrar más materias');");
                RequestContext.getCurrentInstance().execute("PF('wdialogo').hide()");
                return;
            }
            
            if (total > maximoGlobal){//para preguntar por las horas de manera global
                RequestContext.getCurrentInstance()
                        .execute("mensajear('Maximo de horas alcanzado');");
                RequestContext.getCurrentInstance().execute("PF('wdialogo').hide()");
                return;
            }
            
            //codigo que permite grabar
            DistributivoDetalle detalle = new DistributivoDetalle();
            detalle.setEstado("A");
            detalle.setIdDetalle(null);

            //enlace con la materia
            System.out.println("Codigo materia: " + this.getIdMateria());
            List<Materia> materiaLista = materiaFacade.buscarPorCodigo(this.getIdMateria());
            if (materiaLista.size() > 0) {
                detalle.setMateria(materiaLista.get(0));
            }

            //enlace con el paralelo curso
            Integer codigoCurso = 0;
            Integer codigomateria = 0;
            List<ParaleloCurso> listaParalelo = paraleloFacade.buscarParaleloPorCodigo(this.getIdParalelo());
            System.out.println("Paralelo tamaño: " + listaParalelo.size());
            if (listaParalelo.size() > 0) {
                codigoCurso = listaParalelo.get(0).getCurso().getIdCurso();
                codigomateria = this.getIdMateria();
                detalle.setParalelo(listaParalelo.get(0).getParalelo());
                detalle.setCurso(listaParalelo.get(0).getCurso());
            }

                //agregar el id del detalle de la malla
            //buscar el detalle de la malla vigente
            MallaDetalle malla = mallaDetalleFacade.buscarPorMateriaCursoVigente(codigoCurso, codigomateria);
            if (malla != null) {
                detalle.setIdMallaDetalle(malla.getIdDetalle());
            }

            detalle.setObservacion(this.getObservaciones());
            //enlace con la cabecera del distributivo
            DistributivoCabecera distributivo = distributivoFacade.buscarPorDocentePeriodo(this.getIdDocente(), this.getPeriodo().getId());
            if (distributivo != null) {
                System.out.println("es una modificacion");
                distributivo.setDedicacion(dedicaciones.get(0));
                detalle.setDistributivoCabecera(distributivo);
                distributivo.addDistributivoDetalle(detalle);
                distributivoFacade.modificar(distributivo);
                RequestContext.getCurrentInstance()
                        .execute("mensajear('Se registro correctamente');");
                RequestContext.getCurrentInstance().execute("PF('wdialogo').hide()");
            } else {
                System.out.println("es un nuevo registro");
                distributivo = new DistributivoCabecera();
                distributivo.setEstado("ACTIVO");
                distributivo.setDedicacion(dedicaciones.get(0));
                distributivo.setIdDistributivo(null);
                //enlace con el docente
                Docente docenteSeleccionado = docenteFacade.buscarDocentePorId(this.getIdDocente());
                if (docenteSeleccionado != null) {
                    distributivo.setDocente(docenteSeleccionado);
                }
                distributivo.setPeriodo(this.getPeriodo());
                distributivo.setFechaIngreso(new Date());

                List<DistributivoDetalle> detalles = new ArrayList<DistributivoDetalle>();
                detalle.setDistributivoCabecera(distributivo);
                detalles.add(detalle);

                distributivo.setDistributivoDetalles(detalles);
                distributivoFacade.guardar(distributivo);

                RequestContext.getCurrentInstance()
                        .execute("mensajear('Se registro correctamente');");
                RequestContext.getCurrentInstance().execute("PF('wdialogo').hide()");
            }

            cargarMaterias();
            System.out.println("Id paralelo: " + this.getObservaciones());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void mostrarDialogo() {

        if (this.getIdDocente() == null) {
            Mensaje.ADVERTENCIA("Debe seleccionar primero el Docente");
            return;
        }
        if (this.getIdDedicacion() == null) {
            Mensaje.ADVERTENCIA("Debe seleccionar la dedicación");
            return;
        }

        //logica
        Integer maximoHoras = 0;
        Integer minimoHoras = 0;
        Integer horasAsignados = 0;

        List<Dedicacion> dedicaciones = dedicacionFacade.buscarPorCodigo(this.getIdDedicacion());
        System.out.println("Numero maximo de horas permitidad");
        for (Dedicacion ded : dedicaciones) {
            maximoHoras = ded.getHorasMax();
            minimoHoras = ded.getHorasMin();
            System.out.println(maximoHoras);
        }

        //para sumar las horas
        System.out.println("Cantidad de horas asignadas");
        for (Asignaciones det : listaMateriasAsignadas) {
            horasAsignados = horasAsignados + det.getNumHoras();
        }
        System.out.print("Total de horas asignados: " + horasAsignados);
        if (horasAsignados < maximoHoras) {
            RequestContext req = RequestContext.getCurrentInstance();
            req.execute("PF('wdialogo').show();");
        } else {
            Mensaje.ADVERTENCIA("No se puede registrar más materias");
            return;
        }
        //resetear valores del dialogo
        this.setIdCurso(0);
        this.setIdMateria(0);
        this.setIdParalelo(0);
        this.setMateriaCombo(null);
        this.setParaleloCombo(null);
        this.setObservaciones("");
    }

    public void cargarCurso() {
        //verificar los que ya han sido asignados a docentes.. con la finaidad de no mostrarla en el combo
        List<ParaleloCurso> paralelos = paraleloFacade.buscarParaleloPorCurso(this.getIdCurso());
        paraleloCombo = paralelos;
        materiaCombo = new ArrayList<>();

    }

    public void cargarMateriasCombo() {
        //se recupera la malla activa
        Integer idMalla = 0;
        List<MallaCabecera> listaPeriodo = mallaCabeceraFacade.buscarPorEstado();
        if (listaPeriodo.size() > 0) {
            idMalla = listaPeriodo.get(0).getIdMalla();
        }

        List<MateriaMostrar> materiasCargar = new ArrayList<>();

        List<DistributivoCabecera> listaDistributivos = distributivoFacade.buscarPorPeriodo(this.getPeriodo().getId());
        boolean bandera = false;
        List<MallaDetalle> lista = mallaDetalleFacade.buscarPorCurso(this.getIdCurso(), idMalla);
        for (MallaDetalle mal : lista) {
            MateriaMostrar matAdd = new MateriaMostrar();
            bandera = false;
            System.out.println("registros de distributivos: " + listaDistributivos.size());
            for (DistributivoCabecera cab : listaDistributivos) {
                //entro al detalle de cada uno de los distributivos para poder ver si ya esta asignado esa materia
                for (DistributivoDetalle det : cab.getDistributivoDetalles()) {
                    //System.out.println("paralelo recuperado: " + det.getParaleloCurso().getIdCursoParalelo() + " combo: " + this.getIdParalelo());
                    //hay q recuperar el idParalelo
                    if (det.getEstado().equals("A")) {
                        ParaleloCurso paraleloCurso = paraleloFacade.buscarCursoParalelo(det.getCurso().getIdCurso(), det.getParalelo().getIdParalelo());
                        if (paraleloCurso.getIdCursoParalelo() == this.getIdParalelo()) {
                            System.out.println("es el curso");
                            if (det.getMateria().getId() == mal.getMateria().getId()) {
                                bandera = true;//la bandera se hace verdadero cuando existe
                            }
                        }
                    }
                }
            }
            if (bandera == false) {
                System.out.println("Id curso " + this.getIdCurso() + " id materia " + mal.getMateria().getId());
                MallaDetalle detalle = mallaDetalleFacade.buscarPorMateriaCursoVigente(this.getIdCurso(), mal.getMateria().getId());
                matAdd.setIdMateria(mal.getMateria().getId());
                matAdd.setMateria(mal.getMateria().getNombre());

                if (detalle != null) {
                    System.out.println("numero " + detalle.getNumHoras());
                    matAdd.setHoras(detalle.getNumHoras());
                } else {
                    System.out.println("numero " + detalle.getNumHoras());
                    matAdd.setHoras(0);
                }

                materiasCargar.add(matAdd);
            }
        }

        materiaCombo = materiasCargar;
    }

    public void eliminar(Asignaciones trabajo) {
        try {
            System.out.println(trabajo.getMateria());
            DistributivoDetalle distributivoEliminar = trabajo.getDistributivoDetalle();
            distributivoEliminar.setEstado("I");
            distributivoDetalleFacade.modificar(distributivoEliminar);
            Mensaje.SUCESO("Se elimino el registro correctamente");
            cargarMaterias();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    //getters and setters
    public Periodo getPeriodo() {
        List<Periodo> lista = periodoFacade.buscarAutorizado();
        if (lista.size() > 0) {
            periodo = lista.get(0);
        }
        return periodo;
    }

    public void cargarHora() {
        System.out.println("materia seleccionado: " + this.getIdMateria());
        System.out.println("curso seleccionad: " + this.getIdCurso());
        MallaDetalle detalle = mallaDetalleFacade.buscarPorMateriaCursoVigente(this.getIdCurso(), this.getIdMateria());
        if (detalle != null) {
            System.out.println("tiene");
            this.numHoras = detalle.getNumHoras();
            System.out.println(this.numHoras);
        } else {
            System.out.println("no tiene");
            this.numHoras = 0;
            System.out.println(this.numHoras);
        }
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Integer getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(Integer idDocente) {
        this.idDocente = idDocente;
    }

    public List<Docente> getDocenteCombo() {
        docenteCombo = docenteFacade.listadoDocentesActivos();
        return docenteCombo;
    }

    public void setDocenteCombo(List<Docente> docenteCombo) {
        this.docenteCombo = docenteCombo;
    }

    public List<Asignaciones> getListaMateriasAsignadas() {
        return listaMateriasAsignadas;
    }

    public void setListaMateriasAsignadas(List<Asignaciones> listaMateriasAsignadas) {
        this.listaMateriasAsignadas = listaMateriasAsignadas;
    }

    public DistributivoDetalle getDetalleSeleccionado() {
        return detalleSeleccionado;
    }

    public void setDetalleSeleccionado(DistributivoDetalle detalleSeleccionado) {
        this.detalleSeleccionado = detalleSeleccionado;
    }

    public Integer getIdDedicacion() {
        return idDedicacion;
    }

    public void setIdDedicacion(Integer idDedicacion) {
        this.idDedicacion = idDedicacion;
    }

    public List<Dedicacion> getDedicacionCombo() {
        return dedicacionCombo;
    }

    public void setDedicacionCombo(List<Dedicacion> dedicacionCombo) {
        this.dedicacionCombo = dedicacionCombo;
    }

    public DistributivoCabeceraFacade getDistributivoFacade() {
        return distributivoFacade;
    }

    public void setDistributivoFacade(DistributivoCabeceraFacade distributivoFacade) {
        this.distributivoFacade = distributivoFacade;
    }

    public Integer getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }

    public List<MateriaMostrar> getMateriaCombo() {

        return materiaCombo;
    }

    public void setMateriaCombo(List<MateriaMostrar> materiaCombo) {
        this.materiaCombo = materiaCombo;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public List<Curso> getCursoCombo() {
        List<Curso> lista = cursoFacade.buscarPorEstado();
        if (lista.size() > 0) {
            cursoCombo = lista;
        }
        return cursoCombo;
    }

    public void setCursoCombo(List<Curso> cursoCombo) {
        this.cursoCombo = cursoCombo;
    }

    public List<ParaleloCurso> getParaleloCombo() {
        return paraleloCombo;
    }

    public void setParaleloCombo(List<ParaleloCurso> paraleloCombo) {
        this.paraleloCombo = paraleloCombo;
    }

    public Integer getIdParalelo() {
        return idParalelo;
    }

    public void setIdParalelo(Integer idParalelo) {
        this.idParalelo = idParalelo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getNumeroHoras() {
        return numeroHoras;
    }

    public void setNumeroHoras(Integer numeroHoras) {
        this.numeroHoras = numeroHoras;
    }

    public Integer getNumHoras() {
        numHoras = 0;
        return numHoras;
    }

    public void setNumHoras(Integer numHoras) {
        this.numHoras = numHoras;
    }

    public class Asignaciones {

        private Integer idDetalleDistributivo;
        private Integer idMallaDetalle;
        private Integer idDistributivo;
        private Integer idDocente;
        private String nombreDocente;
        private String cedula;
        private String materia;
        private String curso;
        private String observaciones;
        private String estado;
        private String paralelo;
        private Integer numHoras;
        private Integer idCurso;
        private Integer idMateria;
        private DistributivoCabecera distributivoCabecera;
        private DistributivoDetalle distributivoDetalle;
        private Docente docente;

        public Integer getIdDistributivo() {
            return idDistributivo;
        }

        public void setIdDistributivo(Integer idDistributivo) {
            this.idDistributivo = idDistributivo;
        }

        public String getNombreDocente() {
            return nombreDocente;
        }

        public void setNombreDocente(String nombreDocente) {
            this.nombreDocente = nombreDocente;
        }

        public String getMateria() {
            return materia;
        }

        public void setMateria(String materia) {
            this.materia = materia;
        }

        public String getCurso() {
            return curso;
        }

        public void setCurso(String curso) {
            this.curso = curso;
        }

        public String getParalelo() {
            return paralelo;
        }

        public void setParalelo(String paralelo) {
            this.paralelo = paralelo;
        }

        public Integer getNumHoras() {
            return numHoras;
        }

        public void setNumHoras(Integer numHoras) {
            this.numHoras = numHoras;
        }

        public Integer getIdDocente() {
            return idDocente;
        }

        public void setIdDocente(Integer idDocente) {
            this.idDocente = idDocente;
        }

        public String getCedula() {
            return cedula;
        }

        public void setCedula(String cedula) {
            this.cedula = cedula;
        }

        public String getObservaciones() {
            return observaciones;
        }

        public void setObservaciones(String observaciones) {
            this.observaciones = observaciones;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
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

        public Integer getIdDetalleDistributivo() {
            return idDetalleDistributivo;
        }

        public void setIdDetalleDistributivo(Integer idDetalleDistributivo) {
            this.idDetalleDistributivo = idDetalleDistributivo;
        }

        public Integer getIdMallaDetalle() {
            return idMallaDetalle;
        }

        public void setIdMallaDetalle(Integer idMallaDetalle) {
            this.idMallaDetalle = idMallaDetalle;
        }

        public DistributivoCabecera getDistributivoCabecera() {
            return distributivoCabecera;
        }

        public void setDistributivoCabecera(DistributivoCabecera distributivoCabecera) {
            this.distributivoCabecera = distributivoCabecera;
        }

        public DistributivoDetalle getDistributivoDetalle() {
            return distributivoDetalle;
        }

        public void setDistributivoDetalle(DistributivoDetalle distributivoDetalle) {
            this.distributivoDetalle = distributivoDetalle;
        }

        public Docente getDocente() {
            return docente;
        }

        public void setDocente(Docente docente) {
            this.docente = docente;
        }

    }

    public class MateriaMostrar {

        private Integer idMateria;
        private String materia;
        private Integer horas;

        public Integer getIdMateria() {
            return idMateria;
        }

        public void setIdMateria(Integer idMateria) {
            this.idMateria = idMateria;
        }

        public String getMateria() {
            return materia;
        }

        public void setMateria(String materia) {
            this.materia = materia;
        }

        public Integer getHoras() {
            return horas;
        }

        public void setHoras(Integer horas) {
            this.horas = horas;
        }

    }

    public Integer getNumHorasActividades() {
        return numHorasActividades;
    }

    public void setNumHorasActividades(Integer numHorasActividades) {
        this.numHorasActividades = numHorasActividades;
    }

    public Integer getNumHorasFaltantes() {
        return numHorasFaltantes;
    }

    public void setNumHorasFaltantes(Integer numHorasFaltantes) {
        this.numHorasFaltantes = numHorasFaltantes;
    }

}
