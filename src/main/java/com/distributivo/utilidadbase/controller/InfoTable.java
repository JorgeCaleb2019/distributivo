/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.utilidadbase.controller;

import java.util.List;

/**
 *
 * @author Fernando
 */
public class InfoTable<T> {
    
    private Long count;
    private List<T> list;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
    
    
}
