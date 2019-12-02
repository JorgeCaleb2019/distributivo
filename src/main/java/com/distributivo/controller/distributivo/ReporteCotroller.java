package com.distributivo.controller.distributivo;

import com.distributivo.ejb.business.ConfiguracionFacade;
import com.distributivo.ejb.business.CursoFacade;
import com.distributivo.ejb.business.DistributivoCabeceraFacade;
import com.distributivo.ejb.business.PeriodoFacade;
import com.distributivo.ejb.business.MallaCabeceraFacade;
import com.distributivo.ejb.business.ParaleloCursoFacade;
import com.distributivo.model.business.Configuracion;
import com.distributivo.model.business.Curso;
import com.distributivo.model.business.Docente;
import com.distributivo.model.business.Periodo;
import com.distributivo.model.business.MallaCabecera;
import com.distributivo.model.business.DistributivoCabecera;
import com.distributivo.model.business.Materia;
import com.distributivo.model.business.ParaleloCurso;
import com.distributivo.util.JasperReportUtil;
import com.distributivo.utilidadbase.controller.Mensaje;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named(value = "reporteController")
@ViewScoped
public class ReporteCotroller implements Serializable {

    @EJB
    private PeriodoFacade periodoFacade;
    @EJB
    private ConfiguracionFacade configuracionFacade;
    @EJB
    private MallaCabeceraFacade mallaCabeceraFacade;
    @EJB
    private DistributivoCabeceraFacade distributivoCabeceraFacade;

    @EJB
    private ParaleloCursoFacade paraleloFacade;

    @EJB
    private CursoFacade cursofacade;

    private Integer idPeriodo;
    private Integer idPeriodoDistributivo;
    private Integer idPeriodoCargaHoraria;
    private Integer idPeriodoActividad;
    private Integer idPeriodoDocenteActividad;
    private Integer idDocenteActividad;
    private Integer idPeriodoCurso;
    private Integer idPeriodoCursoParalelo;
    private Integer idMalla;
    private Integer idCurso;
    private Integer idCursoParalelo;

    private Integer idParalelo;

    private List<Docente> docenteCombo;
    private Integer idDocente;

    private List<Curso> cursoCombo;

    private List<Materia> materiaCombo;

    private List<Periodo> periodoCombo;
    private List<MallaCabecera> mallaCombo;
    private StreamedContent descarga;
    private String host = "";
    private String puerto = "";
    private String base = "";
    private String clave = "";
    private String usuario = "";

    private List<ParaleloCurso> paraleloCombo;

    @PostConstruct
    public void inicializar() {
        Configuracion configuracion = configuracionFacade.buscarConfiguracion();
        if (configuracion != null) {
            host = configuracion.getHost();
            puerto = configuracion.getPuerto();
            base = configuracion.getBase();
            clave = configuracion.getClave();
            usuario = configuracion.getUsuario();
        }
    }

    public List<Docente> getDocenteCombo() {
        return docenteCombo;
    }

    public void setDocenteCombo(List<Docente> docenteCombo) {
        this.docenteCombo = docenteCombo;
    }

    public List<Curso> getCursoCombo() {
        cursoCombo = cursofacade.buscarPorEstado();
        return cursoCombo;
    }

    public void descargarReporteDocente() throws JRException, FileNotFoundException {
        if (this.getIdPeriodo() != null) {
            Periodo periodoSeleccionado = periodoFacade.buscarPorId(this.getIdPeriodo());
            String nombrePdf = "listado_docente.pdf";
            System.out.println("Periodo seleccionado: " + this.getIdPeriodo());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ID_PERIODO", this.getIdPeriodo());
            map.put("INSTITUCION", "COLEGIO \"HACIA LA CUMBRE\"");
            if (periodoSeleccionado != null) {
                map.put("PERIODO", periodoSeleccionado.getTitulo());
            } else {
                map.put("PERIODO", "");
            }
            JasperReportUtil reporte = new JasperReportUtil();
            String rutaPdf = reporte.crearReporte(map, "/docenteperiodo/rptListadoDocente.jasper", "/docenteperiodo/reporte.pdf", this.getHost(), this.getPuerto(), this.getBase(), this.getUsuario(), this.getClave());
            this.descarga = new DefaultStreamedContent(new FileInputStream(new File(rutaPdf)), "", nombrePdf);
        }
    }

