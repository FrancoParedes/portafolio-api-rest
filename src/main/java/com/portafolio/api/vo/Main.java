/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.portafolio.api.vo;

import java.sql.Connection;


/**
 *
 * @author FRANCO
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic 
        try{
            Connection cnx = new Conexion().getConexion();
            System.out.println("MYSQL CONECTADO");
        }catch(Exception ex){
            System.out.println("ERROR AL CONECTAR CON MYSQL: " + ex.getMessage());
        }
    }
    
}
