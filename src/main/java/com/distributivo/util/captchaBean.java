/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.util;
import com.captcha.botdetect.web.jsf.JsfCaptcha;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import com.distributivo.controller.security.LoginBean;
import com.distributivo.utilidadbase.controller.Mensaje;
@ManagedBean(name="captchaBean")
@RequestScoped
public class captchaBean {
    private String captchaCode;
  private JsfCaptcha captcha;

  
  
  
  public captchaBean() {
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

  public void validate() {
    // validate the Captcha to check we're not dealing with a bot
      System.out.println("valida");
    boolean isHuman = captcha.validate(captchaCode);

    if (isHuman) {
      // TODO: Captcha validation passed, perform protected action
     // LoginBean login = new LoginBean();
      System.out.println("verdadero"); 
    //  login.login();
    } else {
      // TODO: Captcha validation failed, show error message
     System.out.println("falso");
      Mensaje.ERROR("Codigo incorrecto");
   //  incorrectLabelVisible = true;
    }

    this.captchaCode = "";
  }

    
  
  
  
  
}
