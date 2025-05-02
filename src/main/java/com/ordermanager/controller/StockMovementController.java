package com.ordermanager.controller;

import com.ordermanager.model.StockMovement;
import com.ordermanager.service.StockMovementService;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/stockmovements")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StockMovementController {

    private StockMovementService stockMovementService;

    public StockMovementController() {
        this.stockMovementService = new StockMovementService();
    }

    @POST
    public Response create(@QueryParam("itemId") Long itemId,
                           @QueryParam("quantity") int quantity) {


        try {
            stockMovementService.create(itemId,quantity);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity("Erro ao criar movimento de stock")
                    .build();
        }
    }

    @GET
    public Response findAll() {
        try {
            List<StockMovement> movements = stockMovementService.findAll();
            return Response.status(Response.Status.OK).entity(movements).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nenhum movimento de stock encontrado")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response find(@PathParam("id") Long id) {
        try {
            StockMovement movement = stockMovementService.findById(id);
            return Response.status(Response.Status.OK).entity(movement).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Movimento de stock não encontrado com ID: " + id)
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        try {
            stockMovementService.delete(id);
            return Response.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Movimento de stock não encontrado com ID: " + id)
                    .build();
        }
    }
}
