package com.distributivo.model.business;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "malla_cabecera")
@NamedQuery(name = "MallaCabecera.findAll", query = "SELECT m FROM MallaCabecera m")
public class MallaCabecera implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_malla")
    private Integer idMalla;

    private String descripcion;

    private String estado;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_ingreso")
    private Date fechaIngreso;

    private String usuario;

    private String vigente;

    //bi-directional many-to-one association to MallaDetalle
    @OneToMany(mappedBy = "mallaCabecera")
    private List<MallaDetalle> mallaDetalles;

    public MallaCabecera() {
    }

    public Integer getIdMalla() {
        return this.idMalla;
    }

    public void setIdMalla(Integer idMalla) {
        this.idMalla = idMalla;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String getUsuario() {
        return this.usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getVigente() {
        return this.vigente;
    }

    public void setVigente(String vigente) {
        this.vigente = vigente;
    }

    public List<MallaDetalle> getMallaDetalles() {
        return this.mallaDetalles;
    }

    public void setMallaDetalles(List<MallaDetalle> mallaDetalles) {
        this.mallaDetalles = mallaDetalles;
    }

    public MallaDetalle addMallaDetalle(MallaDetalle mallaDetalle) {
        getMallaDetalles().add(mallaDetalle);
        mallaDetalle.setMallaCabecera(this);

        return mallaDetalle;
    }

    public MallaDetalle removeMallaDetalle(MallaDetalle mallaDetalle) {
        getMallaDetalles().remove(mallaDetalle);
        mallaDetalle.setMallaCabecera(null);

        return mallaDetalle;
    }

    @Override
    public String toString() {
        return "MallaCabecera{" + "idMalla=" + idMalla + ", descripcion=" + descripcion + ", estado=" + estado + ", fechaIngreso=" + fechaIngreso + ", usuario=" + usuario + ", vigente=" + vigente + ", mallaDetalles=" + mallaDetalles + '}';
    }
    
}
