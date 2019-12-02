/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.controller.security;

import com.distributivo.ejb.security.RolFacade;
import com.distributivo.model.security.Grupo;
import com.distributivo.model.security.Rol;
import com.distributivo.utilidadbase.controller.Mensaje;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import lombok.Setter;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Toshiba
 */
@Named(value = "rolGrupoBean")
@ViewScoped
public class RolGrupoBean implements Serializable {

    @EJB
    private RolFacade rolFacade;
    @Setter
    private Rol rol;
    
    private List<Grupo> grupSeleccionados;
    private List<Grupo> gruposList;
    private String currentUser;
    /**
     * Creates a new instance of RolGrupoBean
     */
    public RolGrupoBean() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.setCurrentUser(auth.getName());
    }

    public void guardar() {
        try {
            rol.setUsuarioModificacion(currentUser);
            rol.setFechaModificacion(new Date());
            this.rolFacade.modificar(rol);
            Mensaje.SUCESO("Se han guardado los grupos en el Rol");
        } catch (Exception ex) {
            Mensaje.ERROR("Ha sucedido un error:" + ex.getMessage());
        }
    }

    public Rol getRol() {
        if(rol==null){
            rol=new Rol();
        }
        return rol;
    }

    public void onRowSelectGrupo(SelectEvent event) {
        this.grupSeleccionados = ((Rol) event.getObject()).getGrupoList();
    }

    public void onRowSelectPermiso(SelectEvent event) {
        this.getGrupSeleccionados().add((Grupo) event.getObject());
    }

    public void onRowUnselectPermiso(UnselectEvent event) {
        this.getGrupSeleccionados().remove((Grupo) event.getObject());
    }
    
    public List<Grupo> getGrupSeleccionados() {
        if (grupSeleccionados == null) {
            grupSeleccionados = new LinkedList<>();
        }
        return grupSeleccionados;
    }

    public void setGrupSeleccionados(List<Grupo> grupSeleccionados) {
        this.grupSeleccionados = grupSeleccionados;
    }

    public List<Grupo> getGruposList() {
        gruposList = this.getGrupSeleccionados();
        return gruposList;
    }

    public void setGruposList(List<Grupo> gruposList) {
        this.gruposList = gruposList;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
    
}
