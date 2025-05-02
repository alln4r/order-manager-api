package com.ordermanager.controller;

import com.ordermanager.model.Item;
import com.ordermanager.service.ItemService;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemController {

    private ItemService itemService;

    public ItemController() {
        // Injeção manual
        this.itemService = new ItemService();
    }

    @POST
    public Response create(Item item, @Context HttpHeaders headers) {
        System.out.println("Headers recebidos: " + headers.getRequestHeaders());
        System.out.println("Item recebido: " + item);

        try {
            itemService.save(item);
            return Response.status(Response.Status.CREATED).build(); // 204 No Content
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity("Erro: Item não criado: ")
                    .build();
        }
    }

    @GET
    public Response findAll() {

        try {
            List<Item> items =itemService.findAll();
            return Response.status(Response.Status.OK)
                    .entity(items)
                    .build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nenhum Item encontrado")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response find(@PathParam("id") Long id) {
        try {
            Item item = itemService.find(id);
            return Response.status(Response.Status.OK)
                    .entity(item)
                    .build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Item não encontrado com ID: " + id)
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Item item) {
        try {
            Item existing = itemService.find(id);
            existing.setName(item.getName());
            itemService.save(existing);
            return Response.ok(existing).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Item não encontrado com ID: " + id)
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        try {
            Item item = itemService.find(id);
            itemService.delete(item.getId());
            return Response.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Item não encontrado com ID: " + id)
                    .build();
        }
    }
}
