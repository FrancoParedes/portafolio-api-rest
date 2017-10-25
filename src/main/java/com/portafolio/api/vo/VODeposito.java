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
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author FRANCO
 */
public class VODeposito {
    private String id, cuenta_id, ruta_comprobante;
    private char tipo_id;
    private int monto;

    public VODeposito() {
    }

    public VODeposito( String cuenta_id, char tipo_id, int monto ,String ruta_comprobante) throws Exception {
        setId();
        setCuenta_id(cuenta_id);
        setTipo_id(tipo_id);
        setMonto(monto);
        setRuta_comprobante(ruta_comprobante);
    }

    
    public String getId() {
        return id;
    }

    public void setId() {
        this.id = Random.alfanumeric(10);
    }

    public String getCuenta_id() {
        return cuenta_id;
    }

    public void setCuenta_id(String cuenta_id) throws Exception {
        if(cuenta_id.trim().length()==0){
            throw new Exception("Ingresa la cuenta");
        }
        if(!VOCuenta.check(cuenta_id)){
            throw new Exception("No existe la cuenta ingresada");
        }
        this.cuenta_id = cuenta_id;
    }

    public String getRuta_comprobante() {
        return ruta_comprobante;
    }

    public void setRuta_comprobante(String ruta_comprobante) throws Exception {
        if(ruta_comprobante.trim().length()==0){
            throw new Exception("Ingresa el comprobante de pago");
        }else if(ruta_comprobante.trim().length()>255){
            throw new Exception("La url del comprobante tiene demasiados caracteres(max 255)");
        }
        this.ruta_comprobante = ruta_comprobante;
    }

    public char getTipo_id() {
        return tipo_id;
    }

    public void setTipo_id(char tipo_id) throws Exception {
        if (tipo_id=='1' | tipo_id=='2') {
            this.tipo_id = tipo_id;
        }else {
            throw new Exception("Ingresa el tipo de deposito correcto (1 o 2)");

        }
        
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) throws Exception {
        if(monto<0){
            throw new Exception("El monto debe mayor a cero");
        }else if(monto==0) {
            throw new Exception("EL monto debe ser mayor a cero");
        }
        this.monto = monto;
    }

    public static List<Map> allByCourse(String curso_id) throws SQLException, Exception{
        if(!VOCurso.check(curso_id)){
            throw new Exception("El curso seleccionado no existe");
        }
        List<Map> list = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT d.deposito_id, CASE WHEN d.aprobado=0 THEN 'Pendiente' WHEN d.aprobado=1  THEN 'Aprobado' WHEN d.aprobado=2  THEN 'No aprobado' END as \"ESTADO\", ");
        sb.append("TO_CHAR(d.monto, '$999G999') as \"MONTO\", TO_CHAR(dc.prorrateo, '$999G999') as \"prorrateo\",ac.nombre as \"ACTIVIDAD\",d.fecha as \"FECHA_DEPOSITO\",dc.actividad_fecha FROM deposito_curso dc ");
        sb.append("INNER JOIN deposito d ON d.deposito_id=dc.deposito_id ");
        sb.append("INNER JOIN actividad ac ON ac.actividad_id=dc.actividad_id ");
        sb.append("WHERE dc.curso_id=? ORDER BY d.fecha DESC");

        System.out.println(sb.toString());
        Connection cnx = new Conexion().getConexion();
        
        
        PreparedStatement stmt = cnx.prepareStatement(sb.toString());
        stmt.setString(1, curso_id);

        ResultSet rs = stmt.executeQuery();

        while(rs.next()){

            Map map = new LinkedHashMap();
            map.put("deposito_id", rs.getString("deposito_id"));
            map.put("monto", rs.getString("MONTO"));
            map.put("prorrateo", rs.getString("prorrateo"));
            map.put("estado", rs.getString("ESTADO"));
            map.put("actividad", rs.getString("ACTIVIDAD"));
            map.put("fecha_actividad", rs.getString("actividad_fecha"));
            map.put("fecha_deposito", rs.getString("FECHA_DEPOSITO"));

            list.add(map);
        }

        return list;
    }

    public boolean depositoCurso(String curso_id, String actividad_id, String actividad_fecha) throws Exception, SQLException{
        boolean exito = false;
        int resultado = 0;

        String sql = "INSERT INTO deposito(deposito_id, cuenta_id, tipo_id, monto, ruta_comprobante) ";
        sql+= "VALUES(?, ?, ?, ? , ?)";
        
        Connection cnx = new Conexion().getConexion();
        cnx.setAutoCommit(false);
        Savepoint save1 = cnx.setSavepoint();
        PreparedStatement stmt = null;

        try {
            
            
            stmt = cnx.prepareStatement(sql);
            stmt.setString(1, this.id);
            stmt.setString(2, this.cuenta_id);
            stmt.setString(3, String.valueOf(this.tipo_id));
            stmt.setInt(4, this.monto);
            stmt.setString(5, this.ruta_comprobante);

            resultado = stmt.executeUpdate();

            if(resultado==1){        
                VODepositoCurso dc = new VODepositoCurso(curso_id, this.id, actividad_id,  actividad_fecha);
                
                int alumnos_total = VOAlumno.contarAlumnosPorCurso(curso_id);
                if(alumnos_total==0){
                    alumnos_total = 1;
                }
                dc.setProrrateo(monto/alumnos_total);
                
                if(dc.save(cnx)==1){
                    exito=true;
                    cnx.commit();
                }else {
                     cnx.rollback(save1);
                }
            }

        }catch(Exception e){
            
            cnx.rollback(save1);
            cnx.setAutoCommit(true);
            stmt.close();
            throw new Exception(e.getMessage());
        }
        
        cnx.setAutoCommit(true);
        stmt.close();
        if(resultado==0){
            throw new Exception("No se ha registrado el deposito");
        }


        return exito;
    }
    
    public boolean depositoAlumno(String alumno_id) throws Exception, SQLException{
        boolean exito = false;
        int resultado = 0;
        
        if(!VOAlumno.checkApoderado(alumno_id, this.cuenta_id)){
            throw new Exception("No puedes realizar depositos a este alumno");
        }

        String sql = "INSERT INTO deposito(deposito_id, cuenta_id, tipo_id, monto, ruta_comprobante) ";
        sql+= "VALUES(?, ?, ?, ? , ?)";
        
        Connection cnx = new Conexion().getConexion();
        cnx.setAutoCommit(false);
        Savepoint save1 = cnx.setSavepoint();
        PreparedStatement stmt = null;

        try {
            
            
            stmt = cnx.prepareStatement(sql);
            stmt.setString(1, this.id);
            stmt.setString(2, this.cuenta_id);
            stmt.setString(3, String.valueOf(this.tipo_id));
            stmt.setInt(4, this.monto);
            stmt.setString(5, this.ruta_comprobante);

            resultado = stmt.executeUpdate();

            if(resultado==1){        
                VODepositoAlumno da = new VODepositoAlumno(alumno_id, this.id);
                
                
                if(da.save(cnx)==1){
                    exito=true;
                    cnx.commit();
                }else {
                     cnx.rollback(save1);
                }
            }

        }catch(Exception e){
            
            cnx.rollback(save1);
            cnx.setAutoCommit(true);
            stmt.close();
            throw new Exception(e.getMessage());
        }
        
        cnx.setAutoCommit(true);
        stmt.close();
        if(resultado==0){
            throw new Exception("No se ha registrado el deposito");
        }


        return exito;
    }
}
