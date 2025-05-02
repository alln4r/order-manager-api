package com.ordermanager.controller;

import com.ordermanager.model.User;
import com.ordermanager.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    private UserService userService;

    public UserController() {
        // Injeção manual
        this.userService = new UserService();
    }

    @POST
    public Response create(User user, @Context HttpHeaders headers) {
        logger.info("Headers recebidos: {}", headers.getRequestHeaders());
        logger.info("User recebido para criação: {}", user);

        try {
            userService.save(user);
            return Response.status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação ao criar User: {}", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Erro inesperado ao criar User", e);
            return Response.serverError().entity("Erro ao criar o utilizador").build();
        }
    }

    @GET
    public Response findAll() {
        logger.info("Pedido para listar todos os utilizadores");
        try {
            List<User> users = userService.findAll();
            return Response.ok(users).build();
        } catch (Exception e) {
            logger.error("Erro ao obter lista de utilizadores", e);
            return Response.serverError().entity("Erro ao obter utilizadores").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response find(@PathParam("id") Long id) {
        logger.info("Pedido para obter User com ID: {}", id);
        try {
            User user = userService.find(id);
            return Response.ok(user).build();
        } catch (EntityNotFoundException e) {
            logger.warn("User com ID {} não encontrado", id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Utilizador não encontrado com ID: " + id)
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, User user) {
        logger.info("Pedido para atualizar User com ID: {}", id);
        try {
            User existing = userService.find(id);
            existing.setName(user.getName());
            existing.setEmail(user.getEmail());
            userService.save(existing);
            return Response.ok(existing).build();
        } catch (EntityNotFoundException e) {
            logger.warn("User com ID {} não encontrado para atualização", id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Utilizador não encontrado com ID: " + id)
                    .build();
        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação ao atualizar User: {}", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        logger.info("Pedido para apagar User com ID: {}", id);
        try {
            User user = userService.find(id);
            userService.delete(user.getId());
            return Response.noContent().build();
        } catch (EntityNotFoundException e) {
            logger.warn("User com ID {} não encontrado para remoção", id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Utilizador não encontrado com ID: " + id)
                    .build();
        }
    }
}
