/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tesis.utilidadbase.other;

/**
 *
 * @author Toshiba
 */
public class StringUtil {
    
    public static boolean NullOrEmpty(String texto){
        if(texto==null){
            return true;
        }
        if(texto.trim().length()==0){
            return true;
        }
        return false;
    }
}
