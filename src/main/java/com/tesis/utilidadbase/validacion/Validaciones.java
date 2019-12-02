/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tesis.utilidadbase.validacion;

import com.distributivo.utilidadbase.controller.Mensaje;


/**
 * Clase donde se encuentran algunas validaciones necesarias
 * @author Fernando*/

//@Named(value = "validaciones")
//@ViewScoped
public class Validaciones {

    public static final int ERROR_CEDULA=0;
    public static final int ERROR_RUC=1;
    public static final int ERROR_NO_DEFINIDO=2;
    public static final int IDENTIFICACION_OK=3;
    
    /**
     * Método para validar cédual ecuatoriana
     * @param cedula
     * @return 
     */
    public static boolean validarCedula(String cedula) {
        System.out.print("entra");
        if(cedula==null){
            return false;
        }
        for(char c:cedula.toCharArray()){
            if(!Character.isDigit(c)){
                 return false;
            }
        }
        
        System.out.print("sigue");
        int suma = 0;
        if (cedula.length() == 9) {
            System.out.println("Ingrese su cedula de 10 digitos");
            Mensaje.ERROR("Ingrese su cedula de 10 digitos");
            return false;
        } else {
            System.out.print("pasa");
            int a[] = new int[cedula.length() / 2];
            int b[] = new int[(cedula.length() / 2)];
            int c = 0;
            int d = 1;
            for (int i = 0; i < cedula.length() / 2; i++) {
                a[i] = Integer.parseInt(String.valueOf(cedula.charAt(c)));
                c = c + 2;
                if (i < (cedula.length() / 2) - 1) {
                    b[i] = Integer.parseInt(String.valueOf(cedula.charAt(d)));
                    d = d + 2;
                }
            }

            for (int i = 0; i < a.length; i++) {
                a[i] = a[i] * 2;
                if (a[i] > 9) {
                    a[i] = a[i] - 9;
                }
                suma = suma + a[i] + b[i];
            }
            int aux = suma / 10;
            int dec = (aux + 1) * 10;
            if ((dec - suma) == Integer.parseInt(String.valueOf(cedula.charAt(cedula.length() - 1)))) {
                return true;
            } else if (suma % 10 == 0 && cedula.charAt(cedula.length() - 1) == '0') {
                return true;
            } else {
                Mensaje.ERROR("Cedula Erronea");
                return false;                        
            }

        }
    }
    /**
     * Método par avalidar RUC
     * @param ruc
     * @return 
     */
    public static int validarRuc(String ruc) {
        if (ruc == null) {
            return ERROR_RUC;
        }
        if (ruc.length() == 13) {
            return IDENTIFICACION_OK;
        } else {
            return ERROR_RUC;
        }
    }
    	
}
