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
import com.portafolio.api.vo.VODeposito;
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
@Path("/depositos")
public class DepositoService {
    
    @POST
    @Path("{deposito_id}/aprobar")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public Response aprobar(
            @DefaultValue("") @PathParam("deposito_id") String deposito_id)
            { 
        Response res;
        try {
            res = Response.ok().entity(VODeposito.aprobar(deposito_id)).build();
        } catch (Exception ex) {
            Map msj = new HashMap();
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }
        
        return res;
    }
    
    @POST
    @Path("{deposito_id}/rechazar")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public Response rechazar(
            @DefaultValue("") @PathParam("deposito_id") String deposito_id)
            { 
        Response res;
        try {
            res = Response.ok().entity(VODeposito.rechazar(deposito_id)).build();
        } catch (Exception ex) {
            Map msj = new HashMap();
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }
        
        return res;
    }
    
    
    
    
}
