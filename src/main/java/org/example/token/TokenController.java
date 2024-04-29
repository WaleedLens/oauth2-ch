package org.example.token;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.utils.JsonHandler;
import org.example.validation.TokenValidator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenController extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(TokenController.class);
    private final TokenValidator tokenValidator;
    private final TokenService tokenService;

    @Inject
    public TokenController(TokenValidator tokenValidator, TokenService tokenService) {
        this.tokenValidator = tokenValidator;
        this.tokenService = tokenService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            JsonNode nodes = JsonHandler.readTree(req.getReader());
            tokenValidator.validate(nodes);
            logger.info("Token request is valid");

            logger.info("Request body: {}", nodes.toPrettyString());
            TokenDTO tokenDTO = (TokenDTO) JsonHandler.toObject(nodes.toPrettyString(), TokenDTO.class);
            logger.info("Token request: {}", tokenDTO);
            tokenService.initiateTokenGeneration(resp, tokenDTO);

        } catch (Exception e) {
            logger.error("Validation error: ", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}