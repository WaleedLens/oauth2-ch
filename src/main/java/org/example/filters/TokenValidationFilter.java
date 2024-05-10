package org.example.filters;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exception.InvalidTokenException;
import org.example.exception.ValidationException;
import org.example.token.GrantType;
import org.example.token.RefreshTokenDTO;
import org.example.token.TokenDTO;
import org.example.utils.JsonHandler;
import org.example.validation.TokenValidator;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenValidationFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(TokenValidationFilter.class);
    private final TokenValidator tokenValidator;

    @Inject
    public TokenValidationFilter(TokenValidator tokenValidator) {
        this.tokenValidator = tokenValidator;
    }

    /**
     * This method is part of the Filter interface and is called by the server each time a request/response pair is passed
     * through the chain due to a client request for a resource at the end of the chain. The FilterChain passed into this
     * method allows the Filter to pass on the request and response to the next entity in the chain.
     * <p>
     * In this specific implementation, the method reads the JSON from the client's request, validates the token, and checks
     * if the grant type is a refresh token. If it is, it calls the handleRefreshTokenValidation method and returns.
     * If the grant type is not a refresh token, it logs that the token request is valid, converts the JsonNode object to a
     * TokenDTO object, sets the TokenDTO as an attribute of the HTTP request, and passes the request and response along the
     * filter chain.
     * <p>
     * If a ValidationException is caught, it logs the error and throws a ServletException.
     *
     * @param request  The ServletRequest object that contains the client's request.
     * @param response The ServletResponse object that contains the servlet's response.
     * @param chain    The FilterChain for invoking the next filter in the chain.
     * @throws IOException      If an input or output error is detected when the servlet handles this request.
     * @throws ServletException If the request for the POST could not be handled.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            // Parse the JSON from the client's request
            JsonNode nodes = JsonHandler.readTree(httpRequest.getReader());
            // Validate the token
            tokenValidator.validate(nodes);
            // Check if the grant type is a refresh token
            if (nodes.get("grant_type").equals(GrantType.refresh_token.toString())) {
                // Handle the refresh token validation
                handleRefreshTokenValidation(nodes, request, response, chain);
                return;
            }

            // Log that the token request is valid
            logger.info("Token request is valid");

            // Convert the JsonNode object to a TokenDTO object
            TokenDTO tokenDTO = (TokenDTO) JsonHandler.toObject(nodes.toPrettyString(), TokenDTO.class);
            // Set the TokenDTO as an attribute of the HTTP request
            httpRequest.setAttribute("tokenDTO", tokenDTO);
            // Pass the request and response along the filter chain
            chain.doFilter(request, response);
        } catch (ValidationException e) {
            // Log the error and throw a ServletException
            logger.error("Validation error: ", e);
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token request");
            throw new ServletException("Invalid token request", e);
        }
    }


    /**
     * This method handles the validation of refresh tokens. It is called when a client sends a request with a refresh token.
     * If the refresh token is valid, the method sets the RefreshTokenDTO as an attribute of the HTTP request and passes the request and response along the filter chain.
     * If the refresh token is invalid, the method sends an HTTP 401 (Unauthorized) error response to the client and throws a ServletException.
     *
     * @param nodes    The JsonNode object containing the parsed JSON from the client's request.
     * @param request  The ServletRequest object that contains the client's request.
     * @param response The ServletResponse object that contains the servlet's response.
     * @param chain    The FilterChain for invoking the next filter in the chain.
     * @throws IOException      If an input or output error is detected when the servlet handles this request.
     * @throws ServletException If the request for the POST could not be handled.
     */
    private void handleRefreshTokenValidation(JsonNode nodes, ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // Check if the grant type is a refresh token
        if (nodes.get("grant_type").equals(GrantType.refresh_token.toString())) {

            try {
                // Validate the refresh token
                tokenValidator.validateRefreshToken(nodes);
                logger.info("Refresh token is valid");
                // Convert the JsonNode object to a RefreshTokenDTO object
                RefreshTokenDTO refreshTokenDTO = (RefreshTokenDTO) JsonHandler.toObject(nodes.toPrettyString(), RefreshTokenDTO.class);

                // Set the RefreshTokenDTO as an attribute of the HTTP request
                httpRequest.setAttribute("RefreshTokenDTO", refreshTokenDTO);
                // Pass the request and response along the filter chain
                chain.doFilter(request, response);
            } catch (InvalidTokenException e) {
                // Log the error and send an HTTP 401 error response to the client
                logger.error("Invalid refresh token: ", e);
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid refresh token");
                // Throw a ServletException
                throw new ServletException("Invalid refresh token", e);
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}