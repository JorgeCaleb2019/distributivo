
package com.distributivo.converter.distributivo;

import com.distributivo.controller.distributivo.DistributivoController;
import com.distributivo.model.business.DistributivoCabecera;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "distributivoConverter")
public class DistributivoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        DistributivoController controller = (DistributivoController) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "distributivoController");
        if (value == null) {
            return null;
        }
        return controller.getDistributivoFacade().buscarPorCodigo(Long.parseLong(value));
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
        if (object instanceof DistributivoCabecera) {
            DistributivoCabecera o = (DistributivoCabecera) object;
            return getStringKey(Long.valueOf(o.getIdDistributivo()));
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), DistributivoCabecera.class.getName()});
            return null;
        }
    }

}
