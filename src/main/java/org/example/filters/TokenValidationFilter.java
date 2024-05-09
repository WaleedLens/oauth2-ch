package org.example.filters;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exception.ValidationException;
import org.example.token.TokenDTO;
import org.example.utils.JsonHandler;
import org.example.validation.TokenValidator;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenValidationFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(TokenValidationFilter.class);
    private final TokenValidator tokenValidator;

    @Inject
    public TokenValidationFilter(TokenValidator tokenValidator) {
        this.tokenValidator = tokenValidator;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        try {
            JsonNode nodes = JsonHandler.readTree(httpRequest.getReader());
            tokenValidator.validate(nodes);
            logger.info("Token request is valid");

            TokenDTO tokenDTO = (TokenDTO) JsonHandler.toObject(nodes.toPrettyString(), TokenDTO.class);
            httpRequest.setAttribute("tokenDTO", tokenDTO);
            chain.doFilter(request, response);
        } catch (ValidationException e) {
            logger.error("Validation error: ", e);
            throw new ServletException("Invalid token request", e);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}