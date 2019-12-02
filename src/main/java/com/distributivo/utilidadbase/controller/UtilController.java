/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.utilidadbase.controller;

import com.tesis.utilidadbase.facade.AbstractFacade;
import com.tesis.utilidadbase.facade.StateEntity;
import com.tesis.utilidadbase.redirect.RedirectTo;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.event.SelectEvent;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 * Clase abstracta que contiene información similar para varios controladores y
 * nos permite manejar los metodos basicos de guardado, eliminado y modificado.
 *
 * @author Fernando
 * @param <T>
 */
public abstract class UtilController<T> implements Serializable {

    /**
     * @return the mensajeActual
     */
    public String getMensajeActual() {
        return mensajeActual;
    }

    /**
     * @param mensajeActual the mensajeActual to set
     */
    public void setMensajeActual(String mensajeActual) {
        this.mensajeActual = mensajeActual;
    }

    protected String paramId;
    private UploadedFile fileUpload;
    private String rutaReporteServidor;
    protected String rutaPagina;
    protected Class primaryKeyEntity;
    /**
     *
     */
    protected Persistencia persistence;
    /**
     * Indica si los datos de la entidad son solo lectura. Por defecto es false
     */
    protected boolean readOnly = false;
    /**
     * Entidad es la entidad o el objeto al cual se aplicara el CRUD
     */
    protected T entidad;
    /**
     * Es el nombre que se le dara al acrhivo de Exel o PDF el cual sera
     * descargado por el cliente web
     */
    private String nombreArchivo;
    /**
     * Aqui el LazyDataModel que se usara para los listados de tipo Lazy
     */
    protected LazyDataModelAdvance<T> listadoEntidad;
    /**
     * Aqui colocamos el AbstractFacade el cual necesitaremos para realizar el
     * CRUD y las consultas
     */
    private AbstractFacade<T> entidadLogica;
    /**
     * StreamContent nos permite relizar descargas hacia el cliente web
     */
    protected StreamedContent archivoDescarga;
    /**
     * Class<T> nos permite tener la clase generica para poder realizar varias
     * acciones y poder usar reflexion
     */
    private Class<T> claseEntidad;
    /**
     *
     */
    protected JasperPrint jasper;
    /**
     *
     * @param claseEntidad
     */
    private String currentloggeduser;
    //MENSAJES PERSONALIZADOS
    protected String MENSAJE_GUARDAR = "";
    protected String MENSAJE_MODIFICAR = "";
    protected String MENSAJE_ANULAR = "";
    protected String MENSAJE_HABILITAR = "";
    //*******************************************
    private String mensajeActual;

    public UtilController(Class<T> claseEntidad) {
        //get logged in username
        this.claseEntidad = claseEntidad;
        this.listadoEntidad = new LazyDataModelAdvance<T>();
    }

    public UtilController() {

    }

//    public void postLoad() {
//        Map params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
//        if (params.containsKey("id")) {
//            setParamId(params.get("id").toString());
//            if (this.primaryKeyEntity.equals(Long.class)) {
//                this.entidad = entidadLogica.buscarPorCodigo(Long.parseLong(getParamId()));
//            } else {
//                if (this.primaryKeyEntity.equals(Integer.class)) {
//                    this.entidad = entidadLogica.buscarPorCodigo(Integer.parseInt(getParamId()));
//                }
//            }
//        }
//    }
    protected String getRandomName() {
        int i = (int) (Math.random() * 10000000);

        return String.valueOf(i);
    }

