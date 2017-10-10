/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.portafolio.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.portafolio.api.vo.VOCuenta;
import java.util.List;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;

/**
 *
 * @author FRANCO
 */
@Path("/cuentas")
public class CuentaService {
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<VOCuenta> getCuentas(){
        
        return VOCuenta.all();
    }
    
    
    @POST
    @Path("/franco")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public VOCuenta getCuenta(){
        VOCuenta cuentax = new VOCuenta();
        cuentax.setNombre("franco");
        
        return cuentax;
    }
}
