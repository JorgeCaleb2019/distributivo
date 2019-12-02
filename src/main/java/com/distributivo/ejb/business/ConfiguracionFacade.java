package com.distributivo.ejb.business;

import com.distributivo.model.business.Configuracion;
import com.distributivo.utilidadbase.controller.InfoTable;
import com.tesis.utilidadbase.facade.AbstractFacade;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ConfiguracionFacade extends AbstractFacade<Configuracion> {

    @PersistenceContext(unitName = "distributivoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConfiguracionFacade() {
        super(Configuracion.class);
    }

    @Override
    public InfoTable<Configuracion> loadTable(Integer first, Integer pageSize, List<String> search, String addquery, Map<String, Object> parametros) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c FROM Configuracion c where c.estado = 'A'");

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

        InfoTable<Configuracion> info = new InfoTable();
        info.setList(q.getResultList());
        info.setCount((Long) q1.getSingleResult());
        System.out.println("SQL:" + sb.toString());
        //Collections.sort(info);
        return info;
    }

    public Configuracion buscarConfiguracion() {
        try {
            Query q = getEntityManager().createQuery("SELECT c FROM Configuracion c WHERE c.estado = 'A'");
            return (Configuracion) q.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }
}
