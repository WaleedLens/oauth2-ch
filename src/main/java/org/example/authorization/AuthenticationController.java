package org.example.authorization;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;

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
        // Extract query parameters directly using HttpServletRequest's methods
        String responseType = req.getParameter("response_type");
        String clientId = req.getParameter("client_id");
        String redirectUri = req.getParameter("redirect_uri");
        String scope = req.getParameter("scope");
        String state = req.getParameter("state");

        Authentication authentication = new Authentication(responseType, Long.parseLong(clientId), redirectUri, state, scope);
        // Log the extracted parameters
        logger.info("Request parameters:{} ", authentication.toString());
        // Proceed with your method logic
        if (authentication.getResponseType().equals(ResponseType.CODE.toString())) {
            serverStrategy.processAuthentication(authentication, resp);
        } else if (authentication.getResponseType().equals(ResponseType.TOKEN.toString())) {
            clientStrategy.processAuthentication(authentication, resp);
        } else {
            logger.error("Invalid response type: {}", authentication.getResponseType());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid response type");

        }


    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
