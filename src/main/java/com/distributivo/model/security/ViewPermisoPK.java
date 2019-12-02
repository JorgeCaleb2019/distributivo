/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.model.security;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ViewPermisoPK implements Serializable{
    @Column(name = "codigo_perm")
    private Integer codigoPerm;
    @Column(name = "codigo_usua")
    private Long codigoUsua;

    public ViewPermisoPK() {
    }
    
    
    public Long getCodigoUsua() {
        return codigoUsua;
    }

    public void setCodigoUsua(Long codigoUsua) {
        this.codigoUsua = codigoUsua;
    }
    
    public Integer getCodigoPerm() {
        return codigoPerm;
    }

    public void setCodigoPerm(Integer codigoPerm) {
        this.codigoPerm = codigoPerm;
    }

}
