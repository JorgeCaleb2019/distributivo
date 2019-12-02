package com.distributivo.controller.security;

import com.distributivo.util.ExternalResource;
import com.distributivo.util.UtilExternalResource;
import com.distributivo.ejb.security.UsuarioFacade;
import com.distributivo.model.security.Usuario;
import com.distributivo.utilidadbase.controller.Mensaje;
import com.distributivo.utilidadbase.controller.AbstractController;
import com.distributivo.utilidadbase.controller.Table;
import com.tesis.utilidadbase.facade.StateEntity;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
/**
 *
 * @author Toshiba
 */
@Named(value = "usuarioBean")
@ViewScoped
public class UsuarioBean extends AbstractController<Usuario> implements Serializable {

    private String rutaImagen;
    private UploadedFile fileUpload;
    @EJB
    private UsuarioFacade usuarioFacade;

    public UsuarioBean() {
        super(Usuario.class);
    }

    @PostConstruct
    public void inicializar() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.setCurrentUser(auth.getName());
        this.setFacade(usuarioFacade);
        this.table = new Table<>(usuarioFacade);
        this.setClasePK(Long.class);
        this.setTitle("Usuario");
        this.table.load();
    }

    public void upload() {
        if (this.getFileUpload() != null) {
            byte[] data = this.getFileUpload().getContents();
            StringBuilder newFile = new StringBuilder();
            String exte = UtilExternalResource.getExtensionOfFile(this.getFileUpload().getFileName());
            newFile.append(ExternalResource.dirFiles).append(this.getEntidad().getCodigoUsua()).append("_user").append(".").append(exte);
            this.rutaImagen = newFile.toString().replace(ExternalResource.dirFiles, "");
            this.getEntidad().setFoto(rutaImagen);
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
                Mensaje.SUCESO("Se ha actalizado la imagen de usario");
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
                this.table.load();
                this.setPersistence(null);
                this.setTitle("Usuario");
                break;
            case "create":
                this.title = "Usuario / Nuevo";
                this.prepararCrear();
                break;
            case "edit":
                this.title = "Usuario / Editar / " + this.getEntidad().getNombreCompleto();
                this.prepararEditar(id);
                break;
            case "delete":
                this.title = "Usuario / Eliminar / " + this.getEntidad().getNombreCompleto();
                this.prepararAnular(id);
                break;
            case "view":
                this.prepararView(id);
                this.title = "Usuario / " + this.getEntidad().getNombreCompleto();
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
            this.title = "Usuario / Nuevo";
        } else {
            this.title = "Usuario / " + this.getEntidad().getNombreCompleto();
            this.setPersistence(null);
            this.typeOfView = "view";
        }

        this.table.load();
    }

    @Override
    protected void editEntity() {
        try {
            if (this.rutaImagen != null) {
                this.getEntidad().setFoto(this.rutaImagen);
            }
            
            this.usuarioFacade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha modificado el usuario: " + this.getEntidad().getNombreCompleto());
            this.rutaImagen = null;
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar modificar: " + this.getEntidad().getNombreCompleto() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void createEntity() {
        try {
            this.getEntidad().setUsuarioCreacion(this.getCurrentUser());
            this.getEntidad().setFechaCreacion(new Date());
            if (this.rutaImagen != null) {
                this.getEntidad().setFoto(this.rutaImagen);
            }
            this.getEntidad().setPasswordUsua(this.getEntidad().getNickUsua());
            this.getEntidad().setEstado(StateEntity.ACTIVO.name());
            this.usuarioFacade.guardar(this.getEntidad());
            Mensaje.SUCESO("Se ha guardado el usuario: " + this.getEntidad().getNombreCompleto());
            this.rutaImagen = null;
            this.setEntidad(new Usuario());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar guardar: " + this.getEntidad().getNombreCompleto() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void disableEntity() {
        try {
            this.getEntidad().setUsuarioModificacion(this.getCurrentUser());
            this.getEntidad().setEstado(StateEntity.ANULADO.name());
            this.getEntidad().setFechaModificacion(new Date());
            this.usuarioFacade.modificar(this.getEntidad());
            Mensaje.SUCESO("Se ha anulado el usuario: " + this.getEntidad().getNombreCompleto());
        } catch (EJBException ex) {
            Mensaje.SUCESO("Error al intentar anular: " + this.getEntidad().getNombreCompleto() + ".ERROR=" + ex.getMessage());
        }
    }

    @Override
    protected void enableEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public UploadedFile getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(UploadedFile fileUpload) {
        this.fileUpload = fileUpload;
    }

}