    public void descargarReporteMalla() throws JRException, FileNotFoundException {
        if (this.getIdMalla() != null) {
            MallaCabecera malla = mallaCabeceraFacade.buscarPorIdMalla(this.getIdMalla());
            String nombrePdf = "";
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ID_MALLA", this.getIdMalla());
            map.put("INSTITUCION", "COLEGIO \"HACIA LA CUMBRE\"");
            if (malla != null) {
                map.put("DESCRIPCION", malla.getDescripcion());
                nombrePdf = malla.getDescripcion() + ".pdf";
            } else {
                map.put("DESCRIPCION", "");
                nombrePdf = "malla.pdf";
            }
            JasperReportUtil reporte = new JasperReportUtil();
            String rutaPdf = reporte.crearReporte(map, "/mallacurricular/rptMalla.jasper", "/mallacurricular/" + nombrePdf, this.getHost(), this.getPuerto(), this.getBase(), this.getUsuario(), this.getClave());
            this.descarga = new DefaultStreamedContent(new FileInputStream(new File(rutaPdf)), "", nombrePdf);
        }
    }

    public void descargarReporteDistributivo() throws JRException, FileNotFoundException {
        System.out.println("Entra 1");
        System.out.println("id periodo seleccionado 1 " + this.getIdPeriodoCargaHoraria());
        if (this.getIdPeriodoCargaHoraria() != null) {
            System.out.println("Entra");
            System.out.println("id periodo seleccionado " + this.getIdPeriodoCargaHoraria());
            Periodo periodo = periodoFacade.buscarPorId(this.getIdPeriodoCargaHoraria());
            String nombrePdf = "";
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ID_PERIODO", this.getIdPeriodoCargaHoraria());
            map.put("INSTITUCION", "COLEGIO \"HACIA LA CUMBRE\"");
            if (periodo != null) {
                map.put("PERIODO", periodo.getTitulo());
                nombrePdf = "distributivo.pdf";
            } else {
                map.put("DESCRIPCION", "");
                nombrePdf = "distributivo.pdf";
            }
            JasperReportUtil reporte = new JasperReportUtil();
            String rutaPdf = reporte.crearReporte(map, "/cargahoraria/rptDistributivo.jasper", "/cargahoraria/" + nombrePdf, this.getHost(), this.getPuerto(), this.getBase(), this.getUsuario(), this.getClave());
            this.descarga = new DefaultStreamedContent(new FileInputStream(new File(rutaPdf)), "", nombrePdf);
        }
    }

    public void cargarDocentes() {
        System.out.println("cambia combo periodo");
        List<DistributivoCabecera> distributivo = distributivoCabeceraFacade.buscarPorPeriodo(this.getIdPeriodoActividad());
        List<Docente> listadoDocentes = new ArrayList<>();
        for (DistributivoCabecera cab : distributivo) {
            if (cab.getEstado().equals("ACTIVO")) {
                listadoDocentes.add(cab.getDocente());
            }
        }
        docenteCombo = listadoDocentes;
    }

    public void descargarReporteActividadesDocente() throws JRException, FileNotFoundException {

        System.out.println("entro a generar");
        if (this.getIdPeriodoActividad() == null || this.getIdPeriodoActividad() <= 0) {
            Mensaje.ADVERTENCIA("Debe seleccionar un periodo");
            return;
        }

        if (this.getIdDocenteActividad() == null || this.getIdDocenteActividad() <= 0) {
            Mensaje.ADVERTENCIA("Debe seleccionar un docente");
            return;
        }
        System.out.println("docente " + this.getIdDocenteActividad());
        System.out.println("docente " + this.getIdPeriodoActividad());
        if (this.getIdPeriodoActividad() != null) {
            Periodo periodo = periodoFacade.buscarPorId(this.getIdPeriodoActividad());
            System.out.println("periodo " + periodo.getTitulo());
            String nombrePdf = "";
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ID_PERIODO", this.getIdPeriodoActividad());
            map.put("ID_DOCENTE", this.getIdDocenteActividad());

            map.put("INSTITUCION", "COLEGIO \"HACIA LA CUMBRE\"");
            if (periodo != null) {
                map.put("PERIODO", periodo.getTitulo());
                nombrePdf = "docente_actividad.pdf";
            } else {
                map.put("DESCRIPCION", "");
                nombrePdf = "docente_actividad.pdf";
            }
            JasperReportUtil reporte = new JasperReportUtil();
            String rutaPdf = reporte.crearReporte(map, "/docentesactividad/rptDistributivoActividades.jasper", "/docentesactividad/" + nombrePdf, this.getHost(), this.getPuerto(), this.getBase(), this.getUsuario(), this.getClave());
            this.descarga = new DefaultStreamedContent(new FileInputStream(new File(rutaPdf)), "", nombrePdf);
        }
    }

