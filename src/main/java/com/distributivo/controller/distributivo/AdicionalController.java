package com.distributivo.controller.distributivo;

import com.distributivo.ejb.business.CursoFacade;
import com.distributivo.ejb.business.DedicacionFacade;
import com.distributivo.ejb.business.DistributivoCabeceraFacade;
import com.distributivo.ejb.business.DistributivoDetalleFacade;
import com.distributivo.ejb.business.DocenteFacade;
import com.distributivo.ejb.business.AdicionaleFacade;
import com.distributivo.ejb.business.MallaCabeceraFacade;
import com.distributivo.ejb.business.MallaDetalleFacade;
import com.distributivo.ejb.business.TipoActividadFacade;
import com.distributivo.ejb.business.ParaleloCursoFacade;
import com.distributivo.ejb.business.PeriodoFacade;
import com.distributivo.model.business.Dedicacion;
import com.distributivo.model.business.Adicionale;
import com.distributivo.model.business.DistributivoCabecera;
import com.distributivo.model.business.DistributivoDetalle;
import com.distributivo.model.business.Docente;
import com.distributivo.model.business.MallaDetalle;
import com.distributivo.model.business.TipoActividad;
import com.distributivo.model.business.Periodo;
import com.distributivo.model.business.TitularidadDedicacion;
import com.distributivo.utilidadbase.controller.Mensaje;
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

@Named(value = "adicionalController")
@ViewScoped
public class AdicionalController implements Serializable {

    @EJB
    private DistributivoCabeceraFacade distributivoFacade;
    @EJB
    private AdicionaleFacade adicionaleFacade;
    @EJB
    private DistributivoDetalleFacade distributivoDetalleFacade;
    @EJB
    private PeriodoFacade periodoFacade;
    @EJB
    private DocenteFacade docenteFacade;
    @EJB
    private TipoActividadFacade tipoActividadFacade;
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
    private Integer idTipoActividad;
    private Integer idCurso;
    private Integer idParalelo;
    private String deshabilitarCombo;

    private String observaciones;
    private Integer numeroHoras;

    private Integer numHoras;
    private Integer numHorasActividades;
    private Integer numHorasFaltantes;
    //detalle seleccionado
    private Asignaciones detalleSeleccionado;
    private Dedicacion dedicacionSeleccionado;

    //listas para llenar los combos
    private List<Docente> docenteCombo;
    private List<Dedicacion> dedicacionCombo;
    private List<TipoActividad> tipoActividadCombo;
    //lista para el datatable
    private List<Asignaciones> listaMateriasAsignadas;
    private List<Adicionale> actividadesAdicionales;

    @PostConstruct
    public void inicializar() {
    }

