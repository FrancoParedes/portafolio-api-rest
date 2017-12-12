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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author FRANCO
 */
public class VOCursoSeguro {
    private String seguro_id;
    private String curso_id;
    private int cantidad_personas;
    private Date fecha_inicio;
    private Date fecha_termino;
    private int precio;

    public VOCursoSeguro(String seguro_id, String curso_id, String cantidad_personas, String fecha_inicio, String fecha_termino, String precio) throws Exception {
        setSeguro_id(seguro_id);
        setCurso_id(curso_id);
        setCantidad_personas(cantidad_personas);
        setFecha_inicio(fecha_inicio);
        setFecha_termino(fecha_termino);
        setPrecio(precio);
    }

    public String getSeguro_id() {
        return seguro_id;
    }

    public void setSeguro_id(String seguro_id) throws Exception {
        if(seguro_id.trim().length()==0){
             throw new Exception("Selecciona el seguro a asignar");
        }
        if(!VOSeguro.check(seguro_id)){
            throw new Exception("El seguro seleccionado no existe");
        }
        this.seguro_id = seguro_id;
    }

    public String getCurso_id() {
        return curso_id;
    }

    public void setCurso_id(String curso_id) throws Exception {
        if(curso_id.trim().length()==0){
            throw new Exception("Ingresa el curso");
        }
        if(!VOCurso.check(curso_id)){
            throw new Exception("El curso ingresado no existe");
        }
        this.curso_id = curso_id;
    }

    public int getCantidad_personas() {
        return cantidad_personas;
    }

    public void setCantidad_personas(String cantidad_personas) throws Exception {
        int cantidad_personas_int = 0;
        try{
            cantidad_personas_int = Integer.parseInt(cantidad_personas.trim());
        } catch(NumberFormatException e){
            throw new Exception("Ingresa correctamente el número de personas");
        }   
        
        if(cantidad_personas_int <= 0) {
            throw new Exception("La cantidad de personas debe ser mayor a cero");
        }
        int cantidad_alumnos = VOCurso.cantidadAlumnos(this.curso_id);
        
        //Valida que la cantidad de personas a ingresar super la cantidad de alumnos
        int encargados = (int)Math.ceil((double)cantidad_alumnos/(double)4);
        if(cantidad_personas_int<=cantidad_alumnos+encargados){
            throw new Exception("El curso cuenta con "+cantidad_alumnos+" alumnos, ten presente al menos "+ encargados+" encargado(s)");
        }
            this.cantidad_personas = cantidad_personas_int;
            
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) throws Exception {
        if(fecha_inicio.trim().length()==0){
            throw new Exception("Ingresa la fecha de inicio que cubre el seguro");
        }
        try{
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
            
            Date date = formato.parse(fecha_inicio);
            
            this.fecha_inicio = date;
        }catch(ParseException ex){
            throw new Exception("Ingresa correctamente la fecha de inicio (dd-mm-yyyy)");
        }
    }

    public Date getFecha_termino() {
        
        return fecha_termino;
    }

    public void setFecha_termino(String fecha_termino) throws Exception {
        if(fecha_termino.trim().length()==0){
            throw new Exception("Ingresa la fecha de termino del seguro");
        }
        try{
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
            
            Date date = formato.parse(fecha_termino);
            
            this.fecha_termino = date;
        }catch(ParseException ex){
            throw new Exception("Ingresa correctamente la fecha de termino (dd-mm-yyyy)");
        }
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) throws Exception {
        try{
            int precio_int = Integer.parseInt(precio);     
            
            if(precio_int<=0){
                throw new Exception("Ingresa un valor mayor a cero");
            }
            this.precio = Integer.parseInt(precio);
        }catch (NumberFormatException ex){
            throw new Exception("Ingresa correctamente el valor total");
        }
        
        
    }
    
    public int save() throws SQLException, Exception{
        String sql = "INSERT INTO curso_seguro (seguro_id, curso_id, cantidad_personas, fecha_inicio, fecha_termino, precio) ";
        sql+= "VALUES (?, ?, ?, ?, ?, ?)";
        
        Connection cnx = new Conexion().getConexion();
        
        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, this.seguro_id);
        stmt.setString(2, this.curso_id);
        stmt.setString(3, Integer.toString(this.cantidad_personas));
        
        //transforma las fechas a Date SQL
        java.sql.Date sql_inicio = new  java.sql.Date(this.fecha_inicio.getTime());
        java.sql.Date sql_termino = new  java.sql.Date(this.fecha_termino.getTime());
        
        stmt.setDate(4, sql_inicio);
        stmt.setDate(5, sql_termino);
        stmt.setInt(6, this.precio);
        
        int resultado = stmt.executeUpdate();
        
        if(resultado==0){
            throw new Exception("No se ha podido registrar el seguro del curso");
        }
        
        return resultado;
    }
    public static List<Map> listar() throws SQLException{
        List<Map> list = new ArrayList<Map>();
        String sql = "SELECT * FROM curso_seguro";
        
        Connection cnx = new Conexion().getConexion();
        PreparedStatement stmt = cnx.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next()){
            Map map = new LinkedHashMap();
            map.put("seguro_id", rs.getString("seguro_id"));
            map.put("curso_id", rs.getString("curso_id"));
            map.put("cantidad_personas", rs.getString("cantidad_personas"));
            map.put("fecha_inicio", rs.getDate("fecha_inicio"));
            map.put("fecha_termino", rs.getDate("fecha_termino"));
            map.put("precio", rs.getInt("precio"));
            
            list.add(map);
        }
        
        cnx.close();
        
        return list;
    }
    public static Map listarPorCurso(String curso_id) throws SQLException, Exception{
        Map map = null;
        
        String sql = "SELECT * FROM curso_seguro WHERE curso_id=?";
        
        Connection cnx = new Conexion().getConexion();
        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, curso_id);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next()){
            
            map = new LinkedHashMap();
            
            map.put("seguro_id", rs.getString("seguro_id"));
            map.put("curso_id", rs.getString("curso_id"));
            map.put("cantidad_personas", rs.getString("cantidad_personas"));
            map.put("fecha_inicio", rs.getDate("fecha_inicio"));
            map.put("fecha_termino", rs.getDate("fecha_termino"));
            map.put("precio", rs.getInt("precio"));
            
        }
        
        cnx.close();
        
        if(map==null) {
            throw new Exception("El curso no ha contratado un seguro");
        }
        return map;
    }
}
