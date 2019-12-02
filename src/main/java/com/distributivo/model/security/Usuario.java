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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
/**
 *
 * @author Toshiba
 */
@Entity
@Table(name = "segu_usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")
    , @NamedQuery(name = "Usuario.findLogin", query = "SELECT u FROM Usuario u WHERE u.nickUsua = ?1 AND u.passwordUsua  = ?2 AND u.estado = 'ACTIVO'")
    , @NamedQuery(name = "Usuario.findByCodigoUsua", query = "SELECT u FROM Usuario u WHERE u.codigoUsua = :codigoUsua")
    , @NamedQuery(name = "Usuario.findByNickUsua", query = "SELECT u FROM Usuario u WHERE u.nickUsua= ?1")
    , @NamedQuery(name = "Usuario.findByPasswordUsua", query = "SELECT u FROM Usuario u WHERE u.passwordUsua = :passwordUsua")
    , @NamedQuery(name = "Usuario.findByNombresUsua", query = "SELECT u FROM Usuario u WHERE u.nombresUsua = :nombresUsua")
    , @NamedQuery(name = "Usuario.findByApellidosUsua", query = "SELECT u FROM Usuario u WHERE u.apellidosUsua = :apellidosUsua")
    , @NamedQuery(name = "Usuario.findByEstado", query = "SELECT u FROM Usuario u WHERE u.estado = :estado")
    , @NamedQuery(name = "Usuario.findByEmailUsua", query = "SELECT u FROM Usuario u WHERE u.emailUsua = :emailUsua")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo_usua")
    private Long codigoUsua;
    @Size(max = 30)
    @Column(name = "nick_usua")
    private String nickUsua;
    @Size(max = 300)
    @Column(name = "password_usua")
    private String passwordUsua;
    @Size(max = 200)
    @Column(name = "nombres_usua")
    private String nombresUsua;
    @Size(max = 200)
    @Column(name = "apellidos_usua")
    private String apellidosUsua;
    @Size(max = 50)
    @Column(name = "estado_usua")
    private String estado;
    @Size(max = 300)
    @Column(name = "email_usua")
    private String emailUsua;
    @Column(name = "celular")
    private String celular;
    @Column(name = "foto")
    private String foto;
    @Column(name = "cedula")
    private String cedula;
    
    @JoinTable(name = "segu_usuario_rol", joinColumns = {
        @JoinColumn(name = "codigo_usua", referencedColumnName = "codigo_usua")}, inverseJoinColumns = {
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
    
    @Transient
    private String nombreCompleto;
    @Transient
    private String nombreApellido;

    public Usuario() {
    }

    public Usuario(Long codigoUsua) {
        this.codigoUsua = codigoUsua;
    }

    public Long getCodigoUsua() {
        return codigoUsua;
    }

    public void setCodigoUsua(Long codigoUsua) {
        this.codigoUsua = codigoUsua;
    }

    public String getNickUsua() {
        return nickUsua;
    }

    public void setNickUsua(String nickUsua) {
        this.nickUsua = nickUsua;
    }

    public String getPasswordUsua() {
        return passwordUsua;
    }

    public void setPasswordUsua(String passwordUsua) {
        this.passwordUsua = passwordUsua;
    }

    public String getNombresUsua() {
        return nombresUsua;
    }

    public void setNombresUsua(String nombresUsua) {
        this.nombresUsua = nombresUsua;
    }

    public String getApellidosUsua() {
        return apellidosUsua;
    }

    public void setApellidosUsua(String apellidosUsua) {
        this.apellidosUsua = apellidosUsua;
    }

    public String getEmailUsua() {
        return emailUsua;
    }

    public void setEmailUsua(String emailUsua) {
        this.emailUsua = emailUsua;
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
        hash += (codigoUsua != null ? codigoUsua.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.codigoUsua == null && other.codigoUsua != null) || (this.codigoUsua != null && !this.codigoUsua.equals(other.codigoUsua))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Usuario{" + "codigoUsua=" + codigoUsua + ", nickUsua=" + nickUsua + ", passwordUsua=" + passwordUsua + ", nombresUsua=" + nombresUsua + ", apellidosUsua=" + apellidosUsua + ", estado=" + estado + ", emailUsua=" + emailUsua + ", celular=" + celular + ", foto=" + foto + ", cedula=" + cedula + ", rolList=" + rolList + ", usuarioCreacion=" + usuarioCreacion + ", usuarioModificacion=" + usuarioModificacion + ", fechaCreacion=" + fechaCreacion + ", fechaModificacion=" + fechaModificacion + ", nombreCompleto=" + nombreCompleto + ", nombreApellido=" + nombreApellido + '}';
    }

  
    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the nombreCompleto
     */
    public String getNombreCompleto() {
        nombreCompleto = "";
        if (nombresUsua != null) {
            nombreCompleto += nombresUsua + " ";
        }
        if (apellidosUsua != null) {
            nombreCompleto += apellidosUsua;
        }
        return nombreCompleto;
    }

    /**
     * @return the nombreApellido
     */
    public String getNombreApellido() {
        nombreApellido = "";
        if (nombresUsua != null) {
            nombreApellido += nombresUsua.split(" ")[0] + " ";
        }
        if (apellidosUsua != null) {
            nombreApellido += apellidosUsua.split(" ")[0];
        }
        return nombreApellido;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getFoto() {
        if(foto==null){
            foto="noimagen.png";
        }
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
}
