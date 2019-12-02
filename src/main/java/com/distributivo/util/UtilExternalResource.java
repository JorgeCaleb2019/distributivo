/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distributivo.util;

/**
 *
 * @author abel
 */
public class UtilExternalResource {

    public static String getExtensionOfFile(String fileName) {
        char[] chars = fileName.toCharArray();
        StringBuilder strBuilder = new StringBuilder();
        for (int i = chars.length - 1; i >= 0; i--) {
            if(chars[i]!='.'){
                strBuilder.append(chars[i]);
            }else{
                break;
            }
        }
        return reverseString(strBuilder.toString());
    }

    public static String reverseString(String str) {
        char[] chars = str.toCharArray();
        StringBuilder strBuilder = new StringBuilder();
        for (int i = chars.length - 1; i >= 0; i--) {
            strBuilder.append(chars[i]);
        }
        return strBuilder.toString();
    }
}
