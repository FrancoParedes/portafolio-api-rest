/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.portafolio.api.vo;

import com.portafolio.api.utils.Random;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author FRANCO
 */
public class VOActividad {
    private String actividad_id, nombre;

    public VOActividad() {
    }
    
    public VOActividad(String actividad_id, String nombre) throws Exception{
        setId(actividad_id);
        setNombre(nombre);
    } 
    public VOActividad(String nombre) throws Exception{
        setId("");
        setNombre(nombre);
    } 
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws Exception {
        if (nombre.trim().length()==0) {
            throw new Exception("Ingresa un nombre para la actividad");
        }
        if (nombre.trim().length()>15) {
            throw new Exception("Nombre de la actividad demasiado largo(max 15)");
        }
        this.nombre = nombre;
    }

    public String getActividad_id() {
        return actividad_id;
    }

    public void setId(String actividad_id) {
        if (actividad_id.trim().length()==0) {
            this.actividad_id = Random.alfanumeric(2);
        }else {
            this.actividad_id = actividad_id;
        }
    }
    
    public static boolean check(String actividad_id) throws Exception, SQLException{
        String sql = "SELECT actividad_id FROM actividad WHERE actividad_id=?";
       
        Connection cnx = new Conexion().getConexion();
        
        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, actividad_id);
        ResultSet rs = stmt.executeQuery();
        
        
        if(rs.next()){
            return true;
        }else {
            return false;
        }
    }
    
    public static List<VOActividad> all() throws Exception, SQLException{
        List<VOActividad> list = new ArrayList<>();
        
        String sql = "SELECT * FROM actividad";
       
        Connection cnx = new Conexion().getConexion();
        
            PreparedStatement stmt = cnx.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                System.out.println("Fila encontrada");
                VOActividad actiividad = new VOActividad();
                actiividad.actividad_id = rs.getString("actividad_id");
                actiividad.nombre = rs.getString("nombre");
                list.add(actiividad);
                
            }
            if(list.isEmpty()){
                throw new Exception("No se encontraron actividades registradas");
            }
        return list;
    }
    
    
    public static VOActividad find(String actividad_id) throws Exception, SQLException{
        VOActividad actividad = null;
        String sql = "SELECT * FROM actividad WHERE actividad_id=?";
       
        Connection cnx = new Conexion().getConexion();
        
        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, actividad_id);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            
            actividad = new VOActividad();
            
            actividad.actividad_id = rs.getString("actividad_id");
            actividad.nombre = rs.getString("nombre");

        }
        if(actividad==null){
            throw new Exception("No existe la actividad");
        }
        return actividad;
    }    
    public static boolean delete(String actividad_id) throws Exception, SQLException{
        String sql = "DELETE FROM actividad WHERE actividad_id=?";
       
        Connection cnx = new Conexion().getConexion();
        
        PreparedStatement stmt = cnx.prepareStatement(sql);
        
        stmt.setString(1, actividad_id);
        
        
        int resultado = stmt.executeUpdate();
        System.out.println("Resultado de la eliminacion:" + resultado);
        if(resultado==1){
            return true;
        }else {
            return false;
        }
    }
    
    
    public int save() throws Exception, SQLException{
        int resultado = 0;
        
        String sql = "INSERT INTO actividad(actividad_id, nombre) ";
        sql+= "VALUES(?,?)";
        
        PreparedStatement stmt = null;
        Connection cnx = new Conexion().getConexion();
        
        stmt = cnx.prepareStatement(sql);
        stmt.setString(1, this.actividad_id);
        stmt.setString(2, this.nombre);

        resultado = stmt.executeUpdate();

        stmt.close();
        
        if(resultado==0){
            throw new Exception("No se ha registrado la actividad");
        }
        
        
        return resultado;
    }
}
