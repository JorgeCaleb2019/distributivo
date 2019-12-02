/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.converter.distributivo;

import com.distributivo.controller.distributivo.DocenteController;
import com.distributivo.model.business.Docente;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@FacesConverter(value = "docenteConverter")
public class DocenteConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        DocenteController controller = (DocenteController) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "docenteController");
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
        if (object instanceof Docente) {
            Docente o = (Docente) object;
            return getStringKey(o.getId());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Docente.class.getName()});
            return null;
        }
    }

 
    //  @Override
 /*   public Object getAsObject2(FacesContext context, UIComponent component, String value) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        LocalDate ld = null;
        try {
            ld = LocalDate.fromDateFields(df.parse(value));
        }
        catch (ParseException parseException) {
            throw new ConverterException("Could not convert the date.");
        }
        return ld;
    }// end method

  
    public String getAsString2(FacesContext context, UIComponent component, Object value) {
        LocalDate ld = (LocalDate) value;
        return ld.toString("dd/MM/yyyy");
    }// end method

   */ 
    
   /*
     private final DateTimeFormatter formatter =  DateTimeFormat.ofPattern("dd/MM/yyyy");    
        
     @Override
        public Object getAsObject3(FacesContext context, UIComponent component, 
            String value) {

            return formatter.parseDateTime(value).toLocalDate();
        }

        @Override
        public String getAsString3(FacesContext context, UIComponent 
        component, Object value) {
            return formatter.print((LocalDate) value);
       }
*/
    
}
