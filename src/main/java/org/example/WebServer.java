package org.example;

import com.google.inject.Inject;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.example.authorization.AuthenticationController;
import org.example.client.ClientController;

import javax.servlet.http.HttpServlet;

public class WebServer {
    private final static WebServer WEB_SERVER_OBJECT = new WebServer();
    org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(WebServer.class);

    private ServletContextHandler context;

    @Inject
    private ClientController clientController;

    @Inject
    private AuthenticationController authenticationController;

    public static WebServer getInstance() {
        return WEB_SERVER_OBJECT;
    }

    public ServletContextHandler getServletContextHandler() {
        if (context == null) {
            context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
        }
        return context;
    }

    public void start() {
        try {
            int port = 8432;
            Server server = new Server(port);
            server.setHandler(getServletContextHandler());

            // --> servlets <--
            addServlet(clientController, "/client");
            addServlet(authenticationController, "/auth");

            server.start();
            logger.info("Starting server on port: {}", port);
            server.join();

        } catch (Exception e) {
            logger.error("Error starting server", e);
            throw new RuntimeException(e);
        }

    }

    public void addServlet(HttpServlet servlet, String path) {
        logger.info("Adding servlet: {} at path: {}", servlet.getClass().getName(), path);
        getServletContextHandler().addServlet(new ServletHolder(servlet), path);
    }
}