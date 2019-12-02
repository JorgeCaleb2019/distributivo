/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.utilidadbase.controller;

import com.tesis.utilidadbase.facade.AbstractFacade;
import com.tesis.utilidadbase.facade.StateEntity;
import com.tesis.utilidadbase.other.StringUtil;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.faces.event.ActionEvent;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * @author Fernando
 * @param <T>
 */
public abstract class AbstractController<T> implements Serializable {

    private AbstractFacade facade;
    private T entidad;
    protected String title;
    protected Table<T> table;
    private Class<T> claseEntidad;
    private Class clasePK;
    protected String typeOfView = "list";
    //Class ID 
    private Persistencia persistence;
    private List<String> links;
    private String currentUser;

    public AbstractController(Class<T> claseEntidad) {
        this.claseEntidad = claseEntidad;
    }

    /**
     * Metodo para inicializar la entidad
     *
     * @return
     */
    public void prepararCrear() {
        try {
            this.entidad = this.claseEntidad.newInstance();
            this.persistence = Persistencia.GUARDAR;
        } catch (InstantiationException ex) {
            Logger.getLogger(AbstractController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AbstractController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void prepararEditar(String idString) {
        if (!StringUtil.NullOrEmpty(idString)) {
            this.entidad = this.getEntityById(idString, clasePK);
        }
        this.persistence = Persistencia.MODIFICAR;
    }

    public void prepararAnular(String idString) {
        if (!StringUtil.NullOrEmpty(idString)) {
            this.entidad = this.getEntityById(idString, clasePK);
        }
        this.persistence = Persistencia.ANULAR;
    }

    public void prepararHabilitar(String idString) {
       if (!StringUtil.NullOrEmpty(idString)) {
            this.entidad = this.getEntityById(idString, clasePK);
        }
        this.persistence = Persistencia.HABILITAR;
    }

    public void prepararView(String idString) {
        if (!StringUtil.NullOrEmpty(idString)) {
            this.entidad = this.getEntityById(idString, clasePK);
        }
    }

    /**
     * Método para guardar la entidad
     *
     * @param evt
     */
    public void guardarEntidad(ActionEvent evt) {
        try {
            Method m = this.entidad.getClass().getMethod("setEstado", new Class[]{String.class});
            m.invoke(this.entidad, new Object[]{StateEntity.ACTIVO.name()});
            facade.guardar(this.entidad);
            this.entidad = claseEntidad.newInstance();
            Mensaje.SUCESO("Guardar", "Se ha guardado correctamente.!");
        } catch (ConstraintViolationException | EJBException ex) {
            StringBuilder sb=new StringBuilder();
            sb.append("Error al tratar de Guardar: ");
            if(ex instanceof ConstraintViolationException){
               for(ConstraintViolation cv: ((ConstraintViolationException)ex).getConstraintViolations()){
                  sb.append(" CV=>").append(cv.toString());
               }
            }else{
                sb.append(ex.getMessage());
            }
            Mensaje.ERROR("Guardar", sb.toString());
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AbstractController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(AbstractController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(AbstractController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(AbstractController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(AbstractController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(AbstractController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public abstract void changeView(String view, String id);

    public abstract void executeAction(ActionEvent evt);

    protected abstract void editEntity();

    protected abstract void createEntity();

    protected abstract void disableEntity();
    
    protected abstract void enableEntity();

    /**
     * Método para eliminar una entidad
     *
     * @param evt
     */
    public void anularEntidad(ActionEvent evt) {
        try {

            Method m = this.entidad.getClass().getMethod("setEstado", new Class[]{String.class});
            m.invoke(this.entidad, new Object[]{StateEntity.ANULADO.name()});
            facade.modificar(this.entidad);
            this.entidad = claseEntidad.newInstance();

        } catch (Exception ex) {
            Mensaje.ERROR("Edicion", "Error al Tratar de Modificar en " + this.claseEntidad.getSimpleName());
        }
    }

//    public void habilitarEntidad(ActionEvent evt) {
//        try {
//           
//            Method m = this.entidad.getClass().getMethod("setEstado", new Class[]{String.class});
//            m.invoke(this.entidad, new Object[]{StateEntity.ACTIVO.name()});
//            entidadLogica.modificar(this.entidad);
//             prepararMensajes();
//            Mensaje.SUCESO(MENSAJE_HABILITAR);
//            this.entidad = claseEntidad.newInstance();
//            this.nuevoClick();
//            this.mensajeActual = this.MENSAJE_HABILITAR;
//            RedirectTo.PageSimple(this.rutaPagina + "/index.html");
//        } catch (Exception ex) {
//            Mensaje.ERROR("Edicion", "Error al Tratar de Modificar en " + this.claseEntidad.getSimpleName());
//        }
//    }
    protected T getEntityById(String id, Class idEntity) {
        T entity = null;
        switch (idEntity.getSimpleName()) {
            case "Long":
                Long idLong = Long.parseLong(id);
                entity = (T) facade.buscarPorCodigo(idLong);
                break;
            case "Integer":
                Integer idInt = Integer.parseInt(id);
                entity = (T) facade.buscarPorCodigo(idInt);
                break;

        }
        return entity;
    }

    public String getTitle() {
        if (this.claseEntidad != null) {
            if (this.title == null) {
                this.title = this.claseEntidad.getSimpleName();
            }
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Table<T> getTable() {
        return table;
    }

    public void setTable(Table<T> table) {
        this.table = table;
    }

    public Persistencia getPersistence() {
        return persistence;
    }

    public void setPersistence(Persistencia persistence) {
        this.persistence = persistence;
    }

    public AbstractFacade getFacade() {
        return facade;
    }

    public void setFacade(AbstractFacade facade) {
        this.facade = facade;
    }

    public T getEntidad() {
        if (entidad == null) {
            try {
                this.entidad = this.claseEntidad.newInstance();
            } catch (InstantiationException ex) {
                Logger.getLogger(AbstractController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(AbstractController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return entidad;
    }

    public void setEntidad(T entidad) {
        this.entidad = entidad;
    }

    public Class<T> getClaseEntidad() {
        return claseEntidad;
    }

    public void setClaseEntidad(Class<T> claseEntidad) {
        this.claseEntidad = claseEntidad;
    }

    public Class getClasePK() {
        return clasePK;
    }

    public void setClasePK(Class clasePK) {
        this.clasePK = clasePK;
    }

    public String getTypeOfView() {
        return typeOfView;
    }

    public void setTypeOfView(String typeOfView) {
        this.typeOfView = typeOfView;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
    
}
