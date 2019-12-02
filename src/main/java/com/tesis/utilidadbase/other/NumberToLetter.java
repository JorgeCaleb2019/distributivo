/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tesis.utilidadbase.other;

public class NumberToLetter {

    private static final String[] unidades = {"CERO", "UNO", "DOS", "TRES", "CUATRO", "CINCO", "SEIS", "SIETE", "OCHO", "NUEVE"};
    private static final String[] raros = {"ONCE", "DOCE", "TRECE", "CATORCE", "QUINCE", "DIECISEIS", "DIECISIETE", "DIECIOCHO", "DIECINUEVE"};
    private static final String[] decenas = {"DIEZ", "VEINTE", "TREINTA", "CUARENTA", "CINCUENTA", "SESENTA", "SETENTA", "OCHENTA", "NOVENTA"};
    private static final String[] centenas = {"CIENTO", "DOSCIENTOS", "TRESCIENTOS", "CUATROCIENTOS", "QUINIENTOS", "SEISCIENTOS", "SETECIENTOS", "OCHOCIENTOS", "NOVECIENTOS"};

    public static String convertNumber(Double number) {
        StringBuilder sb = new StringBuilder();
        Double datanumber = number;
        sb.append(generarTexto(number));
        //genera el texto del decimal
        if (sb.length() > 0) {
            sb.append(" DÓLARES");
        }
        sb.append(generarDecimal(datanumber));
        return sb.toString();
    }

    public static String convertNumber(Integer number) {
        StringBuilder sb = new StringBuilder();
        sb.append(generarTexto(number.doubleValue()));
        return sb.toString();
    }

    private static String[] separacionDecimal(Double number) {
        String cad = number.toString();
        return cad.split("\\.");
    }

    private static String generarDecimal(Double number) {
        String[] sep = separacionDecimal(number);
        if (sep.length == 2) {
            if (sep[1].length() == 1) {
                Integer sux = Integer.parseInt(sep[1] + "0");
                return " CON " + convertNumber(sux) + " CENTAVOS";
            } else if (sep[1].length() == 2) {
                Integer sux = Integer.parseInt(sep[1]);
                String letra[] = convertNumber(sux).trim().split(" ");
                String axustring = "";
                if (letra.length == 2) {
                    axustring = letra[0] + " " + letra[1];
                } else {
                    axustring = letra[0];
                }
                for (String d : decenas) {
                    if (letra[0].equals(d)) {
                        if (letra.length == 2) {
                            axustring = letra[0] + " Y " + letra[1];
                        } else {
                            axustring = letra[0];
                        }

                        break;
                    }
                }
                return " CON " + axustring + " CENTAVOS";
            } else if (sep[1].length() > 2) {
                Integer sux = Integer.parseInt(sep[1].substring(0, 2));
                String letra[] = convertNumber(sux).trim().split(" ");
                String axustring = "";
                if (letra.length == 2) {
                    axustring = letra[0] + " " + letra[1];
                } else {
                    axustring = letra[0];
                }
                for (String d : decenas) {
                    if (letra[0].equals(d)) {
                        if (letra.length == 2) {
                            axustring = letra[0] + " Y " + letra[1];
                        } else {
                            axustring = letra[0];
                        }
                    }
                }
                return " CON " + axustring + " CENTAVOS";
            }
        }
        return " CON CERO CENTAVOS";
    }

    private static String generarTexto(Double number) {
        StringBuilder sb = new StringBuilder();
        String[] sep = separacionDecimal(number);
        String num = sep[0];
        if (num.length() == 4) {
            int val = Integer.parseInt(num.charAt(0) + "");
            if (val != 0) {
                if (val == 1) {
                    sb.append("MIL");
                    number = number - 1000;
                } else {
                    sb.append(unidades[val]).append(" MIL");
                    number = number - (val * 1000);
                }
            }
        }

        sep = separacionDecimal(number);
        num = sep[0];
        if (num.length() == 3) {
            int val = Integer.parseInt(num.charAt(0) + "");
            if (val != 0) {
                sb.append(" ");
                sb.append(centenas[val - 1]);
                number = number - (val * 100);
            }
        }

        sep = separacionDecimal(number);
        num = sep[0];
        if (num.length() == 2) {
            int val = Integer.parseInt(num + "");
            if (val != 0) {
                if (val == 10) {
                    sb.append(" ");
                    sb.append(decenas[0]);
                    number = 0d;
                } else if (val < 10) {
                    sb.append(" ");
                    sb.append(unidades[val]);
                    number = 0d;
                } else if (val > 10 && val < 20) {
                    sb.append(" ");
                    sb.append(raros[val - 11]);
                    number = 0d;
                } else {
                    val = Integer.parseInt(num.charAt(0) + "");
                    sb.append(" ");
                    sb.append(decenas[val - 1]);
                    number = number - (val * 10);
                }
            }
        }
        sep = separacionDecimal(number);
        num = sep[0];
        if (num.length() == 1) {
            int val = Integer.parseInt(num.charAt(0) + "");
            if (val != 0) {
                sb.append(" ");
                sb.append(unidades[val]);
                number = 0d;
            }
        }

        return sb.toString();
    }

}
