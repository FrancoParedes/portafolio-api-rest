/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.portafolio.api.utils;

/**
 *
 * @author FRANCO
 */
public class Random {
    public static String alfanumeric(int longitud){
    
    char[] elementos = new char[] {'0','1','2','3','4','5','6','7','8','9','a',
    'b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t',
    'u','v','w','x','y','z'};

    char[] conjunto = new char[longitud];
    String pass;

    
    for(int i=0;i<longitud;i++){
        int el = (int)(Math.random()*36);
        conjunto[i] = (char)elementos[el];
    }
        return pass = new String(conjunto);
    }
}
