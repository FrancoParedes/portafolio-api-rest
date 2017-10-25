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
public class VODepositoTipo {
    private char id;
    private String tipo;

    public VODepositoTipo() {
    }

    public char getId() {
        return id;
    }

    public void setId(char id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public int save() throws Exception, SQLException{
        int resultado = 0;
        
        String sql = "INSERT INTO deposito_tipo(tipo_id, tipo) ";
        sql+= "VALUES(?, ?)";
        
        PreparedStatement stmt = null;
        Connection cnx = new Conexion().getConexion();
        
        stmt = cnx.prepareStatement(sql);
        stmt.setString(1, String.valueOf(this.id));
        stmt.setString(2, this.tipo);

        resultado = stmt.executeUpdate();

        stmt.close();
        
        if(resultado==0){
            throw new Exception("No se ha registrado el tipo de deposito");
        }
        
        
        return resultado;
    }
}
