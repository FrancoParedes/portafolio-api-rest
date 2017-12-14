package com.portafolio.api.vo;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.TimeZone;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author FRANCO
 */
public class Conexion {
    String usuario = "system";
    String password = "oracle";

    String host = "104.236.124.24"; // tambien puede ser una ip como "192.168.1.14"
    String puerto = "1521";
    String sid = "XE";

    String driver = "oracle.jdbc.driver.OracleDriver";

    String ulrjdbc = "jdbc:oracle:thin:" + usuario + "/" + password + "@" + host + ":" + puerto + ":" + sid;

    public Conexion() {
    }

    public Connection getConexion() throws Exception {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        TimeZone.setDefault(timeZone);
        Connection con = null;
        try {
            Class.forName(driver).newInstance();
            DriverManager.setLoginTimeout(10);
            con = DriverManager.getConnection(ulrjdbc);
        } catch (SQLException e) {
            throw new Exception(e.toString());
        }
        System.out.println(con);
        return con;
    }
}