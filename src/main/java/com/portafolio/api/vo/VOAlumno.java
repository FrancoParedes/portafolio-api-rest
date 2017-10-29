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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author FRANCO
 */
public class VOAlumno {

    private String id, nombre, apellido, sexo, curso_id, cuenta_id;

    public VOAlumno() {
    }

    public VOAlumno(String nombre, String apellido, String sexo, String curso_id, String cuenta_id) throws Exception {
        setId();
        setNombre(nombre);
        setApellido(apellido);
        setSexo(sexo);
        setCurso_id(curso_id);
        setCuenta_id(cuenta_id);
    }

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = Random.alfanumeric(10);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws Exception {
        if (nombre.trim().length() == 0) {
            throw new Exception("Ingresa el nombre del alumno");
        }
        if (nombre.trim().length() > 15) {
            throw new Exception("Demasiados caracteres para el nombre (15max)");
        }
        this.nombre = nombre.toUpperCase();
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) throws Exception {
        if (apellido.trim().length() == 0) {
            throw new Exception("Ingresa el apellido del alumno");
        }
        if (apellido.trim().length() > 15) {
            throw new Exception("Demasiados caracteres para el apellido (15max)");
        }

        this.apellido = apellido.toUpperCase();
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) throws Exception {
        if (sexo.toUpperCase().equals("M") || sexo.toUpperCase().equals("F")) {
            this.sexo = sexo.toUpperCase();
        } else {
            throw new Exception("Selecciona correctamente el sexo");
        }
    }

    public String getCurso_id() {
        return curso_id;
    }

    public void setCurso_id(String curso_id) throws Exception {
        if (curso_id.trim().length() == 0) {
            throw new Exception("Ingresa el curso del alumno");
        }
        if (!VOCurso.check(curso_id)) {
            throw new Exception("El curso seleccionado no existe");
        }
        this.curso_id = curso_id;
    }

    public String getCuenta_id() {
        return cuenta_id;
    }

    public void setCuenta_id(String cuenta_id) throws Exception {
        if (cuenta_id.trim().length() == 0) {
            throw new Exception("Ingresas la cuenta del apoderado");
        }
        if (!VOCuenta.check(cuenta_id)) {
            throw new Exception("La cuenta del apoderado no existe");
        }

        this.cuenta_id = cuenta_id;
    }

    public static boolean check(String alumno_id) throws Exception, SQLException {
        String sql = "SELECT alumno_id FROM alumno WHERE alumno_id=?";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, alumno_id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkApoderado(String alumno_id, String cuenta_id) throws Exception, SQLException {
        String sql = "SELECT alumno_id FROM alumno WHERE alumno_id=? AND cuenta_id=?";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, alumno_id);
        stmt.setString(2, cuenta_id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean delete(String alumno_id) throws Exception, SQLException {
        String sql = "DELETE FROM alumno WHERE alumno_id=?";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);

        stmt.setString(1, alumno_id);

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

        String sql = "INSERT INTO alumno(alumno_id, nombre, apellido, sexo, curso_id, cuenta_id) ";
        sql += "VALUES(?,?,?,?,?,?)";

        PreparedStatement stmt = null;
        Connection cnx = new Conexion().getConexion();

        stmt = cnx.prepareStatement(sql);
        stmt.setString(1, this.id);
        stmt.setString(2, this.nombre);
        stmt.setString(3, this.apellido);
        stmt.setString(4, this.sexo);
        stmt.setString(5, this.curso_id);
        stmt.setString(6, this.cuenta_id);

        resultado = stmt.executeUpdate();

        stmt.close();

        if (resultado == 0) {
            throw new Exception("No se ha registrado el alumno");
        }

        return resultado;
    }

    public static List<Map> listByCourses(String curso_id) throws Exception, SQLException {
        List<Map> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT  alu.alumno_id, alu.nombre, alu.apellido, alu.cuenta_id, sum(s.precio) as meta,");
        sql.append("(SELECT sum(monto) FROM deposito d ");
        sql.append("INNER JOIN deposito_alumno da ON d.deposito_id=da.deposito_id WHERE da.alumno_id=alu.alumno_id) AS actual, ");
        sql.append("(SELECT sum(prorrateo) FROM deposito_curso where curso_id=alu.curso_id) AS prorrateo ");
        sql.append("FROM alumno alu ");
        sql.append("INNER JOIN curso_servicio cs on alu.curso_id=cs.curso_id ");
        sql.append("INNER JOIN servicio s on cs.servicio_id=s.servicio_id ");
        sql.append("WHERE alu.curso_id=? ");
        sql.append("GROUP BY alu.alumno_id,alu.nombre, alu.apellido,alu.cuenta_id, alu.curso_id ");
        sql.append("ORDER BY alu.apellido");
        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql.toString());
        stmt.setString(1, curso_id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.println("Fila encontrada");
            Map alumno = new LinkedHashMap();
            alumno.put("id", rs.getString("alumno_id"));
            alumno.put("nombre", rs.getString("nombre"));
            alumno.put("apellido", rs.getString("apellido"));
            alumno.put("cuenta_id", rs.getString("cuenta_id"));
            alumno.put("meta", rs.getString("meta"));
            alumno.put("actual", rs.getInt("actual") + rs.getInt("prorrateo"));
            list.add(alumno);

        }

        return list;
    }

