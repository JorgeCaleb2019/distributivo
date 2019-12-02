package com.distributivo.model.business;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The persistent class for the curso database table.
 *
 */
@Entity
@Table(name = "curso")
@NamedQuery(name = "Curso.findAll", query = "SELECT c FROM Curso c")
public class Curso implements Serializable, Comparable<Curso> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private Integer idCurso;

    private String estado;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_modificacion")
    private Date fechaModificacion;

    private String nombre;

    private String tipo;

    //bi-directional many-to-one association to MallaDetalle
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL)
    private List<MallaDetalle> mallaDetalles;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL)
    private List<DistributivoDetalle> distributivoDetalles;


    @JoinTable(name = "paralelo_curso", joinColumns = {
        @JoinColumn(name = "id_curso", referencedColumnName = "id_curso")}, inverseJoinColumns = {
        @JoinColumn(name = "id_paralelo", referencedColumnName = "id_paralelo")})
    @ManyToMany
    private List<Paralelo> paraleloList;

    public Curso() {
    }

    public Integer getIdCurso() {
        return this.idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaCreacion() {
        return this.fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return this.fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<MallaDetalle> getMallaDetalles() {
        return this.mallaDetalles;
    }

    public void setMallaDetalles(List<MallaDetalle> mallaDetalles) {
        this.mallaDetalles = mallaDetalles;
    }

    public MallaDetalle addMallaDetalle(MallaDetalle mallaDetalle) {
        getMallaDetalles().add(mallaDetalle);
        mallaDetalle.setCurso(this);

        return mallaDetalle;
    }

    public MallaDetalle removeMallaDetalle(MallaDetalle mallaDetalle) {
        getMallaDetalles().remove(mallaDetalle);
        mallaDetalle.setCurso(null);

        return mallaDetalle;
    }

    public List<DistributivoDetalle> getDistributivoDetalles() {
        return this.distributivoDetalles;
    }

    public void setDistributivoDetalles(List<DistributivoDetalle> distributivoDetalles) {
        this.distributivoDetalles = distributivoDetalles;
    }

    public DistributivoDetalle addDistributivoDetalle(DistributivoDetalle distributivoDetalle) {
        getDistributivoDetalles().add(distributivoDetalle);
        distributivoDetalle.setCurso(this);

        return distributivoDetalle;
    }

    public DistributivoDetalle removeDistributivoDetalle(DistributivoDetalle distributivoDetalle) {
        getDistributivoDetalles().remove(distributivoDetalle);
        distributivoDetalle.setDistributivoCabecera(null);

        return distributivoDetalle;
    }
    
    @XmlTransient
    public List<Paralelo> getParaleloList() {
        return paraleloList;
    }

    public void setParaleloList(List<Paralelo> paraleloList) {
        this.paraleloList = paraleloList;
    }

    @Override
    public int compareTo(Curso o) {
        if (this.idCurso < o.idCurso) {
            return -1;
        }
        if (this.idCurso > o.idCurso) {
            return 1;
        }
        return 0;
    }

}