    public void descargarDistributivoCurso() throws JRException, FileNotFoundException {
        
        System.out.println("entro a generar reporte por periodo y curso");
        System.out.println("periodo " + getIdPeriodoCurso());
        System.out.println("curso" + getIdCurso());
        
        if (this.getIdPeriodoCurso()== null || this.getIdPeriodoCurso() <= 0) {
            Mensaje.ADVERTENCIA("Debe seleccionar un periodo");
            return;
        }
        if (this.getIdCurso() == null || this.getIdCurso() <= 0) {
            Mensaje.ADVERTENCIA("Debe seleccionar un Curso");
            return;
        }
        
        if (this.getIdPeriodoCurso() != null) {
            Periodo periodo = periodoFacade.buscarPorId(this.getIdPeriodoCurso());
            String nombrePdf = "";
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ID_PERIODO", this.getIdPeriodoCurso());
            map.put("ID_CURSO", this.getIdCurso());

            map.put("INSTITUCION", "COLEGIO \"HACIA LA CUMBRE\"");
            if (periodo != null) {
                map.put("PERIODO", periodo.getTitulo());
                nombrePdf = "distributivo.pdf";
            } else {
                map.put("DESCRIPCION", "");
                nombrePdf = "distributivo.pdf";
            }
            JasperReportUtil reporte = new JasperReportUtil();
            String rutaPdf = reporte.crearReporte(map, "/docenteperiodocurso/rptDistributivoCurso.jasper", "/docenteperiodocurso/" + nombrePdf, this.getHost(), this.getPuerto(), this.getBase(), this.getUsuario(), this.getClave());
            this.descarga = new DefaultStreamedContent(new FileInputStream(new File(rutaPdf)), "", nombrePdf);
        }
    }

    public void descargarDistributivoCursoParalelo() throws JRException, FileNotFoundException {

        System.out.println("entro a generar");
        if (this.getIdPeriodoCursoParalelo()== null || this.getIdPeriodoCursoParalelo() <= 0) {
            Mensaje.ADVERTENCIA("Debe seleccionar un periodo");
            return;
        }

        System.out.println("CURSO" + getIdCursoParalelo());
        System.out.println("pARALELO" + getIdParalelo());
        if (this.getIdCursoParalelo()== null || this.getIdCursoParalelo() <= 0) {
            Mensaje.ADVERTENCIA("Debe seleccionar un Curso");
            return;
        }

        if (this.getIdParalelo() == null || this.getIdParalelo() <= 0) {
            Mensaje.ADVERTENCIA("Debe seleccionar un Paralelo");
            return;
        }

        if (this.getIdPeriodoCursoParalelo() != null) {
            Periodo periodo = periodoFacade.buscarPorId(this.getIdPeriodoCursoParalelo());
            String nombrePdf = "";
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ID_PERIODO", this.getIdPeriodoCursoParalelo());
            map.put("ID_CURSO", this.getIdCursoParalelo());
            map.put("ID_PARALELO", this.getIdParalelo());

            map.put("INSTITUCION", "COLEGIO \"HACIA LA CUMBRE\"");
            if (periodo != null) {
                map.put("PERIODO", periodo.getTitulo());
                nombrePdf = "distributivo.pdf";
            } else {
                map.put("DESCRIPCION", "");
                nombrePdf = "distributivo.pdf";
                //
            }
            JasperReportUtil reporte = new JasperReportUtil();
            String rutaPdf = reporte.crearReporte(map, "/docenteperiodocursoparalelo/rptDistributivoParalelo.jasper", "/docenteperiodocursoparalelo/" + nombrePdf, this.getHost(), this.getPuerto(), this.getBase(), this.getUsuario(), this.getClave());
            this.descarga = new DefaultStreamedContent(new FileInputStream(new File(rutaPdf)), "", nombrePdf);
        }
    }

    public List<Periodo> getPeriodoCombo() {
        periodoCombo = periodoFacade.buscarPeriodoActivo();
        return periodoCombo;
    }

    public void cargarCurso() {
        //verificar los que ya han sido asignados a docentes.. con la finaidad de no mostrarla en el combo
        System.out.println("cargar paralelos");
        List<ParaleloCurso> paralelos = paraleloFacade.buscarParaleloPorCurso(this.getIdCursoParalelo());
        paraleloCombo = paralelos;

    }

