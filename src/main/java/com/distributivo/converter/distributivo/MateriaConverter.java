/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.converter.distributivo;


import com.distributivo.controller.distributivo.MateriaController;
import com.distributivo.model.business.Materia;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "materiaConverter")
public class MateriaConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        MateriaController controller = (MateriaController) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "materiaController");
        if (value == null) {
            return null;
        }
        return controller.getFacade().buscarPorCodigo(Integer.parseInt(value));
    }

    String getStringKey(java.lang.Integer value) {
        StringBuilder sb = new StringBuilder();
        sb.append(value);
        return sb.toString();
    }
    
    java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.parseInt(value);
            return key;
        }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Materia) {
            Materia o = (Materia) object;
            return getStringKey(o.getId());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Materia.class.getName()});
            return null;
        }
    }

}
