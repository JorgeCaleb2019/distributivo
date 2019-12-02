package com.distributivo.model.business;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the docente database table.
 *
 */
@Entity
@Table(name = "docente")
@NamedQuery(name = "Docente.findAll", query = "SELECT d FROM Docente d")
public class Docente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String cedula;

    private String celular;

    @Column(name = "codigo_senescyt")
    private String codigoSenescyt;

    private String correo;

    private String estado;

    @Column(name = "fecha_creacion_audi")
    private Timestamp fechaCreacionAudi;

    @Column(name = "fecha_modificacion_audi")
    private Timestamp fechaModificacionAudi;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;

   /* @Column(name = "horas_adicionales")
    private Integer horasAdicionales;

    @Column(name = "horas_permitidas")
    private Integer horasPermitidas;

    @Column(name = "horas_tutoria")
    private Integer horasTutoria;
*/
    @Column(name = "id_usuario")
    private Long idUsuario;

    private String nombre;

    private String telefono;

    @Column(name = "titulo_cuarto_nivel")
    private String tituloCuartoNivel;

    @Column(name = "titulo_tercer_nivel")
    private String tituloTercerNivel;

    @Column(name = "usuario_creacion_audi")
    private String usuarioCreacionAudi;

    @Column(name = "usuario_modificacion_audi")
    private String usuarioModificacionAudi;

    //bi-directional many-to-one association to DistributivoCabecera
    @OneToMany(mappedBy = "docente")
    private List<DistributivoCabecera> distributivoCabeceras;

    //bi-directional many-to-one association to Titularidad
    @ManyToOne
    @JoinColumn(name = "id_titularidad")
    //@JoinColumn(name = "id_titularidad", referencedColumnName = "id_titularidad")
    private Titularidad titularidad;
   

    public Docente() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCedula() {
        return this.cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCelular() {
        return this.celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCodigoSenescyt() {
        return this.codigoSenescyt;
    }

    public void setCodigoSenescyt(String codigoSenescyt) {
        this.codigoSenescyt = codigoSenescyt;
    }

    public String getCorreo() {
        return this.correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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

    public Date getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

  
    public Long getIdUsuario() {
        return this.idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTituloCuartoNivel() {
        return this.tituloCuartoNivel;
    }

    public void setTituloCuartoNivel(String tituloCuartoNivel) {
        this.tituloCuartoNivel = tituloCuartoNivel;
    }

    public String getTituloTercerNivel() {
        return this.tituloTercerNivel;
    }

    public void setTituloTercerNivel(String tituloTercerNivel) {
        this.tituloTercerNivel = tituloTercerNivel;
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
        distributivoCabecera.setDocente(this);

        return distributivoCabecera;
    }

    public DistributivoCabecera removeDistributivoCabecera(DistributivoCabecera distributivoCabecera) {
        getDistributivoCabeceras().remove(distributivoCabecera);
        distributivoCabecera.setDocente(null);

        return distributivoCabecera;
    }

    public Titularidad getTitularidad() {
        return this.titularidad;
    }

    public void setTitularidad(Titularidad titularidad) {
        this.titularidad = titularidad;
    }

}
