package com.distributivo.model.business;

import com.distributivo.model.security.Usuario;
import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The persistent class for the paralelo database table.
 *
 */
@Entity
@Table(name = "paralelo")
@NamedQuery(name = "Paralelo.findAll", query = "SELECT p FROM Paralelo p")
public class Paralelo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paralelo")
    private Integer idParalelo;

    private String estado;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_modificacion")
    private Date fechaModificacion;

    private String nombre;
    //bi-directional many-to-one association to Adicionale
    @OneToMany(mappedBy = "paralelo", cascade = CascadeType.ALL)
    private List<DistributivoDetalle> distributivoDetalles;

    public List<DistributivoDetalle> getDistributivoDetalles() {
        return this.distributivoDetalles;
    }

    public void setDistributivoDetalles(List<DistributivoDetalle> distributivoDetalles) {
        this.distributivoDetalles = distributivoDetalles;
    }

    public DistributivoDetalle addDistributivoDetalle(DistributivoDetalle distributivoDetalle) {
        getDistributivoDetalles().add(distributivoDetalle);
        distributivoDetalle.setParalelo(this);

        return distributivoDetalle;
    }

    public DistributivoDetalle removeDistributivoDetalle(DistributivoDetalle distributivoDetalle) {
        getDistributivoDetalles().remove(distributivoDetalle);
        distributivoDetalle.setDistributivoCabecera(null);

        return distributivoDetalle;
    }
    
    
     @JoinTable(name = "paralelo_curso", joinColumns = {
        @JoinColumn(name = "id_paralelo", referencedColumnName = "id_paralelo")}, inverseJoinColumns = {
        @JoinColumn(name = "id_curso", referencedColumnName = "id_curso")})
    @ManyToMany
    private List<Curso> cursoList;
     
     
    public Paralelo() {
    }

    public Integer getIdParalelo() {
        return this.idParalelo;
    }

    public void setIdParalelo(Integer idParalelo) {
        this.idParalelo = idParalelo;
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

    @XmlTransient
    public List<Curso> getCursoList() {
        return cursoList;
    }

    public void setCursoList(List<Curso> cursoList) {
        this.cursoList = cursoList;
    }

    

}
