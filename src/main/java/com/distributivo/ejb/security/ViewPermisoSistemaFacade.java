package com.distributivo.ejb.security;

import com.distributivo.model.security.Rol;
import com.distributivo.model.security.ViewPermisoSistema;
import com.distributivo.utilidadbase.controller.InfoTable;
import com.tesis.utilidadbase.facade.AbstractFacade;
import com.tesis.utilidadbase.facade.StateEntity;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Toshiba
 */
@Stateless
public class ViewPermisoSistemaFacade extends AbstractFacade<ViewPermisoSistema> {

    @PersistenceContext(unitName = "distributivoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ViewPermisoSistemaFacade() {
        super(ViewPermisoSistema.class);
    }

    public List<String[]> getPermisosSistema() {
        List<String[]> lista = new LinkedList<>();
        Query q = getEntityManager().createNamedQuery("Rol.findByEstado");
        q.setParameter("estado", StateEntity.ACTIVO.name());
        List<Rol> listaRol = q.getResultList();
        for (Rol rol : listaRol) {
            String nombre = rol.getNombreRol();
            String paginas = "";
            List<ViewPermisoSistema> vp = this.getResultListNameQuery("ViewPermisoSistema.findByNombreRol", nombre);
            for (ViewPermisoSistema v : vp) {
                if (!paginas.contains(v.getDetallePerm())) {
                    paginas += v.getDetallePerm() + ",";
                }
            }
            if (paginas.length() > 1) {
                lista.add(new String[]{nombre, paginas.substring(0, paginas.length() - 1)});
            }
        }
        return lista;
    }

    @Override
    public InfoTable<ViewPermisoSistema> loadTable(Integer first, Integer pageSize, List<String> params,String addquery,Map<String,Object> parametros) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

 
}
