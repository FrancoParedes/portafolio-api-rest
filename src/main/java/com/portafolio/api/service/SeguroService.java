/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.portafolio.api.service;

import com.portafolio.api.vo.VOActividad;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.portafolio.api.vo.VOCuenta;
import com.portafolio.api.vo.VODestino;
import com.portafolio.api.vo.VOSeguro;
import com.portafolio.api.vo.VOServicio;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author FRANCO
 */
@Path("/seguros")
public class SeguroService {
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getSeguros(){
        Response res;
        try {
            res = Response.ok().entity(VOSeguro.all()).build();
        }catch (Exception e) {
            Map msj = new HashMap();
            msj.put("message", e.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }
                
        return res;
    }
    
    @GET
    @Path("{seguro_id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response find(@PathParam("seguro_id") String seguro_id){
        Response res;
        try {
            res = Response.ok().entity(VOSeguro.find(seguro_id)).build();
        } catch (Exception ex) {
            Map msj = new HashMap();
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }
        
        return res;
    }
    
    @DELETE
    @Path("{seguro_id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("seguro_id") String seguro_id){
        Response res;
        try {
            res = Response.ok().entity(VOSeguro.delete(seguro_id)).build();
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
    public Response guardaSeguro(
            @DefaultValue("") @FormParam("destino_id") String destino_id,
            @DefaultValue("") @FormParam("descripcion") String descripcion,
            @DefaultValue("0") @FormParam("valor_persona") String valor_persona_string)
            { 
        Response res;
        try {
            int valor_persona = Integer.parseInt(valor_persona_string);
            VOSeguro seguro =  new VOSeguro(destino_id, descripcion, valor_persona);
            seguro.save();
            res = Response.ok().entity(seguro).build();
        } catch(NumberFormatException ex){
            Map msj = new HashMap();
            msj.put("message", "Ingresa correctamente el valor por persona");
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }
        catch (Exception ex) {
            Map msj = new HashMap();
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }
        
        return res;
    }
    
    
    
    
}
