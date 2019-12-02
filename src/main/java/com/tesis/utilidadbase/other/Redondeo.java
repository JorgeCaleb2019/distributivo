/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tesis.utilidadbase.other;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author Toshiba
 */
public class Redondeo {

    public static Float redondeo2(Float value, int precision) {
        BigDecimal big = new BigDecimal(value);
        big = big.setScale(precision, RoundingMode.HALF_UP);
        return big.floatValue();
    }
    
    public static Double redondeo2(Double value, int precision) {
        BigDecimal big = new BigDecimal(value);
        big = big.setScale(precision, RoundingMode.HALF_UP);
        return big.doubleValue();
    }

}
