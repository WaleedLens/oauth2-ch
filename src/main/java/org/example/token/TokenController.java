// TokenController.java
package org.example.token;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenController extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(TokenController.class);
    private final TokenService tokenService;

    @Inject
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * POST /token --> Generate new access & refresh tokens.
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            TokenDTO tokenDTO = (TokenDTO) req.getAttribute("tokenDTO");
            logger.info("Token request: {}", tokenDTO);
            tokenService.initiateTokenGeneration(resp, tokenDTO);
        } catch (Exception e) {
            logger.error("Error processing token request: ", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * GET /token --> Generate new access token by refresh token & return new access & refresh tokens.
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

}