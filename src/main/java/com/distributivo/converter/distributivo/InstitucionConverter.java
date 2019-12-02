/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.converter.distributivo;

import com.distributivo.controller.distributivo.InstitucionController;
import com.distributivo.model.business.Curso;
import com.distributivo.model.business.Institucion;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "institucionConverter")
public class InstitucionConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        InstitucionController controller = (InstitucionController) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "institucionController");
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

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Institucion) {
            Institucion o = (Institucion) object;
            return getStringKey(o.getId());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Institucion.class.getName()});
            return null;
        }
    }

}
