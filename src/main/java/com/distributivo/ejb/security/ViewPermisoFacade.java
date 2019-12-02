package com.distributivo.ejb.security;

import com.distributivo.model.security.ViewPermiso;
import com.distributivo.utilidadbase.controller.InfoTable;
import com.tesis.utilidadbase.facade.AbstractFacade;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Toshiba
 */
@Stateless
public class ViewPermisoFacade extends AbstractFacade<ViewPermiso> {

    @PersistenceContext(unitName = "distributivoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ViewPermisoFacade() {
        super(ViewPermiso.class);
    }

    public List<ViewPermiso> getPermisosUsuario(String usuario) {
        List<ViewPermiso> lista = this.getResultListNameQuery("ViewPermiso.findPermPadre", usuario);
        for (int i = 0; i < lista.size(); i++) {
            ViewPermiso vp = lista.get(i);
            lista.get(i).setViewPermisoList(this.getResultListNameQuery("ViewPermiso.findPermHijo", usuario, vp.getId().getCodigoPerm()));
        }
        return lista;
    }

    public List<String> getRolesUsuario(String usuario) {
        List<ViewPermiso> lista = this.getResultListNameQuery("ViewPermiso.findByNickUsua", usuario);
        List<String> ls = new LinkedList<>();
        for (ViewPermiso l : lista) {
            if (!ls.contains(l.getNombreRol())) {
                ls.add(l.getNombreRol());
            }
        }
        return ls;
    }

    @Override
    public InfoTable<ViewPermiso> loadTable(Integer first, Integer pageSize, List<String> params, String addquery, Map<String, Object> parametros) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
