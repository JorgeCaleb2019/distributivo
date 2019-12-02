package com.distributivo.controller.distributivo;

import com.distributivo.ejb.business.CursoFacade;
import com.distributivo.ejb.business.MallaCabeceraFacade;
import com.distributivo.ejb.business.MallaDetalleFacade;
import com.distributivo.ejb.business.DistributivoCabeceraFacade;
import com.distributivo.ejb.business.MateriaFacade;
import com.distributivo.ejb.business.PeriodoFacade;
import com.distributivo.model.business.MallaDetalle;
import com.distributivo.model.business.Curso;
import com.distributivo.model.business.DistributivoCabecera;
import com.distributivo.model.business.DistributivoDetalle;
import com.distributivo.model.business.Materia;
import com.distributivo.model.business.Periodo;
import com.distributivo.model.business.MallaCabecera;
import com.distributivo.util.ConectarBD;
import com.distributivo.util.JasperReportUtil;
import com.distributivo.utilidadbase.controller.Mensaje;
import com.tesis.utilidadbase.facade.StateEntity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.engine.util.JRLoader;
import java.util.UUID;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperRunManager;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named(value = "horarioController")
@ViewScoped
public class HorarioController implements Serializable {

    @EJB
    private PeriodoFacade periodoFacade;

    @EJB
    private DistributivoCabeceraFacade distributivoFacade;
    @EJB
    private MallaDetalleFacade mallaDetalleFacade;

    private Integer idPeriodo;
    //variables para recuperar las claves de los combos
    //variables para llenar los combos

    private List<Periodo> periodoCombo;
    private List<Asignaciones> asignados;

    
    private StreamedContent descarga;
    
    @PostConstruct
    public void inicializar() {

    }

    public void mostrarDialogo() {
        //logica
        RequestContext req = RequestContext.getCurrentInstance();
        req.execute("PF('wdialogo').show();");
    }

    public void cargarDistributivo() {
        List<Asignaciones> listado = new ArrayList<>();
        System.out.println("id periodos: " + this.getIdPeriodo());
        List<DistributivoCabecera> listaDistributivos = distributivoFacade.buscarPorPeriodo(this.getIdPeriodo());
        for (DistributivoCabecera dis : listaDistributivos) {
            for (DistributivoDetalle det : dis.getDistributivoDetalles()) {
                Asignaciones asignar = new Asignaciones();
                asignar.setCedula(dis.getDocente().getCedula());
                asignar.setCurso(det.getCurso().getNombre());
                asignar.setIdDistributivo(dis.getIdDistributivo());
                asignar.setIdDocente(dis.getDocente().getId());
                asignar.setMateria(det.getMateria().getNombre());
                asignar.setNombreDocente(dis.getDocente().getNombre());
                //hay q calcular las horas

                List<MallaDetalle> detalle = mallaDetalleFacade.buscarCodigo(det.getIdMallaDetalle());
                for (MallaDetalle mat : detalle) {
                    asignar.setNumHoras(mat.getNumHoras());
                }
                asignar.setParalelo(det.getParalelo().getNombre());
                listado.add(asignar);
            }
        }
        asignados = listado;
    }
    private JasperReport report;
    private JasperPrint reportFilled;
    private JasperViewer viewer;

    public void cargarReporte() throws JRException, FileNotFoundException, IOException {
        //map es el q permite pasar parametros       
    }

    public List<Periodo> getPeriodoCombo() {
        periodoCombo = periodoFacade.buscarPeriodoActivo();
        return periodoCombo;
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

    public List<Asignaciones> getAsignados() {
        return asignados;
    }

    public void setAsignados(List<Asignaciones> asignados) {
        this.asignados = asignados;
    }

    public StreamedContent getDescarga() {
        return descarga;
    }

    public void setDescarga(StreamedContent descarga) {
        this.descarga = descarga;
    }

    
    //clase anidada que me permite visualizar los datos que necesito
    public class Asignaciones {

        private Integer idDistributivo;
        private Integer idDocente;
        private String nombreDocente;
        private String cedula;
        private String materia;
        private String curso;
        private String paralelo;
        private Integer numHoras;

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
    }
}
