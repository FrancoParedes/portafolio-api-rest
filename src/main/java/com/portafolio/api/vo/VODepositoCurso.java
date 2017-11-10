/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.portafolio.api.vo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author FRANCO
 */
public class VODepositoCurso {
    private String curso_id, deposito_id, actividad_id;
    private int prorrateo;
    private Date actividad_fecha;

    public VODepositoCurso(String curso_id, String deposito_id, String actividad_id, String actividad_fecha) throws Exception {
        setCurso_id(curso_id);
        this.deposito_id = deposito_id;
        setActividad_id(actividad_id);
        setActividad_fecha(actividad_fecha);
    }

    
    public String getCurso_id() {
        return curso_id;
    }

    public void setCurso_id(String curso_id) throws Exception {
        if(curso_id.trim().length()==0){
            throw new Exception("No existe el curso ingresado");
        }
        if(!VOCurso.check(curso_id)){
            throw new Exception("No existe el curso ingresado");
        }
        this.curso_id = curso_id;
    }

    public String getDeposito_id() {
        return deposito_id;
    }

    public void setDeposito_id(String deposito_id) {
        this.deposito_id = deposito_id;
    }

    public String getActividad_id() {
        return actividad_id;
    }

    public void setActividad_id(String actividad_id) throws Exception, SQLException {
        if(actividad_id.trim().length()==0){
            throw new Exception("Ingresa la actividad");
        }
        if(!VOActividad.check(actividad_id)){
            throw new Exception("La actividad no existe");
        }
        this.actividad_id = actividad_id;
    }

    public int getProrrateo() {
        return prorrateo;
    }

    public void setProrrateo(int prorrateo) {
        this.prorrateo = prorrateo;
    }

    public Date getActividad_fecha() {
        return actividad_fecha;
    }

    public void setActividad_fecha(String actividad_fecha) throws  Exception {
        if(actividad_fecha.equals("")){
            throw new Exception("Ingresa la fecha de la actividad");
        }else {
            try{
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

                Date date = formatter.parse(actividad_fecha);

                this.actividad_fecha = date;
            }catch(ParseException ex){
                throw new Exception("Ingresa correctamente la fecha de la actividad(dd-mm-aaaa)");
            }
        }
        
    }
    
    public int save(Connection cnx) throws Exception, SQLException{
        int resultado = 0;
        
        String sql = "INSERT INTO deposito_curso(curso_id, deposito_id, prorrateo, actividad_id, actividad_fecha) ";
        sql+= "VALUES(?, ?, ?, ? , ?)";
        
        PreparedStatement stmt = null;
        
        stmt = cnx.prepareStatement(sql);
        stmt.setString(1, this.curso_id);
        stmt.setString(2, this.deposito_id);
        stmt.setInt(3, this.prorrateo);
        stmt.setString(4, this.actividad_id);
        java.sql.Date dateSQL = new java.sql.Date(actividad_fecha.getTime());
        stmt.setDate(5, dateSQL);

        resultado = stmt.executeUpdate();
 
        if(resultado==0){
            throw new Exception("No se ha registrado el deposito del curso");
        }
        
        
        return resultado;
    }
}
