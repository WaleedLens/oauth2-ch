package org.example.filters;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.authorization.Authentication;
import org.example.client.Client;
import org.example.client.ClientRepository;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationSecurityFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(AuthenticationSecurityFilter.class);


    @Inject
    private ClientRepository clientRepository;



    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        Authentication authentication = extractParameters(req);
        logger.debug("Authentication request: {}", authentication);

        if (validateAuthentication(authentication)) {
            logger.info("Authentication request is valid");
            req.setAttribute("Authentication", authentication);
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unauthorized Request");
        }
    }


    private boolean validateAuthentication(Authentication authentication) {
        logger.debug("Validating authentication request: {}", authentication);
        Client client = clientRepository.find(authentication.getClientId());
        if (client == null) {
            throw new IllegalArgumentException("Invalid client id");
        }

        // Validate the redirect URI
        if (!client.getRedirectUri().equals(authentication.getRedirectUri())) {
            throw new IllegalArgumentException("Invalid redirect URI");
        }

        return true;
    }


    private Authentication extractParameters(HttpServletRequest req) {
        logger.info("Extracting parameters from request");
        String responseType = req.getParameter("response_type");
        String clientId = req.getParameter("client_id");
        String redirectUri = req.getParameter("redirect_uri");
        String scope = req.getParameter("scope");
        String state = req.getParameter("state");

        return new Authentication(responseType, Long.parseLong(clientId), redirectUri, state, scope);
    }

    @Override
    public void destroy() {

    }
}
