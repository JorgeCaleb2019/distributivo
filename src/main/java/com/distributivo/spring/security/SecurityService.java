/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.spring.security;

import com.distributivo.ejb.security.ViewPermisoSistemaFacade;
import java.util.List;
import javax.ejb.EJB;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 *
 * @author Toshiba
 */
public class SecurityService {

    @EJB(mappedName = "java:global/Distributivo/ViewPermisoSistemaFacade")
    private ViewPermisoSistemaFacade viewpermisoSistemaFacade;

    public void configuracionPermisos(HttpSecurity http) throws Exception {
        List<String[]> lista = this.viewpermisoSistemaFacade.getPermisosSistema();
        for (String[] o : lista) {
            String acceso = "hasRole('" + o[0] + "')";
            String paginas = o[1];
            paginas = "/" + paginas.substring(0, paginas.length() - 1);
            http.authorizeRequests().antMatchers(paginas.split("\\,")).access(acceso).and().csrf().disable();
        }
        http.authorizeRequests().antMatchers("/admin.html","/admin_user.html").fullyAuthenticated();
    }
}
