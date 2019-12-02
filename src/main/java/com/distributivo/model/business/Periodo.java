package com.distributivo.model.business;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the periodo database table.
 *
 */
@Entity
@Table(name = "periodo")
@NamedQuery(name = "Periodo.findAll", query = "SELECT p FROM Periodo p")
public class Periodo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Boolean activo;

    private String estado;

    @Column(name = "fecha_creacion_audi")
    private Timestamp fechaCreacionAudi;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_final")
    private Date fechaFinal;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    @Column(name = "fecha_modificacion_audi")
    private Timestamp fechaModificacionAudi;

    private String titulo;

    @Column(name = "usuario_creacion_audi")
    private String usuarioCreacionAudi;

    @Column(name = "usuario_modificacion_audi")
    private String usuarioModificacionAudi;

    //bi-directional many-to-one association to DistributivoCabecera
    @OneToMany(mappedBy = "periodo", cascade = CascadeType.ALL)
    private List<DistributivoCabecera> distributivoCabeceras;

    @Column(name = "estado_distributivo")
    private String estadoDistributivo;
    
    @Column(name = "usuario_autoriza")
    private String usuarioAutoriza;
    
    @Column(name = "usuario_elimina")
    private String usuarioElimina;
    
    @Column(name = "observacion_elimina")
    private String observacionesElimina;
    //bi-directional many-to-one association to Institucion
    @ManyToOne
    @JoinColumn(name = "id_institucion")
    private Institucion institucion;

    public Periodo() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
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

    public Date getFechaFinal() {
        return this.fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Date getFechaInicio() {
        return this.fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Timestamp getFechaModificacionAudi() {
        return this.fechaModificacionAudi;
    }

    public void setFechaModificacionAudi(Timestamp fechaModificacionAudi) {
        this.fechaModificacionAudi = fechaModificacionAudi;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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

    public List<DistributivoCabecera> getDistributivoCabeceras() {
        return this.distributivoCabeceras;
    }

    public void setDistributivoCabeceras(List<DistributivoCabecera> distributivoCabeceras) {
        this.distributivoCabeceras = distributivoCabeceras;
    }

    public DistributivoCabecera addDistributivoCabecera(DistributivoCabecera distributivoCabecera) {
        getDistributivoCabeceras().add(distributivoCabecera);
        distributivoCabecera.setPeriodo(this);

        return distributivoCabecera;
    }

    public DistributivoCabecera removeDistributivoCabecera(DistributivoCabecera distributivoCabecera) {
        getDistributivoCabeceras().remove(distributivoCabecera);
        distributivoCabecera.setPeriodo(null);

        return distributivoCabecera;
    }

    public Institucion getInstitucion() {
        return this.institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public String getEstadoDistributivo() {
        return estadoDistributivo;
    }

    public void setEstadoDistributivo(String estadoDistributivo) {
        this.estadoDistributivo = estadoDistributivo;
    }

    public String getUsuarioAutoriza() {
        return usuarioAutoriza;
    }

    public void setUsuarioAutoriza(String usuarioAutoriza) {
        this.usuarioAutoriza = usuarioAutoriza;
    }

    public String getUsuarioElimina() {
        return usuarioElimina;
    }

    public void setUsuarioElimina(String usuarioElimina) {
        this.usuarioElimina = usuarioElimina;
    }

    public String getObservacionesElimina() {
        return observacionesElimina;
    }

    public void setObservacionesElimina(String observacionesElimina) {
        this.observacionesElimina = observacionesElimina;
    }

}
