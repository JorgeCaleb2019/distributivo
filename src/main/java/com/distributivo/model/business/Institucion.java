package com.distributivo.model.business;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the institucion database table.
 *
 */
@Entity
@Table(name = "institucion")
@NamedQuery(name = "Institucion.findAll", query = "SELECT i FROM Institucion i")
public class Institucion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String direccion;

    private String email;

    private String estado;

    @Column(name = "fecha_creacion_audi")
    private Timestamp fechaCreacionAudi;

    @Column(name = "fecha_modificacion_audi")
    private Timestamp fechaModificacionAudi;

    private String logo;

    private String mision;

    private String nombre;

    private String rector;

    private String telefono;

    @Column(name = "usuario_creacion_audi")
    private String usuarioCreacionAudi;

    @Column(name = "usuario_modificacion_audi")
    private String usuarioModificacionAudi;

    private String vicerector;

    private String vision;

    private String web;

    //bi-directional many-to-one association to Periodo
    @OneToMany(mappedBy = "institucion")
    private List<Periodo> periodos;

    public Institucion() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Timestamp getFechaCreacionAudi() {
        return this.fechaCreacionAudi;
    }

    public void setFechaCreacionAudi(Timestamp fechaCreacionAudi) {
        this.fechaCreacionAudi = fechaCreacionAudi;
    }

    public Timestamp getFechaModificacionAudi() {
        return this.fechaModificacionAudi;
    }

    public void setFechaModificacionAudi(Timestamp fechaModificacionAudi) {
        this.fechaModificacionAudi = fechaModificacionAudi;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMision() {
        return this.mision;
    }

    public void setMision(String mision) {
        this.mision = mision;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRector() {
        return this.rector;
    }

    public void setRector(String rector) {
        this.rector = rector;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUsuarioCreacionAudi() {
        return this.usuarioCreacionAudi;
    }

    public void setUsuarioCreacionAudi(String usuarioCreacionAudi) {
        this.usuarioCreacionAudi = usuarioCreacionAudi;
    }

    public String getUsuarioModificacionAudi() {
        return this.usuarioModificacionAudi;
    }

    public void setUsuarioModificacionAudi(String usuarioModificacionAudi) {
        this.usuarioModificacionAudi = usuarioModificacionAudi;
    }

    public String getVicerector() {
        return this.vicerector;
    }

    public void setVicerector(String vicerector) {
        this.vicerector = vicerector;
    }

    public String getVision() {
        return this.vision;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    public String getWeb() {
        return this.web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public List<Periodo> getPeriodos() {
        return this.periodos;
    }

    public void setPeriodos(List<Periodo> periodos) {
        this.periodos = periodos;
    }

    public Periodo addPeriodo(Periodo periodo) {
        getPeriodos().add(periodo);
        periodo.setInstitucion(this);

        return periodo;
    }

    public Periodo removePeriodo(Periodo periodo) {
        getPeriodos().remove(periodo);
        periodo.setInstitucion(null);

        return periodo;
    }

}
