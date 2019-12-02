package com.distributivo.ejb.business;
import com.distributivo.model.business.Adicionale;
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
public class AdicionaleFacade extends AbstractFacade<Adicionale> {

    @PersistenceContext(unitName = "distributivoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdicionaleFacade() {
        super(Adicionale.class);
    }

    @Override
    public InfoTable<Adicionale> loadTable(Integer first, Integer pageSize, List<String> search, String addquery, Map<String, Object> parametros) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c FROM Adicionale c where c.estado = 'ACTIVO'");

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

        
        
        InfoTable<Adicionale> info = new InfoTable();
        info.setList(q.getResultList());
        info.setCount((Long) q1.getSingleResult());
        System.out.println("SQL:" + sb.toString());
        //Collections.sort(info);
        return info;
    }

    public List<Adicionale> buscarPorCoincidencia(String filter) {
        filter = "%" + filter + "%";
        Query q = getEntityManager().createQuery("SELECT l FROM Curso l WHERE UPPER(l.nombre) like UPPER(:dato) AND l.estado='ACTIVO'");
        q.setParameter("dato", filter);
        q.setMaxResults(10);
        return q.getResultList();
    }

    public List<Adicionale> buscarPorEstado() {
        Query q = getEntityManager().createQuery("SELECT l FROM Curso l WHERE l.estado='ACTIVO'");
        return q.getResultList();
    }
    public List<Adicionale> buscarPorIdCabecera(Integer codigo) {
        Query q = getEntityManager().createQuery("SELECT l FROM Adicionale l WHERE l.distributivoCabecera.idDistributivo =  " + codigo + " AND l.estado='ACTIVO'");
        q.setMaxResults(10);
        return q.getResultList();
    }
    
}
