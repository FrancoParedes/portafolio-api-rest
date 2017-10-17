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
@Path("/destinos")
public class DestinoService {
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getDestinos(){
        Response res;
        try {
            res = Response.ok().entity(VODestino.all()).build();
        }catch (Exception e) {
            Map msj = new HashMap();
            msj.put("message", e.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }
                
        return res;
    }
    
    @GET
    @Path("{destino_id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response find(@PathParam("destino_id") String destino_id){
        Response res;
        try {
            res = Response.ok().entity(VODestino.find(destino_id)).build();
        } catch (Exception ex) {
            Map msj = new HashMap();
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }
        
        return res;
    }
    
    @DELETE
    @Path("{destino_id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("destino_id") String destino_id){
        Response res;
        try {
            res = Response.ok().entity(VODestino.delete(destino_id)).build();
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
            @DefaultValue("") @FormParam("nombre") String nombre)
            { 
        Response res;
        try {
            VODestino destino = new VODestino(nombre);
            destino.save();
            res = Response.ok().entity(destino).build();
        } catch (Exception ex) {
            Map msj = new HashMap();
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }
        
        return res;
    }
    
    
    
    
}
