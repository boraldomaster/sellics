package com.sellics;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.servlet.Servlet;

public class Runner {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        ServletContextHandler handler = new ServletContextHandler(server, "/");
        ResourceConfig config = new ResourceConfig().packages("com.sellics.controller");
        Servlet servlet = new ServletContainer(config);
        handler.addServlet(new ServletHolder(servlet), "/*");
        server.setHandler(handler);
        server.start();
        server.join();
    }
}
