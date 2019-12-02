/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tesis.utilidadbase.facade;

import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * Clase utilidad para guardar filtros para que luego se puedan crear consultas
 * JPQL
 *
 * @author Fernando
 */
public class Filtro implements Serializable {

    private String tipo;
    private String key1;
    private Object value1;
    private String key2;
    private Object value2;
    private String opciones;

    public Filtro(String tipo, String key1, Object value1) {
        this.tipo = tipo;
        this.key1 = key1;
        this.value1 = value1;
    }

    public Filtro(String tipo, String key1, Object value1, String key2, Object value2) {
        this.tipo = tipo;
        this.key1 = key1;
        this.value1 = value1;
        this.key2 = key2;
        this.value2 = value2;
    }

    public Filtro(String tipo, String key1, Object value1, String opciones) {
        this.tipo = tipo;
        this.key1 = key1;
        this.value1 = value1;
        this.opciones = opciones;
    }

    public String parametroKey1() {
        StringTokenizer t = new StringTokenizer(key1, ".");
        String val = "";
        while (t.hasMoreTokens()) {
            val = t.nextToken();
        }
        return val;
    }

    public String parametroKey2() {
        StringTokenizer t = new StringTokenizer(key2, ".");
        String val = "";
        while (t.hasMoreTokens()) {
            val = t.nextToken();
        }
        return val;
    }

    /**
     * Método para obtener una consulta parcial
     *
     * @return
     */
    public StringBuilder obtenerConsultaParcial() {
        StringBuilder sb = new StringBuilder();
        if (tipo.equals(">=")) {
            sb.append("x.");
            sb.append(key1);
            sb.append(" >= :");
            sb.append(parametroKey1());
        }
        if (tipo.equals("=")) {
            int nulo = 0;
            if (value1 != null) {
                if (value1.equals("nulo")) {
                    sb.append("x.");
                    sb.append(key1);
                    sb.append(" is null");
                    nulo = 1;
                }
            }
            if (nulo == 0) {
                sb.append("x.");
                sb.append(key1);
                sb.append(" = :");
                sb.append(parametroKey1());
            }
        }
        if (tipo.equals("!=")) {
            int nulo = 0;
            if (value1 != null) {
                if (value1.equals("nulo")) {
                    sb.append("x.");
                    sb.append(key1);
                    sb.append(" is not null");
                    nulo = 1;
                }
            }
            if (nulo == 0) {
                sb.append("x.");
                sb.append(key1);
                sb.append(" != :");
                sb.append(parametroKey1());
            }
        }
        if (tipo.equals("LIKE")) {
            if (value1 instanceof Integer) {
                tipo = "=";
                sb.append("x.");
                sb.append(key1);
                sb.append(" = :");
                sb.append(parametroKey1());
            } else {
                if (value1 instanceof String) {
                    sb.append(" UPPER(x.");
                    sb.append(key1);
                    sb.append(")");
                    sb.append(" LIKE :");
                    sb.append(parametroKey1());
                }
            }
        }
        if (tipo.equals("BETWEEN")) {
            sb.append("x.");
            sb.append(key1);
            sb.append(" BETWEEN :");
            sb.append(parametroKey1());
            sb.append(" AND :");
            sb.append(parametroKey2());
        }
        if (tipo.equals("BETWEENFECHA")) {
            sb.append("x.");
            sb.append(key1);
            sb.append(" BETWEEN :");
            sb.append(parametroKey1());
            sb.append(" AND :");
            sb.append(parametroKey2());
        }
        if (tipo.equals("ORDER")) {
            sb.append("ORDER BY x.");
            sb.append(key1);
            sb.append(" ");
            sb.append(value1);
        }
        if (tipo.equals("SUB")) {
            sb.append(" (x.");
            sb.append(key1);
            sb.append(" = :");
            sb.append(parametroKey1());
            sb.append(" OR x.");
            sb.append(key1);
            sb.append(" = :");
            sb.append(parametroKey2() + parametroKey2());
            sb.append(") ");

        }
        return sb;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        if (key1 == null) {
            key1 = "";
        }
        this.key1 = key1;
    }

    public Object getValue1() {
        return value1;
    }

    public void setValue1(Object value1) {
        this.value1 = value1;
    }

    public String getKey2() {
        if (key2 == null) {
            key2 = "";
        }
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

    public Object getValue2() {
        return value2;
    }

    public void setValue2(Object value2) {
        this.value2 = value2;
    }

    public String getOpciones() {
        return opciones;
    }

    public void setOpciones(String opciones) {
        this.opciones = opciones;
    }

}
