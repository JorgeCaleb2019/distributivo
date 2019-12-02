package com.distributivo.controller.distributivo;

import com.distributivo.ejb.business.ConfiguracionFacade;
import com.distributivo.ejb.business.MallaDetalleFacade;
import com.distributivo.ejb.business.PeriodoFacade;
import com.distributivo.model.business.Configuracion;
import com.distributivo.model.business.DistributivoCabecera;
import com.distributivo.model.business.MallaDetalle;
import com.distributivo.model.business.DistributivoDetalle;
import com.distributivo.model.business.Adicionale;
import com.distributivo.model.business.Periodo;
import com.distributivo.util.JasperReportUtil;
import com.distributivo.utilidadbase.controller.Mensaje;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named(value = "autorizarController")
@ViewScoped
public class AutorizarController implements Serializable {

    @EJB
    private PeriodoFacade periodoFacade;

    @EJB
    private ConfiguracionFacade configuracionFacade;
    @EJB
    private MallaDetalleFacade mallaFacade;
    private Integer idPeriodo;
    private String observacionAnular;
    //variables para recuperar las claves de los combos
    //variables para llenar los combos

    private List<PeriodoDistributivo> periodoLista;
    private List<Periodo> periodoCombo;
    private PeriodoDistributivo periodoSeleccionado;
    private Integer idPeriodoComboSeleccionado;
    private StreamedContent descarga;
    private String host = "";
    private String puerto = "";
    private String base = "";
    private String clave = "";
    private String usuario = "";

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

