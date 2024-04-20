package org.example.authorization;

import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationController extends HttpServlet {
    private final AuthenticationService service = new AuthenticationService();
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
        logger.info("Request parameters:{} " , authentication.toString());
        // Proceed with your method logic
        service.processAuthentication(authentication,resp);


    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
