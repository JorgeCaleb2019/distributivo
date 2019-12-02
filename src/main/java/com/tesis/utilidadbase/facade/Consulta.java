/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tesis.utilidadbase.facade;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Clase Utilidad para la generación de consultas
 *
 * @author Fernando
 */
public class Consulta {

    private StringBuilder sb;
    private List<Filtro> filtros;
    private int filas;

    /**
     * Método para inicializar unaconsulta
     *
     * @param clase
     */
    public Consulta(String clase) {
        sb = new StringBuilder();
        sb.append("SELECT x FROM ");
        sb.append(clase);
        sb.append(" x");
    }

    /**
     * Métodos para agregar Filtors
     *
     * @param fil
     */
    public void agregarFiltrado(List<Filtro> fil) {
        filtros = fil;
        int order = 0;
        if (filtros != null) {
            if (filtros.size() > 0) {
                sb.append(" WHERE ");
                for (Filtro f : filtros) {
                    if (f.getTipo().equals("ORDER")) {
                        order = 1;
                    } else {
                        sb.append(f.obtenerConsultaParcial());
                        sb.append(" AND ");
                    }
                }
                sb.delete(sb.length() - 4, sb.length());
                if (order == 1) {
                    sb.append(" ");
                    for (Filtro f : filtros) {
                        if (f.getTipo().equals("ORDER")) {
                            sb.append(f.obtenerConsultaParcial());
                        }
                    }

                }
            }
        }
    }

    /**
     * Método para crear una consulta
     *
     * @param em
     * @param first
     * @param count
     * @return
     */
    public Query crearConsulta(EntityManager em, int first, int count) {
        Query q = em.createQuery(sb.toString());
        int orderby = sb.toString().indexOf("ORDER");
        String countQuery = "";
        if (orderby == -1) {
            countQuery = sb.toString().replace("SELECT x", "SELECT Count(x)");
        } else {
            countQuery = sb.toString().replace("SELECT x", "SELECT Count(x)").substring(0, orderby + 5);
        }
        Query q1 = em.createQuery(countQuery);
        for (Filtro f : filtros) {
            if (f.getTipo().equals("BETWEENFECHA")) {
                if (f.getKey1().trim().length() != 0 && f.getKey2().trim().length() != 0) {
                    q.setParameter(f.parametroKey1(), f.getValue1());
                    q.setParameter(f.parametroKey2(), f.getValue2());
                    q1.setParameter(f.parametroKey1(), f.getValue1());
                    q1.setParameter(f.parametroKey2(), f.getValue2());
                }
            } else if (!f.getTipo().equals("ORDER")) {
                if (f.getKey1().trim().length() != 0) {
                    if (f.getTipo().equals("LIKE")) {

                        f.setValue1("%" + f.getValue1().toString().toUpperCase() + "%");
                    }
                    if (f.getTipo().equals("SUB")) {
                        q.setParameter(f.parametroKey1(), f.getValue1());
                        q.setParameter(f.parametroKey2() + f.parametroKey2(), f.getValue2());
                        q1.setParameter(f.parametroKey1(), f.getValue1());
                        q1.setParameter(f.parametroKey2() + f.parametroKey2(), f.getValue2());
                    } else {
                        int nulo = 0;
                        if (f.getValue1() != null) {
                            if (f.getValue1().equals("nulo")) {
                                nulo = 1;
                            }
                        }
                        if (nulo == 0) {
                            q.setParameter(f.parametroKey1(), f.getValue1());
                            q1.setParameter(f.parametroKey1(), f.getValue1());
                        }
                    }
                }
                if (!f.getTipo().equals("SUB")) {
                    if (f.getKey2().trim().length() != 0) {
                        if (f.getTipo().equals("LIKE")) {
                            f.setValue2(f.getValue2().toString().toUpperCase() + "%");
                        }
                        q.setParameter(f.parametroKey2(), f.getValue2());
                        q1.setParameter(f.parametroKey2(), f.getValue2());
                    }
                }
            }
        }
        filas = Integer.parseInt(q1.getSingleResult().toString());
        q.setMaxResults(count);
        q.setFirstResult(first);
        return q;
    }

    /**
     * Método para crear una consulta
     *
     * @param em
     * @return
     */
    public Query crearConsulta(EntityManager em) {
        Query q = em.createQuery(sb.toString());
        for (Filtro f : filtros) {
            if (!f.getTipo().equals("ORDER")) {
                if (f.getKey1().trim().length() != 0) {
                    if (f.getTipo().equals("LIKE")) {
                        f.setValue1(f.getValue1().toString().toUpperCase() + "%");
                    }
                    if (f.getTipo().equals("SUB")) {
                        q.setParameter(f.parametroKey1(), f.getValue1());
                        q.setParameter(f.parametroKey2() + f.parametroKey2(), f.getValue2());
                    } else {
                        q.setParameter(f.parametroKey1(), f.getValue1());
                    }
                }
                if (!f.getTipo().equals("SUB")) {
                    if (f.getKey2().trim().length() != 0) {
                        if (f.getTipo().equals("LIKE")) {
                            f.setValue2(f.getValue2().toString().toUpperCase() + "%");
                        }
                        q.setParameter(f.parametroKey2(), f.getValue2());
                    }
                }
            }
        }

        return q;
    }

    /**
     * @return the filas
     */
    public int getFilas() {
        return filas;
    }

    /**
     * @param filas the filas to set
     */
    public void setFilas(int filas) {
        this.filas = filas;
    }
}
