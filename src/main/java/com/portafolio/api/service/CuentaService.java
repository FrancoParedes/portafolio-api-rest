/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.portafolio.api.service;

import com.portafolio.api.utils.RespuestaServidor;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.portafolio.api.vo.VOCuenta;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author FRANCO
 */
@Path("/cuentas")
public class CuentaService {
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public RespuestaServidor getCuentas(){
        RespuestaServidor rs = new RespuestaServidor();
        try {
            rs.setBody(VOCuenta.all());
            rs.setStatus("OK");
            rs.setCode(200);
        }catch (Exception e) {
            rs.setStatus("FAIL");
            rs.setCode(404);
            rs.setMessage(e.getMessage());
        }
                
        return rs;
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public Response save(
            @FormParam("rut") String rut,
            @FormParam("nombre") String nombre,
            @FormParam("apellido_p") String apellido_p,
            @FormParam("apellido_m") String apellido_m,
            @FormParam("sexo") String sexo,
            @FormParam("email") String email,
            @FormParam("telefono") String telefono,
            @FormParam("celular") String celular,
            @FormParam("password") String password,
            @FormParam("rol_id") int rol_id)
            { 
        Response res;
        try {
            VOCuenta cuentax = new VOCuenta(rut, nombre, apellido_p, apellido_m,
                    "sdfsdf", telefono, celular, password, 'M', rol_id);
            cuentax.save();
            res = Response.ok().entity(cuentax).build();
        } catch (Exception ex) {
            Map msj = new HashMap();
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }
        
        return res;
    }
}