    public Integer getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public List<PeriodoDistributivo> getPeriodoLista() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        List<Periodo> lista = periodoFacade.buscarPeriodoActivo();
        List<PeriodoDistributivo> periodoDis = new ArrayList<>();
        for (Periodo per : lista) {
            PeriodoDistributivo add = new PeriodoDistributivo();
            add.setAutorizacion(per.getEstadoDistributivo());
            add.setEstado(per.getEstado());
            add.setIdPeriodo(per.getId());
            add.setNombre(per.getTitulo());
            add.setPeriodo(per);
            add.setFechaInicio(format.format(per.getFechaInicio()));
            add.setFechaFin(format.format(per.getFechaFinal()));
            periodoDis.add(add);
        }
        periodoLista = periodoDis;
        return periodoLista;
    }

    public void clonar() {
        try {
            if (this.getPeriodoSeleccionado() == null) {
                Mensaje.ERROR("Debe seleccionar un PERIODO!!");
                return;
            }
            Integer numeroDistributivo = 0;
            for (DistributivoCabecera dis : this.getPeriodoSeleccionado().getPeriodo().getDistributivoCabeceras()) {
                if (dis.getEstado().equals("ACTIVO")) {
                    numeroDistributivo = numeroDistributivo + 1;
                }
            }
            System.out.println("num: " + numeroDistributivo);
            if (numeroDistributivo == 0) {
                Mensaje.ERROR("El periodo seleccionado no tiene DISTRIBUTIVOS!!");
                return;
            }
            //cargar los periodos que estan disponibles para clonar
            List<Periodo> lista = periodoFacade.buscarPeriodoActivo();
            List<Periodo> listaPer = new ArrayList<>();
            Integer numDis = 0;
            for (Periodo per : lista) {
                numDis = 0;
                for (DistributivoCabecera cab : per.getDistributivoCabeceras()) {
                    if (cab.getEstado().equals("ACTIVO")) {
                        numDis++;
                    }
                }
                if (numDis == 0) {
                    listaPer.add(per);
                }
            }
            this.periodoCombo = listaPer;
            System.out.println("clonar: " + this.getPeriodoSeleccionado().getNombre());
            RequestContext req = RequestContext.getCurrentInstance();
            req.execute("PF('wdialogo').show();");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void autorizar() {
        try {
            if (this.getPeriodoSeleccionado() == null) {
                Mensaje.ERROR("Debe seleccionar un PERIODO!!");
                return;
            }
            System.out.println("autorizar: " + this.getPeriodoSeleccionado().getNombre());
            boolean bandera = false;
            for (PeriodoDistributivo per : this.periodoLista) {
                if (per.getAutorizacion().equals("AUTORIZADO")) {
                    bandera = true;
                }
            }
            if (bandera == true) {
                Mensaje.ERROR("Solo puede existir un registro AUTORIZADO!!");
                return;
            }
            if (!this.getPeriodoSeleccionado().getAutorizacion().equals("GENERADO")) {
                Mensaje.ERROR("Solo puede AUTORIZAR un registro GENERADO!!");
                return;
            }
            //permite autorizar
            Periodo periodoAutorizar = this.getPeriodoSeleccionado().getPeriodo();
            periodoAutorizar.setEstadoDistributivo("AUTORIZADO");
            periodoFacade.modificar(periodoAutorizar);
            Mensaje.SUCESO("Se ha AUTORIZADO el periodo!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void publicar() {
        try {
            if (this.getPeriodoSeleccionado() == null) {
                Mensaje.ERROR("Debe seleccionar un PERIODO!!");
                return;
            }
            System.out.println("publicar: " + this.getPeriodoSeleccionado().getNombre());
            boolean bandera = false;
            for (PeriodoDistributivo per : this.periodoLista) {
                if (per.getAutorizacion().equals("PUBLICADO")) {
                    bandera = true;
                }
            }
            if (bandera == true) {
                Mensaje.ERROR("Ya existe un distributivo PUBLICADO, debe de finalizarlo para PUBLICAR otro distributivo!!");
                return;
            }
            if (!this.getPeriodoSeleccionado().getAutorizacion().equals("AUTORIZADO")) {
                Mensaje.ERROR("Solo se puede PUBLICAR un distributivo AUTORIZADO!!");
                return;
            }
            //verificar si existen horas minimas
            Periodo periodo = this.getPeriodoSeleccionado().getPeriodo();
            for (DistributivoCabecera cab : periodo.getDistributivoCabeceras()) {
                //primero preguntar por las horas de clases
                Integer minClases = cab.getDedicacion().getHorasMin();
                Integer numeroClases = 0;
                for (DistributivoDetalle det : cab.getDistributivoDetalles()) {
                    List<MallaDetalle> malla = mallaFacade.buscarCodigo(det.getIdMallaDetalle());
                    if(malla != null)
                        for(MallaDetalle de : malla){
                            numeroClases = numeroClases + de.getNumHoras();
                        }
                }
                if(numeroClases < minClases){
                    Mensaje.ERROR("Existen docentes que no cumplen el MINIMO de horas clases!");
                    return;
                }
                //despues preguntar por las horas adicionales
                Integer minAdicional = cab.getDedicacion().getMinActividades();
                Integer numeroAdicional = 0;
                for (Adicionale det : cab.getAdicionales()) {
                    numeroAdicional = numeroAdicional + det.getHoras();
                }
                if(numeroAdicional < minAdicional){
                    Mensaje.ERROR("Existen docentes que no cumplen el MINIMO de horas de actividades adicionales!");
                    return;
                }
            }

            //permite autorizar
            Periodo periodoAutorizar = this.getPeriodoSeleccionado().getPeriodo();
            periodoAutorizar.setEstadoDistributivo("PUBLICADO");
            periodoFacade.modificar(periodoAutorizar);
            Mensaje.SUCESO("Se ha PUBLICADO el periodo!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void finalizar() {
        try {
            if (this.getPeriodoSeleccionado() == null) {
                Mensaje.ERROR("Debe seleccionar un PERIODO!!");
                return;
            }
            System.out.println("finalizar: " + this.getPeriodoSeleccionado().getNombre());
            if (this.getPeriodoSeleccionado().getAutorizacion().equals("FINALIZADO")) {
                Mensaje.ERROR("El periodo ya habia sido FINALIZADO!!");
                return;
            }
            if (!this.getPeriodoSeleccionado().getAutorizacion().equals("PUBLICADO")) {
                Mensaje.ERROR("Solo puede FINALIZAR un registro PUBLICADO!!");
                return;
            }
            //permite autorizar
            Periodo periodoAutorizar = this.getPeriodoSeleccionado().getPeriodo();
            periodoAutorizar.setEstadoDistributivo("FINALIZADO");
            periodoFacade.modificar(periodoAutorizar);
            Mensaje.SUCESO("Se ha FINALIZADO el periodo!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void anular() {
        try {
            System.out.println("anular: " + this.getPeriodoSeleccionado().getNombre());
            if (this.getPeriodoSeleccionado() == null) {
                Mensaje.ERROR("Debe seleccionar un PERIODO!!");
                return;
            }
            if (this.getPeriodoSeleccionado().getAutorizacion().equals("ANULADO")) {
                Mensaje.ERROR("El periodo ya habia sido ANULADO!!");
                return;
            }
            if (this.getPeriodoSeleccionado().getAutorizacion().equals("FINALIZADO")) {
                Mensaje.ERROR("No se puede ANULAR un periodo FINALIZADO!!");
                return;
            }
            if (this.getPeriodoSeleccionado().getAutorizacion().equals("PUBLICADO")) {
                Mensaje.ERROR("No se puede ANULAR un periodo PUBLICADO!!");
                return;
            }
            RequestContext req = RequestContext.getCurrentInstance();
            req.execute("PF('wdialogoAnular').show();");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void confirmarAnulacion() {
        Periodo periodoAnular = this.getPeriodoSeleccionado().getPeriodo();
        periodoAnular.setEstadoDistributivo("ANULADO");
        periodoAnular.setObservacionesElimina(this.getObservacionAnular());
        periodoFacade.modificar(periodoAnular);
        Mensaje.SUCESO("Se ha ANULADO el periodo!");
    }

    public void descargar() throws JRException, FileNotFoundException {
        if (this.getPeriodoSeleccionado() == null) {
            Mensaje.ERROR("Debe seleccionar un PERIODO!!");
            return;
        }
        System.out.println("descargar: " + this.getPeriodoSeleccionado().getNombre());
        if (this.getPeriodoSeleccionado() != null) {
            String nombrePdf = "";
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ID_PERIODO", this.getPeriodoSeleccionado().getIdPeriodo());
            map.put("INSTITUCION", "COLEGIO \"HACIA LA CUMBRE\"");

            map.put("PERIODO", this.getPeriodoSeleccionado().getNombre());
            nombrePdf = "distributivo.pdf";

            JasperReportUtil reporte = new JasperReportUtil();
            String rutaPdf = reporte.crearReporte(map, "/cargahoraria/rptDistributivo.jasper", "/" + nombrePdf, this.getHost(), this.getPuerto(), this.getBase(), this.getUsuario(), this.getClave());
            this.descarga = new DefaultStreamedContent(new FileInputStream(new File(rutaPdf)), "", nombrePdf);
        }
    }

    public void eliminar(PeriodoDistributivo periodoDistributivo) {
        if (this.getPeriodoSeleccionado() == null) {
            Mensaje.ERROR("Debe seleccionar un PERIODO!!");
            return;
        }
        System.out.println("eliminar: " + periodoDistributivo.getNombre());
        //se procede a eliminar el registro
        Periodo periodoEliminar = periodoDistributivo.getPeriodo();
        periodoEliminar.setEstado("INACTIVO");
        periodoFacade.modificar(periodoEliminar);
        Mensaje.SUCESO("Se ha eliminado el registro!");
    }

    public void grabarClonacion() {
        try {
            System.out.println("Entra grabar");
            System.out.println("periodo seleccionado combo: " + this.getIdPeriodoComboSeleccionado());
            Periodo periodoDestino = periodoFacade.buscarPorCodigo(this.getIdPeriodoComboSeleccionado());
            if (periodoDestino != null) {
                //aqui es donde se clona los datos dependiendo del periodo seleccionado
                Periodo periodoOrigen = this.getPeriodoSeleccionado().getPeriodo();
                List<DistributivoCabecera> listaCabecera = new ArrayList<>();
                for (DistributivoCabecera cab : periodoOrigen.getDistributivoCabeceras()) {
                    if (cab.getEstado().equals("ACTIVO")) {
                        DistributivoCabecera disAdd = new DistributivoCabecera();
                        disAdd.setIdDistributivo(null);
                        disAdd.setDedicacion(cab.getDedicacion());
                        disAdd.setDocente(cab.getDocente());
                        disAdd.setEstado(cab.getEstado());
                        disAdd.setFechaIngreso(new Date());
                        disAdd.setObservacion("");
                        disAdd.setPeriodo(periodoDestino);
                        disAdd.setUsuario(cab.getUsuario());

                        List<DistributivoDetalle> listaDetalle = new ArrayList<>();
                        //ahora siguen los detalles
                        for (DistributivoDetalle det : cab.getDistributivoDetalles()) {
                            if (det.getEstado().equals("A")) {
                                DistributivoDetalle detAdd = new DistributivoDetalle();
                                detAdd.setIdDetalle(null);
                                detAdd.setDistributivoCabecera(disAdd);
                                detAdd.setMateria(det.getMateria());
                                detAdd.setObservacion(det.getObservacion());
                                detAdd.setCurso(det.getCurso());
                                detAdd.setParalelo(det.getParalelo());
                                detAdd.setIdMallaDetalle(det.getIdMallaDetalle());
                                detAdd.setEstado(det.getEstado());
                                listaDetalle.add(detAdd);
                            }
                        }
                        disAdd.setDistributivoDetalles(listaDetalle);
                        listaCabecera.add(disAdd);
                    }
                }
                periodoDestino.setDistributivoCabeceras(listaCabecera);

                periodoFacade.modificar(periodoDestino);
                Mensaje.SUCESO("Se ha realizado la COPIA del distributivo!");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String getObservacionAnular() {
        return observacionAnular;
    }

    public void setObservacionAnular(String observacionAnular) {
        this.observacionAnular = observacionAnular;
    }

    public void setPeriodoLista(List<PeriodoDistributivo> periodoLista) {
        this.periodoLista = periodoLista;
    }

    public PeriodoDistributivo getPeriodoSeleccionado() {
        return periodoSeleccionado;
    }

    public void setPeriodoSeleccionado(PeriodoDistributivo periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
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

    public StreamedContent getDescarga() {
        return descarga;
    }

    public void setDescarga(StreamedContent descarga) {
        this.descarga = descarga;
    }

    public List<Periodo> getPeriodoCombo() {
        return periodoCombo;
    }

    public void setPeriodoCombo(List<Periodo> periodoCombo) {
        this.periodoCombo = periodoCombo;
    }

    public Integer getIdPeriodoComboSeleccionado() {
        return idPeriodoComboSeleccionado;
    }

    public void setIdPeriodoComboSeleccionado(Integer idPeriodoComboSeleccionado) {
        this.idPeriodoComboSeleccionado = idPeriodoComboSeleccionado;
    }

    public class PeriodoDistributivo {

        private Integer idPeriodo;
        private Periodo periodo;
        private String nombre;
        private String fechaInicio;
        private String fechaFin;
        private String autorizacion;
        private String estado;

        public Integer getIdPeriodo() {
            return idPeriodo;
        }

        public void setIdPeriodo(Integer idPeriodo) {
            this.idPeriodo = idPeriodo;
        }

        public Periodo getPeriodo() {
            return periodo;
        }

        public void setPeriodo(Periodo periodo) {
            this.periodo = periodo;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getFechaInicio() {
            return fechaInicio;
        }

        public void setFechaInicio(String fechaInicio) {
            this.fechaInicio = fechaInicio;
        }

        public String getFechaFin() {
            return fechaFin;
        }

        public void setFechaFin(String fechaFin) {
            this.fechaFin = fechaFin;
        }

        public String getAutorizacion() {
            return autorizacion;
        }

        public void setAutorizacion(String autorizacion) {
            this.autorizacion = autorizacion;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

    }
}
