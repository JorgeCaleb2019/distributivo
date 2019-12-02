/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.ejb.business;

import com.distributivo.model.business.Curso;
import com.distributivo.model.business.Paralelo;
import com.distributivo.model.business.ParaleloCurso;
import com.tesis.utilidadbase.facade.AbstractFacade;
import com.distributivo.utilidadbase.controller.InfoTable;
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
public class ParaleloFacade extends AbstractFacade<Paralelo> {

    @PersistenceContext(unitName = "distributivoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ParaleloFacade() {
        super(Paralelo.class);
    }

    @Override
    public InfoTable<Paralelo> loadTable(Integer first, Integer pageSize, List<String> search, String addquery, Map<String, Object> parametros) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT p FROM Paralelo p where p.estado = 'ACTIVO'");
        

        Query q = getEntityManager().createQuery(sb.toString());
        Query q1 = getEntityManager().createQuery(sb.toString().replace("SELECT p", "SELECT COUNT(p)"));
        for (int i = 0; i < search.size(); i++) {
            StringBuilder par = new StringBuilder();
            par.append("dato").append(i);
            q.setParameter(par.toString(), ("%" + search.get(i).toUpperCase() + "%"));
            q1.setParameter(par.toString(), ("%" + search.get(i).toUpperCase() + "%"));
        }
        q.setFirstResult(first);
        q.setMaxResults(pageSize);

        InfoTable<Paralelo> info = new InfoTable();
        info.setList(q.getResultList());
        info.setCount((Long) q1.getSingleResult());
        System.out.println("SQL:" + sb.toString());
        return info;
    }

    public List<Paralelo> buscarPorCoincidencia(String filter) {
        filter = "%" + filter + "%";
        Query q = getEntityManager().createQuery("SELECT l FROM Paralelo l WHERE UPPER(l.nombre) like UPPER(:dato) AND l.estado='ACTIVO'");
        q.setParameter("dato", filter);
        q.setMaxResults(10);
        return q.getResultList();
    }
}
