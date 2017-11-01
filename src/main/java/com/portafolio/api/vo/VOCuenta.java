/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.portafolio.api.vo;

import com.portafolio.api.utils.Email;
import com.portafolio.api.utils.Random;
import com.portafolio.api.utils.Rut;
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

    private String cuenta_id, rut, nombre, apellido_p, apellido_m, email, telefono, celular, password, sexo;

    int rol_id;

    public VOCuenta() {
    }

    public VOCuenta(String rut, String nombre, String apellido_p, String apellido_m, String email, String telefono, String celular, String password, String sexo, int rol_id) throws Exception {
        setId("");
        setRut(rut);
        setNombre(nombre);
        setApellido_p(apellido_p);
        setApellido_m(apellido_m);
        setSexo(sexo);
        setEmail(email);
        setTelefono(telefono);
        setCelular(celular);
        setPassword(password);
        setRol_id(rol_id);
    }

    public VOCuenta(String email, String password) throws Exception {
        setEmail(email);
        setPassword(password);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws Exception {
        if (nombre.trim().length() == 0) {
            throw new Exception("Ingresa el nombre");
        }
        if (nombre.trim().length() < 3) {
            throw new Exception("Nombre no valido");
        }
        if (nombre.trim().length() > 15) {
            throw new Exception("Demasiados caracteres para el nombre(max 15)");
        }
        this.nombre = nombre;
    }

    public String getCuenta_id() {
        return cuenta_id;
    }

    public void setId(String cuenta_id) {
        if (cuenta_id.trim().length() == 0) {
            this.cuenta_id = Random.alfanumeric(10);
        } else {
            this.cuenta_id = cuenta_id;
        }
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) throws Exception {
        if (rut.trim().length() == 0) {
            throw new Exception("Ingresa el RUT");
        }
        if (!Rut.validar(rut)) {
            throw new Exception("RUT no valido");
        }
        if (VOCuenta.checkRut(rut)) {
            throw new Exception("El RUT ingresado ya se encuentra registrado");
        }
        this.rut = rut;
    }

    public String getApellido_p() {
        return apellido_p;
    }

    public void setApellido_p(String apellido_p) throws Exception {
        if (apellido_p.trim().length() == 0) {
            throw new Exception("Ingresa el apellido paterno");
        }

        if (apellido_p.trim().length() > 20) {
            throw new Exception("Demasiados caracteres para el apellido paterno(max 20)");
        }
        this.apellido_p = apellido_p;
    }

    public String getApellido_m() {
        return apellido_m;
    }

    public void setApellido_m(String apellido_m) throws Exception {
        if (apellido_m.trim().length() == 0) {
            throw new Exception("Ingresa el apellido materno");
        }
        if (apellido_m.trim().length() < 3) {
            throw new Exception("Apellido materno no valido");
        }
        if (apellido_m.trim().length() > 20) {
            throw new Exception("Demasiados caracteres para el apellido materno(max 20)");
        }
        this.apellido_m = apellido_m;
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
        this.sexo = sexo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception {
        if (email.trim().length() == 0) {
            throw new Exception("Ingresa un correo electronico");
        }
        if (email.trim().length() > 60) {
            throw new Exception("Demasiados caracteres para el correo(max 60)");
        }
        if (!Email.validar(email)) {
            throw new Exception("Ingresa correctamente el correo electronico");
        }
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) throws Exception {

        if (telefono.trim().length() > 30) {
            throw new Exception("Demasiados caracteres para el telefono(max 15)");
        }
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) throws Exception {
        if (celular.trim().length() > 9) {
            throw new Exception("Demasiados caracteres para el celular(max 9)");
        }
        this.celular = celular;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws Exception {
        if (password.trim().length() == 0) {
            throw new Exception("Ingresa una contraseña");
        }
        if (password.trim().length() < 6) {
            throw new Exception("Contraseña demasiada corta (min 6)");
        }
        if (password.trim().length() > 30) {
            throw new Exception("Demasiados caracteres para la contraseña(max 30)");
        }
        this.password = password;
    }

    public int getRol_id() {
        return rol_id;
    }

    public void setRol_id(int rol_id) throws Exception {
        if (rol_id == 0) {
            throw new Exception("Ingresa el rol de la cuenta");
        }
        if (!VOCuentaRol.check(rol_id)) {
            throw new Exception("El rol ingresado no existe");
        }
        this.rol_id = rol_id;
    }

    public static boolean check(String cuenta_id) throws Exception, SQLException {
        String sql = "SELECT cuenta_id FROM cuenta WHERE cuenta_id=?";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, cuenta_id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkRut(String rut) throws Exception, SQLException {
        boolean resultado = false;
        String sql = "SELECT rut FROM cuenta WHERE rut=?";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, rut);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            resultado = true;
        }
        cnx.close();
        return resultado;
    }

    public static boolean checkEmail(String email) throws Exception, SQLException {
        String sql = "SELECT cuenta_id FROM cuenta WHERE email=?";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    public static List<Map> all() throws Exception, SQLException {
        List<Map> list = new ArrayList<>();

        String sql = "SELECT * FROM cuenta";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.println("Fila encontrada");

            Map map = new LinkedHashMap();

            map.put("cuenta_id", rs.getString("cuenta_id"));
            map.put("rut", rs.getString("rut"));
            map.put("nombre", rs.getString("nombre"));
            map.put("apellido_p", rs.getString("apellido_p"));
            map.put("apellido_m", rs.getString("apellido_m"));
            map.put("sexo", rs.getString("sexo").charAt(0));
            map.put("email", rs.getString("email"));
            map.put("telefono", rs.getString("telefono"));
            map.put("celular", rs.getString("celular"));
            map.put("rol_id", rs.getInt("rol_id"));
            list.add(map);

        }
        if (list.isEmpty()) {
            throw new Exception("No se encontraron cuentas registradas");
        }
        return list;
    }

    public static Map find(String cuenta_id) throws Exception, SQLException {
        Map cuenta = null;
        String sql = "SELECT * FROM cuenta WHERE cuenta_id=?";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, cuenta_id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            cuenta = new LinkedHashMap();

            cuenta.put("cuenta_id", cuenta_id);
            cuenta.put("rut", rs.getString("rut"));
            cuenta.put("nombre", rs.getString("nombre"));
            cuenta.put("apellido_p", rs.getString("apellido_p"));
            cuenta.put("apellido_m", rs.getString("apellido_m"));
            cuenta.put("sexo", rs.getString("sexo").charAt(0));
            cuenta.put("email", rs.getString("email"));
            cuenta.put("telefono", rs.getString("telefono"));
            cuenta.put("celular", rs.getString("celular"));
            cuenta.put("rol_id", rs.getInt("rol_id"));

            cuenta.put("alumnos", "/api/cuentas/" + cuenta_id + "/alumnos");

        }
        if (cuenta == null) {
            throw new Exception("No existe el numero de cuenta");
        }
        return cuenta;
    }

    public boolean update() throws Exception, SQLException {
        String sql = "UPDATE cuenta SET nombre=?, apellido_p=?, apellido_m=?, sexo=?, email=?, ";
        sql += "telefono=?, celular=?, rol_id=? WHERE cuenta_id=?";

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
        if (resultado == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean delete(String cuenta_id) throws Exception, SQLException {
        int resultado = 0;
        String sql = "DELETE FROM cuenta WHERE cuenta_id=?";

        try {
            Connection cnx = new Conexion().getConexion();

            PreparedStatement stmt = cnx.prepareStatement(sql);

            stmt.setString(1, cuenta_id);

            resultado = stmt.executeUpdate();

        } catch (SQLException ex) {
            throw new Exception("Ya no puedes eliminar esta cuenta");
        }
        System.out.println("Resultado de la eliminacion:" + resultado);
        if (resultado == 1) {
            return true;
        } else {
            return false;
        }
    }

    public int save() throws Exception, SQLException {

        int resultado = 0;

        if (VOCuenta.checkEmail(email)) {
            throw new Exception("El correo ya se encuentra registrado");
        }

        String sql = "INSERT INTO cuenta(cuenta_id, rut, nombre, apellido_p, apellido_m, sexo, email, telefono, celular, password, rol_id) ";
        sql += "VALUES(?,?,?,?,?,?,?,?,?,?,?)";

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

        if (resultado == 0) {
            throw new Exception("No se ha registrado la cuenta");
        }

        return resultado;
    }

    public Map<String, String> iniciar() throws SQLException, Exception {
        Map<String, String> cuenta = null;
        String sql = "SELECT * FROM cuenta WHERE email=? AND password=?";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, this.email);
        stmt.setString(2, this.password);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            cuenta = new LinkedHashMap();
            cuenta.put("cuenta_id", rs.getString("cuenta_id"));
            cuenta.put("rut", rs.getString("rut"));
            cuenta.put("nombre", rs.getString("nombre"));
            cuenta.put("apellido_p", rs.getString("apellido_p"));
            cuenta.put("apellido_m", rs.getString("apellido_m"));
            cuenta.put("email", rs.getString("email"));
            cuenta.put("rol_id", rs.getString("rol_id"));

        }
        if (cuenta == null) {
            throw new Exception("El correo electronico y/o la contraseña no coinciden");
        }
        return cuenta;
    }

    public static String getRolCuenta(String cuenta_id) throws Exception, SQLException {
        String rol_id = "";

        String sql = "SELECT rol_id FROM cuenta WHERE cuenta_id=?";

        Connection cnx = new Conexion().getConexion();

        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, cuenta_id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            rol_id = rs.getString("rol_id");
        }

        return rol_id;
    }

}
