package com.distributivo.ejb.business;

import com.distributivo.model.business.MallaCabecera;
import com.tesis.utilidadbase.facade.AbstractFacade;
import com.distributivo.model.business.MallaDetalle;
import com.distributivo.utilidadbase.controller.InfoTable;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class MallaDetalleFacade extends AbstractFacade<MallaDetalle> {

    @PersistenceContext(unitName = "distributivoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MallaDetalleFacade() {
        super(MallaDetalle.class);
    }

    @Override
    public InfoTable<MallaDetalle> loadTable(Integer first, Integer pageSize, List<String> search, String addquery, Map<String, Object> parametros) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT m FROM MallaDetalle m where m.estado = 'ACTIVO'");

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

        InfoTable<MallaDetalle> info = new InfoTable();
        info.setList(q.getResultList());
        info.setCount((Long) q1.getSingleResult());
        System.out.println("SQL:" + sb.toString());
        return info;
    }

    public List<MallaDetalle> buscarPorCoincidencia(Integer idMalla) {
        String cadena = "";
        Query q = getEntityManager().createQuery("SELECT m FROM MallaDetalle m WHERE m.mallaCabecera.idMalla = " + idMalla + " and m.estado='ACTIVO'");
        //q.setParameter("dato", filter);
        return q.getResultList();
    }

    public List<MallaDetalle> buscarPorCurso(Integer idCurso, Integer idMalla) {
        String cadena = "SELECT m FROM MallaDetalle m WHERE m.curso.idCurso = " + idCurso + " and m.estado = 'ACTIVO'";
        cadena = cadena + " and m.mallaCabecera.idMalla = " + idMalla;
        Query q = getEntityManager().createQuery(cadena);
        return q.getResultList();
    }

    public List<MallaDetalle> buscarPorMateriaCurso(Integer idCurso, Integer idMateria) {
        String cadena = "SELECT m FROM MallaDetalle m WHERE m.curso.idCurso = " + idCurso + " and m.estado = 'ACTIVO'";
        cadena = cadena + " and m.materia.id = " + idMateria + " and m.mallaCabecera.estado = 'ACTIVO'";
        Query q = getEntityManager().createQuery(cadena);
        return q.getResultList();
    }

    public List<MallaDetalle> buscarCodigo(Integer codigo) {
        Query q = getEntityManager().createQuery("SELECT m FROM MallaDetalle m WHERE m.idDetalle = " + codigo);
        return q.getResultList();
    }

    public MallaDetalle buscarPorMateriaCursoVigente(Integer idCurso, Integer idMateria) {
        try {
            String cadena = "SELECT m FROM MallaDetalle m WHERE m.curso.idCurso = " + idCurso + " and m.estado = 'ACTIVO'";
            cadena = cadena + " and m.materia.id = " + idMateria + " and m.mallaCabecera.estado = 'ACTIVO' and m.mallaCabecera.vigente = 'S'";
            Query q = getEntityManager().createQuery(cadena);
            //q.setParameter("dato", filter);
            return (MallaDetalle) q.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }
    
    public List<MallaDetalle> buscarMateriaMalla(Integer idCurso, Integer idMalla) {
        String cadena = "SELECT m FROM MallaDetalle m WHERE m.curso.idCurso = " + idCurso + " and m.estado = 'ACTIVO'";
        cadena = cadena + " and m.mallaCabecera.idMalla = " + idMalla;
        Query q = getEntityManager().createQuery(cadena);
        return q.getResultList();
    }
}
