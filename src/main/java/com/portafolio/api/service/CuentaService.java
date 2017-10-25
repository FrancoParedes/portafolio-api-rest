/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.portafolio.api.service;

import com.portafolio.api.vo.VOAlumno;
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
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
    public Response getCuentas(){
        Response res;
        try {
            res = Response.ok().entity(VOCuenta.all()).build();
        }catch (Exception e) {
            Map msj = new HashMap();
            msj.put("message", e.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }
                
        return res;
    }
    
    @GET
    @Path("{cuenta_id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response find(@PathParam("cuenta_id") String cuenta_id){
        Response res;
        try {
            res = Response.ok().entity(VOCuenta.find(cuenta_id)).build();
        }catch (SQLException e){
            System.out.println(e.toString());
            Map msj = new HashMap();
            msj.put("message", e.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }catch (Exception ex) {
            Map msj = new HashMap();
             System.out.println(ex.toString());
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }
        
        return res;
    }
    @PUT
    @Path("{cuenta_id}")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(
            @PathParam("cuenta_id") String cuenta_id,
            @FormParam("nombre") String nombre,
            @FormParam("apellido_p") String apellido_p,
            @FormParam("apellido_m") String apellido_m,
            @FormParam("sexo") String sexo,
            @FormParam("email") String email,
            @FormParam("telefono") String telefono,
            @FormParam("celular") String celular,
            @FormParam("rol_id") int rol_id)
          {
        Response res;
        
        
        try {
            VOCuenta cuenta = new VOCuenta();
            cuenta.setId(cuenta_id);
            cuenta.setNombre(nombre);
            cuenta.setApellido_p(apellido_p);
            cuenta.setApellido_m(apellido_m);
            cuenta.setSexo(sexo.charAt(0));
            cuenta.setEmail(email);
            cuenta.setTelefono(telefono);
            cuenta.setCelular(celular);
            cuenta.setRol_id(rol_id);
            
            res = Response.ok().entity(cuenta.update()).build();
            
        } catch (Exception ex) {
            Map msj = new HashMap();
            msj.put("message", ex.toString());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }
        
        return res;
    }
    
    @DELETE
    @Path("{cuenta_id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("cuenta_id") String cuenta_id){
        Response res;
        try {
            res = Response.ok().entity(VOCuenta.delete(cuenta_id)).build();
        } catch (Exception ex) {
            Map msj = new HashMap();
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }
        return res;
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public Response save(
            @DefaultValue("") @FormParam("rut") String rut,
            @DefaultValue("") @FormParam("nombre") String nombre,
            @DefaultValue("") @FormParam("apellido_p") String apellido_p,
            @DefaultValue("") @FormParam("apellido_m") String apellido_m,
            @DefaultValue("N") @FormParam("sexo") String sexo,
            @DefaultValue("") @FormParam("email") String email,
            @DefaultValue("") @FormParam("telefono") String telefono,
            @DefaultValue("") @FormParam("celular") String celular,
            @DefaultValue("") @FormParam("password") String password,
            @DefaultValue("0") @FormParam("rol_id") String rol_id_string)
            { 
        Response res;
        try {
            int rol_id = Integer.parseInt(rol_id_string);
            
            VOCuenta cuentax = new VOCuenta(rut, nombre, apellido_p, apellido_m,
                    email, telefono, celular, password, sexo.charAt(0), rol_id);
            cuentax.save();
            res = Response.ok().entity(cuentax).build();
        }catch (NumberFormatException e) {
            Map msj = new HashMap();
            msj.put("message", "Ingresa correctamente el rol");
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        } catch (Exception ex) {
            Map msj = new HashMap();
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }
        
        return res;
    }
    
    
    @GET
    @Path("{cuenta_id}/alumnos")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAlumnos(@PathParam("cuenta_id") String cuenta_id){
        Response res;
        try {
            res = Response.ok().entity(VOAlumno.getAlumnosApoderado(cuenta_id)).build();
        }catch (SQLException e){
            System.out.println(e.toString());
            Map msj = new HashMap();
            msj.put("message", e.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }catch (Exception ex) {
            Map msj = new HashMap();
             System.out.println(ex.toString());
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }
        
        return res;
    }
    
}
