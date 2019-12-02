package com.distributivo.model.business;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The persistent class for the titularidad database table.
 *
 */
@Entity
@Table(name = "titularidad")
@NamedQuery(name = "Titularidad.findAll", query = "SELECT t FROM Titularidad t")
public class Titularidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_titularidad")
    private Long idTitularidad;

    private String estado;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    private String nombre;

    private String usuario;

    //bi-directional many-to-one association to Docente
    @OneToMany(mappedBy = "titularidad")
    private List<Docente> docentes;

    //bi-directional many-to-one association to TitularidadDedicacion
    @OneToMany(mappedBy = "titularidad")
    private List<TitularidadDedicacion> titularidadDedicacions;
    
    
    @JoinTable(name = "titularidad_dedicacion", joinColumns = {
        @JoinColumn(name = "id_titularidad", referencedColumnName = "id_titularidad")}, inverseJoinColumns = {
        @JoinColumn(name = "id_dedicacion", referencedColumnName = "id")})
    @ManyToMany
    private List<Dedicacion> dedicacionList;

    @XmlTransient
    public List<Dedicacion> getDedicacionList() {
        return dedicacionList;
    }

    public void setDedicacionList(List<Dedicacion> dedicacionList) {
        this.dedicacionList = dedicacionList;
    }

    public Titularidad() {
        
    }

    public Long getIdTitularidad() {
        return idTitularidad;
    }

    public void setIdTitularidad(Long idTitularidad) {
        this.idTitularidad = idTitularidad;
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

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return this.usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public List<Docente> getDocentes() {
        return this.docentes;
    }

    public void setDocentes(List<Docente> docentes) {
        this.docentes = docentes;
    }

    public Docente addDocente(Docente docente) {
        getDocentes().add(docente);
        docente.setTitularidad(this);

        return docente;
    }

    public Docente removeDocente(Docente docente) {
        getDocentes().remove(docente);
        docente.setTitularidad(null);

        return docente;
    }

    public List<TitularidadDedicacion> getTitularidadDedicacions() {
        return this.titularidadDedicacions;
    }

    public void setTitularidadDedicacions(List<TitularidadDedicacion> titularidadDedicacions) {
        this.titularidadDedicacions = titularidadDedicacions;
    }

    public TitularidadDedicacion addTitularidadDedicacion(TitularidadDedicacion titularidadDedicacion) {
        getTitularidadDedicacions().add(titularidadDedicacion);
        titularidadDedicacion.setTitularidad(this);

        return titularidadDedicacion;
    }

    public TitularidadDedicacion removeTitularidadDedicacion(TitularidadDedicacion titularidadDedicacion) {
        getTitularidadDedicacions().remove(titularidadDedicacion);
        titularidadDedicacion.setTitularidad(null);

        return titularidadDedicacion;
    }

}
