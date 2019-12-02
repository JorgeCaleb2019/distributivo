package com.distributivo.model.business;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the malla_detalle database table.
 *
 */
@Entity
@Table(name = "malla_detalle")
@NamedQuery(name = "MallaDetalle.findAll", query = "SELECT m FROM MallaDetalle m")
public class MallaDetalle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetalle;

    private String estado;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_ingreso")
    private Date fechaIngreso;

    @Column(name = "num_horas")
    private Integer numHoras;

    private String observacion;

    private String usuario;

    //bi-directional many-to-one association to Curso
    @ManyToOne
    @JoinColumn(name = "id_curso")
    private Curso curso;

    //bi-directional many-to-one association to MallaCabecera
    @ManyToOne
    @JoinColumn(name = "id_malla")
    private MallaCabecera mallaCabecera;

    //bi-directional many-to-one association to Materia
    @ManyToOne
    @JoinColumn(name = "id_materia")
    private Materia materia;

    public MallaDetalle() {
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

    public Date getFechaIngreso() {
        return this.fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Integer getNumHoras() {
        return this.numHoras;
    }

    public void setNumHoras(Integer numHoras) {
        this.numHoras = numHoras;
    }

    public String getObservacion() {
        return this.observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getUsuario() {
        return this.usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Curso getCurso() {
        return this.curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public MallaCabecera getMallaCabecera() {
        return this.mallaCabecera;
    }

    public void setMallaCabecera(MallaCabecera mallaCabecera) {
        this.mallaCabecera = mallaCabecera;
    }

    public Materia getMateria() {
        return this.materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

}
