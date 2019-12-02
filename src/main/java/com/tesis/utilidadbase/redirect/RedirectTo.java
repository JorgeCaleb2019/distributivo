/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tesis.utilidadbase.redirect;

import java.io.IOException;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Diseñador
 */
public class RedirectTo {

    /**
     *
     * @param page
     * @param tituloMensaje
     * @param mensaje
     * @param severe info,warn,fatal,error
     */
    public static void Page(String page, String tituloMensaje, String mensaje, String severe) {
        if (mensaje == null) {
            RequestContext.getCurrentInstance().execute("LoadHtml('#page_content_inner', '" + page + ".xhtml')");
        } else {
            if (severe == null) {
                severe = "info";
            }
            RequestContext.getCurrentInstance().execute("LoadHtml('#page_content_inner', '" + page + ".xhtml');PF('growlMensaje').renderMessage({\"summary\":\"" + tituloMensaje + "\",\n"
                    + "                             \"detail\":\"" + mensaje + "\",\n"
                    + "                             \"severity\":\"" + severe + "\"})");
        }
    }

    public static void Page(String page) {
        RequestContext.getCurrentInstance().execute("LoadHtml('#page_content_inner', '" + page + ".xhtml')");
    }
    
    public static void PageSimple(String page) throws IOException {
        String ruta=FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        FacesContext.getCurrentInstance().getExternalContext().redirect(ruta+"/"+page);
    }

    public static void Message(String tituloMensaje, String mensaje, String severe) {
        if (severe == null) {
            severe = "info";
        }
        RequestContext.getCurrentInstance().execute("PF('growlMensaje').renderMessage({\"summary\":\"" + tituloMensaje + "\",\n"
                + "                             \"detail\":\"" + mensaje + "\",\n"
                + "                             \"severity\":\"" + severe + "\"})");
    }
}
