package com.distributivo.model.business;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the distributivo_detalle database table.
 *
 */
@Entity
@Table(name = "distributivo_detalle")
@NamedQuery(name = "DistributivoDetalle.findAll", query = "SELECT d FROM DistributivoDetalle d")
public class DistributivoDetalle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetalle;

    private String estado;

    private String observacion;

    //bi-directional many-to-one association to DistributivoCabecera
    @ManyToOne
    @JoinColumn(name = "id_distributivo")
    private DistributivoCabecera distributivoCabecera;

    //bi-directional many-to-one association to curso
    @ManyToOne
    @JoinColumn(name = "id_curso")
    private Curso curso;

    //bi-directional many-to-one association to paralelo
    @ManyToOne
    @JoinColumn(name = "id_paralelo")
    private Paralelo paralelo;
    //bi-directional many-to-one association to Materia
    @ManyToOne
    @JoinColumn(name = "id_materia")
    private Materia materia;

    @Column(name = "id_malla_detalle")
    private Integer idMallaDetalle;

    public DistributivoDetalle() {
    }

    public Integer getIdDetalle() {
        return this.idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return this.observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public DistributivoCabecera getDistributivoCabecera() {
        return this.distributivoCabecera;
    }

    public void setDistributivoCabecera(DistributivoCabecera distributivoCabecera) {
        this.distributivoCabecera = distributivoCabecera;
    }

    public Materia getMateria() {
        return this.materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Integer getIdMallaDetalle() {
        return idMallaDetalle;
    }

    public void setIdMallaDetalle(Integer idMallaDetalle) {
        this.idMallaDetalle = idMallaDetalle;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Paralelo getParalelo() {
        return paralelo;
    }

    public void setParalelo(Paralelo paralelo) {
        this.paralelo = paralelo;
    }

}
