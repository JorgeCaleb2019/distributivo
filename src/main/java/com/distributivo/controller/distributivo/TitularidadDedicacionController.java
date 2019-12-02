/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.controller.distributivo;

import com.distributivo.ejb.business.TitularidadFacade;
import com.distributivo.model.business.Titularidad;
import com.distributivo.utilidadbase.controller.Mensaje;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Desarrollo
 */

@Named(value = "titularidadDedicacionController")
@ViewScoped
public class TitularidadDedicacionController implements Serializable{
    
    
    @EJB
    private TitularidadFacade titularidadFacade;

    private Titularidad titularidad;
    private Long idTitularidad;
    private String currentUser;//para recuperar el usuario loggeado
    
    
     public TitularidadDedicacionController() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.setCurrentUser(auth.getName());
    }

    public void guardar() {
        try {
            System.out.println("hola munso");
            //titularidad.setFechaModificacion(new Date());
            this.titularidadFacade.modificar(titularidad);
            System.out.println("Curso mosificar: " + titularidad.toString());
            Mensaje.SUCESO("Se hn guardado las asignaciones de dedicaciones");
        } catch (Exception ex) {
            Mensaje.ERROR("Ha sucedido un error:" + ex.getMessage());
        }
    }

    
    
    /*public void PruebaRegistroAsistencia() throws Exception {
    for(String it : checkPersona) {
        for (Asistencia asis : selectedAsistencia) {
         .... //las condiciones que consideres necesarias
        }

        for(int i = 0; i < lstAsistencia.size(); i++) {
            if(it == lstAsistencia.get(i).getC_c_usuario()) {
                System.out.print(it.indexOf(i));
            }
        }
    }
}*/
    public Titularidad getTitularidad() {
        return titularidad;
    }

    public void setTitularidad(Titularidad titularidad) {
        this.titularidad = titularidad;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
    
    
    
    
    
}
