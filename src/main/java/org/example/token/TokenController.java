package org.example.token;

import org.example.exception.ValidationException;
import org.example.validation.TokenValidator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenController extends HttpServlet {
    private final TokenValidator tokenValidator = new TokenValidator();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            tokenValidator.validate(req);

            super.doPost(req, resp);
        } catch (ValidationException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }

    }
}
