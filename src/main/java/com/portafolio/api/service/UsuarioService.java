/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.portafolio.api.service;

import com.portafolio.api.vo.VOCuenta;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author FRANCO
 */
@Path("/")
public class UsuarioService {
    
    @Path("/iniciar")
    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public Response login(
            @DefaultValue("") @FormParam("email") String email,
            @DefaultValue("") @FormParam("password") String password){
        
        Response res;
        try {
            VOCuenta cuentax = new VOCuenta(email, password);
            res = Response.ok().entity(cuentax.iniciar()).build();
        } catch (Exception ex) {
            Map msj = new HashMap();
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }
        return res;
    }
            
    
}
