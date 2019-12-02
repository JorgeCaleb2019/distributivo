package com.distributivo.model.business;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the titularidad_dedicacion database table.
 *
 */
@Entity
@Table(name = "titularidad_dedicacion")
@NamedQuery(name = "TitularidadDedicacion.findAll", query = "SELECT t FROM TitularidadDedicacion t")
public class TitularidadDedicacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_titularidad_dedicacion")
    private Integer idTitularidadDedicacion;

    private String estado;

    //bi-directional many-to-one association to Dedicacion
    @ManyToOne
    @JoinColumn(name = "id_dedicacion")
    private Dedicacion dedicacion;

    //bi-directional many-to-one association to Titularidad
    @ManyToOne
    @JoinColumn(name = "id_titularidad")
    private Titularidad titularidad;

    public TitularidadDedicacion() {
    }

    public Integer getIdTitularidadDedicacion() {
        return this.idTitularidadDedicacion;
    }

    public void setIdTitularidadDedicacion(Integer idTitularidadDedicacion) {
        this.idTitularidadDedicacion = idTitularidadDedicacion;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Dedicacion getDedicacion() {
        return this.dedicacion;
    }

    public void setDedicacion(Dedicacion dedicacion) {
        this.dedicacion = dedicacion;
    }

    public Titularidad getTitularidad() {
        return this.titularidad;
    }

    public void setTitularidad(Titularidad titularidad) {
        this.titularidad = titularidad;
    }

}
