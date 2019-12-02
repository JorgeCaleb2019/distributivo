/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.ejb.business;

import com.distributivo.model.business.MallaDetalle;
import com.tesis.utilidadbase.facade.AbstractFacade;
import com.distributivo.model.business.Materia;
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
public class MateriaFacade extends AbstractFacade<Materia> {

    @PersistenceContext(unitName = "distributivoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MateriaFacade() {
        super(Materia.class);
    }

    @Override
    public InfoTable<Materia> loadTable(Integer first, Integer pageSize, List<String> params, String addquery, Map<String, Object> parametros) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List<Materia> buscarPorCoincidencia(String filter) {
        filter = "%" + filter + "%";
        Query q = getEntityManager().createQuery("SELECT l FROM Materia l WHERE UPPER(l.nombre) like UPPER(:dato) AND l.estado='ACTIVO'");
        q.setParameter("dato", filter);
        return q.getResultList();
    }
    
    public List<Materia> buscarPorEstado() {
        Query q = getEntityManager().createQuery("SELECT l FROM Materia l WHERE l.estado='ACTIVO'");
        return q.getResultList();
    }
    
    public List<Materia> buscarPorCodigo(Integer codigo) {
        Query q = getEntityManager().createQuery("SELECT l FROM Materia l WHERE l.id = " + codigo + " AND l.estado='ACTIVO'");
        return q.getResultList();
    }
}
