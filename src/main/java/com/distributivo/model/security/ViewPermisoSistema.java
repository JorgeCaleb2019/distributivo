/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.model.security;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Toshiba
 */
@Entity
@Table(name = "segu_view_permiso_sistema")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ViewPermisoSistema.findAll", query = "SELECT v FROM ViewPermisoSistema v")
    , @NamedQuery(name = "ViewPermisoSistema.findByNombreRol", query = "SELECT v FROM ViewPermisoSistema v WHERE v.nombreRol = ?1")
    , @NamedQuery(name = "ViewPermisoSistema.findByNombrePerm", query = "SELECT v FROM ViewPermisoSistema v WHERE v.nombrePerm = :nombrePerm")
    , @NamedQuery(name = "ViewPermisoSistema.findByDetallePerm", query = "SELECT v FROM ViewPermisoSistema v WHERE v.detallePerm = :detallePerm")
    , @NamedQuery(name = "ViewPermisoSistema.findByCodigoPerm", query = "SELECT v FROM ViewPermisoSistema v WHERE v.codigoPerm = :codigoPerm")})
public class ViewPermisoSistema implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "codigo_perm")
    private Integer codigoPerm;
    @Size(max = 200)
    @Column(name = "nombre_rol")
    private String nombreRol;
    @Size(max = 300)
    @Column(name = "nombre_perm")
    private String nombrePerm;
    @Size(max = 300)
    @Column(name = "detalle_perm")
    private String detallePerm;

    public ViewPermisoSistema() {
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

    public String getDetallePerm() {
        return detallePerm;
    }

    public void setDetallePerm(String detallePerm) {
        this.detallePerm = detallePerm;
    }

    public Integer getCodigoPerm() {
        return codigoPerm;
    }

    public void setCodigoPerm(Integer codigoPerm) {
        this.codigoPerm = codigoPerm;
    }

}
