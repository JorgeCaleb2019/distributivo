/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.controller.security;

import com.distributivo.ejb.security.GrupoFacade;
import com.distributivo.model.security.Grupo;
import com.distributivo.model.security.Permiso;
import com.distributivo.utilidadbase.controller.Mensaje;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Toshiba
 */
@Named(value = "grupoPermisoBean")
@ViewScoped
public class GrupoPermisoBean implements Serializable {

    @EJB
    private GrupoFacade grupoFacade;
    private Grupo grupo;
    private List<Permiso> permisosSeleccionados;
    private List<Permiso> permisosList;
    private String currentUser;
    public GrupoPermisoBean() {

    }

    public void guardar() {
        try {
              Authentication auth = SecurityContextHolder.getContext().getAuthentication();
             this.setCurrentUser(auth.getName());
            System.out.println("se han modificado :"+this.getPermisosSeleccionados().size()+" permisos");
            grupo.setPermisoList(this.getPermisosSeleccionados());
            grupo.setFechaModificacion(new Date());
            grupo.setUsuarioModificacion(currentUser);
            this.grupoFacade.modificar(grupo);
            Mensaje.SUCESO("Se hn guardado los permisos en el Grupo");
        } catch (Exception ex) {
            Mensaje.ERROR("Ha sucedido un error:" + ex.getMessage());
        }
    }

    /**
     * @return the grupo
     */
    public Grupo getGrupo() {
        return grupo;
    }

    /**
     * @param grupo the grupo to set
     */
    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public void onRowSelectGrupo(SelectEvent event) {
        this.permisosSeleccionados = ((Grupo) event.getObject()).getPermisoList();
    }

    public void onRowSelectPermiso(SelectEvent event) {
        this.getPermisosSeleccionados().add((Permiso) event.getObject());
    }

    public void onRowUnselectPermiso(UnselectEvent event) {
        this.getPermisosSeleccionados().remove((Permiso) event.getObject());
    }

    public List<Permiso> getPermisosSeleccionados() {
        if (permisosSeleccionados == null) {
            permisosSeleccionados = new LinkedList<>();
        }
        return permisosSeleccionados;
    }

    public List<Permiso> getPermisosList() {
        permisosList = this.getPermisosSeleccionados();
        return permisosList;
    }

    public void setPermisosList(List<Permiso> permisosList) {
        this.permisosList = permisosList;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
    
}
