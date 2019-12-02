/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.util;

import java.io.File;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.xmlbeans.XmlCursor;

/**
 * A simple WOrdprocessingML document created by POI XWPF API
 *
 * @author Yegor Kozlov, Adaptado por Antonio Álvarez.
 */
public class GeneraArchivoWord {

    public void replaceTextFoundDevolucion(String path, String plantilla, String resultado, Map<String, String> param) throws IOException {

        String inputfilepath = plantilla;
        String outputfilepath = path + resultado;

        System.out.println(inputfilepath);
        System.out.println(outputfilepath);

        InputStream fs = new FileInputStream(inputfilepath);
        XWPFDocument doc = new XWPFDocument(fs);

        doc.getParagraphs().forEach((p) -> {
            List<XWPFRun> runs = p.getRuns();
            String data = p.getText();
            System.out.println("PARRAFO: " + data);
            this.reemplazar(runs, param);
        });

        File outFile = new File(outputfilepath);
        OutputStream out = new FileOutputStream(outFile);
        PdfOptions options = null;
        PdfConverter.getInstance().convert(doc, out, options);
    }

    public void replaceTextFoundContrato(String plantilla, String archivogenerado, Map<String, String> param) throws IOException {
        String inputfilepath = plantilla;
        String outputfilepath = archivogenerado;

        System.out.println(inputfilepath);
        System.out.println(outputfilepath);

        InputStream fs = new FileInputStream(inputfilepath);
        XWPFDocument doc = new XWPFDocument(fs);

        doc.getParagraphs().forEach((p) -> {
            List<XWPFRun> runs = p.getRuns();

            String data = p.getText();
            this.reemplazar2(runs, param);
        });
        File fxd = new File(outputfilepath);
        if (fxd.exists()) {
            fxd.delete();
        }
        try (FileOutputStream out = new FileOutputStream(outputfilepath)) {
            doc.write(out);
        }
        fs = new FileInputStream(outputfilepath);
        doc = new XWPFDocument(fs);
        File outFile = new File(outputfilepath);
        OutputStream out = new FileOutputStream(outFile);
        PdfOptions options = null;
        PdfConverter.getInstance().convert(doc, out, options);
    }

    private void createParagraphs(XWPFParagraph p, String[] paragraphs) {
        if (p != null) {
            XWPFDocument doc = p.getDocument();
            XmlCursor cursor = p.getCTP().newCursor();
            for (int i = 0; i < paragraphs.length; i++) {
                XWPFParagraph newP = doc.createParagraph();
                newP.getCTP().setPPr(p.getCTP().getPPr());
                XWPFRun newR = newP.createRun();
                newR.getCTR().setRPr(p.getRuns().get(0).getCTR().getRPr());
                newR.setText(paragraphs[i]);
                XmlCursor c2 = newP.getCTP().newCursor();
                c2.moveXml(cursor);
                c2.dispose();
            }
            cursor.removeXml(); // Removes replacement text paragraph
            cursor.dispose();
        }
    }

    public void reemplazar(List<XWPFRun> runs, Map<String, String> param) {
        if (runs != null) {
            List<XWPFRun> listRunSelec = new LinkedList<>();
            int pos = 0;
            String tkey = "";
            for (XWPFRun r : runs) {
                String text = r.getText(0);
                if (text != null) {
                    if (pos == 0) {
                        if (text.trim().contains("$") || text.trim().contains("[")) {
                            listRunSelec.add(r);
                            pos = 1;
                            tkey += r.getText(0);
                            r.setText("", 0);
                            System.err.println("ENCONTRO $ POS=1");
                        }
                        if (text.trim().contains("]")) {
                            pos = 3;
                            for (Map.Entry<String, String> p : param.entrySet()) {
                                System.err.println("VALIDACION " + tkey.toUpperCase() + " y " + p.getKey().toUpperCase());
                                if (tkey.toUpperCase().contains(p.getKey().toUpperCase())) {
                                    XWPFRun xd = listRunSelec.get(0);
                                    int xdpos = runs.indexOf(xd);
                                    for (int i = (xdpos + 1); i < listRunSelec.size(); i++) {
                                        runs.get(i).setText("", 0);
                                    }
                                    runs.get(xdpos).setText(p.getValue(), 0);
                                    listRunSelec = new LinkedList<>();
                                    pos = 0;
                                    tkey = "";
                                    break;
                                }
                            }
                            System.err.println("ENCNTRO ] POS=3");
                        }
                    } else if (pos == 1) {
                        listRunSelec.add(r);
                        tkey += r.getText(0);
                        r.setText("", 0);
                        if (text.trim().contains("]")) {
                            pos = 3;
                            for (Map.Entry<String, String> p : param.entrySet()) {
                                System.err.println("VALIDACION " + tkey.toUpperCase() + " y " + p.getKey().toUpperCase());
                                if (tkey.toUpperCase().contains(p.getKey().toUpperCase())) {
                                    XWPFRun xd = listRunSelec.get(0);
                                    int xdpos = runs.indexOf(xd);
                                    for (int i = (xdpos + 1); i < listRunSelec.size(); i++) {
                                        runs.get(i).setText("", 0);
                                    }
                                    runs.get(xdpos).setText(p.getValue(), 0);
                                    listRunSelec = new LinkedList<>();
                                    pos = 0;
                                    tkey = "";
                                    break;
                                }
                            }
                            System.err.println("ENCNTRO ] POS=3 ulti");
                        }
                    }
                }
            }
        }
    }

    public void reemplazar2(List<XWPFRun> runs, Map<String, String> param) {
        if (runs != null) {
            int encontro = 0;
            for (XWPFRun r : runs) {
                String text = r.getText(0);
                if (encontro == 1) {
                    for (Map.Entry<String, String> entry : param.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        if(text != null && text.contains(key+"]")){
                        text = text.replace(key + "]", value);
                        r.setText(text, 0);
                        break;
                        }
                    }
                    encontro = 0;
                }
                if (text != null && text.contains("$[")) {
                    encontro = 1;
                    text = text.replace("$[", "");
                    r.setText(text, 0);
                }
            }
        }
    }
}
