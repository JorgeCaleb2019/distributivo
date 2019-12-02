/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.converter.security;

import com.distributivo.controller.security.PermisoBean;
import com.distributivo.model.security.Permiso;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Convertidor usado para Combos y autocompletados
 * @author Fernando
 */
@FacesConverter(value = "permisoConverter")
public class PermisoConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
       if(string==null || string.length()==0 || string.toUpperCase().contains("SELECCIONE")){
           return null;
       }
        PermisoBean bean=(PermisoBean)fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "permisoBean");
        try {
            return bean.getPermisoFacade().buscarPorCodigo(Integer.parseInt(string));
        } catch (Exception ex) {
            Logger.getLogger(PermisoConverter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
       if(o==null){
        return null;   
       }
       if(o instanceof String){
           return null;
       }
       if(o instanceof Permiso){
           Permiso p=(Permiso)o;
           if(p.getCodigoPerm()==null){
               return null;
           }else{
               return p.getCodigoPerm().toString();
           }
       }else{
           throw new IllegalArgumentException("El objeto debe ser una permiso"+ o);
       }
    }
    
}
