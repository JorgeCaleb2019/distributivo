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
@Table(name = "segu_grupo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grupo.findAll", query = "SELECT g FROM Grupo g")
    , @NamedQuery(name = "Grupo.findByCodigoGrup", query = "SELECT g FROM Grupo g WHERE g.codigoGrup = :codigoGrup")
    , @NamedQuery(name = "Grupo.findByNombreGrup", query = "SELECT g FROM Grupo g WHERE g.nombreGrup = :nombreGrup")
    , @NamedQuery(name = "Grupo.findByDesripcionGrup", query = "SELECT g FROM Grupo g WHERE g.desripcionGrup = :desripcionGrup")
    , @NamedQuery(name = "Grupo.findByEstadoGrup", query = "SELECT g FROM Grupo g WHERE g.estado = :estado")})
public class Grupo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo_grup")
    private Integer codigoGrup;
    @Size(max = 200)
    @Column(name = "nombre_grup")
    private String nombreGrup;
    @Size(max = 300)
    @Column(name = "desripcion_grup")
    private String desripcionGrup;
    @Size(max = 50)
    @Column(name = "estado_grup")
    private String estado;
    @JoinTable(name = "segu_grupo_permiso", joinColumns = {
        @JoinColumn(name = "codigo_grup", referencedColumnName = "codigo_grup")}, inverseJoinColumns = {
        @JoinColumn(name = "codigo_perm", referencedColumnName = "codigo_perm")})
    @ManyToMany
    private List<Permiso> permisoList;
    @JoinTable(name = "segu_rol_grupo", joinColumns = {
        @JoinColumn(name = "codigo_grup", referencedColumnName = "codigo_grup")}, inverseJoinColumns = {
        @JoinColumn(name = "codigo_rol", referencedColumnName = "codigo_rol")})
    @ManyToMany
    private List<Rol> rolList;
    
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

    public Grupo() {
    }

    public Grupo(Integer codigoGrup) {
        this.codigoGrup = codigoGrup;
    }

    public Integer getCodigoGrup() {
        return codigoGrup;
    }

    public void setCodigoGrup(Integer codigoGrup) {
        this.codigoGrup = codigoGrup;
    }

    public String getNombreGrup() {
        return nombreGrup;
    }

    public void setNombreGrup(String nombreGrup) {
        this.nombreGrup = nombreGrup;
    }

    public String getDesripcionGrup() {
        return desripcionGrup;
    }

    public void setDesripcionGrup(String desripcionGrup) {
        this.desripcionGrup = desripcionGrup;
    }


    @XmlTransient
    public List<Permiso> getPermisoList() {
        return permisoList;
    }

    public void setPermisoList(List<Permiso> permisoList) {
        this.permisoList = permisoList;
    }

    @XmlTransient
    public List<Rol> getRolList() {
        return rolList;
    }

    public void setRolList(List<Rol> rolList) {
        this.rolList = rolList;
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
        hash += (codigoGrup != null ? codigoGrup.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grupo)) {
            return false;
        }
        Grupo other = (Grupo) object;
        if ((this.codigoGrup == null && other.codigoGrup != null) || (this.codigoGrup != null && !this.codigoGrup.equals(other.codigoGrup))) {
            return false;
        }
        return true;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Grupo{" + "codigoGrup=" + codigoGrup + ", nombreGrup=" + nombreGrup + ", desripcionGrup=" + desripcionGrup + ", estado=" + estado + ", permisoList=" + permisoList + ", rolList=" + rolList + ", usuarioCreacion=" + usuarioCreacion + ", usuarioModificacion=" + usuarioModificacion + ", fechaCreacion=" + fechaCreacion + ", fechaModificacion=" + fechaModificacion + '}';
    }
    
    
    
}
