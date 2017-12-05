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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author FRANCO
 */
public class VOSeguro {
    private String seguro_id, destino_id, descripcion;
    private int valor_persona;

    public VOSeguro() {
        
    }

    public VOSeguro(String destino_id, String descripcion, int valor_persona) throws Exception {
        setSeguro_id();
        setDestino_id(destino_id);
        setDescripcion(descripcion);
        setValor_persona(valor_persona);
    }
    
    
    public String getSeguro_id() {
        return seguro_id;
    }

    public void setSeguro_id() {
        this.seguro_id = Random.alfanumeric(2);
    }

    public String getDestino_id() {
        return destino_id;
    }

    public void setDestino_id(String destino_id) throws Exception {
        if(destino_id.trim().length()==0){
            throw new Exception("Ingresa el destino");
        }
        if(!VODestino.check(destino_id)){
            throw new Exception("No existe el destino seleccionado");
        }
        this.destino_id = destino_id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) throws Exception {
        if(descripcion.trim().length()==0){
            throw new Exception("Ingresa la descripcion del seguro");
        }
        this.descripcion = descripcion;
    }

    public int getValor_persona() {
        return valor_persona;
    }

    public void setValor_persona(int valor_persona) throws Exception {
        if(valor_persona<=0){
            throw new Exception("Ingresa el valor del seguro por persona");
        }
        this.valor_persona = valor_persona;
    }
       
    public static boolean check(String seguro_id) throws Exception, SQLException {
        String sql = "SELECT seguro_id FROM seguro WHERE seguro_id=?";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, seguro_id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            cnx.close();
            return true;
        } else {
            cnx.close();
            return false;
        }
    }
    
    public static List<Map> all() throws Exception, SQLException{ 
        List<Map> list = new ArrayList();
        String sql = "SELECT * FROM seguro";
       
        Connection cnx = new Conexion().getConexion();
        
            PreparedStatement stmt = cnx.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Map map = new LinkedHashMap();
                map.put("seguro_id", rs.getString("seguro_id"));
                map.put("destino_id", rs.getString("destino_id"));
                map.put("descripcion", rs.getString("descripcion"));
                map.put("valor_persona", rs.getInt("valor_persona"));
                list.add(map);
                
            }
            cnx.close();
            if(list.isEmpty()){
                throw new Exception("No se encontraron seguros registrados");
            }
        return list;
    }
    
    
    public static Map find(String seguro_id) throws Exception, SQLException{
        Map map = null;
        String sql = "SELECT * FROM seguro WHERE seguro_id=?";
       
        Connection cnx = new Conexion().getConexion();
        
        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, seguro_id);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            
            map = new LinkedHashMap();
            
            map.put("seguro_id", rs.getString("seguro_id"));
            map.put("destino_id", rs.getString("destino_id"));
            map.put("descripcion", rs.getString("descripcion"));
            map.put("valor_persona", rs.getInt("valor_persona"));

        }
        cnx.close();
        if(map==null){
            throw new Exception("No existe el seguro seleccioado");
        }
        return map;
    }    
    public static boolean delete(String seguro_id) throws Exception, SQLException{
        String sql = "DELETE FROM seguro WHERE seguro_id=?";
       
        Connection cnx = new Conexion().getConexion();
        
        PreparedStatement stmt = cnx.prepareStatement(sql);
        
        stmt.setString(1, seguro_id);
        
        
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
        
        String sql = "INSERT INTO seguro(seguro_id, destino_id, descripcion, valor_persona) ";
        sql+= "VALUES(?,?, ?, ?)";
        
        PreparedStatement stmt = null;
        Connection cnx = new Conexion().getConexion();
        
        stmt = cnx.prepareStatement(sql);
        stmt.setString(1, this.seguro_id);
        stmt.setString(2, this.destino_id);
        stmt.setString(3, this.descripcion);
        stmt.setInt(4, this.valor_persona);

        resultado = stmt.executeUpdate();

        stmt.close();
        cnx.close();
        if(resultado==0){
            throw new Exception("No se ha registrado el seguro");
        }
        
        
        return resultado;
    }
}
