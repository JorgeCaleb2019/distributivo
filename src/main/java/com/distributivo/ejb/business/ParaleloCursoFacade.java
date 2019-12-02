/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.ejb.business;

import com.distributivo.model.business.Materia;
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
public class ParaleloCursoFacade extends AbstractFacade<ParaleloCurso> {

    @PersistenceContext(unitName = "distributivoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ParaleloCursoFacade() {
        super(ParaleloCurso.class);
    }

    @Override
    public InfoTable<ParaleloCurso> loadTable(Integer first, Integer pageSize, List<String> search, String addquery, Map<String, Object> parametros) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT u FROM Grupo u ");
        if (search.size() > 0) {
            sb.append("WHERE ");
            sb.append("(");
            for (int i = 0; i < search.size(); i++) {
                sb.append("(");
                sb.append(" UPPER(u.nombreGrup) like :dato").append(i);
                sb.append(" OR UPPER(u.desripcionGrup) like :dato").append(i);
                sb.append(" OR UPPER(u.estado) like :dato").append(i);
                sb.append(")");
                if ((i + 1) != search.size()) {
                    sb.append(" OR ");
                }
            }
            sb.append(")");
        }

        Query q = getEntityManager().createQuery(sb.toString());
        Query q1 = getEntityManager().createQuery(sb.toString().replace("SELECT u", "SELECT COUNT(u)"));
        for (int i = 0; i < search.size(); i++) {
            StringBuilder par = new StringBuilder();
            par.append("dato").append(i);
            q.setParameter(par.toString(), ("%" + search.get(i).toUpperCase() + "%"));
            q1.setParameter(par.toString(), ("%" + search.get(i).toUpperCase() + "%"));
        }
        q.setFirstResult(first);
        q.setMaxResults(pageSize);

        InfoTable<ParaleloCurso> info = new InfoTable();
        info.setList(q.getResultList());
        info.setCount((Long) q1.getSingleResult());
        System.out.println("SQL:" + sb.toString());
        return info;
    }

    public List<ParaleloCurso> buscarParaleloPorCurso(Integer idCurso) {
        Query q = getEntityManager().createQuery("SELECT p FROM ParaleloCurso p WHERE p.curso.idCurso = " + idCurso);
        return q.getResultList();
    }

    public List<ParaleloCurso> buscarParaleloPorCodigo(Integer idParaleloCurso) {
        Query q = getEntityManager().createQuery("SELECT p FROM ParaleloCurso p WHERE p.idCursoParalelo = " + idParaleloCurso);
        return q.getResultList();
    }

    public ParaleloCurso buscarCursoParalelo(Integer idCurso, Integer idParalelo) {
        try {
            Query q = getEntityManager().createQuery("SELECT p FROM ParaleloCurso p WHERE p.curso.idCurso = " + idCurso + " and p.paralelo.idParalelo = " + idParalelo);
            return (ParaleloCurso) q.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }
}
