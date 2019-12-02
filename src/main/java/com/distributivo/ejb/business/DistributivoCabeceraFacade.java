package com.distributivo.ejb.business;

import com.distributivo.model.business.Dedicacion;
import com.distributivo.model.business.DistributivoCabecera;
import com.tesis.utilidadbase.facade.AbstractFacade;
import com.distributivo.utilidadbase.controller.InfoTable;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class DistributivoCabeceraFacade extends AbstractFacade<DistributivoCabecera> {

    @PersistenceContext(unitName = "distributivoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DistributivoCabeceraFacade() {
        super(DistributivoCabecera.class);
    }

    @Override
    public InfoTable<DistributivoCabecera> loadTable(Integer first, Integer pageSize, List<String> params, String addquery, Map<String, Object> parametros) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<DistributivoCabecera> buscarPorCoincidencia(String filter) {
        filter = "%" + filter + "%";
        Query q = getEntityManager().createQuery("SELECT l FROM Distributivo l WHERE UPPER(l.nombre) like UPPER(:dato) AND l.estado='ACTIVO'");
        q.setParameter("dato", filter);
        q.setMaxResults(10);
        return q.getResultList();
    }

    public DistributivoCabecera buscarPorDocentePeriodo(Integer idDocente, Integer idPeriodo) {
        try {
            String cadena = "SELECT d FROM DistributivoCabecera d WHERE d.docente.id = " + idDocente
                    + " and d.periodo.id = " + idPeriodo + " and d.estado = 'ACTIVO'";
            Query q = getEntityManager().createQuery(cadena);
            return (DistributivoCabecera) q.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<DistributivoCabecera> buscarPorPeriodo(Integer idPeriodo) {
        Query q = getEntityManager().createQuery("SELECT l FROM DistributivoCabecera l WHERE l.periodo.id = " + idPeriodo + " AND l.estado='ACTIVO'");
        return q.getResultList();
    }

    public DistributivoCabecera buscarCodigo(Integer codigo) {
        try {
            Query q = getEntityManager().createQuery("SELECT l FROM DistributivoCabecera l WHERE l.idDistributivo = " + codigo);
            return (DistributivoCabecera) q.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }
}
