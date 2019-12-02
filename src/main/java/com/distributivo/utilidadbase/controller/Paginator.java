/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.utilidadbase.controller;

import java.io.Serializable;

/**
 *
 * @author Fernando
 */
public class Paginator implements Serializable{
    
    private Integer first;
    private Integer pageSize;
    private Long totalRows;
    private Integer totalPages;
    
    private Integer rowFirst;
    private Integer rows;

    public Paginator() {
        this.first=0;
        this.pageSize=50;
    }

    public Paginator(Integer first, Integer pageSize, Long totalRows) {
        this.first = first;
        this.pageSize = pageSize;
        this.totalRows = totalRows;
    }
    
    public Integer getFirst() {
        if(first==null){
            first=0;
        }
        return first;
    }

    public void setFirst(Integer first) {
        this.first = first;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Long totalRows) {
        this.totalRows = totalRows;
    }

    public Integer getTotalPages() {
        if(totalRows!=null && pageSize!=null){
          Double aux=totalRows.doubleValue()/pageSize.doubleValue();
          if(aux>aux.intValue()){
              totalPages=aux.intValue()+1;
          }else{
              totalPages=aux.intValue();
          }
        }
        return totalPages;
    }
    
    public void next(){
        first++;
        if(first>=getTotalPages()){
            first--;
        }
    }
    
    public void back(){
        first--;
        if(first<0){
            first++;
        }
    }

    public Integer getRowFirst() {
        rowFirst=getFirst()*getPageSize();
        return rowFirst;
    }

    public Integer getRows() {
        if(getTotalRows()<getPageSize()){
            rows=getTotalRows().intValue();
        }else{
            rows=getPageSize();
        }
        return rows;
    }
    
}
