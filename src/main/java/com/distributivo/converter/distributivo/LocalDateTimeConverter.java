/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.converter.distributivo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
 
/**
 * Faces converter for support of LocalDate
 * @author Juneau
 */
@FacesConverter(value="localDateTimeConverter")
public class LocalDateTimeConverter implements javax.faces.convert.Converter {
 
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
          
          System.out.println("entro por aqui");
        return LocalDate.parse(value);
    }
 
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
 
        LocalDate dateValue = (LocalDate) value;
         System.out.print("entro por aca");
        return dateValue.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        
    }
     
}