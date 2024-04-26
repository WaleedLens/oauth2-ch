package org.example.token;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exception.ValidationException;
import org.example.validation.TokenValidator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenController extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(TokenController.class);
    private final TokenValidator tokenValidator;

    @Inject
    public TokenController(TokenValidator tokenValidator) {
        this.tokenValidator = tokenValidator;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            tokenValidator.validate(req);
            // Replace with your own implementation
            // For example, you might want to create a token and send it in the response
        } catch (ValidationException e) {
            logger.error("Validation error: ", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}