/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.portafolio.api.vo;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author FRANCO
 */
public class VOCuenta {
    private String nombre, apellido;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    static List<VOCuenta> all(){
        List<VOCuenta> list = new ArrayList<>();
        
        String sql = "SELECT * FROM cuenta";
        
        Conexion coo = new Conexion();
        Connection cnx = coo.getConexion();
        
        return list;
    }
    
    
}
