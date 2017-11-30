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

/**
 *
 * @author FRANCO
 */
public class VORegion {
    private String region_id, nombre;

    public VORegion(String region_id, String nombre) {
        this.region_id = region_id;
        this.nombre = nombre;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public static boolean check(String region_id) throws Exception, SQLException {
        String sql = "SELECT region_id FROM region WHERE region_id=?";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, region_id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            cnx.close();
            return true;
        } else {
            cnx.close();
            return false;
        }
    }
}
