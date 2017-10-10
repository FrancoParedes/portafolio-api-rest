/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.portafolio.api.vo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    public static List<VOCuenta> all(){
        List<VOCuenta> list = new ArrayList<>();
        
        String sql = "SELECT * FROM cuenta";
        
        Connection cnx = new Conexion().getConexion();
        
        try {
            PreparedStatement stmt = cnx.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()){
                VOCuenta cuenta = new VOCuenta();
                cuenta.setNombre(rs.getString("nombre"));
                cuenta.setApellido(rs.getString("apellido_p"));
                
                list.add(cuenta);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VOCuenta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    
}
