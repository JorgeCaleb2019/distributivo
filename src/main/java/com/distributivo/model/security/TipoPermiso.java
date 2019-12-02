/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.model.security;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Toshiba
 */
@Entity
@Table(name = "segu_tipo_permiso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoPermiso.findAll", query = "SELECT t FROM TipoPermiso t")
    , @NamedQuery(name = "TipoPermiso.findByCodigoTiper", query = "SELECT t FROM TipoPermiso t WHERE t.codigoTiper = :codigoTiper")
    , @NamedQuery(name = "TipoPermiso.findByNombreTiper", query = "SELECT t FROM TipoPermiso t WHERE t.nombreTiper = :nombreTiper")
    , @NamedQuery(name = "TipoPermiso.findByEstado", query = "SELECT t FROM TipoPermiso t WHERE t.estado = :estado")})
public class TipoPermiso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo_tiper")
    private Integer codigoTiper;
    @Size(max = 200)
    @Column(name = "nombre_tiper")
    private String nombreTiper;
    @Size(max = 50)
    @Column(name = "estado_tiper")
    private String estado;
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

    public TipoPermiso() {
    }

    public TipoPermiso(Integer codigoTiper) {
        this.codigoTiper = codigoTiper;
    }

    public Integer getCodigoTiper() {
        return codigoTiper;
    }

    public void setCodigoTiper(Integer codigoTiper) {
        this.codigoTiper = codigoTiper;
    }

    public String getNombreTiper() {
        return nombreTiper;
    }

    public void setNombreTiper(String nombreTiper) {
        this.nombreTiper = nombreTiper;
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
        hash += (codigoTiper != null ? codigoTiper.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoPermiso)) {
            return false;
        }
        TipoPermiso other = (TipoPermiso) object;
        if ((this.codigoTiper == null && other.codigoTiper != null) || (this.codigoTiper != null && !this.codigoTiper.equals(other.codigoTiper))) {
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
        return "TipoPermiso{" + "codigoTiper=" + codigoTiper + ", nombreTiper=" + nombreTiper + ", estado=" + estado + ", usuarioCreacion=" + usuarioCreacion + ", usuarioModificacion=" + usuarioModificacion + ", fechaCreacion=" + fechaCreacion + ", fechaModificacion=" + fechaModificacion + '}';
    }
    
    
}
