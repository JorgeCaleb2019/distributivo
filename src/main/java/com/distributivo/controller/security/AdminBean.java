/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.controller.security;

import com.distributivo.utilities.base.ItemGrafico;
import com.distributivo.ejb.security.UsuarioFacade;
import com.distributivo.ejb.security.ViewPermisoFacade;
import com.distributivo.model.security.Usuario;
import com.distributivo.model.security.ViewPermiso;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.Getter;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.PieChartModel;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Toshiba
 */
@Named(value = "adminBean")
@SessionScoped
public class AdminBean implements Serializable {

    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private ViewPermisoFacade permisoFacade;

    private Usuario usuario;
    @Getter
    private List<ViewPermiso> permisosUsuario;

    @Getter
    private List<ViewPermiso> permisosUsuarioMenuUser;

    private MenuModel modelMenu;

    private String pageSelected;

    private BarChartModel graficoVentasDia;

    private PieChartModel graficoProductosMasVendidos;
    private List<ItemGrafico> listaProductosMasVendidos;

    private String nuevaClave;
    private String confirmarClave;
    private String claveActual;

    public AdminBean() {

    }

    @PostConstruct
    public void init() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.usuario = this.usuarioFacade.getUserByName(auth.getName());
        this.permisosUsuario = this.permisoFacade.getPermisosUsuario(usuario.getNickUsua());
        modelMenu = new DefaultMenuModel();
        DefaultMenuItem itemda = new DefaultMenuItem("Dashboard");
        itemda.setAjax(true);
        itemda.setIcon("dashboard");
        itemda.setCommand("#{adminBean.changePage('/dashboard_table.xhtml')}");
        itemda.setUpdate(":pages");
        itemda.setStyle("font-weight: bold");
        modelMenu.addElement(itemda);
        for (ViewPermiso s : this.permisosUsuario) {
            DefaultSubMenu menu = new DefaultSubMenu(s.getNombrePerm());
            menu.setIcon(s.getIconoPerm());
            menu.setStyle("font-weight: bold");
            for (ViewPermiso x : s.getViewPermisoList()) {
                DefaultMenuItem item = new DefaultMenuItem(x.getNombrePerm());
                item.setAjax(true);
                item.setCommand("#{adminBean.changePage('/module" + x.getDetallePerm().replace(".html", ".xhtml") + "')}");
                item.setUpdate(":pages");
                item.setStyle("font-weight: lighter");
                menu.addElement(item);

            }
            modelMenu.addElement(menu);
        }
    }

    public MenuModel getModelMenu() {
        return modelMenu;
    }

    public void setModelMenu(MenuModel modelMenu) {
        this.modelMenu = modelMenu;
    }

    public String getPageSelected() {
        if (pageSelected == null) {

            pageSelected = "/dashboard_table.xhtml";
        }
        return pageSelected;
    }

    public void setPageSelected(String pageSelected) {
        this.pageSelected = pageSelected;
    }

    public void changePage(String page) {
        this.pageSelected = page;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public BarChartModel getGraficoVentasDia() {
        return graficoVentasDia;
    }

    public PieChartModel getGraficoProductosMasVendidos() {
        return graficoProductosMasVendidos;
    }

    public List<ItemGrafico> getListaProductosMasVendidos() {
        return listaProductosMasVendidos;
    }

    public String getNuevaClave() {
        return nuevaClave;
    }

    public void setNuevaClave(String nuevaClave) {
        this.nuevaClave = nuevaClave;
    }

    public String getConfirmarClave() {
        return confirmarClave;
    }

    public void setConfirmarClave(String confirmarClave) {
        this.confirmarClave = confirmarClave;
    }

    public String getClaveActual() {
        return claveActual;
    }

    public void setClaveActual(String claveActual) {
        this.claveActual = claveActual;
    }

}
