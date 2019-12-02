package com.distributivo.util;

import javax.faces.validator.FacesValidator;
import javax.faces.context.FacesContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;


@FacesValidator("validadorFechaNacimiento")
public class ValidadorFechaNacimiento implements javax.faces.validator.Validator {

    private static int EDAD_MINIMA_PERMITIDA = 18;

    @Override
    public void validate(FacesContext facesContext, UIComponent componente, Object valor) throws ValidatorException {

        //El objeto valor es de tipo LocalDate porque el converter JodaLocalDateConverter
        //invoco al metodo getAsObject
//         LocalDate fechaNacimiento = valor.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        Date input = (Date) valor;
        LocalDateTime  conv=LocalDateTime.ofInstant(input.toInstant(), ZoneId.systemDefault());
        LocalDate fechaNacimiento=conv.toLocalDate();

      //  LocalDate fechaNacimiento = LocalDate.ofInstant(valor().toInstant(), ZoneId.systemDefault());    

        LocalDate ahora = LocalDate.now();
        // LocalDate fechaNacimiento = new LocalDate(valor);       
        Period periodo = Period.between(fechaNacimiento, ahora);
        //Years anios = Years.yearsBetween(fechaNacimiento,ahora;
        int anio = (fechaNacimiento.getYear());

        if (anio >= 10000) {
            FacesMessage facesMessage = new FacesMessage("Seleccione un Año incorrecto");
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(facesMessage);
        } else {

            System.out.printf("Tu edad es: %s años, %s meses y %s días",
                    periodo.getYears(), periodo.getMonths(), periodo.getDays());

            if (periodo.getYears() < 18 || periodo.getYears() > 75) {
                // FacesMessage facesMessage = new FacesMessage("Edad no permitida", "Debes tener más de " + EDAD_MINIMA_PERMITIDA + " para ingresar");
                FacesMessage facesMessage = new FacesMessage("Debes tener entre 18 Y 75 años para ingresar");
                facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(facesMessage);
            }
        }
    }
}
