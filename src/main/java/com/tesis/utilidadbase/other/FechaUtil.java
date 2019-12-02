/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tesis.utilidadbase.other;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Toshiba
 */
public class FechaUtil {

    static String meses[] = {"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};

    public static String mesActual() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.MONTH, 0);
        Locale espanol = new Locale("es", "ES");
        return cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, espanol).toUpperCase();
    }

    public static String mesFechaActual() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        Locale espanol = new Locale("es", "ES");
        return cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, espanol).toUpperCase();
    }

    public static String anioActual() {
        Date d = new Date();
        return "" + (d.getYear() + 1900);
    }

    public static String mes(Date fecha) {
        return meses[fecha.getMonth()];
    }

    public static String anio(Date fecha) {
        return "" + (fecha.getYear() + 1900);
    }

    public static String mes(String mes) {
        int m = Integer.parseInt(mes);
        return meses[m - 1].toUpperCase();
    }

    public static String siguienteMes(String mes) {
        int m = 0;
        for (int i = 0; i < meses.length; i++) {
            if (mes.toUpperCase().equals(meses[i].substring(0, 3).toUpperCase())) {
                m = i;
                break;
            }
        }
        m++;
        if (m >= 11) {
            m = 0;
        }
        return meses[m].toUpperCase();
    }

    public static Date[] getFechaEntreMes() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 1);
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(new Date());
        calendario.set(Calendar.DAY_OF_MONTH, calendario.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendario.set(Calendar.HOUR_OF_DAY, 23);
        return new Date[]{cal.getTime(), calendario.getTime()};
    }

}
