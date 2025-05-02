package com.ordermanager;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080); // Porta 8080

        ServletContextHandler ctx = new ServletContextHandler();
        ctx.setContextPath("/");

        // Configura Jersey (JAX-RS)
        ServletHolder jerseyServlet = ctx.addServlet(ServletContainer.class, "/api/*");
        jerseyServlet.setInitOrder(0);
        jerseyServlet.setInitParameter(
                "javax.ws.rs.Application",
                "com.ordermanager.config.AppConfig"
        );


        server.setHandler(ctx);
        server.start();
        System.out.println("API a rodar em: http://localhost:8080/api");
        server.join();
    }
}