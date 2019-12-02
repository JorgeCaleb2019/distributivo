package com.distributivo.utilidadbase.controller;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Clase con métodos para verifcar contraseñas
 *
 * @author Fernando
 */
public class UtilParametrosPassword {

    public static int cantidadNumeros(String clave) {
        int cont = 0;
        for (char c : clave.toCharArray()) {
            if (Character.isDigit(c)) {
                cont++;
            }
        }
        return cont;
    }

    public static int cantidadLetras(String clave) {
        int cont = 0;
        for (char c : clave.toCharArray()) {
            if (Character.isLetter(c)) {
                cont++;
            }
        }
        return cont;
    }

    public static int cantidadEspeciales(String clave) {
        int cont = 0;
        for (char c : clave.toCharArray()) {
            if (!Character.isLetter(c) && !Character.isDigit(c)) {
                cont++;
            }
        }
        return cont;
    }

    public static int tamanioClave(String clave) {
        return clave.trim().length();
    }

    public static String generarPassword() throws NoSuchAlgorithmException {
        String[] symbols = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
        int length = 10;
        Random random = SecureRandom.getInstanceStrong();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int indexRandom = random.nextInt(symbols.length);
            sb.append(symbols[indexRandom]);
        }
        return sb.toString();
    }
}
