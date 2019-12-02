/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.util;

import java.io.BufferedInputStream;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author abel
 */
public class ExternalResource extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public static String dirFiles="C:\\temporal\\";
    //public static String dirFiles="/opt/temporal/";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String fileName = request.getParameter("obj");
           //String extension = UtilExternalResource.getExtensionOfFile(fileName);
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(dirFiles + fileName));
            byte[] buffer = new byte[4096];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b = 0;
            while (b != -1) {
                b = in.read(buffer);
                if (b != -1) {
                    baos.write(buffer, 0, b);
                }
            }
            baos.flush();
            byte[] bytes = baos.toByteArray();
            baos.close();
            in.close();
            response.getOutputStream().write(bytes);
        } catch (Exception e) {
            System.out.println("[WARN] Error" + e.toString());        
        }
    }
}
