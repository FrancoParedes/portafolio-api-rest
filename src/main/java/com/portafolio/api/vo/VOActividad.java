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
import java.util.ArrayList;
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
public class VOCuenta {
    private String cuenta_id, rut,nombre, apellido_p, apellido_m, email, telefono, celular, password;
    char sexo;
    int rol_id;

    public VOCuenta() {
    }

    public VOCuenta(String rut, String nombre, String apellido_p, String apellido_m, String email, String telefono, String celular, String password, char sexo, int rol_id) throws Exception{
        setId("");
        setRut(rut);
        setNombre(nombre);
        setApellido_p(apellido_p);
        setApellido_m(apellido_m);
        setEmail(email);
        setTelefono(telefono);
        setCelular(celular);
        setPassword(password);
        setSexo(sexo);
        setRol_id(rol_id);
    } 
    
    public VOCuenta(String email, String password) throws Exception{
        setEmail(email);
        setPassword(password);
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCuenta_id() {
        return cuenta_id;
    }

    public void setId(String cuenta_id) {
        if (cuenta_id.trim().length()==0) {
            this.cuenta_id = Random.alfanumeric(10);
        }else {
            this.cuenta_id = cuenta_id;
        }
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getApellido_p() {
        return apellido_p;
    }

    public void setApellido_p(String apellido_p) {
        this.apellido_p = apellido_p;
    }

    public String getApellido_m() {
        return apellido_m;
    }

    public void setApellido_m(String apellido_m) {
        this.apellido_m = apellido_m;
    }

    

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }
    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception {
        if(email.trim().length()==0){
            throw new Exception("Ingresa un correo electronico");
        }
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws Exception {
        if(password.trim().length()==0){
            throw new Exception("Ingresa una contraseña");
        }
        this.password = password;
    }

    public int getRol_id() {
        return rol_id;
    }

    public void setRol_id(int rol_id) {
        this.rol_id = rol_id;
    }

    
    
    public static List<VOCuenta> all() throws Exception, SQLException{
        List<VOCuenta> list = new ArrayList<>();
        
        String sql = "SELECT * FROM cuenta";
       
        Connection cnx = new Conexion().getConexion();
        
            PreparedStatement stmt = cnx.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                System.out.println("Fila encontrada");
                VOCuenta cuenta = new VOCuenta();
                cuenta.cuenta_id = rs.getString("cuenta_id");
                cuenta.rut = rs.getString("rut");
                cuenta.nombre = rs.getString("nombre");
                cuenta.apellido_p = rs.getString("apellido_p");
                cuenta.apellido_m = rs.getString("apellido_m");
                cuenta.sexo = rs.getString("sexo").charAt(0);
                cuenta.email = rs.getString("email");
                cuenta.telefono = rs.getString("telefono");
                cuenta.celular = rs.getString("celular");
                cuenta.rol_id = rs.getInt("rol_id");
                list.add(cuenta);
                
            }
            if(list.isEmpty()){
                throw new Exception("No se encontraron cuentas registradas");
            }
        return list;
    }
    
    
    public static VOCuenta find(String cuenta_id) throws Exception, SQLException{
        VOCuenta cuenta = null;
        String sql = "SELECT * FROM cuenta WHERE cuenta_id=?";
       
        Connection cnx = new Conexion().getConexion();
        
        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, cuenta_id);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            
            cuenta = new VOCuenta();
            
            cuenta.cuenta_id = rs.getString("cuenta_id");
            cuenta.rut = rs.getString("rut");
            cuenta.nombre = rs.getString("nombre");
            cuenta.apellido_p = rs.getString("apellido_p");
            cuenta.apellido_m = rs.getString("apellido_m");
            cuenta.sexo = rs.getString("sexo").charAt(0);
            cuenta.email = rs.getString("email");
            cuenta.telefono = rs.getString("telefono");
            cuenta.celular = rs.getString("celular");
            cuenta.rol_id = rs.getInt("rol_id");

        }
        if(cuenta==null){
            throw new Exception("No existe el numero de cuenta");
        }
        return cuenta;
    }
    public boolean update() throws Exception, SQLException{
        String sql = "UPDATE cuenta SET nombre=?, apellido_p=?, apellido_m=?, sexo=?, email=?, ";
        sql+= "telefono=?, celular=?, rol_id=? WHERE cuenta_id=?";
       
        Connection cnx = new Conexion().getConexion();
        
        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, this.nombre);
        stmt.setString(2, this.apellido_p);
        stmt.setString(3, this.apellido_m);
        stmt.setString(4, String.valueOf(this.sexo));
        stmt.setString(5, this.email);
        stmt.setString(6, this.telefono);
        stmt.setString(7, this.celular);
        stmt.setInt(8, this.rol_id);
        stmt.setString(9, this.cuenta_id);
        
        System.out.println("ID CUENTA: " + this.cuenta_id);
        
        int resultado = stmt.executeUpdate();
        System.out.println("Resultado de la actualizacion:" + resultado);
        if(resultado==1){
            return true;
        }else {
            return false;
        }
    }
    
    public static boolean delete(String cuenta_id) throws Exception, SQLException{
        String sql = "DELETE FROM cuenta WHERE cuenta_id=?";
       
        Connection cnx = new Conexion().getConexion();
        
        PreparedStatement stmt = cnx.prepareStatement(sql);
        
        stmt.setString(1, cuenta_id);
        
        
        int resultado = stmt.executeUpdate();
        System.out.println("Resultado de la eliminacion:" + resultado);
        if(resultado==1){
            return true;
        }else {
            return false;
        }
    }
    
    
    public int save() throws Exception, SQLException{
        int resultado = 0;
        
        String sql = "INSERT INTO cuenta(cuenta_id, rut, nombre, apellido_p, apellido_m, sexo, email, telefono, celular, password, rol_id) ";
        sql+= "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        
        PreparedStatement stmt = null;
        Connection cnx = new Conexion().getConexion();
        
        stmt = cnx.prepareStatement(sql);
        stmt.setString(1, this.cuenta_id);
        stmt.setString(2, this.rut);
        stmt.setString(3, this.nombre);
        stmt.setString(4, this.apellido_p);
        stmt.setString(5, this.apellido_m);
        stmt.setString(6, String.valueOf(this.sexo));
        stmt.setString(7, this.email);
        stmt.setString(8, this.telefono);
        stmt.setString(9, this.celular);
        stmt.setString(10, this.password);
        stmt.setInt(11, this.rol_id);

        resultado = stmt.executeUpdate();

        stmt.close();
        
        if(resultado==0){
            throw new Exception("No se ha registrado la cuenta");
        }
        
        
        return resultado;
    }
    
    public Map<String, String> iniciar() throws SQLException, Exception{
        Map<String, String> cuenta = null;
        String sql = "SELECT * FROM cuenta WHERE email=? AND password=?";
       
        Connection cnx = new Conexion().getConexion();
        
        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, this.email);
        stmt.setString(2, this.password);
        
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            cuenta = new LinkedHashMap();
            cuenta.put("cuenta_id", rs.getString("cuenta_id"));
            cuenta.put("rut", rs.getString("rut"));
            cuenta.put("nombre", rs.getString("nombre"));
            cuenta.put("apellido_p", rs.getString("apellido_p"));
            cuenta.put("apellido_m", rs.getString("apellido_m"));
            cuenta.put("email", rs.getString("email"));
            cuenta.put("rol_id", rs.getString("rol_id"));
            

        }
        if(cuenta==null){
            throw new Exception("El correo electronico y/o la contraseña no coinciden");
        }
        return cuenta;
    }
}
