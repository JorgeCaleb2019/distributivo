
package com.distributivo.ejb.business;

import com.distributivo.model.business.DistributivoDetalle;
import com.tesis.utilidadbase.facade.AbstractFacade;
import com.distributivo.model.business.Docente;
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
public class DocenteFacade extends AbstractFacade<Docente> {

    @PersistenceContext(unitName = "distributivoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocenteFacade() {
        super(Docente.class);
    }

    @Override
    public InfoTable<Docente> loadTable(Integer first, Integer pageSize, List<String> params, String addquery, Map<String, Object> parametros) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Docente> buscarPorCoincidencia(String filter) {
        filter = "%" + filter + "%";
        Query q = getEntityManager().createQuery("SELECT l FROM Docente l WHERE ( UPPER(l.nombre) like UPPER(:dato) OR l.cedula like :dato ) AND l.estado='ACTIVO'");
        q.setParameter("dato", filter);
        q.setMaxResults(10);
        return q.getResultList();
    }
    
    public List<Docente> listadoDocentesActivos() {
        Query q = getEntityManager().createQuery("SELECT l FROM Docente l WHERE l.estado='ACTIVO'");
        return q.getResultList();
    }
    public Docente buscarDocentePorId(Integer idDocente) {
        try {
            String cadena = "SELECT d FROM Docente d WHERE d.id = " + idDocente;
            Query q = getEntityManager().createQuery(cadena);
            return (Docente) q.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    public Docente buscarPorCedula(String Cedula) {
        try {
            String cadena = "SELECT d FROM Docente d WHERE d.cedula = '"+Cedula+"' and d.estado = 'ACTIVO'";
            Query q = getEntityManager().createQuery(cadena);
            return (Docente) q.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }
}