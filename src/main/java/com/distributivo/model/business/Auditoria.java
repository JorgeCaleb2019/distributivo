package com.distributivo.model.business;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The persistent class for the auditoria database table.
 *
 */
@Entity
@Table(name = "auditoria")
@NamedQuery(name = "Auditoria.findAll", query = "SELECT a FROM Auditoria a")
public class Auditoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Timestamp fecha;

    @Column(name = "info_actual")
    private String infoActual;

    @Column(name = "info_anterior")
    private String infoAnterior;

    private String operacion;

    private String tabla;

    private String usuario;

    public Auditoria() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getFecha() {
        return this.fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public String getInfoActual() {
        return this.infoActual;
    }

    public void setInfoActual(String infoActual) {
        this.infoActual = infoActual;
    }

    public String getInfoAnterior() {
        return this.infoAnterior;
    }

    public void setInfoAnterior(String infoAnterior) {
        this.infoAnterior = infoAnterior;
    }

    public String getOperacion() {
        return this.operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public String getTabla() {
        return this.tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getUsuario() {
        return this.usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

}
