package com.distributivo.model.business;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the dedicacion database table.
 *
 */
@Entity
@Table(name = "dedicacion")
@NamedQuery(name = "Dedicacion.findAll", query = "SELECT d FROM Dedicacion d")
public class Dedicacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String estado;

    @Column(name = "horas_max")
    private Integer horasMax;

    @Column(name = "horas_min")
    private Integer horasMin;

    
    @Column(name = "horas_min_actividades")
    private Integer minActividades;
    
    @Column(name = "horas_max_actividades")
    private Integer maxActividades;
    
    @Column(name = "total")
    private Integer total;

    private String nombre;

    //bi-directional many-to-one association to DistributivoCabecera
    @OneToMany(mappedBy = "dedicacion")
    private List<DistributivoCabecera> distributivoCabeceras;

    //bi-directional many-to-one association to TitularidadDedicacion
    @OneToMany(mappedBy = "dedicacion")
    private List<TitularidadDedicacion> titularidadDedicacions;

    public Dedicacion() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getHorasMax() {
        return this.horasMax;
    }

    public void setHorasMax(Integer horasMax) {
        this.horasMax = horasMax;
    }

    public Integer getHorasMin() {
        return this.horasMin;
    }

    public void setHorasMin(Integer horasMin) {
        this.horasMin = horasMin;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<DistributivoCabecera> getDistributivoCabeceras() {
        return this.distributivoCabeceras;
    }

    public void setDistributivoCabeceras(List<DistributivoCabecera> distributivoCabeceras) {
        this.distributivoCabeceras = distributivoCabeceras;
    }

    public DistributivoCabecera addDistributivoCabecera(DistributivoCabecera distributivoCabecera) {
        getDistributivoCabeceras().add(distributivoCabecera);
        distributivoCabecera.setDedicacion(this);

        return distributivoCabecera;
    }

    public DistributivoCabecera removeDistributivoCabecera(DistributivoCabecera distributivoCabecera) {
        getDistributivoCabeceras().remove(distributivoCabecera);
        distributivoCabecera.setDedicacion(null);

        return distributivoCabecera;
    }

    public List<TitularidadDedicacion> getTitularidadDedicacions() {
        return this.titularidadDedicacions;
    }

    public void setTitularidadDedicacions(List<TitularidadDedicacion> titularidadDedicacions) {
        this.titularidadDedicacions = titularidadDedicacions;
    }

    public TitularidadDedicacion addTitularidadDedicacion(TitularidadDedicacion titularidadDedicacion) {
        getTitularidadDedicacions().add(titularidadDedicacion);
        titularidadDedicacion.setDedicacion(this);

        return titularidadDedicacion;
    }

    public TitularidadDedicacion removeTitularidadDedicacion(TitularidadDedicacion titularidadDedicacion) {
        getTitularidadDedicacions().remove(titularidadDedicacion);
        titularidadDedicacion.setDedicacion(null);

        return titularidadDedicacion;
    }

    public Integer getMinActividades() {
        return minActividades;
    }

    public void setMinActividades(Integer minActividades) {
        this.minActividades = minActividades;
    }

    public Integer getMaxActividades() {
        return maxActividades;
    }

    public void setMaxActividades(Integer maxActividades) {
        this.maxActividades = maxActividades;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    
    
    
}
