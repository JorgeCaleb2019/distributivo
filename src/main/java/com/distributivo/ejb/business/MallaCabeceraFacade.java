package com.distributivo.ejb.business;

import com.distributivo.model.business.Curso;
import com.distributivo.model.business.MallaCabecera;
import com.distributivo.model.business.MallaDetalle;
import com.tesis.utilidadbase.facade.AbstractFacade;
import com.distributivo.utilidadbase.controller.InfoTable;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class MallaCabeceraFacade extends AbstractFacade<MallaCabecera> {

    @PersistenceContext(unitName = "distributivoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MallaCabeceraFacade() {
        super(MallaCabecera.class);
    }

    @Override
    public InfoTable<MallaCabecera> loadTable(Integer first, Integer pageSize, List<String> search, String addquery, Map<String, Object> parametros) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT m FROM MallaCabecera m where m.estado = 'ACTIVO'");

        Query q = getEntityManager().createQuery(sb.toString());
        Query q1 = getEntityManager().createQuery(sb.toString().replace("SELECT m", "SELECT COUNT(m)"));
        for (int i = 0; i < search.size(); i++) {
            StringBuilder par = new StringBuilder();
            par.append("dato").append(i);
            q.setParameter(par.toString(), ("%" + search.get(i).toUpperCase() + "%"));
            q1.setParameter(par.toString(), ("%" + search.get(i).toUpperCase() + "%"));
        }
        q.setFirstResult(first);
        q.setMaxResults(pageSize);

        InfoTable<MallaCabecera> info = new InfoTable();
        info.setList(q.getResultList());
        info.setCount((Long) q1.getSingleResult());
        System.out.println("SQL:" + sb.toString());
        return info;
    }

    public List<MallaCabecera> buscarPorCoincidencia(String filter) {
        filter = "%" + filter + "%";
        Query q = getEntityManager().createQuery("SELECT m FROM MallaCabecera m WHERE UPPER(m.descripcion) like UPPER(:dato) AND m.estado='ACTIVO'");
        q.setParameter("dato", filter);
        q.setMaxResults(10);
        return q.getResultList();
    }

    public List<MallaCabecera> buscarPorCodigo(Integer codigo) {
        Query q = getEntityManager().createQuery("SELECT m FROM MallaCabecera m WHERE m.idMalla = " + codigo);
        return q.getResultList();
    }

    public List<MallaCabecera> buscarPorEstado() {
        Query q = getEntityManager().createQuery("SELECT m FROM MallaCabecera m Where m.estado = 'ACTIVO'");
        return q.getResultList();
    }

    public List<MallaCabecera> buscarTodasMallas() {
        Query q = getEntityManager().createQuery("SELECT m FROM MallaCabecera m ");
        return q.getResultList();
    }

    public MallaCabecera buscarPorIdMalla(Integer codigo) {
        try {
            Query q = getEntityManager().createQuery("SELECT m FROM MallaCabecera m WHERE m.idMalla = " + codigo);
            return (MallaCabecera) q.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }
}