    public void cargarMaterias() {
        System.out.println("Codigo docente " + this.getIdDocente());
        System.out.println("Codigo periodo  " + this.getPeriodo().getId());
        if (this.getIdDocente() == null) {
            Mensaje.ADVERTENCIA("Debe seleccionar un docente");
            return;
        }
        Long idDedSelecionado = null;
        DistributivoCabecera distributivo = distributivoFacade.buscarPorDocentePeriodo(this.getIdDocente(), this.getPeriodo().getId());
        if (distributivo != null) {
            if (distributivo.getDedicacion() != null) {
                idDedSelecionado = distributivo.getDedicacion().getId();
            }

            dedicacionSeleccionado = distributivo.getDedicacion();
            List<AdicionalController.Asignaciones> listado = new ArrayList<>();
            System.out.println("Listado de detalles: " + distributivo.getDistributivoDetalles().size());
            if (distributivo.getDistributivoDetalles().size() > 0) {
                for (DistributivoDetalle det : distributivo.getDistributivoDetalles()) {
                    if (det.getEstado().equals("A")) {
                        AdicionalController.Asignaciones asignar = new AdicionalController.Asignaciones();
                        asignar.setCedula(distributivo.getDocente().getCedula());
                        asignar.setEstado("ACTIVO");
                        asignar.setCurso(det.getCurso().getNombre());
                        asignar.setIdDistributivo(distributivo.getIdDistributivo());
                        asignar.setIdDocente(distributivo.getDocente().getId());
                        asignar.setMateria(det.getMateria().getNombre());
                        asignar.setNombreDocente(distributivo.getDocente().getNombre());
                        asignar.setObservaciones(det.getObservacion());
                        asignar.setDistributivoCabecera(det.getDistributivoCabecera());
                        //hay q calcular las horas
                        List<MallaDetalle> detalle = mallaDetalleFacade.buscarCodigo(det.getIdMallaDetalle());
                        for (MallaDetalle mat : detalle) {
                            asignar.setNumHoras(mat.getNumHoras());
                        }
                        asignar.setParalelo(det.getParalelo().getNombre());
                        listado.add(asignar);
                    }
                }
                listaMateriasAsignadas = listado;
                this.deshabilitarCombo = "SI";
            } else {
                listaMateriasAsignadas = new ArrayList<Asignaciones>();
                dedicacionSeleccionado = new Dedicacion();
                this.deshabilitarCombo = "NO";
            }

            Docente docente = docenteFacade.buscarDocentePorId(this.getIdDocente());
            if (docente != null) {
                List<Dedicacion> dedicacionDisponibles = new ArrayList<Dedicacion>();
                if (docente.getTitularidad().getTitularidadDedicacions().size() > 0) {
                    for (TitularidadDedicacion tit : docente.getTitularidad().getTitularidadDedicacions()) {
                        dedicacionDisponibles.add(tit.getDedicacion());
                    }
                }
                this.dedicacionCombo = dedicacionDisponibles;
            } else {
                this.dedicacionCombo = new ArrayList<>();
            }

            if (idDedSelecionado == null) {
                this.setIdDedicacion(0);
            } else {
                this.setIdDedicacion(Integer.parseInt(String.valueOf(idDedSelecionado)));
            }

            System.out.println(this.deshabilitarCombo + " ded: " + idDedSelecionado);

            //ademas cargar el listado de actividades, si en algun caso tiene
            List<Adicionale> actividadesAd = adicionaleFacade.buscarPorIdCabecera(distributivo.getIdDistributivo());
            if (actividadesAd.size() > 0) {
                actividadesAdicionales = actividadesAd;
            } else {
                actividadesAdicionales = new ArrayList<>();
            }

        } else {
            Docente docente = docenteFacade.buscarDocentePorId(this.getIdDocente());
            if (docente != null) {
                List<Dedicacion> dedicacionDisponibles = new ArrayList<Dedicacion>();
                if (docente.getTitularidad().getTitularidadDedicacions().size() > 0) {
                    for (TitularidadDedicacion tit : docente.getTitularidad().getTitularidadDedicacions()) {
                        dedicacionDisponibles.add(tit.getDedicacion());
                    }
                }
                this.dedicacionCombo = dedicacionDisponibles;
            } else {
                this.dedicacionCombo = new ArrayList<>();
            }

            System.out.println("No hay datos");
            listaMateriasAsignadas = new ArrayList<Asignaciones>();
            this.setIdDedicacion(0);
            this.deshabilitarCombo = "NO";
        }

        //recuperar las actividades si tiene
        //realziar el conteo 
        Integer numero = 0;
        Integer totalHoras = 0;
        Long idDed = null;
        for (Asignaciones det : listaMateriasAsignadas) {
            idDed = det.getDistributivoCabecera().getDedicacion().getId();
            numero = numero + det.getNumHoras();
        }
        System.out.println("numero de horas clases: " + numero);
        System.out.println("dedicacion seleccionado: " + idDed + " - " + this.getIdDedicacion());
        Dedicacion de = new Dedicacion();
        if (idDed == null) {
            de = dedicacionFacade.buscarPorCodigoDedicacion(this.getIdDedicacion());
            this.setIdDedicacion(0);
        } else {
            this.setIdDedicacion(Integer.parseInt(String.valueOf(idDed)));
            de = dedicacionFacade.buscarPorCodigoDedicacion(Integer.parseInt(String.valueOf(idDed)));
        }
        numeroHoras = numero;

        if (de != null) {
            totalHoras = de.getTotal();
        }
        System.out.println("Total de horas General: " + totalHoras);
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
        System.out.println("numero de horas actividades: " + numActividad);
        numHorasActividades = numActividad;
        Integer restantes = totalHoras - (numActividad + numero);
        numHorasFaltantes = restantes;
    }

