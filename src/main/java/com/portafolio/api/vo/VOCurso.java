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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class VOCurso {
    private String curso_id, cuenta_id, escuela_id, destino_id, nivel, anio;
    private int monto_meta;
    private Date fecha_viaje;

    public VOCurso() {
        
    }

    public VOCurso(String cuenta_id, String escuela_id, String destino_id, int monto_meta, String fecha_viaje,
            String nivel, String anio) throws Exception {
        setCurso_id();
        setCuenta_id(cuenta_id);
        setEscuela_id(escuela_id);
        setNivel(nivel);
        setAnio(anio);
        setDestino_id(destino_id);
        setMonto_meta(monto_meta);
        setFecha_viaje(fecha_viaje);
        
    }

    public String getCurso_id() {
        return curso_id;
    }

    public void setCurso_id() {
            this.curso_id = Random.alfanumeric(10);
    }

    public String getCuenta_id() {
        return cuenta_id;
    }

    public void setCuenta_id(String cuenta_id) throws Exception {
        if(cuenta_id.trim().length()==0){
            throw new Exception("Ingresa la cuenta del representante");
        }
        if(!VOCuenta.check(cuenta_id)){
            throw new Exception("La cuenta ingresada no existe");
        }
        this.cuenta_id = cuenta_id;
    }

    public String getEscuela_id() {
        return escuela_id;
    }

    public void setEscuela_id(String escuela_id) throws Exception {
        if(escuela_id.trim().length()==0){
            throw new Exception("Ingresa la escuela");
        }
        if(!VOEscuela.check(escuela_id)){
            throw new Exception("La escuela seleccionada no existe");
        }
        this.escuela_id = escuela_id;
    }

    public String getDestino_id() {
        return destino_id;
    }

    public void setDestino_id(String destino_id) throws Exception {
        if (destino_id.trim().length()==0) {
            throw new Exception("Ingresa el destino");
        }
        if (!VODestino.check(destino_id)) {
            throw new Exception("El destino seleccionado no existe");
        }
        this.destino_id = destino_id;
    }

    public int getMonto_meta() {
        return monto_meta;
    }

    public void setMonto_meta(int monto_meta) throws Exception {
        if (monto_meta==0) {
            throw new Exception("Ingresa el monto a juntar");
        }
        if(monto_meta<0){
            throw new Exception("El monto debe ser mayor a 0");
        }
        this.monto_meta = monto_meta;
    }

    public Date getFecha_viaje() {
        return fecha_viaje;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) throws Exception {
        if(nivel.trim().length()==0){
            throw new Exception("Ingresa el nivel del curso");
        }
        if(nivel.trim().length()>5){
            throw new Exception("Muchos caracteres en el nivel(max 5)");
        }
        this.nivel = nivel.toUpperCase();
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) throws Exception {
        if(anio.trim().length()==0){
            throw new Exception("Ingresa el año");
        }
        int anio_int = 0;
        try {
            anio_int = Integer.parseInt(anio);
        }catch(NumberFormatException ex){
            throw new Exception("Ingresa correctamente el año del curso");
        }
        
        if(anio_int<2017 || anio_int>2050){
            throw new Exception("El año tiene que ser desde el 2017 hasta el 2050");
        }
        this.anio = anio;
    }
    

    public void setFecha_viaje(String fecha_viaje) throws Exception {
        if(fecha_viaje.trim().length()==0){
            throw new Exception("Ingresa la fecha del viaje");
        }
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

            Date date = formatter.parse(fecha_viaje);
            
            this.fecha_viaje = date;
        }catch(ParseException ex){
                throw new Exception("Ingresa correctamente la fecha del viaje(dd-mm-aaaa)");
            }
        
    }

    
    
    
    public static List<Map> all() throws Exception, SQLException{
        List<Map> list = new ArrayList<>();
        
        String sql = "SELECT * FROM curso";
       
        Connection cnx = new Conexion().getConexion();
        
            PreparedStatement stmt = cnx.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                System.out.println("Fila encontrada");
                Map map = new LinkedHashMap();
                map.put("curso_id", rs.getString("curso_id"));
                map.put("nivel", rs.getString("nivel"));
                map.put("anio", rs.getString("anio"));
                map.put("cuenta_id", rs.getString("cuenta_id"));
                map.put("escuela_id", rs.getString("escuela_id"));
                map.put("destino_id", rs.getString("destino_id"));
                map.put("monto_meta",  rs.getInt("monto_meta"));
                map.put("fecha_viaje",  rs.getDate("fecha_viaje"));
                list.add(map);
                
            }
            cnx.close();
            if(list.isEmpty()){
                throw new Exception("No se encontraron cursos registrados");
            }
        return list;
    }
    
    
    public static Map find(String curso_id) throws Exception, SQLException{
        Map map = null;
        String sql = "SELECT c.curso_id, c.cuenta_id, c.contrato_ruta, c.escuela_id, c.destino_id, c.monto_meta, c.fecha_viaje, c.nivel, c.anio, ";
        sql+="e.nombre AS escuela_nombre, d.nombre AS destino_nombre, cta.nombre || ' ' || cta.apellido_p AS nombre_representante, ";
        sql+="cta.telefono, cta.celular, cta.email ";
        sql+="FROM curso c ";
        sql+="INNER JOIN escuela e ON c.escuela_id=e.escuela_id ";
        sql+="INNER JOIN destino d ON c.destino_id=d.destino_id ";
        sql+="INNER JOIN cuenta cta ON c.cuenta_id=cta.cuenta_id ";
        sql+="WHERE c.curso_id=?";
       
        Connection cnx = new Conexion().getConexion();
        
        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, curso_id);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            map = new LinkedHashMap<>();
            
            Map representante = new LinkedHashMap();
            representante.put("id", rs.getString("cuenta_id"));
            representante.put("nombre", rs.getString("nombre_representante"));
            representante.put("telefono", rs.getString("telefono"));
            representante.put("celular", rs.getString("celular"));
            representante.put("email", rs.getString("email"));
            
            Map escuela = new LinkedHashMap();
            escuela.put("id", rs.getString("escuela_id"));
            escuela.put("nombre", rs.getString("escuela_nombre"));
            
            Map destino = new LinkedHashMap();
            destino.put("id", rs.getString("destino_id"));
            destino.put("nombre", rs.getString("destino_nombre"));
            
            
            
            map.put("curso_id", rs.getString("curso_id"));
            map.put("nivel", rs.getString("nivel"));
            map.put("anio", rs.getString("anio"));
            map.put("representante", representante);
            map.put("escuela", escuela);
            map.put("destino",  destino);
            
            try{
                map.put("servicios", VOCursoServicio.all(curso_id));
            }catch(Exception ex){
                System.out.println(ex.getMessage());
                map.put("servicios", null);
            }
            
            map.put("monto_meta", rs.getInt("monto_meta"));
            map.put("fecha_viaje", rs.getDate("fecha_viaje"));
            map.put("alumnos", VOAlumno.listByCourses(rs.getString("curso_id")));
            map.put("contrato_ruta", rs.getString("contrato_ruta"));        

        }
        if(map==null){
            throw new Exception("No existe el curso seleccionado");
        }
        return map;
    }
    public static boolean check(String curso_id) throws Exception, SQLException{
        String sql = "SELECT curso_id FROM curso WHERE curso_id=?";
       
        Connection cnx = new Conexion().getConexion();
        
        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, curso_id);
        ResultSet rs = stmt.executeQuery();
        
        
        if(rs.next()){
            cnx.close();
            return true;
        }else {
            cnx.close();
            return false;
        }
    }
    public int save() throws Exception, SQLException{
        int resultado = 0;
        
        String sql = "INSERT INTO curso(curso_id, cuenta_id, escuela_id, destino_id, monto_meta, fecha_viaje, nivel, anio) ";
        sql+= "VALUES(?,?,?,?,?,?,?,?)";
        
        PreparedStatement stmt = null;
        Connection cnx = new Conexion().getConexion();
        
        stmt = cnx.prepareStatement(sql);
        stmt.setString(1, this.curso_id);
        stmt.setString(2, this.cuenta_id);
        stmt.setString(3, this.escuela_id);
        stmt.setString(4, this.destino_id);
        stmt.setInt(5, this.monto_meta);
        java.sql.Date dateSQL = new java.sql.Date( this.fecha_viaje.getTime());
        stmt.setDate(6, dateSQL);
        stmt.setString(7, this.nivel);
        stmt.setString(8, this.anio);

        resultado = stmt.executeUpdate();

        stmt.close();
        cnx.close();
        if(resultado==0){
            throw new Exception("No se ha registrado el curso");
        }
        
        
        return resultado;
    }
    public static boolean registrarContrato(String curso_id, String contrato_ruta) throws SQLException, Exception{ 
        //Valida el contrato
        validarContrato(curso_id, contrato_ruta);
        
        String sql = "UPDATE curso SET contrato_ruta=? WHERE curso_id=?";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, contrato_ruta);
        stmt.setString(2, curso_id);

        int resultado = stmt.executeUpdate();
        cnx.close();
        System.out.println("Resultado de la actualizacion:" + resultado);
        if (resultado == 1) {
            return true;
        } else {
            return false;
        }
    }
    private static void validarContrato(String curso_id, String contrato_ruta) throws SQLException, Exception{
        if (contrato_ruta.trim().length()==0) {
            throw new Exception("Ingresa el contrato");
        }
        if (contrato_ruta.trim().length()>255) {
            throw new Exception("Demasiados caracteres para el contrato(max 255)");
        }
        if(cantidadAlumnos(curso_id)<15){
            throw new Exception("El curso debe tener minimo 15 alumnos para registrar un contrato");
        }
        
        //consultar si hay algun contrato registrado
        String contrato_registrado=null;
        
        Connection cnx = new Conexion().getConexion();
        PreparedStatement stmt = cnx.prepareStatement("select contrato_ruta from curso where curso_id=?");
        stmt.setString(1, curso_id);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next()){
            contrato_registrado = rs.getString("contrato_ruta");
        }
        
        if(contrato_registrado!=null)
        {
            throw new Exception("Ya se encuentra registrado un contrato");
        }
        
    }
    public static int cantidadAlumnos(String curso_id) throws Exception{
    
        String sql = "select count(alumno_id) from alumno where curso_id=?";
        
        Connection cnx = new Conexion().getConexion();
        int cantidadAlumnos = 0;
        try {
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setString(1, curso_id);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()){
                cantidadAlumnos = rs.getInt(1);
            }
            
            cnx.close();
            
        } catch (SQLException ex) {
            throw new Exception(ex.getMessage());
        }
        
        
        
        
        return cantidadAlumnos;
    }
    
}
