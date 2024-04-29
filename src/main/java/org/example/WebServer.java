package org.example;

import com.google.inject.Inject;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.example.authorization.AuthenticationController;
import org.example.client.ClientController;
import org.example.token.TokenController;

import javax.servlet.http.HttpServlet;

public class WebServer {

    org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(WebServer.class);

    private ServletContextHandler context;


    private final ClientController clientController;
    private final AuthenticationController authenticationController;
    private final TokenController tokenController;

    @Inject
    public WebServer(ClientController clientController, AuthenticationController authenticationController, TokenController tokenController) {
        this.clientController = clientController;
        this.authenticationController = authenticationController;
        this.tokenController = tokenController;
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
            addServlet(tokenController, "/token");
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