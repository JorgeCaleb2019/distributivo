/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.model.security;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Toshiba
 */
@Entity
@Table(name = "segu_rol")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rol.findAll", query = "SELECT r FROM Rol r")
    , @NamedQuery(name = "Rol.findRol", query = "SELECT DISTINCT r FROM Rol r WHERE r.estado = :estado")
    , @NamedQuery(name = "Rol.findByCodigoRol", query = "SELECT r FROM Rol r WHERE r.codigoRol = :codigoRol")
    , @NamedQuery(name = "Rol.findByNombreRol", query = "SELECT r FROM Rol r WHERE r.nombreRol = :nombreRol")
    , @NamedQuery(name = "Rol.findByDescripcionRol", query = "SELECT r FROM Rol r WHERE r.descripcionRol = :descripcionRol")
    , @NamedQuery(name = "Rol.findByEstado", query = "SELECT r FROM Rol r WHERE r.estado = :estado")})
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo_rol")
    private Integer codigoRol;
    @Size(max = 200)
    @Column(name = "nombre_rol")
    private String nombreRol;
    @Size(max = 300)
    @Column(name = "descripcion_rol")
    private String descripcionRol;
    @Size(max = 50)
    @Column(name = "estado_rol")
    private String estado;
    
    
    @JoinTable(name = "segu_usuario_rol", joinColumns = {
        @JoinColumn(name = "codigo_rol", referencedColumnName = "codigo_rol")}, inverseJoinColumns = {
        @JoinColumn(name = "codigo_usua", referencedColumnName = "codigo_usua")})
    @ManyToMany
    private List<Usuario> usuarioList;
    
    
    @JoinTable(name = "segu_rol_grupo", joinColumns = {
        @JoinColumn(name = "codigo_rol", referencedColumnName = "codigo_rol")}, inverseJoinColumns = {
        @JoinColumn(name = "codigo_grup", referencedColumnName = "codigo_grup")})
    @ManyToMany
    private List<Grupo> grupoList;
    @Size(max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Size(max = 100)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_modificacion")
    private Date fechaModificacion;

    public Rol() {
    }

    public Rol(Integer codigoRol) {
        this.codigoRol = codigoRol;
    }

    public Integer getCodigoRol() {
        return codigoRol;
    }

    public void setCodigoRol(Integer codigoRol) {
        this.codigoRol = codigoRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getDescripcionRol() {
        return descripcionRol;
    }

    public void setDescripcionRol(String descripcionRol) {
        this.descripcionRol = descripcionRol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    @XmlTransient
    public List<Grupo> getGrupoList() {
        return grupoList;
    }

    public void setGrupoList(List<Grupo> grupoList) {
        this.grupoList = grupoList;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoRol != null ? codigoRol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rol)) {
            return false;
        }
        Rol other = (Rol) object;
        if ((this.codigoRol == null && other.codigoRol != null) || (this.codigoRol != null && !this.codigoRol.equals(other.codigoRol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Rol{" + "codigoRol=" + codigoRol + ", nombreRol=" + nombreRol + ", descripcionRol=" + descripcionRol + ", estado=" + estado + ", usuarioList=" + usuarioList + ", grupoList=" + grupoList + ", usuarioCreacion=" + usuarioCreacion + ", usuarioModificacion=" + usuarioModificacion + ", fechaCreacion=" + fechaCreacion + ", fechaModificacion=" + fechaModificacion + '}';
    }

    
}
