package com.distributivo.util;

import com.distributivo.ejb.business.ConfiguracionFacade;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import com.distributivo.model.business.Configuracion;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

public class JasperReportUtil {

    

    public String crearReporte(Map<String, Object> map, String nombreReporte, String nombrePdf, String host,String puerto,String base,String usuario,String clave) throws JRException {
        
        String rutaAplicacion = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reportes/");
        String pathPdf = rutaAplicacion + nombrePdf;
        String reportPath = rutaAplicacion + nombreReporte;

        System.out.println("direccion jasper: " + reportPath);
        System.out.println("direccion pdf: " + pathPdf);
        JasperPrint jp = JasperFillManager.fillReport(reportPath, map, ConectarBD.connectDatabase(host, puerto, base, usuario, clave));
        JasperExportManager.exportReportToPdfFile(jp, pathPdf);
        return pathPdf;

    }

}