    public void mostrarDialogo() {
        //resetear valores del dialogo
        this.setNumeroHoras(0);
        this.setIdTipoActividad(0);
        this.setObservaciones("");

        if (this.getIdDocente() == null) {
            Mensaje.ADVERTENCIA("Debe seleccionar el docente");
            return;
        }

        if (this.getIdDedicacion() == null) {
            Mensaje.ADVERTENCIA("Debe seleccionar dedicacion");
            return;
        }
        //validar las horas ya registradas con las horas maximas de la dedicacion
        Integer maximoHoras = 0;
        Integer minimoHoras = 0;
        Integer maximoGlobal = 0;
        Integer horasAsignados = 0;
        Integer totalHoras = 0;
        Integer horasactividades = 0;
        List<Dedicacion> dedicaciones = dedicacionFacade.buscarPorCodigo(this.getIdDedicacion());
        for (Dedicacion ded : dedicaciones) {
            maximoHoras = ded.getMaxActividades();
            minimoHoras = ded.getMinActividades();
            maximoGlobal = ded.getTotal();

        }
        //se suman las actividades agregadas y las horas de la materia
        if (actividadesAdicionales != null) {
            for (Adicionale ac : actividadesAdicionales) {
                //horasAsignados = horasAsignados + ac.getHoras();
                horasactividades = horasactividades + ac.getHoras();
            }
        }
        if (listaMateriasAsignadas != null) {
            for (Asignaciones asig : listaMateriasAsignadas) {
                horasAsignados = horasAsignados + asig.getNumHoras();
            }
        }
        totalHoras = horasactividades + horasAsignados;
        System.out.print("Horas asignadas: " + horasAsignados);
        System.out.print("Horas Total: " + maximoGlobal);
        if (totalHoras >= maximoGlobal) {
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se puede registrar más actividades", "");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            return;
        }
        if (horasactividades >= maximoHoras) {
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se puede registrar más actividades", "");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            return;
        }

        RequestContext req = RequestContext.getCurrentInstance();
        req.execute("PF('wdialogo').show();");
    }

