package com.distributivo.controller.security;

import com.distributivo.ejb.security.UsuarioFacade;
import com.distributivo.model.security.Usuario;
import com.distributivo.utilidadbase.controller.Mensaje;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Named(value = "usuarioRolBean")
@ViewScoped
public class UsuarioRolBean implements Serializable {

    @EJB
    private UsuarioFacade usuarioFacade;

    private Usuario usuario;
    private String currentUser;
    /**
     * Creates a new instance of UsuarioRolBean
     */
    public UsuarioRolBean() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.setCurrentUser(auth.getName());
    }

    public void guardar() {
        try {
            usuario.setUsuarioModificacion(currentUser);
            usuario.setFechaModificacion(new Date());
            this.usuarioFacade.modificar(usuario);
            Mensaje.SUCESO("Se hn guardado los permisos en el grupo");
        } catch (Exception ex) {
            Mensaje.ERROR("Ha sucedido un error:" + ex.getMessage());
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
    
}
