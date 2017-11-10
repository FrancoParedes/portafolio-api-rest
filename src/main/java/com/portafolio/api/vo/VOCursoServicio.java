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
public class VOCursoServicio {

    private String curso_id, servicio_id;

    public VOCursoServicio() {
    }

    public VOCursoServicio(String curso_id, String servicio_id) {
        this.curso_id = curso_id;
        this.servicio_id = servicio_id;
    }

    public String getCurso_id() {
        return curso_id;
    }

    public void setCurso_id(String curso_id) {
        this.curso_id = curso_id;
    }

    public String getServicio_id() {
        return servicio_id;
    }

    public void setServicio_id(String servicio_id) {
        this.servicio_id = servicio_id;
    }
    
    
    
    
    public static boolean check(String curso_id, String servicio_id) throws Exception, SQLException {
        String sql = "SELECT curso_id FROM curso_servicio WHERE curso_id=? AND servicio_id=?";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, curso_id);
        stmt.setString(2, servicio_id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            cnx.close();
            return true;
        } else {
            cnx.close();
            return false;
        }
    }

    public static List<Map> all(String curso_id) throws Exception, SQLException {
        List<Map> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.servicio_id, s.descripcion, s.precio FROM curso_servicio cs ");
        sql.append("INNER JOIN servicio s ON cs.servicio_id=s.servicio_id ");
        sql.append("WHERE cs.curso_id=?");

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql.toString());
        stmt.setString(1, curso_id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.println("Fila encontrada");

            Map<String, Object> map = new LinkedHashMap<>();

            map.put("servicio_id", rs.getString("servicio_id"));
            map.put("descripcion", rs.getString("descripcion"));
            map.put("precio", rs.getInt("precio"));
            list.add(map);

        }
        cnx.close();
        if (list.isEmpty()) {
            throw new Exception("El curso no ha contratado servicios");
        }
        return list;
    }

    public static boolean delete(String curso_id, String servicio_id) throws Exception, SQLException {
        String sql = "DELETE FROM curso_servicio WHERE curso_id=? AND servicio_id=?";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);

        stmt.setString(1, servicio_id);

        int resultado = stmt.executeUpdate();
        cnx.close();
        System.out.println("Resultado de la eliminacion:" + resultado);
        if (resultado == 1) {
            return true;
        } else {
            return false;
        }
    }

  
}
