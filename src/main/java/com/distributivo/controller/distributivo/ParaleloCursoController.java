package com.distributivo.controller.distributivo;

import com.distributivo.ejb.business.CursoFacade;
import com.distributivo.model.business.Curso;
import com.distributivo.utilidadbase.controller.Mensaje;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Named(value = "paraleloCursoController")
@ViewScoped
public class ParaleloCursoController implements Serializable{
    
    @EJB
    private CursoFacade cursoFacade;

    private Curso curso;
    private String currentUser;//para recuperar el usuario loggeado
    /**
     * Creates a new instance of ParaleloCursoController
     */
    public ParaleloCursoController() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.setCurrentUser(auth.getName());
    }

    public void guardar() {
        try {
            curso.setFechaModificacion(new Date());
            this.cursoFacade.modificar(curso);
            System.out.println("Curso mosificar: " + curso.toString());
            Mensaje.SUCESO("Se hn guardado las asignaciones de los paralelos");
        } catch (Exception ex) {
            Mensaje.ERROR("Ha sucedido un error:" + ex.getMessage());
        }
    }

    public Curso getCurso() {
        return curso;
    }
    public void setCurso(Curso curso) {
        this.curso = curso;
    }
    public String getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
}
