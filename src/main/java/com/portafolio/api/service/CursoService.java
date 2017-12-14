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
import com.portafolio.api.vo.VOCurso;
import com.portafolio.api.vo.VOCursoSeguro;
import com.portafolio.api.vo.VODeposito;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.util.Date;
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
@Path("/cursos")
public class CursoService {

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getCuros() {
        Response res;
        try {
            res = Response.ok().entity(VOCurso.all()).build();
        } catch (Exception e) {
            Map msj = new HashMap();
            msj.put("message", e.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }

        return res;
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public Response save(
            @DefaultValue("") @FormParam("cuenta_id") String cuenta_id,
            @DefaultValue("") @FormParam("escuela_id") String escuela_id,
            @DefaultValue("") @FormParam("destino_id") String destino_id,
            @DefaultValue("0") @FormParam("monto_meta") String monto_meta_string,
            @DefaultValue("") @FormParam("nivel") String nivel,
            @DefaultValue("") @FormParam("anio") String anio,
            @DefaultValue("") @FormParam("fecha_viaje") String fecha_viaje)
            { 
        Response res;
        try {
            int monto_meta = Integer.parseInt(monto_meta_string.trim());
            VOCurso curso = new VOCurso(cuenta_id, escuela_id, destino_id, monto_meta, fecha_viaje,nivel, anio);
            curso.save();
            
            res = Response.ok().entity(curso).build();
        }catch(NumberFormatException e) {
            System.out.println("Error al guardar curso: " + e.toString());
            Map msj = new HashMap();
            msj.put("message", "Ingresa correctamente el monto");
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }catch (Exception ex) {
            System.out.println("Error al guardar curso: " + ex.toString());
            Map msj = new HashMap();
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }
        
        return res;
    }

    @GET
    @Path("{curso_id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response find(@PathParam("curso_id") String curso_id) {
        Response res;
        try {
            res = Response.ok().entity(VOCurso.find(curso_id)).build();
        } catch (Exception ex) {
            Map msj = new HashMap();
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }

        return res;
    }

    @GET
    @Path("{curso_id}/depositos")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getDepositos(@PathParam("curso_id") String curso_id) {
        Response res;
        try {
            res = Response.ok().entity(VODeposito.allByCourse(curso_id)).build();
        } catch (Exception ex) {
            Map msj = new HashMap();
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }

        return res;
    }

    @POST
    @Path("{curso_id}/depositos")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public Response save(
            @DefaultValue("") @FormParam("cuenta_id") String cuenta_id,
            @DefaultValue("") @FormParam("monto") String monto_string,
            @PathParam("curso_id") String curso_id,
            @DefaultValue("") @FormParam("actividad_id") String actividad_id,
            @DefaultValue("") @FormParam("actividad_fecha") String actividad_fecha,
            @DefaultValue("") @FormParam("ruta_comprobante") String ruta_comprobante) {
        Response res;
        try {
            int monto = Integer.parseInt(monto_string.trim());
            
            VODeposito deposito = new VODeposito(cuenta_id, '2', monto, ruta_comprobante);
            
            boolean depositado = deposito.depositoCurso(curso_id, actividad_id, actividad_fecha);
            
            res = Response.ok().entity(depositado).build();
        }catch (NumberFormatException e) {
            Map msj = new HashMap();
            msj.put("message", "Ingresa correctamente el monto");
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }catch (Exception ex) {
            Map msj = new HashMap();
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }

        return res;
    }
    @PUT
    @Path("{curso_id}/contrato")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public Response registrarContrato(
            @DefaultValue("") @FormParam("contrato_ruta") String contrato_ruta,
            @PathParam("curso_id") String curso_id
    ) {
        Response res;
        try {
            boolean registrado = VOCurso.registrarContrato(curso_id, contrato_ruta);
            
            res = Response.ok().entity(registrado).build();
        }catch (NumberFormatException e) {
            Map msj = new HashMap();
            msj.put("message", "Ingresa correctamente el monto");
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }catch (Exception ex) {
            Map msj = new HashMap();
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }

        return res;
    }
    @GET
    @Path("{curso_id}/seguro")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getSeguroContratado(@PathParam("curso_id") String curso_id) {
        Response res;
        try {
            res = Response.ok().entity(VOCursoSeguro.listarPorCurso(curso_id)).build();
        } catch (Exception ex) {
            Map msj = new HashMap();
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }

        return res;
    }
    @POST
    @Path("{curso_id}/seguro")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public Response setSeguroContratado(
            @PathParam("curso_id") String curso_id,
            @DefaultValue("") @FormParam("seguro_id") String seguro_id,
            @DefaultValue("") @FormParam("cantidad_personas") String cantidad_personas,
            @DefaultValue("") @FormParam("fecha_inicio") String fecha_inicio,
            @DefaultValue("") @FormParam("fecha_termino") String fecha_termino,
            @DefaultValue("") @FormParam("precio") String precio) {
        Response res;
        try {
            VOCursoSeguro seguro = new VOCursoSeguro(seguro_id, curso_id, cantidad_personas, fecha_inicio, fecha_termino, precio);
            
            res = Response.ok().entity(seguro).build();
        }catch (Exception ex) {
            Map msj = new HashMap();
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }

        return res;
    }
}
