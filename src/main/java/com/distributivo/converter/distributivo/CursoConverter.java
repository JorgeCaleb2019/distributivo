
package com.distributivo.converter.distributivo;

import com.distributivo.controller.distributivo.CursoController;
import com.distributivo.model.business.Curso;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "cursoConverter")
public class CursoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        CursoController controller = (CursoController) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "cursoController");
        if (value == null) {
            return null;
        }
        return controller.getFacade().buscarPorCodigo(Integer.parseInt(value));
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
        if (object instanceof Curso) {
            Curso o = (Curso) object;
            return getStringKey(Long.valueOf(o.getIdCurso()));
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Curso.class.getName()});
            return null;
        }
    }

}
