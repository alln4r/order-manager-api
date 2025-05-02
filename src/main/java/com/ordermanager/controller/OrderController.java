package com.ordermanager.controller;

import com.ordermanager.model.Order;
import com.ordermanager.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderController {
    private static final Logger logger = LogManager.getLogger(OrderController.class);
    private OrderService orderService;

    public OrderController() {
        // Injeção manual
        this.orderService = new OrderService();
    }

    @POST
    public Response create(@QueryParam("userId") Long userId,
                           @QueryParam("itemId") Long itemId,
                           @QueryParam("quantity") int quantity) {

        System.out.println("Headers recebidos: " +userId);

        try {

            if (userId == null || itemId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("UserId e ItemId são obrigatórios")
                        .build();
            }

            if (quantity <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Quantity deve ser maior que zero")
                        .build();
            }


            orderService.save(userId, quantity,itemId);
            return Response.status(Response.Status.CREATED).build();

        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (IllegalArgumentException e) {
            logger.warn("Ordem não criada por falta de stock: {}", e.getMessage());
            return Response.status(Response.Status.CONFLICT) // ou .status(422)
                    .entity(e.getMessage())
                    .build();
        }

        catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao criar pedido: " + e.getMessage())
                    .build();
        }
    }

    @GET
    public Response findAll() {

        try {
            List<Order> order =orderService.findAll();
            return Response.status(Response.Status.OK)
                    .entity(order)
                    .build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nenhuma Order encontrada")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response find(@PathParam("id") Long id) {

        try {
            Order order = orderService.find(id);
            return Response.status(Response.Status.OK)
                    .entity(order)
                    .build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Order não encontrada com ID: " + id)
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {

        try {
            Order order = orderService.find(id);
            orderService.delete(order.getId());
            return Response.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Order não encontrada com ID: " + id)
                    .build();
        }
    }
}
