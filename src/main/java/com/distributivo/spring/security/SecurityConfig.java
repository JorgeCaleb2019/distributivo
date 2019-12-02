package com.distributivo.spring.security;

import com.distributivo.controller.security.LoginBean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
public class SecurityConfig extends WebSecurityConfigurerAdapter {
   
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        securityService().configuracionPermisos(http);
        http.sessionManagement().invalidSessionUrl("/error/expired-session.html").and().csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/***", "/error/**");
    }

    @Bean
    AuthenticationProvider customAuthenticationProvider() {
        CustomAuthenticationProvider impl = new CustomAuthenticationProvider();
        return impl;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean(); //To change body of generated methods, choose Tools | Templates.
    }

    @Bean
    @Scope("request")
    public LoginBean loginBean() {
        LoginBean loginController = new LoginBean();
        try {
            loginController.setAuthenticationManager(this.authenticationManagerBean());
        } catch (Exception ex) {
            Logger.getLogger(SecurityConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return loginController;
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SecurityService securityService() {
        SecurityService ss = new SecurityService();
        return ss;
    }
}
