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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FRANCO
 */
public class VOEscuela {

    private String escuela_id, nombre, region_id, ciudad;

    public VOEscuela() {
    }

    public VOEscuela(String nombre, String ciudad, String region_id) throws Exception {
        setEscuela_id("");
        setNombre(nombre);
        setCiudad(ciudad);
        setRegion_id(region_id);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws Exception {
        if (nombre.trim().length() == 0) {
            throw new Exception("Escribe el nombre de la escuela");
        }
        this.nombre = nombre.toUpperCase();
    }

    public String getEscuela_id() {
        return escuela_id;
    }

    public void setEscuela_id(String escuela_id) {
        if (escuela_id.trim().length() == 0) {
            this.escuela_id = Random.alfanumeric(2);
        } else {
            this.escuela_id = escuela_id;
        }
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) throws Exception {

        if (region_id.trim().length() == 0) {
            throw new Exception("Selecciona la region");
        }
        this.region_id = region_id;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) throws Exception {
        if (ciudad.trim().length() == 0) {
            throw new Exception("Ingresa una ciudad");
        }
        this.ciudad = ciudad.toUpperCase();
    }

    public static boolean check(String escuela_id) throws Exception, SQLException {
        String sql = "SELECT escuela_id FROM escuela WHERE escuela_id=?";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, escuela_id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    public static List<Map> all() throws Exception, SQLException {
        List<Map> list = new ArrayList<>();

        String sql = "SELECT e.escuela_id, e.nombre AS e_nombre, e.ciudad, r.region_id, r.nombre AS r_nombre FROM escuela e INNER JOIN region r ON ";
        sql += "e.region_id=r.region_id";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.println("Fila encontrada");

            Map<String, Object> map = new LinkedHashMap<>();

            map.put("escuela_id", rs.getString("escuela_id"));
            map.put("escuela_nombre", rs.getString("e_nombre"));
            map.put("ciudad", rs.getString("ciudad"));

            VORegion region = new VORegion(rs.getString("region_id"), rs.getString("r_nombre"));
            map.put("region", region);
            list.add(map);

        }
        if (list.isEmpty()) {
            throw new Exception("No se encontraron cuentas registradas");
        }
        return list;
    }

    public static Map find(String escuela_id) throws Exception, SQLException {
        Map<String, Object> map = null;

        String sql = "SELECT e.escuela_id, e.nombre AS e_nombre, e.ciudad, r.region_id, r.nombre AS r_nombre FROM escuela e INNER JOIN region r ON ";
        sql += "e.region_id=r.region_id WHERE escuela_id=? ORDER BY e.nombre ASC";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, escuela_id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {

            map = new LinkedHashMap<>();

            map.put("escuela_id", rs.getString("escuela_id"));
            map.put("escuela_nombre", rs.getString("e_nombre"));
            map.put("ciudad", rs.getString("ciudad"));

            VORegion region = new VORegion(rs.getString("region_id"), rs.getString("r_nombre"));
            map.put("region", region);

        }
        if (map == null) {
            throw new Exception("No existe la escuela seleccionada");
        }
        return map;
    }

    public static boolean delete(String escuela_id) throws Exception, SQLException {
        String sql = "DELETE FROM escuela WHERE escuela_id=?";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);

        stmt.setString(1, escuela_id);

        int resultado = stmt.executeUpdate();
        System.out.println("Resultado de la eliminacion:" + resultado);
        if (resultado == 1) {
            return true;
        } else {
            return false;
        }
    }

    public int save() throws Exception, SQLException {
        int resultado = 0;

        String sql = "INSERT INTO escuela(escuela_id, nombre, ciudad, region_id) ";
        sql += "VALUES(?,?,?,?)";

        PreparedStatement stmt = null;
        Connection cnx = new Conexion().getConexion();

        stmt = cnx.prepareStatement(sql);
        stmt.setString(1, this.escuela_id);
        stmt.setString(2, this.nombre);
        stmt.setString(3, this.ciudad);
        stmt.setString(4, this.region_id);

        resultado = stmt.executeUpdate();

        stmt.close();

        if (resultado == 0) {
            throw new Exception("No se ha registrado la escuela");
        }

        return resultado;
    }
//    
//    public Map<String, String> iniciar() throws SQLException, Exception{
//        Map<String, String> cuenta = null;
//        String sql = "SELECT * FROM cuenta WHERE email=? AND password=?";
//       
//        Connection cnx = new Conexion().getConexion();
//        
//        PreparedStatement stmt = cnx.prepareStatement(sql);
//        stmt.setString(1, this.email);
//        stmt.setString(2, this.password);
//        
//        ResultSet rs = stmt.executeQuery();
//        while(rs.next()){
//            cuenta = new LinkedHashMap();
//            cuenta.put("cuenta_id", rs.getString("cuenta_id"));
//            cuenta.put("rut", rs.getString("rut"));
//            cuenta.put("nombre", rs.getString("nombre"));
//            cuenta.put("apellido_p", rs.getString("apellido_p"));
//            cuenta.put("apellido_m", rs.getString("apellido_m"));
//            cuenta.put("email", rs.getString("email"));
//            cuenta.put("rol_id", rs.getString("rol_id"));
//            
//
//        }
//        if(cuenta==null){
//            throw new Exception("El correo electronico y/o la contraseña no coinciden");
//        }
//        return cuenta;
//    }
}