    /**
     * Metodo para inicializar la entidad
     *
     * @return
     */
    public String prepararCrear() {
        try {
            this.entidad = this.claseEntidad.newInstance();
            this.nuevoClick();
            this.persistence = Persistencia.GUARDAR;
            this.mensajeActual = "";
        } catch (InstantiationException ex) {
            Logger.getLogger(UtilController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(UtilController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "add.html";
    }

    public void inicializarCrear() {
        try {
            this.entidad = this.claseEntidad.newInstance();
            this.nuevoClick();
            this.persistence = Persistencia.GUARDAR;
            this.mensajeActual = "";
        } catch (InstantiationException ex) {
            Logger.getLogger(UtilController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(UtilController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clean() {
        this.listadoEntidad = null;
        this.entidad = null;

    }

    /**
     * Preparar para editar una entidad
     *
     * @param evt
     */
    public String prepararEditar(ActionEvent evt) {
        this.persistence = Persistencia.MODIFICAR;
        this.mensajeActual = "";
        return "edit.html";
    }

    public String prepararAnular(ActionEvent evt) {
        this.persistence = Persistencia.ANULAR;
        this.mensajeActual = "";
        return "delete.html";
    }

    public void inicializarMensaje() {
        this.mensajeActual = "";
    }

    protected abstract void prepararMensajes();

    /**
     * Método para incializar reporte
     *
     * @param parametros
     * @param listadoObjetos
     * @param ruta
     * @throws JRException
     */
    protected void inicializarReporte(Map parametros, List<T> listadoObjetos, String ruta) throws JRException {
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listadoObjetos);
        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(ruta);
        jasper = JasperFillManager.fillReport(reportPath, parametros, beanCollectionDataSource);
    }

    /**
     * Método para cuando se selecciona para editar una entidad
     *
     * @param evt
     */
    public void seleccionarEditar(SelectEvent evt) {
        this.entidad = (T) evt.getObject();
        this.dobleClick();
    }

    /**
     * Método para guardar la entidad
     *
     * @param evt
     */
    public void guardarEntidad(ActionEvent evt) {
        try {
            Method m = this.entidad.getClass().getMethod("setEstado", new Class[]{String.class});
            m.invoke(this.entidad, new Object[]{StateEntity.ACTIVO.name()});
            entidadLogica.guardar(this.entidad);
            prepararMensajes();
            Mensaje.SUCESO(MENSAJE_GUARDAR);
            this.entidad = claseEntidad.newInstance();
            this.nuevoClick();
        } catch (Exception ex) {
            Mensaje.ERROR("Guardar", "Error al Tratar de Guardar en " + this.claseEntidad.getSimpleName());
        }
    }

    /**
     * Método para modificar une entidad
     *
     * @param evt
     */
    public void modificarEntidad(ActionEvent evt) {
        try {
            entidadLogica.modificar(this.entidad);
            prepararMensajes();
            Mensaje.SUCESO(MENSAJE_MODIFICAR);
            this.mensajeActual = this.MENSAJE_MODIFICAR;
            RedirectTo.PageSimple(this.rutaPagina + "/index.html");
        } catch (Exception ex) {
            Mensaje.ERROR("Edicion", "Error al Tratar de Modificar en " + this.claseEntidad.getSimpleName());
        }
    }

    /**
     * Método para eliminar una entidad
     *
     * @param evt
     */
    public void anularEntidad(ActionEvent evt) {
        try {
            
            Method m = this.entidad.getClass().getMethod("setEstado", new Class[]{String.class});
            m.invoke(this.entidad, new Object[]{StateEntity.ANULADO.name()});
            entidadLogica.modificar(this.entidad);
            prepararMensajes();
            Mensaje.SUCESO(MENSAJE_ANULAR);
            this.entidad = claseEntidad.newInstance();
            this.nuevoClick();
            this.mensajeActual = this.MENSAJE_ANULAR;
            RedirectTo.PageSimple(this.rutaPagina + "/index.html");
        } catch (Exception ex) {
            Mensaje.ERROR("Edicion", "Error al Tratar de Modificar en " + this.claseEntidad.getSimpleName());
        }
    }

    public void habilitarEntidad(ActionEvent evt) {
        try {
           
            Method m = this.entidad.getClass().getMethod("setEstado", new Class[]{String.class});
            m.invoke(this.entidad, new Object[]{StateEntity.ACTIVO.name()});
            entidadLogica.modificar(this.entidad);
             prepararMensajes();
            Mensaje.SUCESO(MENSAJE_HABILITAR);
            this.entidad = claseEntidad.newInstance();
            this.nuevoClick();
            this.mensajeActual = this.MENSAJE_HABILITAR;
            RedirectTo.PageSimple(this.rutaPagina + "/index.html");
        } catch (Exception ex) {
            Mensaje.ERROR("Edicion", "Error al Tratar de Modificar en " + this.claseEntidad.getSimpleName());
        }
    }

    public T getEntidad() {
        if (entidad == null) {
            try {
                this.entidad = this.claseEntidad.newInstance();
            } catch (InstantiationException ex) {
                Logger.getLogger(UtilController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(UtilController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return entidad;
    }

    /**
     * Método cuando se da clik en el boton nuevo
     */
    private void nuevoClick() {
        this.readOnly = false;
    }

    /**
     * Método para cuando se da doble clikc en un registro
     */
    private void dobleClick() {
        this.readOnly = true;
    }

    public void setEntidad(T entidad) {
        this.entidad = entidad;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public LazyDataModelAdvance<T> getListadoEntidad() {
//        if (listadoEntidad == null) {
//            listadoEntidad = new LazyDataModelAdvance<>();
//            listadoEntidad.setClasePK(primaryKeyEntity);
//            listadoEntidad.setFacade(entidadLogica);
//        }
        return listadoEntidad;
    }

    public void setListadoEntidad(LazyDataModelAdvance<T> listadoEntidad) {
        this.listadoEntidad = listadoEntidad;
    }

    public AbstractFacade<T> getEntidadLogica() {
        return entidadLogica;
    }

    public void setEntidadLogica(AbstractFacade<T> entidadLogica) {
        this.entidadLogica = entidadLogica;
    }

    public StreamedContent getArchivoDescarga() {
        return archivoDescarga;
    }

    public void setArchivoDescarga(StreamedContent archivoDescarga) {
        this.archivoDescarga = archivoDescarga;
    }

    public Class<T> getClaseEntidad() {
        return claseEntidad;
    }

    public void setClaseEntidad(Class<T> claseEntidad) {
        this.claseEntidad = claseEntidad;
    }

    /**
     * @return the readOnly
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * @param readOnly the readOnly to set
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * @return the jasper
     */
    public JasperPrint getJasper() {
        return jasper;
    }

    /**
     * @param jasper the jasper to set
     */
    public void setJasper(JasperPrint jasper) {
        this.jasper = jasper;
    }

    /**
     * Método para exportar reporte a PDF
     *
     * @param actionEvent
     * @throws JRException
     * @throws IOException
     */
    public void reportePDF(ActionEvent actionEvent) throws JRException, IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.pdf");
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasper, servletOutputStream);
        FacesContext.getCurrentInstance().responseComplete();
    }

    /**
     * Método para exportar reporte a WORD
     *
     * @param actionEvent
     * @throws JRException
     * @throws IOException
     */
    public void reporteWORD(ActionEvent actionEvent) throws JRException, IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.docx");
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        JRDocxExporter docxExporter = new JRDocxExporter();
        docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasper);
        docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
        docxExporter.exportReport();
        FacesContext.getCurrentInstance().responseComplete();
    }

    /**
     * Método para exportar reporte a EXCEL
     *
     * @param actionEvent
     * @throws JRException
     * @throws IOException
     */
    public void reporteEXCEL(ActionEvent actionEvent) throws JRException, IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.xlsx");
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        JRXlsxExporter docxExporter = new JRXlsxExporter();
        docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasper);
        docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
        docxExporter.exportReport();
        FacesContext.getCurrentInstance().responseComplete();
    }

    /**
     * Método para exportar reporte a ODT
     *
     * @param actionEvent
     * @throws JRException
     * @throws IOException
     */
    public void reporteODT(ActionEvent actionEvent) throws JRException, IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.odt");
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        JROdtExporter docxExporter = new JROdtExporter();
        docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasper);
        docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
        docxExporter.exportReport();
        FacesContext.getCurrentInstance().responseComplete();
    }

    /**
     * Método para exportar reporte a POWEr POINT
     *
     * @param actionEvent
     * @throws JRException
     * @throws IOException
     */
    public void reportePOWER_POINT(ActionEvent actionEvent) throws JRException, IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.pptx");
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        JRPptxExporter docxExporter = new JRPptxExporter();
        docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasper);
        docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
        docxExporter.exportReport();
        FacesContext.getCurrentInstance().responseComplete();
    }

    public String getCurrentloggeduser() {
        return currentloggeduser;
    }

    public void setCurrentloggeduser(String currentloggeduser) {
        this.currentloggeduser = currentloggeduser;
    }

    /**
     * @return the rutaReporteServidor
     */
    public String getRutaReporteServidor() {
        return rutaReporteServidor;
    }

    /**
     * @param rutaServidor the rutaReporteServidor to set
     */
    public void setRutaReporteServidor(String rutaServidor) {
        this.rutaReporteServidor = rutaServidor;
    }

    /**
     * @return the paramId
     */
    public String getParamId() {
        return paramId;
    }

    /**
     * @param paramId the paramId to set
     */
    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    /**
     * @return the fileUpload
     */
    public UploadedFile getFileUpload() {
        return fileUpload;
    }

    /**
     * @param fileUpload the fileUpload to set
     */
    public void setFileUpload(UploadedFile fileUpload) {
        this.fileUpload = fileUpload;
    }

}
