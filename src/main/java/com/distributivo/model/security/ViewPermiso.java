package com.distributivo.model.security;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Toshiba
 */
@Entity
@Table(name = "segu_view_permiso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ViewPermiso.findAll", query = "SELECT v FROM ViewPermiso v")
    , @NamedQuery(name = "ViewPermiso.findByCodigoUsua", query = "SELECT v FROM ViewPermiso v WHERE v.id.codigoUsua = :codigoUsua")
    , @NamedQuery(name = "ViewPermiso.findByNickUsua", query = "SELECT v FROM ViewPermiso v WHERE v.nickUsua = ?1")
    , @NamedQuery(name = "ViewPermiso.findPermPadre", query = "SELECT v FROM ViewPermiso v WHERE v.nickUsua = ?1 AND v.codigoPadrePerm is null")
    , @NamedQuery(name = "ViewPermiso.findPermHijo", query = "SELECT v FROM ViewPermiso v WHERE v.nickUsua = ?1 AND v.codigoPadrePerm = ?2")
    , @NamedQuery(name = "ViewPermiso.findByNombreRol", query = "SELECT v FROM ViewPermiso v WHERE v.nombreRol = :nombreRol")
    , @NamedQuery(name = "ViewPermiso.findByNombrePerm", query = "SELECT v FROM ViewPermiso v WHERE v.nombrePerm = :nombrePerm")
    , @NamedQuery(name = "ViewPermiso.findByIconoPerm", query = "SELECT v FROM ViewPermiso v WHERE v.iconoPerm = :iconoPerm")
    , @NamedQuery(name = "ViewPermiso.findByCodigoTiper", query = "SELECT v FROM ViewPermiso v WHERE v.codigoTiper = :codigoTiper")
    , @NamedQuery(name = "ViewPermiso.findByCodigoPadrePerm", query = "SELECT v FROM ViewPermiso v WHERE v.codigoPadrePerm = :codigoPadrePerm")
    , @NamedQuery(name = "ViewPermiso.findByDetallePerm", query = "SELECT v FROM ViewPermiso v WHERE v.detallePerm = :detallePerm")
    , @NamedQuery(name = "ViewPermiso.findByNombreTiper", query = "SELECT v FROM ViewPermiso v WHERE v.nombreTiper = :nombreTiper")
    , @NamedQuery(name = "ViewPermiso.findByCodigoPerm", query = "SELECT v FROM ViewPermiso v WHERE v.id.codigoPerm = :codigoPerm")
    , @NamedQuery(name = "ViewPermiso.findByAppPermiso", query = "SELECT v FROM ViewPermiso v WHERE v.nombreTiper = ?1")
})
public class ViewPermiso implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    private ViewPermisoPK id;
    @Size(max = 30)
    @Column(name = "nick_usua")
    private String nickUsua;
    @Size(max = 200)
    @Column(name = "nombre_rol")
    private String nombreRol;
    @Size(max = 300)
    @Column(name = "nombre_perm")
    private String nombrePerm;
    @Size(max = 100)
    @Column(name = "icono_perm")
    private String iconoPerm;
    @Column(name = "codigo_tiper")
    private Integer codigoTiper;
    @Column(name = "codigo_padre_perm")
    private Integer codigoPadrePerm;
    @Size(max = 300)
    @Column(name = "detalle_perm")
    private String detallePerm;
    @Size(max = 200)
    @Column(name = "nombre_tiper")
    private String nombreTiper;
    @Column(name = "posicion_perm")
    private Integer posicionPerm;
    @Transient
    private List<ViewPermiso> viewPermisoList;

    public ViewPermiso() {
    }


    public String getNickUsua() {
        return nickUsua;
    }

    public void setNickUsua(String nickUsua) {
        this.nickUsua = nickUsua;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
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

    public Integer getCodigoTiper() {
        return codigoTiper;
    }

    public void setCodigoTiper(Integer codigoTiper) {
        this.codigoTiper = codigoTiper;
    }

    public Integer getCodigoPadrePerm() {
        return codigoPadrePerm;
    }

    public void setCodigoPadrePerm(Integer codigoPadrePerm) {
        this.codigoPadrePerm = codigoPadrePerm;
    }

    public String getDetallePerm() {
        return detallePerm;
    }

    public void setDetallePerm(String detallePerm) {
        this.detallePerm = detallePerm;
    }

    public String getNombreTiper() {
        return nombreTiper;
    }

    public void setNombreTiper(String nombreTiper) {
        this.nombreTiper = nombreTiper;
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

    /**
     * @return the viewPermisoList
     */
    public List<ViewPermiso> getViewPermisoList() {
        return viewPermisoList;
    }

    /**
     * @param viewPermisoList the viewPermisoList to set
     */
    public void setViewPermisoList(List<ViewPermiso> viewPermisoList) {
        this.viewPermisoList = viewPermisoList;
    }

    public ViewPermisoPK getId() {
        return id;
    }

    public void setId(ViewPermisoPK id) {
        this.id = id;
    }

}
