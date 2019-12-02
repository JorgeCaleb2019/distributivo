package com.distributivo.model.business;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the paralelo_curso database table.
 *
 */
@Entity
@Table(name = "paralelo_curso")
@NamedQuery(name = "ParaleloCurso.findAll", query = "SELECT p FROM ParaleloCurso p")
public class ParaleloCurso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso_paralelo")
    private Integer idCursoParalelo;

    private String estado;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_registro")
    private Date fechaRegistro;

    private String observacion;

    //bi-directional many-to-one association to Curso
    @ManyToOne
    @JoinColumn(name = "id_curso")
    private Curso curso;

    //bi-directional many-to-one association to Paralelo
    @ManyToOne
    @JoinColumn(name = "id_paralelo")
    private Paralelo paralelo;

    public ParaleloCurso() {
    }

    public Integer getIdCursoParalelo() {
        return this.idCursoParalelo;
    }

    public void setIdCursoParalelo(Integer idCursoParalelo) {
        this.idCursoParalelo = idCursoParalelo;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaRegistro() {
        return this.fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getObservacion() {
        return this.observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Curso getCurso() {
        return this.curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Paralelo getParalelo() {
        return this.paralelo;
    }

    public void setParalelo(Paralelo paralelo) {
        this.paralelo = paralelo;
    }
}
