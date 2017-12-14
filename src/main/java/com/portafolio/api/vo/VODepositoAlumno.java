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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author FRANCO
 */
public class VODepositoAlumno {
    private String alumno_id, deposito_id;

    public VODepositoAlumno(String alumno_id, String deposito_id) throws Exception {
        setAlumno_id(alumno_id);
        setDeposito_id(deposito_id);
    }

    public String getAlumno_id() {
        return alumno_id;
    }

    public void setAlumno_id(String alumno_id) throws Exception {
        if(alumno_id.trim().length()==0){
            throw new Exception("Selecciona el alumnos para depositar");
        }
        if(!VOAlumno.check(alumno_id)){
            throw new Exception("El alumno no existe");
        }
        this.alumno_id = alumno_id;
    }

    public String getDeposito_id() {
        return deposito_id;
    }

    public void setDeposito_id(String deposito_id) {
        this.deposito_id = deposito_id;
    }
    
    public static List<Map> getDepositos(String alumno_id) throws SQLException, Exception {
        
        List<Map> list = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        



        sb.append("SELECT d.deposito_id, d.monto, d.fecha, CASE WHEN d.aprobado=0 THEN 'Pendiente' WHEN d.aprobado=1  THEN 'Aprobado' WHEN d.aprobado=2  THEN 'No aprobado' END as \"ESTADO\" FROM deposito d ");
        sb.append("INNER JOIN deposito_alumno da ON d.deposito_id=da.deposito_id ");
        sb.append("WHERE da.alumno_id=? ");
        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sb.toString());
        stmt.setString(1, alumno_id);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            Map map = new LinkedHashMap();
            map.put("deposito_id", rs.getString("deposito_id"));
            map.put("monto", rs.getInt("monto"));
            map.put("estado", rs.getString("estado"));
            map.put("fecha", rs.getString("fecha"));

            list.add(map);

        }
        cnx.close();

        return list;
    }
    public int save(Connection cnx) throws Exception, SQLException{
        int resultado = 0;
        
        String sql = "INSERT INTO deposito_alumno(alumno_id, deposito_id) ";
        sql+= "VALUES(?, ?)";
        
        PreparedStatement stmt = null;
        
        stmt = cnx.prepareStatement(sql);
        stmt.setString(1, this.alumno_id);
        stmt.setString(2, this.deposito_id);

        resultado = stmt.executeUpdate();

        if(resultado==0){
            throw new Exception("No se ha registrado el deposito para el alumno");
        }
        
        
        return resultado;
    }
}
