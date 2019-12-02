package com.tesis.utilidadbase.facade;

import com.distributivo.utilidadbase.controller.InfoTable;
import com.distributivo.utilidadbase.controller.Paginator;
import com.distributivo.utilidadbase.controller.Table;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolationException;
import org.primefaces.model.SortOrder;

/**
 * Clase que nos permite realizar el CRUD sobre entidades JPA
 *
 * @author Fernando
 * @param <T>
 */
public abstract class AbstractFacade<T> implements Serializable {

    private Class<T> entityClass;
    private int countFiltro;
    private boolean autoCommit;
    private boolean entityWithState;
    private FacesContext contexto;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.entityWithState = true;
    }

//    public abstract InfoTable<T> loadTable(Integer first, Integer pageSize, List<String> params, String addquery);

    public abstract InfoTable<T> loadTable(Integer first, Integer pageSize, List<String> params, String addquery, Map<String, Object> parametros);

    /**
     * Método para obetner ruta completa de un archivo en el servidor
     *
     * @param rutaArchivo
     * @return
     */
    public String obtenerArchivos(String rutaArchivo) {
        return this.getContexto().getExternalContext().getRealPath(rutaArchivo);
    }

    protected abstract EntityManager getEntityManager();

    protected List<T> getResultListNameQuery(String query, Object... parametros) throws IllegalArgumentException {
        Query q = getEntityManager().createNamedQuery(query);
        for (int i = 0; i < parametros.length; i++) {
            q.setParameter(i + 1, parametros[i]);
        }
        return q.getResultList();
    }

    protected List<T> getResultListQuery(String query, Object... parametros) throws IllegalArgumentException {
        Query q = getEntityManager().createQuery(query);
        for (int i = 0; i < parametros.length; i++) {
            q.setParameter(i + 1, parametros[i]);
        }
        return q.getResultList();
    }

    protected T getResultNameQuery(String query, Object... parametros) throws IllegalArgumentException {
        Query q = getEntityManager().createNamedQuery(query);
        for (int i = 0; i < parametros.length; i++) {
            q.setParameter(i + 1, parametros[i]);
        }
        List<T> dato = q.getResultList();
        if (dato.size() > 0) {
            return dato.get(0);
        } else {
            return null;
        }
    }

    protected T getResultNameQuery(String query, int first, int max, Object... parametros) throws IllegalArgumentException {
        Query q = getEntityManager().createNamedQuery(query);
        for (int i = 0; i < parametros.length; i++) {
            q.setParameter(i + 1, parametros[i]);
        }
        
        q.setFirstResult(first);
        q.setMaxResults(max);
        List<T> dato = q.getResultList();
        if (dato.size() > 0) {
            return dato.get(0);
        } else {
            return null;
        }
    }

    /**
     * Método para guardar una entidad JPA
     *
     * @param entity
     * @throws EJBException
     */
    public void guardar(T entity) throws ConstraintViolationException,EJBException {
        getEntityManager().persist(entity);
    }

    /**
     * Método para modificar una entidad JPA
     *
     * @param entity
     * @throws EJBException
     */
    public void modificar(T entity) throws ConstraintViolationException, EJBException {

        getEntityManager().merge(entity);
    }

    /**
     * Método para eliminar JPA
     *
     * @param entity
     * @throws EJBException
     */
    public void eliminar(T entity) throws ConstraintViolationException, EJBException {
        getEntityManager().remove(entity);
    }

    /**
     * Método para buscar una entidad JPA por su primary key
     *
     * @param id
     * @return
     * @throws EJBException
     */
    public T buscarPorCodigo(Object id) {
        if (id == null) {
            return null;
        } else {
            return getEntityManager().find(getEntityClass(), id);
        }
    }

    /**
     * Método para listar todos lso datos de una entidad JPA
     *
     * @return
     * @throws EJBException
     */
    public List<T> listarTodos() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(getEntityClass()));
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Método para listar por estado todos los datos de uan entidad JPA
     *
     * @param estado
     * @return
     * @throws EJBException
     */
    public List<T> listarPorEstado(StateEntity estado) {
        if (entityWithState) {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<T> q = cb.createQuery(getEntityClass());
            Root<T> c = q.from(getEntityClass());
            List<Predicate> lista = new LinkedList<Predicate>();
            lista.add(cb.equal(c.get("estado"), estado.name()));
            q.select(c).where(lista.<Predicate>toArray(
                    new Predicate[lista.size()]));
            return getEntityManager().createQuery(q).getResultList();
        } else {
            return new LinkedList<>();
        }
    }

    /**
     * Método para utilizar una datatable de primefaces con lazy loading
     *
     * @param first
     * @param count
     * @param sortField
     * @param sortOrder
     * @param filters
     * @param filtros
     * @return
     * @throws Exception
     */
    public List<T> load(int first, int count, String sortField, SortOrder sortOrder, Map<String, Object> filters, List<Filtro> filtros) {
        List<T> datos = new LinkedList<T>();
        List<Filtro> filtrados = new LinkedList<Filtro>();        
        filtrados.addAll(filtros);
        System.out.println("TIEMPO 1: " + System.currentTimeMillis());
        if (filters != null) {
            Set<Map.Entry<String, Object>> entries = filters.entrySet();
            for (Map.Entry<String, Object> filter : entries) {
                if (filter.getValue().toString().trim().length() != 0) {
                    if (filter.getKey().split("\\.").length == 1) {
                        try {
                            Field f = entityClass.getDeclaredField(filter.getKey());
                            f.setAccessible(true);
                            switch(f.getType().getSimpleName()){
                                case "Integer":
                                    String valor = filter.getValue().toString().trim();
                                     
                                    break;
                                case "Double":
                                    
                                    break;
                                case "Float":
                                    
                                    break;
                                case "String":
                                    
                                    break;
                            }
                            if (f.getType().equals(Integer.class)) {
                                String valor = filter.getValue().toString().trim();
                                if (valor.contains("=")) {
                                    if (valor.length() > 1) {
                                        String[] comp = valor.split("\\=");
                                        filtrados.add(new Filtro("=", filter.getKey(), Integer.parseInt(comp[1])));
                                    }
                                } else {
                                    if (valor.contains(">")) {
                                        if (valor.length() > 1) {
                                            String[] comp = valor.split("\\>");
                                            filtrados.add(new Filtro(">=", filter.getKey(), Integer.parseInt(comp[1])));
                                        }
                                    }
                                }
                            } else {
                                if (f.getType().equals(String.class)) {
                                    filtrados.add(new Filtro("LIKE", filter.getKey(), filter.getValue()));
                                } else {
                                    filtrados.add(new Filtro("LIKE", filter.getKey(), filter.getValue()));
                                }
                            }
                            f.setAccessible(false);
                        } catch (NoSuchFieldException ex) {
                            Logger.getLogger(AbstractFacade.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SecurityException ex) {
                            Logger.getLogger(AbstractFacade.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        filtrados.add(new Filtro("LIKE", filter.getKey(), filter.getValue()));
                    }
                    //filtrados.add(new Filtro("LIKE", filter.getKey(), filter.getValue()));
                }
            }
        }
        Consulta cq = new Consulta(getEntityClass().getSimpleName());
        cq.agregarFiltrado(filtrados);
        System.out.println("TIEMPO 2: " + System.currentTimeMillis());
        datos = cq.crearConsulta(getEntityManager(), first, count).getResultList();
        System.out.println("TIEMPO 3: " + System.currentTimeMillis());
        countFiltro = cq.getFilas();
        System.out.println("TIEMPO 4: " + System.currentTimeMillis());
        return datos;
    }

    /**
     * Método para selecionar todos los registros que exsiten de una carga
     * liviana
     *
     * @param filters
     * @param filtros
     * @return
     * @throws Exception
     */
    public List<T> loadFull(Map<String, Object> filters, List<Filtro> filtros) {
        List<T> datos = new LinkedList<T>();
        List<Filtro> filtrados = new LinkedList<Filtro>();
        filtrados.addAll(filtros);
        if (filters != null) {
            Set<Map.Entry<String, Object>> entries = filters.entrySet();
            for (Map.Entry<String, Object> filter : entries) {
                if (filter.getValue().toString().trim().length() != 0) {
                    filtrados.add(new Filtro("LIKE", filter.getKey(), filter.getValue()));
                }
            }
        }
        Consulta cq = new Consulta(getEntityClass().getSimpleName());
        cq.agregarFiltrado(filtrados);
        datos = cq.crearConsulta(getEntityManager()).getResultList();
        countFiltro = cq.getFilas();
        return datos;
    }

    /**
     * Método para obtener lso filtros desde el datatable primefaces
     *
     * @param filters
     * @param filtros
     * @return
     */
    public List<Filtro> load(Map<String, Object> filters, List<Filtro> filtros) {
        List<Filtro> filtrados = new LinkedList<Filtro>();
        filtrados.addAll(filtros);
        if (filters != null) {
            Set<Map.Entry<String, Object>> entries = filters.entrySet();
            for (Map.Entry<String, Object> filter : entries) {
                if (filter.getValue().toString().trim().length() != 0) {
                    if (filter.getKey().split("\\.").length == 1) {
                        try {
                            Field f = entityClass.getDeclaredField(filter.getKey());
                            f.setAccessible(true);
                            if (f.getType().equals(Integer.class)) {
                                String valor = filter.getValue().toString().trim();
                                if (valor.contains("=")) {
                                    if (valor.length() > 1) {
                                        String[] comp = valor.split("\\=");
                                        filtrados.add(new Filtro("=", filter.getKey(), Integer.parseInt(comp[1])));
                                    }
                                } else {
                                    if (valor.contains(">")) {
                                        if (valor.length() > 1) {
                                            String[] comp = valor.split("\\>");
                                            filtrados.add(new Filtro(">=", filter.getKey(), Integer.parseInt(comp[1])));
                                        }
                                    }
                                }
                            } else {
                                if (f.getType().equals(String.class)) {
                                    filtrados.add(new Filtro("LIKE", filter.getKey(), filter.getValue()));
                                } else {
                                    filtrados.add(new Filtro("LIKE", filter.getKey(), filter.getValue()));
                                }
                            }
                            f.setAccessible(false);
                        } catch (NoSuchFieldException ex) {
                            Logger.getLogger(AbstractFacade.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SecurityException ex) {
                            Logger.getLogger(AbstractFacade.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        filtrados.add(new Filtro("LIKE", filter.getKey(), filter.getValue()));
                    }
                }
            }
        }
        return filtrados;
    }

    /**
     * @return the count
     */
    public int getCountFiltro() {
        return countFiltro;
    }

    /**
     * @param count the count to set
     */
    public void setCountFiltro(int countFiltro) {
        this.countFiltro = countFiltro;
    }

    /**
     * @return the autoCommit
     */
    public boolean getAutoCommit() {
        return autoCommit;
    }

    /**
     * @param autoCommit the autoCommit to set
     */
    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    /**
     * @return the entityWithState
     */
    public boolean getEntityWithState() {
        return entityWithState;
    }

    /**
     * @param entityWithState the entityWithState to set
     */
    public void setEntityWithState(boolean entityWithState) {
        this.entityWithState = entityWithState;
    }

    /**
     * @return the entityClass
     */
    public Class<T> getEntityClass() {
        return entityClass;
    }

    /**
     * @return the contexto
     */
    public FacesContext getContexto() {
        return contexto;
    }

    /**
     * @param contexto the contexto to set
     */
    public void setContexto(FacesContext contexto) {
        this.contexto = contexto;
    }
}
