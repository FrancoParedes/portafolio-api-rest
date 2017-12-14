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
public class VODestino {
    private String destino_id, nombre;

    public VODestino() {
    }
    
    public VODestino(String destino_id, String nombre) throws Exception{
        setId(destino_id);
        setNombre(nombre);
    } 
    public VODestino(String nombre) throws Exception{
        setId("");
        setNombre(nombre);
    } 
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws Exception {
        if (nombre.trim().length()==0) {
            throw new Exception("Ingresa el nombre del destino");
        }
        if(nombre.trim().length()>30){
            throw new Exception("Demasiados caracteres para el destino");
        }
        this.nombre = nombre.toUpperCase();
    }

    public String getId() {
        return destino_id;
    }

    public void setId(String destino_id) {
        if (destino_id.trim().length()==0) {
            this.destino_id = Random.alfanumeric(2);
        }else {
            this.destino_id = destino_id;
        }
    }
    
    public static boolean check(String destino_id) throws Exception, SQLException {
        String sql = "SELECT destino_id FROM destino WHERE destino_id=?";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, destino_id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            cnx.close();
            return true;
        } else {
            cnx.close();
            return false;
        }
    }
    
    public static List<VODestino> all() throws Exception, SQLException{
        List<VODestino> list = new ArrayList<>();
        
        String sql = "SELECT * FROM destino";
       
        Connection cnx = new Conexion().getConexion();
        
            PreparedStatement stmt = cnx.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                System.out.println("Fila encontrada");
                VODestino destino = new VODestino();
                destino.destino_id = rs.getString("destino_id");
                destino.nombre = rs.getString("nombre");
                list.add(destino);
                
            }
            cnx.close();
            if(list.isEmpty()){
                throw new Exception("No se encontraron destinos registrados");
            }
        return list;
    }
    
    
    public static VODestino find(String destino_id) throws Exception, SQLException{
        VODestino destino = null;
        String sql = "SELECT * FROM destino WHERE destino_id=?";
       
        Connection cnx = new Conexion().getConexion();
        
        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, destino_id);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            
            destino = new VODestino();
            
            destino.destino_id = rs.getString("destino_id");
            destino.nombre = rs.getString("nombre");

        }
        cnx.close();
        if(destino==null){
            throw new Exception("No existe el destino seleccioado");
        }
        return destino;
    }    
    public static boolean delete(String destino_id) throws Exception, SQLException{
        String sql = "DELETE FROM destino WHERE destino_id=?";
       
        Connection cnx = new Conexion().getConexion();
        
        PreparedStatement stmt = cnx.prepareStatement(sql);
        
        stmt.setString(1, destino_id);
        
        
        int resultado = stmt.executeUpdate();
        cnx.close();
        System.out.println("Resultado de la eliminacion:" + resultado);
        if(resultado==1){
            return true;
        }else {
            return false;
        }
    }
    
    
    public int save() throws Exception, SQLException{
        int resultado = 0;
        
        String sql = "INSERT INTO destino(destino_id, nombre) ";
        sql+= "VALUES(?,?)";
        
        PreparedStatement stmt = null;
        Connection cnx = new Conexion().getConexion();
        
        stmt = cnx.prepareStatement(sql);
        stmt.setString(1, this.destino_id);
        stmt.setString(2, this.nombre);

        resultado = stmt.executeUpdate();

        stmt.close();
        cnx.close();
        if(resultado==0){
            throw new Exception("No se ha registrado el destino");
        }
        
        
        return resultado;
    }
}
