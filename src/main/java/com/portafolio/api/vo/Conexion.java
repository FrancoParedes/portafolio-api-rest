package com.portafolio.api.vo;


import java.sql.Connection;
import java.sql.DriverManager;

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
    String usuario = "C##_equipo2";
    String password = "portafolio02";

    String host = "165.227.213.232"; // tambien puede ser una ip como "192.168.1.14"
    String puerto = "1521";
    String sid = "dbportafolio";

    String driver = "oracle.jdbc.driver.OracleDriver";

    String ulrjdbc = "jdbc:oracle:thin:" + usuario + "/" + password + "@" + host + ":" + puerto + ":" + sid;

    public Conexion() {
    }

    public Connection getConexion() {
        Connection con = null;
        try {
            Class.forName(driver).newInstance();
            
            con = DriverManager.getConnection(ulrjdbc);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(con);
        return con;
    }
}