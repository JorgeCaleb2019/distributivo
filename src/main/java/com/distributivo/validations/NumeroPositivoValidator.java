/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author Fernando
 */
public class NumeroPositivoValidator implements ConstraintValidator<NumeroPositivo, Double> {
    
    @Override
    public void initialize(NumeroPositivo constraintAnnotation) {
       
    }
    
    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if(value==null){
            return false;
        }
        if(value<0){
            return false;
        }
        return true;
    }
}
