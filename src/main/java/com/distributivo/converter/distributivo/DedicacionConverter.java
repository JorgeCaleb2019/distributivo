
package com.distributivo.converter.distributivo;

import com.distributivo.controller.distributivo.DedicacionController;
import com.distributivo.model.business.Dedicacion;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "dedicacionConverter")
public class DedicacionConverter implements Converter {

/*    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        DedicacionController controller = (DedicacionController) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "dedicacionController");
        if (value == null) {
            return null;
        }
        return controller.getFacade().buscarPorCodigo(Long.parseLong(value));
    }

    String getStringKey(java.lang.Long value) {
        StringBuilder sb = new StringBuilder();
        sb.append(value);
        return sb.toString();
    }
    
    java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Dedicacion) {
            Dedicacion o = (Dedicacion) object;
            return getStringKey(o.getId());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Dedicacion.class.getName()});
            return null;
        }
    }
*/
    
    
         @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        DedicacionController controller = (DedicacionController) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "dedicacionController");
        if (value == null) {
            return null;
        }
        return controller.getFacade().buscarPorCodigo(Long.parseLong(value));
    }

    String getStringKey(java.lang.Long value) {
        StringBuilder sb = new StringBuilder();
        sb.append(value);
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Dedicacion) {
            Dedicacion o = (Dedicacion) object;
            return getStringKey(Long.valueOf(o.getId()));
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Dedicacion.class.getName()});
            return null;
        }
    }
}
