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
public class VOServicio {

    private String servicio_id, destino_id, descripcion;
    private int precio;

    public VOServicio() {
    }

    public VOServicio(String servicio_id, String destino_id, String descripcion, int precio) {
        this.servicio_id = servicio_id;
        this.destino_id = destino_id;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public String getServicio_id() {
        return servicio_id;
    }

    public void setServicio_id(String servicio_id) {
        this.servicio_id = servicio_id;
    }

    public String getDestino_id() {
        return destino_id;
    }

    public void setDestino_id(String destino_id) {
        this.destino_id = destino_id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    
    public static boolean check(String servicio_id) throws Exception, SQLException {
        String sql = "SELECT servicio_id FROM servicio_id WHERE servicio_id=?";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, servicio_id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    public static List<Map> all(String destino_id) throws Exception, SQLException {
        List<Map> list = new ArrayList<>();

        String sql = "SELECT * FROM servicio";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.println("Fila encontrada");

            Map<String, Object> map = new LinkedHashMap<>();

            map.put("servicio_id", rs.getString("servicio_id"));
            map.put("destino_id", rs.getString("destino_id"));
            map.put("descripcion", rs.getString("descripcion"));
            map.put("precio", rs.getInt("precio"));
            list.add(map);

        }
        if (list.isEmpty()) {
            throw new Exception("No se encontraron servicios para el destino seleccionado");
        }
        return list;
    }

    public static Map find(String servicio_id) throws Exception, SQLException {
        Map<String, Object> map = null;

        String sql = "SELECT * FROM servicio WHERE servicio_id=?";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, servicio_id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {

            map = new LinkedHashMap<>();

            map.put("servicio_id", rs.getString("servicio_id"));
            map.put("destino_id", rs.getString("destino_id"));
            map.put("descripcion", rs.getString("descripcion"));
            map.put("precio", rs.getInt("precio"));

        }
        if (map == null) {
            throw new Exception("No existe el servicio seleccionado");
        }
        return map;
    }

    public static boolean delete(String servicio_id) throws Exception, SQLException {
        String sql = "DELETE FROM servicio WHERE servicio_id=?";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);

        stmt.setString(1, servicio_id);

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

        String sql = "INSERT INTO servicio(servicio_id, destino_id, descripcion, precio) ";
        sql += "VALUES(?,?,?,?)";

        PreparedStatement stmt = null;
        Connection cnx = new Conexion().getConexion();

        stmt = cnx.prepareStatement(sql);
        stmt.setString(1, this.servicio_id);
        stmt.setString(2, this.destino_id);
        stmt.setString(3, this.descripcion);
        stmt.setInt(4, this.precio);

        resultado = stmt.executeUpdate();

        stmt.close();

        if (resultado == 0) {
            throw new Exception("No se ha registrado el servicio");
        }

        return resultado;
    }
}
