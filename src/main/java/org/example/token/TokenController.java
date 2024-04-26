package org.example.token;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exception.ValidationException;
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
            tokenValidator.validate(req);
            logger.info("Token request is valid");

            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = req.getReader().readLine()) != null) {
                requestBody.append(line);
            }

            TokenDTO tokenDTO = (TokenDTO) JsonHandler.toObject(requestBody.toString(), TokenDTO.class);
            logger.info("Token request: {}", tokenDTO);
            tokenService.initiateTokenGeneration(resp, tokenDTO);

        } catch (ValidationException e) {
            logger.error("Validation error: ", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}