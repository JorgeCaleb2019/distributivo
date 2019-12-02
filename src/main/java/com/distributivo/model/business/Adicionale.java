package com.distributivo.model.business;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "adicionales")
@NamedQuery(name = "Adicionale.findAll", query = "SELECT a FROM Adicionale a")
public class Adicionale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_adicional")
    private Integer idAdicional;

    private String estado;

    private Integer horas;

    private String observaciones;

    //bi-directional many-to-one association to DistributivoCabecera
    @ManyToOne
    @JoinColumn(name = "id_distributivo")
    private DistributivoCabecera distributivoCabecera;

    //bi-directional many-to-one association to TipoActividad
    @ManyToOne
    @JoinColumn(name = "id_tipo_actividad")
    private TipoActividad tipoActividad;

    public Adicionale() {
    }

    public Integer getIdAdicional() {
        return this.idAdicional;
    }

    public void setIdAdicional(Integer idAdicional) {
        this.idAdicional = idAdicional;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getHoras() {
        return this.horas;
    }

    public void setHoras(Integer horas) {
        this.horas = horas;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public DistributivoCabecera getDistributivoCabecera() {
        return this.distributivoCabecera;
    }

    public void setDistributivoCabecera(DistributivoCabecera distributivoCabecera) {
        this.distributivoCabecera = distributivoCabecera;
    }

    public TipoActividad getTipoActividad() {
        return this.tipoActividad;
    }

    public void setTipoActividad(TipoActividad tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

}
