/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tesis.utilidadbase.message;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Diseñador
 */
public class Notification {

    public static void Success(String mensaje) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", mensaje);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public static void Error(String mensaje) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensaje);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public static void Fatal(String mensaje) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "", mensaje);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public static void Warning(String mensaje) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", mensaje);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public static void SuccessDialog(String titulo,String mensaje) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, titulo, mensaje);
        RequestContext.getCurrentInstance().showMessageInDialog(msg);
    }

    public static void ErrorDialog(String titulo,String mensaje) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, mensaje);
        RequestContext.getCurrentInstance().showMessageInDialog(msg);
    }

    public static void FatalDialog(String titulo,String mensaje) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, titulo, mensaje);
        RequestContext.getCurrentInstance().showMessageInDialog(msg);
    }

    public static void WarningDialog(String titulo,String mensaje) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, titulo, mensaje);
        RequestContext.getCurrentInstance().showMessageInDialog(msg);
    }
}
