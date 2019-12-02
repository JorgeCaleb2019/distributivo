/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.ejb.business;

import com.distributivo.model.business.Titularidad;import com.distributivo.utilidadbase.controller.InfoTable;
;
import com.tesis.utilidadbase.facade.AbstractFacade;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 *
 * @author BYRON
 */
@Stateless
public class TitularidadFacade extends AbstractFacade<Titularidad>{

    @PersistenceContext(unitName = "distributivoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TitularidadFacade() {
        super(Titularidad.class);
    }

    /*@Override
    public InfoTable<Titularidad> loadTable(Integer first, Integer pageSize, List<String> params, String addquery, Map<String, Object> parametros) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/

    
    @Override
    public InfoTable<Titularidad> loadTable(Integer first, Integer pageSize, List<String> search, String addquery, Map<String, Object> parametros) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c FROM Titularidad c where c.estado = 'ACTIVO'");
        Query q = getEntityManager().createQuery(sb.toString());
        Query q1 = getEntityManager().createQuery(sb.toString().replace("SELECT c", "SELECT COUNT(c)"));
        for (int i = 0; i < search.size(); i++) {
            StringBuilder par = new StringBuilder();
            par.append("dato").append(i);
            q.setParameter(par.toString(), ("%" + search.get(i).toUpperCase() + "%"));
            q1.setParameter(par.toString(), ("%" + search.get(i).toUpperCase() + "%"));
        }
        q.setFirstResult(first);
        q.setMaxResults(pageSize);

        InfoTable<Titularidad> info = new InfoTable();
        info.setList(q.getResultList());
        info.setCount((Long) q1.getSingleResult());
        System.out.println("SQL:" + sb.toString());
        return info;
    }

     public List<Titularidad> buscarPorCoincidencia(String filter) {
        filter = "%" + filter + "%";
        Query q = getEntityManager().createQuery("SELECT l FROM Titularidad l WHERE UPPER(l.nombre) like UPPER(:dato) AND l.estado='ACTIVO'");
        q.setParameter("dato", filter);
        q.setMaxResults(10);
        return q.getResultList();
    }
   
    
    public List<Titularidad> buscarPorCodigo(Integer codigo) {
        Query q = getEntityManager().createQuery("SELECT l FROM Titularidad l WHERE l.idTitularidad =  " + codigo + " AND l.estado='ACTIVO'");
        q.setMaxResults(10);
        return q.getResultList();
    }
    
    
}
