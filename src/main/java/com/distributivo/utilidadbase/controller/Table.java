/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.utilidadbase.controller;

import com.tesis.utilidadbase.facade.AbstractFacade;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Fernando
 */
public class Table<T> implements Serializable {

    private AbstractFacade facade;
    private boolean kanban = false;
    private boolean table = true;
    private Paginator paginator;
    private List<T> listItems;
    private List<String> searchKeys;
    private String addquery;
    private Map<String,Object> parametros;

    public Table() {
    }

    public Table(AbstractFacade facade) {
        this.facade = facade;
        this.load();
        
    }
    public Table(AbstractFacade facade,String addquery) {
        this.facade = facade;
        this.addquery=addquery;
        this.load();
        
    }
    public void activeKanban() {
        this.setKanban(true);
        this.setTable(false);
    }

    public void activeList() {
        this.setKanban(false);
        this.setTable(true);
    }
    public void load(){
        InfoTable<T> info=this.facade.loadTable(getPaginator().getRowFirst(),getPaginator().getPageSize(),getSearchKeys(),addquery,parametros);
        this.listItems=info.getList();
        this.paginator.setTotalRows(info.getCount());
        System.out.println(this.toString());
        System.out.println("hola");
    }
    public AbstractFacade getFacade() {
        return facade;
    }

    public void setFacade(AbstractFacade facade) {
        this.facade = facade;
    }

    public boolean isKanban() {
        return kanban;
    }

    public void setKanban(boolean kanban) {
        this.kanban = kanban;
    }

    public boolean isTable() {
        return table;
    }

    public void setTable(boolean table) {
        this.table = table;
    }

    public Paginator getPaginator() {
        if(paginator==null){
            paginator=new Paginator();
        }
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public List<T> getListItems() {
        return listItems;
    }

    public List<String> getSearchKeys() {
        if(searchKeys==null){
            searchKeys=new LinkedList<>();
        }
        return searchKeys;
    }

    public void setSearchKeys(List<String> searchKeys) {
        this.searchKeys = searchKeys;
    }

    @Override
    public String toString() {
        return "Table{" + ", kanban=" + kanban + ", table=" + table + ", paginator=" + getPaginator().getTotalRows()+"::first"+getPaginator().getFirst()+"::pagesize"+getPaginator().getPageSize() + ", listItems=" + getListItems().size() + ", searchKeys=" + getSearchKeys() + '}';
    }

    public String getAddquery() {
        return addquery;
    }

    public void setAddquery(String addquery) {
        this.addquery = addquery;
    }

    public Map<String,Object> getParametros() {
        if(parametros==null){
            parametros=new HashMap<>();
        }
        return parametros;
    }

    public void setParametros(Map<String,Object> parametros) {
        this.parametros = parametros;
    }

}
