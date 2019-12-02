package com.distributivo.ejb.business;

import com.distributivo.model.business.Auditoria;
import com.tesis.utilidadbase.facade.AbstractFacade;
import com.distributivo.utilidadbase.controller.InfoTable;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class AuditoriaFacade extends AbstractFacade<Auditoria> {

    @PersistenceContext(unitName = "distributivoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AuditoriaFacade() {
        super(Auditoria.class);
    }

    @Override
    public InfoTable<Auditoria> loadTable(Integer first, Integer pageSize, List<String> search, String addquery, Map<String, Object> parametros) {
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

        InfoTable<Auditoria> info = new InfoTable();
        info.setList(q.getResultList());
        info.setCount((Long) q1.getSingleResult());
        System.out.println("SQL:" + sb.toString());
        return info;
    }
}