    public void descargarDocenteActividad() throws JRException, FileNotFoundException {
        System.out.println("entro a generar docentes por actividades");
        if (this.getIdPeriodoDocenteActividad()== null || this.getIdPeriodoDocenteActividad() <= 0) {
            Mensaje.ADVERTENCIA("Debe seleccionar un periodo");
            return;
        }

        if (this.getIdPeriodoDocenteActividad() != null) {
            Periodo periodo = periodoFacade.buscarPorId(this.getIdPeriodoDocenteActividad());
            String nombrePdf = "";
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ID_PERIODO", this.getIdPeriodoDocenteActividad());

            map.put("INSTITUCION", "COLEGIO \"HACIA LA CUMBRE\"");
            if (periodo != null) {
                map.put("PERIODO", periodo.getTitulo());
                nombrePdf = "distributivo.pdf";
            } else {
                map.put("DESCRIPCION", "");
                nombrePdf = "distributivo.pdf";
            }
            JasperReportUtil reporte = new JasperReportUtil();
            String rutaPdf = reporte.crearReporte(map, "/docentesporactividad/rptActividadDocente.jasper", "/docentesporactividad/" + nombrePdf, this.getHost(), this.getPuerto(), this.getBase(), this.getUsuario(), this.getClave());
            this.descarga = new DefaultStreamedContent(new FileInputStream(new File(rutaPdf)), "", nombrePdf);
        }
    }
    public void setPeriodoCombo(List<Periodo> periodoCombo) {
        this.periodoCombo = periodoCombo;
    }

    public Integer getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public StreamedContent getDescarga() {
        return descarga;
    }

    public void setDescarga(StreamedContent descarga) {
        this.descarga = descarga;
    }

    public Integer getIdMalla() {
        return idMalla;
    }

    public void setIdMalla(Integer idMalla) {
        this.idMalla = idMalla;
    }

    public List<MallaCabecera> getMallaCombo() {
        mallaCombo = mallaCabeceraFacade.buscarTodasMallas();
        return mallaCombo;
    }

    public void setMallaCombo(List<MallaCabecera> mallaCombo) {
        this.mallaCombo = mallaCombo;
    }

    public Integer getIdPeriodoDistributivo() {
        return idPeriodoDistributivo;
    }

    public void setIdPeriodoDistributivo(Integer idPeriodoDistributivo) {
        this.idPeriodoDistributivo = idPeriodoDistributivo;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPuerto() {
        return puerto;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(Integer idDocente) {
        this.idDocente = idDocente;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public ParaleloCursoFacade getParaleloFacade() {
        return paraleloFacade;
    }

    public void setParaleloFacade(ParaleloCursoFacade paraleloFacade) {
        this.paraleloFacade = paraleloFacade;
    }

    public List<Materia> getMateriaCombo() {
        return materiaCombo;
    }

    public void setMateriaCombo(List<Materia> materiaCombo) {
        this.materiaCombo = materiaCombo;
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

    public Integer getIdPeriodoCargaHoraria() {
        return idPeriodoCargaHoraria;
    }

    public void setIdPeriodoCargaHoraria(Integer idPeriodoCargaHoraria) {
        this.idPeriodoCargaHoraria = idPeriodoCargaHoraria;
    }

    public Integer getIdPeriodoActividad() {
        return idPeriodoActividad;
    }

    public void setIdPeriodoActividad(Integer idPeriodoActividad) {
        this.idPeriodoActividad = idPeriodoActividad;
    }

    public Integer getIdDocenteActividad() {
        return idDocenteActividad;
    }

    public void setIdDocenteActividad(Integer idDocenteActividad) {
        this.idDocenteActividad = idDocenteActividad;
    }

    public Integer getIdPeriodoCurso() {
        return idPeriodoCurso;
    }

    public void setIdPeriodoCurso(Integer idPeriodoCurso) {
        this.idPeriodoCurso = idPeriodoCurso;
    }

    public Integer getIdPeriodoCursoParalelo() {
        return idPeriodoCursoParalelo;
    }

    public void setIdPeriodoCursoParalelo(Integer idPeriodoCursoParalelo) {
        this.idPeriodoCursoParalelo = idPeriodoCursoParalelo;
    }

    public Integer getIdCursoParalelo() {
        return idCursoParalelo;
    }

    public void setIdCursoParalelo(Integer idCursoParalelo) {
        this.idCursoParalelo = idCursoParalelo;
    }

    public Integer getIdPeriodoDocenteActividad() {
        return idPeriodoDocenteActividad;
    }

    public void setIdPeriodoDocenteActividad(Integer idPeriodoDocenteActividad) {
        this.idPeriodoDocenteActividad = idPeriodoDocenteActividad;
    }
    
}
