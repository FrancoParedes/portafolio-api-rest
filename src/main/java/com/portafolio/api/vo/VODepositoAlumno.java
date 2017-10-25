/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.portafolio.api.vo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    
    public int save(Connection cnx) throws Exception, SQLException{
        int resultado = 0;
        
        String sql = "INSERT INTO deposito_alumno(alumno_id, deposito_id) ";
        sql+= "VALUES(?, ?)";
        
        PreparedStatement stmt = null;
        
        stmt = cnx.prepareStatement(sql);
        stmt.setString(1, this.alumno_id);
        stmt.setString(2, this.deposito_id);

        resultado = stmt.executeUpdate();

        stmt.close();
        
        if(resultado==0){
            throw new Exception("No se ha registrado el deposito para el alumno");
        }
        
        
        return resultado;
    }
}
