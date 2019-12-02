package com.distributivo.model.business;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the materia database table.
 *
 */
@Entity
@Table(name = "materia")
@NamedQuery(name = "Materia.findAll", query = "SELECT m FROM Materia m")
public class Materia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String descripcion;

    private String estado;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_creacion_audi")
    private Date fechaCreacionAudi;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_modificacion_audi")
    private Date fechaModificacionAudi;

    private String nombre;

    @Column(name = "usuario_creacion_audi")
    private String usuarioCreacionAudi;

    @Column(name = "usuario_modificacion_audi")
    private String usuarioModificacionAudi;

    //bi-directional many-to-one association to DistributivoDetalle
    @OneToMany(mappedBy = "materia")
    private List<DistributivoDetalle> distributivoDetalles;

    //bi-directional many-to-one association to MallaDetalle
    @OneToMany(mappedBy = "materia")
    private List<MallaDetalle> mallaDetalles;

    public Materia() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Date getFechaCreacionAudi() {
        return this.fechaCreacionAudi;
    }

    public void setFechaCreacionAudi(Date fechaCreacionAudi) {
        this.fechaCreacionAudi = fechaCreacionAudi;
    }

    public Date getFechaModificacionAudi() {
        return this.fechaModificacionAudi;
    }

    public void setFechaModificacionAudi(Date fechaModificacionAudi) {
        this.fechaModificacionAudi = fechaModificacionAudi;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public List<DistributivoDetalle> getDistributivoDetalles() {
        return this.distributivoDetalles;
    }

    public void setDistributivoDetalles(List<DistributivoDetalle> distributivoDetalles) {
        this.distributivoDetalles = distributivoDetalles;
    }

    public DistributivoDetalle addDistributivoDetalle(DistributivoDetalle distributivoDetalle) {
        getDistributivoDetalles().add(distributivoDetalle);
        distributivoDetalle.setMateria(this);

        return distributivoDetalle;
    }

    public DistributivoDetalle removeDistributivoDetalle(DistributivoDetalle distributivoDetalle) {
        getDistributivoDetalles().remove(distributivoDetalle);
        distributivoDetalle.setMateria(null);

        return distributivoDetalle;
    }

    public List<MallaDetalle> getMallaDetalles() {
        return this.mallaDetalles;
    }

    public void setMallaDetalles(List<MallaDetalle> mallaDetalles) {
        this.mallaDetalles = mallaDetalles;
    }

    public MallaDetalle addMallaDetalle(MallaDetalle mallaDetalle) {
        getMallaDetalles().add(mallaDetalle);
        mallaDetalle.setMateria(this);

        return mallaDetalle;
    }

    public MallaDetalle removeMallaDetalle(MallaDetalle mallaDetalle) {
        getMallaDetalles().remove(mallaDetalle);
        mallaDetalle.setMateria(null);

        return mallaDetalle;
    }

}
