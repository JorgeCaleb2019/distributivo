package com.distributivo.model.business;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the distributivo_cabecera database table.
 *
 */
@Entity
@Table(name = "distributivo_cabecera")
@NamedQuery(name = "DistributivoCabecera.findAll", query = "SELECT d FROM DistributivoCabecera d")
public class DistributivoCabecera implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_distributivo")
    private Integer idDistributivo;

    private String estado;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_ingreso")
    private Date fechaIngreso;

    private String observacion;

    private String usuario;

    //bi-directional many-to-one association to Adicionale
    @OneToMany(mappedBy = "distributivoCabecera", cascade = CascadeType.ALL)
    private List<Adicionale> adicionales;

    //bi-directional many-to-one association to Dedicacion
    @ManyToOne
    @JoinColumn(name = "id_dedicacion")
    private Dedicacion dedicacion;

    //bi-directional many-to-one association to Docente
    @ManyToOne
    @JoinColumn(name = "id_docente")
    private Docente docente;

    //bi-directional many-to-one association to Periodo
    @ManyToOne
    @JoinColumn(name = "id_periodo")
    private Periodo periodo;

    //bi-directional many-to-one association to DistributivoDetalle
    @OneToMany(mappedBy = "distributivoCabecera", cascade = CascadeType.ALL)
    private List<DistributivoDetalle> distributivoDetalles;

    public DistributivoCabecera() {
    }

    public Integer getIdDistributivo() {
        return this.idDistributivo;
    }

    public void setIdDistributivo(Integer idDistributivo) {
        this.idDistributivo = idDistributivo;
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

    public List<Adicionale> getAdicionales() {
        return this.adicionales;
    }

    public void setAdicionales(List<Adicionale> adicionales) {
        this.adicionales = adicionales;
    }

    public Adicionale addAdicionale(Adicionale adicionale) {
        getAdicionales().add(adicionale);
        adicionale.setDistributivoCabecera(this);

        return adicionale;
    }

    public Adicionale removeAdicionale(Adicionale adicionale) {
        getAdicionales().remove(adicionale);
        adicionale.setDistributivoCabecera(null);

        return adicionale;
    }

    public Dedicacion getDedicacion() {
        return this.dedicacion;
    }

    public void setDedicacion(Dedicacion dedicacion) {
        this.dedicacion = dedicacion;
    }

    public Docente getDocente() {
        return this.docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public Periodo getPeriodo() {
        return this.periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public List<DistributivoDetalle> getDistributivoDetalles() {
        return this.distributivoDetalles;
    }

    public void setDistributivoDetalles(List<DistributivoDetalle> distributivoDetalles) {
        this.distributivoDetalles = distributivoDetalles;
    }

    public DistributivoDetalle addDistributivoDetalle(DistributivoDetalle distributivoDetalle) {
        getDistributivoDetalles().add(distributivoDetalle);
        distributivoDetalle.setDistributivoCabecera(this);

        return distributivoDetalle;
    }

    public DistributivoDetalle removeDistributivoDetalle(DistributivoDetalle distributivoDetalle) {
        getDistributivoDetalles().remove(distributivoDetalle);
        distributivoDetalle.setDistributivoCabecera(null);

        return distributivoDetalle;
    }
}
