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
@Path("/actividades")
public class ActividadService {
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getActividades(){
        Response res;
        try {
            res = Response.ok().entity(VOActividad.all()).build();
        }catch (Exception e) {
            Map msj = new HashMap();
            msj.put("message", e.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }
                
        return res;
    }
    
    @GET
    @Path("{actividad_id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response find(@PathParam("actividad_id") String actividad_id){
        Response res;
        try {
            res = Response.ok().entity(VOActividad.find(actividad_id)).build();
        } catch (Exception ex) {
            Map msj = new HashMap();
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }
        
        return res;
    }
    
    @DELETE
    @Path("{actividad_id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("actividad_id") String actividad_id){
        Response res;
        try {
            res = Response.ok().entity(VOActividad.delete(actividad_id)).build();
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
            VOActividad actividadx = new VOActividad(nombre);
            actividadx.save();
            res = Response.ok().entity(actividadx).build();
        } catch (Exception ex) {
            Map msj = new HashMap();
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }
        
        return res;
    }
    
    
    
    
}