    public static int contarAlumnosPorCurso(String curso_id) throws Exception, SQLException {
        String sql = "SELECT count(alumno_id) as count FROM alumno WHERE curso_id=?";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, curso_id);
        ResultSet rs = stmt.executeQuery();

        int num = 0;
        while (rs.next()) {
            num = rs.getInt("count");
        }

        return num;
    }

    public static List<Map> getAlumnosApoderado(String cuenta_id) throws SQLException, Exception {
        if (!VOCuenta.check(cuenta_id)) {
            throw new Exception("La cuenta no existe");
        }
        List<Map> list = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append("select alu.alumno_id, alu.nombre, alu.apellido, alu.curso_id, ");
        sb.append("c.nivel, c.anio, e.nombre as escuela from alumno alu  ");
        sb.append("INNER JOIN curso c ON alu.curso_id=c.curso_id ");
        sb.append("INNER JOIN escuela e ON c.escuela_id=e.escuela_id ");
        sb.append("WHERE alu.cuenta_id=?");

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sb.toString());
        stmt.setString(1, cuenta_id);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            Map map = new LinkedHashMap();
            map.put("alumno_id", rs.getString("alumno_id"));
            map.put("nombre", rs.getString("nombre"));
            map.put("apellido", rs.getString("apellido"));
            map.put("curso_id", rs.getString("curso_id"));     
            map.put("curso", "/api/cursos/"+rs.getString("curso_id"));
            map.put("nivel", rs.getString("nivel"));
            map.put("anio", rs.getString("anio"));
            map.put("escuela", rs.getString("escuela"));

            map.put("depositos", "/api/alumnos/" + rs.getString("alumno_id") + "/depositos");

            map.put("depositos_curso", "/api/cursos/" + rs.getString("curso_id") + "/depositos");

            list.add(map);

        }

        return list;
    }
//    
//    
//    public static VOAlumno find(String actividad_id) throws Exception, SQLException{
//        VOAlumno actividad = null;
//        String sql = "SELECT * FROM actividad WHERE actividad_id=?";
//       
//        Connection cnx = new Conexion().getConexion();
//        
//        PreparedStatement stmt = cnx.prepareStatement(sql);
//        stmt.setString(1, actividad_id);
//        ResultSet rs = stmt.executeQuery();
//        while(rs.next()){
//            
//            actividad = new VOAlumno();
//            
//            actividad.actividad_id = rs.getString("actividad_id");
//            actividad.nombre = rs.getString("nombre");
//
//        }
//        if(actividad==null){
//            throw new Exception("No existe el numero de cuenta");
//        }
//        return actividad;
//    }    
//    public static boolean delete(String actividad_id) throws Exception, SQLException{
//        String sql = "DELETE FROM actividad WHERE actividad_id=?";
//       
//        Connection cnx = new Conexion().getConexion();
//        
//        PreparedStatement stmt = cnx.prepareStatement(sql);
//        
//        stmt.setString(1, actividad_id);
//        
//        
//        int resultado = stmt.executeUpdate();
//        System.out.println("Resultado de la eliminacion:" + resultado);
//        if(resultado==1){
//            return true;
//        }else {
//            return false;
//        }
//    }
//    
//    
//    public int save() throws Exception, SQLException{
//        int resultado = 0;
//        
//        String sql = "INSERT INTO actividad(actividad_id, nombre) ";
//        sql+= "VALUES(?,?)";
//        
//        PreparedStatement stmt = null;
//        Connection cnx = new Conexion().getConexion();
//        
//        stmt = cnx.prepareStatement(sql);
//        stmt.setString(1, this.actividad_id);
//        stmt.setString(2, this.nombre);
//
//        resultado = stmt.executeUpdate();
//
//        stmt.close();
//        
//        if(resultado==0){
//            throw new Exception("No se ha registrado la actividad");
//        }
//        
//        
//        return resultado;
//    }
}
