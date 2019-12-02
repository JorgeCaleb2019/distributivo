/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.controller.security;

import com.captcha.botdetect.web.jsf.JsfCaptcha;
import com.distributivo.ejb.security.UsuarioFacade;
import com.distributivo.model.security.Usuario;
import com.distributivo.utilidadbase.controller.Mensaje;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Toshiba
 */
@Named(value = "loginBean")
//@ViewScoped
@RequestScoped
public class LoginBean implements Serializable {

    @EJB(mappedName = "java:global/Distributivo/UsuarioFacade")
    private UsuarioFacade usuarioFacade;
    private String userName;
    private String password;
    private String texto;
    private AuthenticationManager authenticationManager;
    
    
    private String captchaCode;
    private JsfCaptcha captcha;


    public LoginBean() {

    }
    /**
     * Método para logearse el sistema.
     */
    public void login() {
        
    System.out.println("valida");
    boolean isHuman = captcha.validate(captchaCode);

    if (isHuman) {
      // TODO: Captcha validation passed, perform protected action
           System.out.println("verdadero"); 
           FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();

        try {
            Authentication result = null;
            Authentication request = new UsernamePasswordAuthenticationToken(
                    this.userName.trim(), this.password.trim());
            result = getAuthenticationManager().authenticate(request);
            SecurityContextHolder.getContext().setAuthentication(result);
            Usuario usuario = this.usuarioFacade.getUserByName(this.userName);
            String url = extContext.encodeActionURL(
                    context.getApplication().getViewHandler().getActionURL(context, "/admin.html"));
            extContext.redirect(url);
        } catch (AuthenticationException e) {
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "", e.getMessage()));
        } catch (IOException ex) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    } else {
      // TODO: Captcha validation failed, show error message
     System.out.println("falso");
      Mensaje.ERROR("Codigo incorrecto");
    
    }

    this.captchaCode = "";

        
    }
    
    
    
    
     /*respaldo
    public void login() {
        System.out.println("Entra loggeo");
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();

        try {
            System.out.println("User: " + this.getUserName() + " pass: " + this.getPassword());
            Authentication result = null;
            Authentication request = new UsernamePasswordAuthenticationToken(
                    this.userName.trim(), this.password.trim());
            result = getAuthenticationManager().authenticate(request);
            SecurityContextHolder.getContext().setAuthentication(result);
            Usuario usuario = this.usuarioFacade.getUserByName(this.userName);
            String url = extContext.encodeActionURL(
                    context.getApplication().getViewHandler().getActionURL(context, "/admin.html"));
            extContext.redirect(url);
        } catch (AuthenticationException e) {
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "", e.getMessage()));
        } catch (IOException ex) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/

   public void logout() {
        SecurityContextHolder.clearContext();
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        String url = extContext.encodeActionURL(
                context.getApplication().getViewHandler().getActionURL(context, "/login.html"));
        try {
            ((HttpSession) extContext.getSession(false)).invalidate();
            extContext.redirect(url);
        } catch (IOException ex) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


   public void validate() {
    // validate the Captcha to check we're not dealing with a bot
      System.out.println("valida");
    boolean isHuman = captcha.validate(captchaCode);

    if (isHuman) {
      // TODO: Captcha validation passed, perform protected action
           System.out.println("verdadero"); 
    
    } else {
      // TODO: Captcha validation failed, show error message
     System.out.println("falso");
      Mensaje.ERROR("Codigo incorrecto");
    
    }

    this.captchaCode = "";
  }
   
   
    public void presionar_texto(String num) {
        if (getTexto().length() <= 4) {
            texto = getTexto() + num;
        }
    }

    public String getTexto() {
        if (texto == null) {
            texto = "";
        }
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void limpiar() {
        texto = "";
    }

    public void recuperar() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public String getCaptchaCode() {
        return captchaCode;
    }

    public void setCaptchaCode(String captchaCode) {
        this.captchaCode = captchaCode;
    }

    public JsfCaptcha getCaptcha() {
        return captcha;
    }

    public void setCaptcha(JsfCaptcha captcha) {
        this.captcha = captcha;
    }

    
    
    
}
