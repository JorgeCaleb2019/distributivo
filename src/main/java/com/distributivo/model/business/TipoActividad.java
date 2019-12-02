package com.distributivo.model.business;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the tipo_actividad database table.
 *
 */
@Entity
@Table(name = "tipo_actividad")
@NamedQuery(name = "TipoActividad.findAll", query = "SELECT t FROM TipoActividad t")
public class TipoActividad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_actividad")
    private Integer idTipoActividad;

    private String actividad;

    private String estado;

    //bi-directional many-to-one association to Adicionale
    @OneToMany(mappedBy = "tipoActividad")
    private List<Adicionale> adicionales;

    public TipoActividad() {
    }

    public Integer getIdTipoActividad() {
        return this.idTipoActividad;
    }

    public void setIdTipoActividad(Integer idTipoActividad) {
        this.idTipoActividad = idTipoActividad;
    }

    public String getActividad() {
        return this.actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Adicionale> getAdicionales() {
        return this.adicionales;
    }

    public void setAdicionales(List<Adicionale> adicionales) {
        this.adicionales = adicionales;
    }

    public Adicionale addAdicionale(Adicionale adicionale) {
        getAdicionales().add(adicionale);
        adicionale.setTipoActividad(this);

        return adicionale;
    }

    public Adicionale removeAdicionale(Adicionale adicionale) {
        getAdicionales().remove(adicionale);
        adicionale.setTipoActividad(null);

        return adicionale;
    }

}
