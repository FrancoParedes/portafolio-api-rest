/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.portafolio.api.service;

import com.portafolio.api.vo.VOActividad;
import com.portafolio.api.vo.VOAlumno;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.portafolio.api.vo.VOCuenta;
import com.portafolio.api.vo.VODeposito;
import com.portafolio.api.vo.VODepositoAlumno;
import java.sql.SQLException;
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
@Path("/alumnos")
public class AlumnoService {

    @GET
    @Path("{alumno_id}/depositos")
    @Produces({MediaType.APPLICATION_JSON})
    public Response find(@PathParam("alumno_id") String alumno_id) {
        Response res;
        try {
            res = Response.ok().entity(VODepositoAlumno.getDepositos(alumno_id)).build();
        } catch (Exception ex) {
            Map msj = new HashMap();
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }

        return res;
    }

    @POST
    @Path("{alumno_id}/depositos")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public Response depositar(
            @DefaultValue("") @FormParam("cuenta_id") String cuenta_id,
            @DefaultValue("0") @FormParam("monto") String monto_string,
            @PathParam("alumno_id") String alumno_id,
            @DefaultValue("") @FormParam("ruta_comprobante") String ruta_comprobante) {
        Response res;
        try {
            int monto = Integer.parseInt(monto_string);

            VODeposito deposito = new VODeposito(cuenta_id, '1', monto, ruta_comprobante);
            boolean depositado = deposito.depositoAlumno(alumno_id);
            res = Response.ok().entity(depositado).build();
        } catch (NumberFormatException e) {
            Map msj = new HashMap();
            msj.put("message", "Ingresa correctamente el monto a depositar");
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        } catch (Exception ex) {
            Map msj = new HashMap();
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }

        return res;
    }

    @DELETE
    @Path("{alumno_id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("alumno_id") String alumno_id) {
        Response res;
        try {
            res = Response.ok().entity(VOAlumno.delete(alumno_id)).build();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Map msj = new HashMap();
            msj.put("message", "No se puede el eliminar el alumno");
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        } catch (Exception ex) {
            Map msj = new HashMap();
            msj.put("message", ex.getMessage());
            res = Response.status(Response.Status.UNAUTHORIZED).entity(msj).build();
        }
        return res;
    }

}
