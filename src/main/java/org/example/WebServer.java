package org.example;

import com.google.inject.Inject;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.example.authorization.AuthenticationController;
import org.example.client.ClientController;
import org.example.filters.AuthenticationSecurityFilter;
import org.example.filters.TokenValidationFilter;
import org.example.token.TokenController;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;
import java.util.EnumSet;

public class WebServer {

    org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(WebServer.class);

    private ServletContextHandler context;
    // >-- controllers <--
    @Inject
    private ClientController clientController;
    @Inject
    private AuthenticationController authenticationController;
    @Inject
    private TokenController tokenController;

    // >-- filters <--
    @Inject
    private AuthenticationSecurityFilter authenticationFilter;
    @Inject
    private TokenValidationFilter tokenValidationFilter;

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
            // --> filters <--
            addFilter(authenticationFilter, "/auth");
            addFilter(tokenValidationFilter, "/token");
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

    private void addServlet(HttpServlet servlet, String path) {
        logger.info("Adding servlet: {} at path: {}", servlet.getClass().getName(), path);
        getServletContextHandler().addServlet(new ServletHolder(servlet), path);
    }

    private void addFilter(Filter filter, String path) {
        logger.info("Adding filter: {} at path: {}", filter.getClass().getName(), path);
        getServletContextHandler().addFilter(new FilterHolder(filter), path, EnumSet.of(DispatcherType.REQUEST));
    }

}