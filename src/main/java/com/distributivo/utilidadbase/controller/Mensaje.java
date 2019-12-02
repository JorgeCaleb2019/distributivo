package com.distributivo.utilidadbase.controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

public class Mensaje {

    public static String TITULO = "";

    public static void ERROR(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, TITULO, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void ERROR(String titulo, String detalle) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, detalle);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void SUCESO(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, TITULO, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void SUCESO(String titulo, String detalle) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, titulo, detalle);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void ADVERTENCIA(String titulo, String detalle) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, titulo, detalle);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void FATAL(String titulo, String detalle) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_FATAL, titulo, detalle);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void ADVERTENCIA(String detalle) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, TITULO, detalle);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void FATAL(String detalle) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_FATAL, TITULO, detalle);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void SUCESO_DIALOG(String titulo, String detalle) {
        FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, titulo, detalle);
        RequestContext.getCurrentInstance().showMessageInDialog(mensaje);
    }

    public static void ERROR_DIALOG(String titulo, String detalle) {
        FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, detalle);
        RequestContext.getCurrentInstance().showMessageInDialog(mensaje);
    }

    public static void ADVERTENCIA_DIALOG(String titulo, String detalle) {
        FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, titulo, detalle);
        RequestContext.getCurrentInstance().showMessageInDialog(mensaje);
    }

    public static void FATAL_DIALOG(String titulo, String detalle) {
        FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_FATAL, titulo, detalle);
        RequestContext.getCurrentInstance().showMessageInDialog(mensaje);
    }
    
}
