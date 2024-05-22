package org.example.authorization;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exception.InvalidResponseTypeException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationController extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(AuthenticationController.class);

    private final ClientAuthenticationStrategy clientStrategy;
    private final ServerAuthenticationStrategy serverStrategy;

    @Inject
    public AuthenticationController(ClientAuthenticationStrategy clientStrategy, ServerAuthenticationStrategy serverStrategy) {
        this.clientStrategy = clientStrategy;
        this.serverStrategy = serverStrategy;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Starting doGet method");

        Authentication authentication = (Authentication) req.getAttribute("Authentication");
        if (authentication == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing authentication details");
            return;
        }
        logger.info("Request parameters: {}", authentication);

        try {
            processRequest(authentication, resp);
        } catch (InvalidResponseTypeException e) {
            logger.error("Invalid response type: {}", e.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid response type");
        }

        logger.info("Finished doGet method");
    }

    private void processRequest(Authentication authentication, HttpServletResponse resp) throws InvalidResponseTypeException {
        final String CODE = ResponseType.CODE.toString();
        final String TOKEN = ResponseType.TOKEN.toString();

        if (ResponseType.CODE.name().equals(authentication.getResponseType())) {
            serverStrategy.processAuthentication(authentication, resp);
        } else if (ResponseType.TOKEN.name().equals(authentication.getResponseType())) {
            clientStrategy.processAuthentication(authentication, resp);
        } else {
            throw new InvalidResponseTypeException("Invalid response type");
        }
    }
}