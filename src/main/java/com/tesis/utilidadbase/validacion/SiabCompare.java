/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tesis.utilidadbase.validacion;

/**
 *
 * @author Fernando
 */
public class SiabCompare {

    public static boolean equals(Object primero, Object segundo) {
        if (primero == null) {
            return false;
        }
        if (segundo == null) {
            return false;
        }
        return primero.equals(segundo);
    }

    public static boolean equals(String primero, String segundo) {
        if (primero == null || segundo == null) {
            return false;
        }
        return primero.toUpperCase().equals(segundo.toUpperCase());
    }
    
}
