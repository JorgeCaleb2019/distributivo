/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tesis.utilidadbase.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Diseñador
 */
public class FileUtilEcuasis {

    public static void subirFichero(UploadedFile uploadFile,String directorio,String nombreFichero) throws IOException {
        String ruta = directorio+File.separatorChar+ nombreFichero;
        File file = new File(ruta);
        FileOutputStream fos = new FileOutputStream(file);
        IOUtils.copy(uploadFile.getInputstream(), fos);
    }
}
