/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.utilities.base;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Fernando
 */
public class ItemGrafico implements Serializable {

    private String key;
    private Number valor;

    public ItemGrafico() {
    }

    public ItemGrafico(String key, Number valor) {
        this.key = key;
        BigDecimal bd = new BigDecimal(valor.doubleValue());
        bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
        this.valor = bd;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Number getValor() {
        return valor;
    }

    public void setValor(Number valor) {
        this.valor = valor;
    }

}
