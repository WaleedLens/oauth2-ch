package org.example.authorization;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.example.exception.InvalidResponseTypeException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationController extends HttpServlet {
    @Inject
    private ClientAuthenticationStrategy clientStrategy;

    @Inject
    private ServerAuthenticationStrategy serverStrategy;
    Logger logger = org.apache.logging.log4j.LogManager.getLogger(AuthenticationController.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Starting doGet method");
        Authentication authentication = extractParameters(req);
        logger.info("Request parameters: {}", authentication.toString());

        try {
            processRequest(authentication, resp);
        } catch (InvalidResponseTypeException e) {
            logger.error("Invalid response type: {}", authentication.getResponseType());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid response type");
        }

        logger.info("Finished doGet method");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private Authentication extractParameters(HttpServletRequest req) {
        String responseType = req.getParameter("response_type");
        String clientId = req.getParameter("client_id");
        String redirectUri = req.getParameter("redirect_uri");
        String scope = req.getParameter("scope");
        String state = req.getParameter("state");

        return new Authentication(responseType, Long.parseLong(clientId), redirectUri, state, scope);
    }

    private void processRequest(Authentication authentication, HttpServletResponse resp) throws InvalidResponseTypeException {
        if (authentication.getResponseType().equals(ResponseType.CODE.toString())) {
            serverStrategy.processAuthentication(authentication, resp);
        } else if (authentication.getResponseType().equals(ResponseType.TOKEN.toString())) {
            clientStrategy.processAuthentication(authentication, resp);
        } else {
            throw new InvalidResponseTypeException("Invalid response type");
        }
    }

}
