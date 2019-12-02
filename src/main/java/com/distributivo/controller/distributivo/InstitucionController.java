package com.distributivo.controller.distributivo;

import com.distributivo.util.ExternalResource;
import com.distributivo.util.UtilExternalResource;
import com.distributivo.ejb.business.InstitucionFacade;
import com.distributivo.model.business.Institucion;
import com.distributivo.utilidadbase.controller.LazyDataModelAdvance;
import com.distributivo.utilidadbase.controller.Mensaje;
import com.distributivo.utilidadbase.controller.AbstractController;
import com.tesis.utilidadbase.facade.StateEntity;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.FacesException;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.imageio.stream.FileImageOutputStream;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;


@Named(value = "institucionController")
@ViewScoped
public class InstitucionController extends AbstractController<Institucion> implements Serializable {

    @EJB
    private InstitucionFacade facade;
    private List<Institucion> institucionList;

    private LazyDataModelAdvance<Institucion> tablaLazy;
    private UploadedFile fileUpload;

    public InstitucionController() {
        super(Institucion.class);
    }

    @PostConstruct
    public void inicializar() {
        this.setFacade(facade);
        this.setClasePK(Integer.class);
        this.setTitle("Institución");
        tablaLazy = new LazyDataModelAdvance<>(facade);
        tablaLazy.setFacade(facade);
        tablaLazy.setClasePK(Integer.class);
    }

    public void upload() {
        if (this.getFileUpload() != null) {
            byte[] data = this.getFileUpload().getContents();
            StringBuilder newFile = new StringBuilder();
            String exte = UtilExternalResource.getExtensionOfFile(this.getFileUpload().getFileName());
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddhhmmss");
            String archivo = sdf.format(new Date()) + "." + exte;
            newFile.append(ExternalResource.dirFiles).append(archivo);
            this.getEntidad().setLogo(archivo);
            FileImageOutputStream imageOutput;
            try {
                File f = new File(newFile.toString());
                if (f.exists()) {
                    f.delete();
                    f.createNewFile();
                } else {
                    f.createNewFile();
                }
                imageOutput = new FileImageOutputStream(f);
                imageOutput.write(data, 0, data.length);
                imageOutput.close();
                Mensaje.SUCESO("Se ha actalizado la imagen de la Institucion ");
            } catch (IOException e) {
                throw new FacesException("Error al guardar la Imagen.", e);
            }
            System.out.println("SE GUARDO LA FOTO");
        } else {
            System.out.println("NO HAY QUE GUARDAR GUARDO LA FOTO");
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        this.fileUpload = event.getFile();
        this.upload();

    }

    @Override
    public void changeView(String view, String id) {
        this.typeOfView = view;
        switch (view) {
            case "list":
                this.setPersistence(null);
                this.setTitle("Institución");
                break;
            case "create":
                this.title = "Institución / Nuevo";
                this.prepararCrear();
                break;
            case "edit":
                this.title = "Institución / Editar / " + this.getEntidad().getId();
                this.prepararEditar(id);
                break;
            case "delete":
                this.title = "Institución / Eliminar / " + this.getEntidad().getId();
                this.prepararAnular(id);
                break;
            case "view":
                this.prepararView(id);
                this.title = "Institución / " + this.getEntidad().getId();
                break;
        }
    }

    @Override
    public void executeAction(ActionEvent evt) {
        switch (this.typeOfView) {
            case "create":
                this.createEntity();
                break;
            case "edit":
                this.editEntity();
                break;
            case "delete":
                this.disableEntity();
                break;
        }
        if (this.typeOfView.equals("create")) {
            this.title = "Institución / Nuevo";
        } else {
            this.title = "Institución / " + this.getEntidad().getId();
            this.setPersistence(null);
            this.typeOfView = "view";
        }
    }

    @Override
    protected void editEntity() {
        try {
            this.facade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha editado la Institución: " + this.getEntidad().getId());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar editar: " + this.getEntidad().getId() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void createEntity() {
        try {
            this.getEntidad().setEstado(StateEntity.ACTIVO.name());
            this.facade.guardar(this.getEntidad());
            Mensaje.SUCESO("Se ha guardado la Institución: " + this.getEntidad().getId());
            this.setEntidad(new Institucion());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar guardar: " + this.getEntidad().getId() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void disableEntity() {
        try {
            this.getEntidad().setEstado(StateEntity.ANULADO.name());
            this.facade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha anulado la Periodo: " + this.getEntidad().getId());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar anular: " + this.getEntidad().getId() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void enableEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Institucion> complete(String query) {
        return facade.buscarPorCoincidencia(query);
    }

    public Institucion getInstitucion(Integer id) {
        return this.facade.buscarPorCodigo(id);
    }

    public LazyDataModelAdvance<Institucion> getTablaLazy() {
        return tablaLazy;
    }

    public void setTablaLazy(LazyDataModelAdvance<Institucion> tablaLazy) {
        this.tablaLazy = tablaLazy;
    }

    public List<Institucion> getPeriodoList() {
        institucionList = this.facade.listarPorEstado(StateEntity.ACTIVO);
        return institucionList;
    }

    public void setPeriodoList(List<Institucion> periodoList) {
        this.institucionList = periodoList;
    }

    public UploadedFile getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(UploadedFile fileUpload) {
        this.fileUpload = fileUpload;
    }
}
