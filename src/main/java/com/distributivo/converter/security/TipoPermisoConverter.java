package com.distributivo.converter.security;

import com.distributivo.controller.security.TipoPermisoBean;
import com.distributivo.model.security.TipoPermiso;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@FacesConverter(value = "tipoPermisoConverter")
public class TipoPermisoConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
       if(string==null || string.length()==0 || string.toUpperCase().contains("SELECCIONE")){
           return null;
       }
        TipoPermisoBean bean=(TipoPermisoBean)fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "tipoPermisoBean");
        try {
            return bean.getTipoPermisoFacade().buscarPorCodigo(Integer.parseInt(string));
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
       if(o instanceof TipoPermiso){
           TipoPermiso p=(TipoPermiso)o;
           if(p.getCodigoTiper()==null){
               return null;
           }else{
               return p.getCodigoTiper().toString();
           }
       }else{
           throw new IllegalArgumentException("El objeto debe ser un Tipo de Permiso"+ o);
       }
    }
    
}
