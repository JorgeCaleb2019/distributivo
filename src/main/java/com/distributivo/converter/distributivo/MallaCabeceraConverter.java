
package com.distributivo.converter.distributivo;

import com.distributivo.model.business.MallaCabecera;
import com.distributivo.model.business.Titularidad;
import com.distributivo.controller.distributivo.MallaCabeceraController;
import com.distributivo.converter.security.TipoPermisoConverter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "mallaCabeceraConverter")
public class MallaCabeceraConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
       if(string==null || string.length()==0 || string.toUpperCase().contains("SELECCIONE")){
           return null;
       }
        MallaCabeceraController bean=(MallaCabeceraController)fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "mallaCabeceraConverter");
        try {
            return bean.getFacade().buscarPorCodigo(Integer.parseInt(string));
        } catch (Exception ex) {
            Logger.getLogger(TipoPermisoConverter.class.getName()).log(Level.SEVERE, null, ex);
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
       if(o instanceof MallaCabecera){
           MallaCabecera p=(MallaCabecera)o;
           if(p.getIdMalla()==null){
               return null;
           }else{
               return p.getIdMalla().toString();
           }
       }else{
           throw new IllegalArgumentException("El objeto debe ser una malla"+ o);
       }
    }
    
}