    public void grabarAsignacion() {
        try {
            //validar el ingreso de los datos
            if (this.getIdTipoActividad() == null) {
                System.out.println("Debe registrar la actividad");
                Mensaje.ADVERTENCIA("Debes seleccionar la actividad");
                return;
            }
            if (this.getNumeroHoras() == null) {
                System.out.println("Debe registrar el numero de horas");
                RequestContext.getCurrentInstance()
                        .execute("mensajear('Debe registrar el número de horas');");
                return;
            }
            //antes de grabar hay q validar las horas si cumple
            //validar las horas ya registradas con las horas maximas de la dedicacion
            Integer maximoHoras = 0;
            Integer minimoHoras = 0;
            Integer horasAsignados = 0;
            Integer horasactividades = 0;
            List<Dedicacion> dedicaciones = dedicacionFacade.buscarPorCodigo(this.getIdDedicacion());
            for (Dedicacion ded : dedicaciones) {
                maximoHoras = ded.getMaxActividades();
                minimoHoras = ded.getMinActividades();
            }
            //se suman las actividades agregadas y las horas de la materia
            if (actividadesAdicionales != null) {
                for (Adicionale ac : actividadesAdicionales) {
                    horasAsignados = horasAsignados + ac.getHoras();
                    horasactividades = horasactividades + ac.getHoras();

                }
            }
            //  horasactividades = horasactividades + this.getNumHoras();

            if (listaMateriasAsignadas != null) {
                for (Asignaciones asig : listaMateriasAsignadas) {
                    horasAsignados = horasAsignados + asig.getNumHoras();
                }

            }
            System.out.println("numero");
            System.out.println(this.getNumeroHoras());
            horasAsignados = horasAsignados + this.getNumeroHoras();
            horasactividades = horasactividades + this.getNumeroHoras();
            
            Integer horasDisponible = maximoHoras - (horasAsignados);
            System.out.println("numero de horas disponibles " + horasDisponible);
            
            
            if (horasactividades <= maximoHoras || horasDisponible >= 0) {//aqui es el prpblema
                System.out.println("id: " + this.getIdTipoActividad());
                System.out.println("Materias asignadas " + listaMateriasAsignadas.size());

                TipoActividad tipoActividad = tipoActividadFacade.buscarCodigo(this.getIdTipoActividad());
                Adicionale adicionalGrabar = new Adicionale();
                DistributivoCabecera distributivoCabecera = new DistributivoCabecera();
                if (listaMateriasAsignadas.size() > 0) {//tiene registros en la cabecera
                    //recupero la cabecera para modificar
                    distributivoCabecera = distributivoFacade.buscarCodigo(listaMateriasAsignadas.get(0).getIdDistributivo());
                    adicionalGrabar.setEstado("ACTIVO");
                    adicionalGrabar.setDistributivoCabecera(distributivoCabecera);
                    adicionalGrabar.setHoras(this.getNumeroHoras());
                    adicionalGrabar.setIdAdicional(null);
                    adicionalGrabar.setObservaciones(this.getObservaciones());
                    adicionalGrabar.setTipoActividad(tipoActividad);
                    if (distributivoCabecera.getAdicionales().size() > 0) {
                        distributivoCabecera.getAdicionales().add(adicionalGrabar);
                    } else {
                        List<Adicionale> listaAdicional = new ArrayList<>();
                        listaAdicional.add(adicionalGrabar);
                        distributivoCabecera.setAdicionales(listaAdicional);
                    }
                    distributivoFacade.modificar(distributivoCabecera);
                    Mensaje.SUCESO("Se registro con exito la actividad");
                    cargarMaterias();
                } else {//es un nuevo registro en la cabecera
                    //creo una nueva cabecera
                    distributivoCabecera.setFechaIngreso(new Date());
                    distributivoCabecera.setPeriodo(this.getPeriodo());
                    distributivoCabecera.setIdDistributivo(null);
                    Docente docente = docenteFacade.buscarDocentePorId(this.getIdDocente());
                    if (docente != null) {
                        distributivoCabecera.setDocente(docente);
                    }
                    Dedicacion dedicacion = dedicacionFacade.buscarPorCodigoDedicacion(this.getIdDedicacion());
                    if (dedicacion != null) {
                        distributivoCabecera.setDedicacion(dedicacion);
                    }
                    distributivoCabecera.setEstado("ACTIVO");
                    distributivoCabecera.setObservacion("");
                    distributivoCabecera.setUsuario(observaciones);
                    //detalle,q seria las actividades

                    adicionalGrabar.setEstado("ACTIVO");
                    adicionalGrabar.setDistributivoCabecera(distributivoCabecera);
                    adicionalGrabar.setHoras(this.getNumeroHoras());
                    adicionalGrabar.setIdAdicional(null);
                    adicionalGrabar.setObservaciones(this.getObservaciones());
                    adicionalGrabar.setTipoActividad(tipoActividad);

                    //luego el enlace con la cabecera
                    List<Adicionale> listaAdicional = new ArrayList<>();
                    listaAdicional.add(adicionalGrabar);
                    distributivoCabecera.setAdicionales(listaAdicional);
                    distributivoFacade.guardar(distributivoCabecera);
                    RequestContext.getCurrentInstance()
                            .execute("mensajear('Se registro correctamente');");
                    RequestContext.getCurrentInstance().execute("PF('wdialogo').hide()");
                    cargarMaterias();
                    System.out.println("se grabo");
                }
            } else {
                System.out.println("No se puede agregar mas horas de lo permitido");
                RequestContext.getCurrentInstance()
                        .execute("mensajear('No se puede agregar mas horas de lo permitido');");
            }

        } catch (Exception ex) {
        }
    }

