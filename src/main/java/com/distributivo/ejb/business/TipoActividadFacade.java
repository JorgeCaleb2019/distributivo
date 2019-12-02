package com.distributivo.ejb.business;

import com.distributivo.model.business.TipoActividad;
import com.distributivo.model.security.Grupo;
import com.distributivo.model.security.Usuario;
import com.distributivo.utilidadbase.controller.InfoTable;
import com.tesis.utilidadbase.facade.AbstractFacade;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class TipoActividadFacade extends AbstractFacade<TipoActividad> {

    @PersistenceContext(unitName = "distributivoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoActividadFacade() {
        super(TipoActividad.class);
    }

    @Override
    public InfoTable<TipoActividad> loadTable(Integer first, Integer pageSize, List<String> search, String addquery, Map<String, Object> parametros) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT t FROM TipoActividad t where t.estado = 'ACTIVO'");

        Query q = getEntityManager().createQuery(sb.toString());
        Query q1 = getEntityManager().createQuery(sb.toString().replace("SELECT t", "SELECT COUNT(t)"));
        for (int i = 0; i < search.size(); i++) {
            StringBuilder par = new StringBuilder();
            par.append("dato").append(i);
            q.setParameter(par.toString(), ("%" + search.get(i).toUpperCase() + "%"));
            q1.setParameter(par.toString(), ("%" + search.get(i).toUpperCase() + "%"));
        }
        q.setFirstResult(first);
        q.setMaxResults(pageSize);

        InfoTable<TipoActividad> info = new InfoTable();
        info.setList(q.getResultList());
        info.setCount((Long) q1.getSingleResult());
        System.out.println("SQL:" + sb.toString());
        //Collections.sort(info);
        return info;
    }

    public List<TipoActividad> buscarPorCoincidencia(String filter) {
        Query q = getEntityManager().createQuery("SELECT t FROM TipoActividad t");
        return q.getResultList();
    }

    public List<TipoActividad> buscarPorEstado() {
        Query q = getEntityManager().createQuery("SELECT t FROM TipoActividad t where t.estado = 'ACTIVO'");
        return q.getResultList();
    }

    public TipoActividad buscarCodigo(Integer codigo) {
        try {
            Query q = getEntityManager().createQuery("SELECT t FROM TipoActividad t where t.idTipoActividad = " + codigo);
            return (TipoActividad) q.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }
}
