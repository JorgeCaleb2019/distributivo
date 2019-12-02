package com.distributivo.model.security;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "segu_permiso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Permiso.findAll", query = "SELECT p FROM Permiso p")
    , @NamedQuery(name = "Permiso.findByPadre", query = "SELECT p FROM Permiso p WHERE p.codigoPadrePerm is null AND p.estado = 'ACTIVO'")    
    , @NamedQuery(name = "Permiso.findByCodigoPerm", query = "SELECT p FROM Permiso p WHERE p.codigoPerm = :codigoPerm")
    , @NamedQuery(name = "Permiso.findByNombrePerm", query = "SELECT p FROM Permiso p WHERE p.nombrePerm = :nombrePerm")
    , @NamedQuery(name = "Permiso.findByIconoPerm", query = "SELECT p FROM Permiso p WHERE p.iconoPerm = :iconoPerm")
    , @NamedQuery(name = "Permiso.findByEstado", query = "SELECT p FROM Permiso p WHERE p.estado = :estado")
    , @NamedQuery(name = "Permiso.findByDetallePerm", query = "SELECT p FROM Permiso p WHERE p.detallePerm = :detallePerm")})
public class Permiso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo_perm")
    private Integer codigoPerm;
    @Size(max = 300)
    @Column(name = "nombre_perm")
    private String nombrePerm;
    @Size(max = 100)
    @Column(name = "icono_perm")
    private String iconoPerm;
    @Size(max = 50)
    @Column(name = "estado_perm")
    private String estado;
    @Column(name = "posicion_perm")
    private Integer posicionPerm;
    @Size(max = 300)
    @Column(name = "detalle_perm")
    private String detallePerm;
    @ManyToMany(mappedBy = "permisoList")
    private List<Grupo> grupoList;
    @OneToMany(mappedBy = "codigoPadrePerm")
    private List<Permiso> permisoList;
    
    @JoinColumn(name = "codigo_padre_perm", referencedColumnName = "codigo_perm")
    @ManyToOne
    private Permiso codigoPadrePerm;
    
    @JoinColumn(name = "codigo_tiper", referencedColumnName = "codigo_tiper")
    @ManyToOne
    private TipoPermiso codigoTiper;
    
    
    
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

    public Permiso() {
    }

    public Permiso(Integer codigoPerm) {
        this.codigoPerm = codigoPerm;
    }

    public Integer getCodigoPerm() {
        return codigoPerm;
    }

    public void setCodigoPerm(Integer codigoPerm) {
        this.codigoPerm = codigoPerm;
    }

    public String getNombrePerm() {
        return nombrePerm;
    }

    public void setNombrePerm(String nombrePerm) {
        this.nombrePerm = nombrePerm;
    }

    public String getIconoPerm() {
        return iconoPerm;
    }

    public void setIconoPerm(String iconoPerm) {
        this.iconoPerm = iconoPerm;
    }

    public String getDetallePerm() {
        return detallePerm;
    }

    public void setDetallePerm(String detallePerm) {
        this.detallePerm = detallePerm;
    }

    @XmlTransient
    public List<Grupo> getGrupoList() {
        return grupoList;
    }

    public void setGrupoList(List<Grupo> grupoList) {
        this.grupoList = grupoList;
    }

    @XmlTransient
    public List<Permiso> getPermisoList() {
        return permisoList;
    }

    public void setPermisoList(List<Permiso> permisoList) {
        this.permisoList = permisoList;
    }

    public Permiso getCodigoPadrePerm() {
        return codigoPadrePerm;
    }

    public void setCodigoPadrePerm(Permiso codigoPadrePerm) {
        this.codigoPadrePerm = codigoPadrePerm;
    }

    public TipoPermiso getCodigoTiper() {
        return codigoTiper;
    }

    public void setCodigoTiper(TipoPermiso codigoTiper) {
        this.codigoTiper = codigoTiper;
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
        hash += (codigoPerm != null ? codigoPerm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permiso)) {
            return false;
        }
        Permiso other = (Permiso) object;
        if ((this.codigoPerm == null && other.codigoPerm != null) || (this.codigoPerm != null && !this.codigoPerm.equals(other.codigoPerm))) {
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

    /**
     * @return the posicionPerm
     */
    public Integer getPosicionPerm() {
        return posicionPerm;
    }

    /**
     * @param posicionPerm the posicionPerm to set
     */
    public void setPosicionPerm(Integer posicionPerm) {
        this.posicionPerm = posicionPerm;
    }

    @Override
    public String toString() {
        return "Permiso{" + "codigoPerm=" + codigoPerm + ", nombrePerm=" + nombrePerm + ", iconoPerm=" + iconoPerm + ", estado=" + estado + ", posicionPerm=" + posicionPerm + ", detallePerm=" + detallePerm + ", grupoList=" + grupoList + ", permisoList=" + permisoList + ", codigoPadrePerm=" + codigoPadrePerm + ", codigoTiper=" + codigoTiper + ", usuarioCreacion=" + usuarioCreacion + ", usuarioModificacion=" + usuarioModificacion + ", fechaCreacion=" + fechaCreacion + ", fechaModificacion=" + fechaModificacion + '}';
    }
    
    

}