    public void eliminar(Adicionale adicional) {
        System.out.println("objeto seleccionado: " + adicional.getTipoActividad().getActividad());
        adicional.setEstado("INACTIVO");
        adicionaleFacade.modificar(adicional);
        Mensaje.SUCESO("Se elimino el registro correctamente");
        cargarMaterias();
    }

    //getters and setters
    public Periodo getPeriodo() {
        List<Periodo> lista = periodoFacade.buscarAutorizado();
        if (lista.size() > 0) {
            periodo = lista.get(0);
        }
        return periodo;
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

    public Integer getIdDedicacion() {
        return idDedicacion;
    }

    public void setIdDedicacion(Integer idDedicacion) {
        this.idDedicacion = idDedicacion;
    }

    public DistributivoCabeceraFacade getDistributivoFacade() {
        return distributivoFacade;
    }

    public void setDistributivoFacade(DistributivoCabeceraFacade distributivoFacade) {
        this.distributivoFacade = distributivoFacade;
    }

    public String getDeshabilitarCombo() {
        return deshabilitarCombo;
    }

    public void setDeshabilitarCombo(String deshabilitarCombo) {
        this.deshabilitarCombo = deshabilitarCombo;
    }

    public Integer getIdTipoActividad() {
        return idTipoActividad;
    }

    public void setIdTipoActividad(Integer idTipoActividad) {
        this.idTipoActividad = idTipoActividad;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
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

    public Dedicacion getDedicacionSeleccionado() {
        return dedicacionSeleccionado;
    }

    public void setDedicacionSeleccionado(Dedicacion dedicacionSeleccionado) {
        this.dedicacionSeleccionado = dedicacionSeleccionado;
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

    public List<Asignaciones> getListaMateriasAsignadas() {
        return listaMateriasAsignadas;
    }

    public void setListaMateriasAsignadas(List<Asignaciones> listaMateriasAsignadas) {
        this.listaMateriasAsignadas = listaMateriasAsignadas;
    }

    public Asignaciones getDetalleSeleccionado() {
        return detalleSeleccionado;
    }

    public void setDetalleSeleccionado(Asignaciones detalleSeleccionado) {
        this.detalleSeleccionado = detalleSeleccionado;
    }

    public List<TipoActividad> getTipoActividadCombo() {
        tipoActividadCombo = tipoActividadFacade.buscarPorEstado();
        return tipoActividadCombo;
    }

    public List<Dedicacion> getDedicacionCombo() {
        return dedicacionCombo;
    }

    public void setDedicacionCombo(List<Dedicacion> dedicacionCombo) {
        this.dedicacionCombo = dedicacionCombo;
    }

    public void setTipoActividadCombo(List<TipoActividad> tipoActividadCombo) {
        this.tipoActividadCombo = tipoActividadCombo;
    }

    public List<Adicionale> getActividadesAdicionales() {
        return actividadesAdicionales;
    }

    public void setActividadesAdicionales(List<Adicionale> actividadesAdicionales) {
        this.actividadesAdicionales = actividadesAdicionales;
    }

    //clase anidada que me permite visualizar los datos que necesito
    public class Asignaciones {

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

        public DistributivoCabecera getDistributivoCabecera() {
            return distributivoCabecera;
        }

        public void setDistributivoCabecera(DistributivoCabecera distributivoCabecera) {
            this.distributivoCabecera = distributivoCabecera;
        }

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
    }

    public Integer getNumHoras() {
        return numHoras;
    }

    public void setNumHoras(Integer numHoras) {
        this.numHoras = numHoras;
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
