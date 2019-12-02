package com.distributivo.ejb.business;

import com.distributivo.model.business.Dedicacion;
import com.distributivo.utilidadbase.controller.InfoTable;
import com.tesis.utilidadbase.facade.AbstractFacade;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class DedicacionFacade extends AbstractFacade<Dedicacion> {

    @PersistenceContext(unitName = "distributivoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DedicacionFacade() {
        super(Dedicacion.class);
    }

    /*@Override
     public InfoTable<Dedicacion> loadTable(Integer first, Integer pageSize, List<String> params, String addquery, Map<String, Object> parametros) {
     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }*/
    @Override
    public InfoTable<Dedicacion> loadTable(Integer first, Integer pageSize, List<String> search, String addquery, Map<String, Object> parametros) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT p FROM Dedicacion p where p.estado = 'ACTIVO'");

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

        InfoTable<Dedicacion> info = new InfoTable();
        info.setList(q.getResultList());
        info.setCount((Long) q1.getSingleResult());
        System.out.println("SQL:" + sb.toString());
        return info;
    }

    public List<Dedicacion> buscarPorCoincidencia(String filter) {
        Query q = getEntityManager().createQuery("SELECT l FROM dedicacion l WHERE UPPER(l.nombre) like UPPER(:dato) AND l.estado='A'");
        return q.getResultList();
    }

    public List<Dedicacion> buscarPorCodigo(Integer codigo) {
        Query q = getEntityManager().createQuery("SELECT d FROM Dedicacion d WHERE d.id = " + codigo);
        return q.getResultList();
    }

    public Dedicacion buscarPorCodigoDedicacion(Integer codigo) {
        try {
            Query q = getEntityManager().createQuery("SELECT d FROM Dedicacion d WHERE d.id = " + codigo);
            System.out.println("cadena " + codigo);
            return (Dedicacion) q.getSingleResult();
        } catch (Exception ex) {
            return null;
        }

    }
}
