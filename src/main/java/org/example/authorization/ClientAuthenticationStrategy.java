package org.example.authorization;

import com.google.inject.Inject;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.Logger;
import org.example.client.ClientRepository;
import org.example.exception.RedirectException;
import org.example.utils.OAuthUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;


public class ClientAuthenticationStrategy extends AuthenticationStrategy {
    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ClientAuthenticationStrategy.class);
    @Inject
    public ClientAuthenticationStrategy(ClientRepository repository, AuthorizationRepository authorizationCodeRepository) {
        super(repository, authorizationCodeRepository);
    }

    @Override
    public void redirectUserAgent(Authentication authentication, HttpServletResponse resp) {
        logger.info("Starting redirectUserAgent method");

        try {
            URIBuilder uriBuilder = new URIBuilder(authentication.getRedirectUri());
            uriBuilder.setPath("/callback");
            uriBuilder.addParameter("access_token", OAuthUtils.generateAccessToken());
            uriBuilder.addParameter("token_type", "Bearer");
            uriBuilder.addParameter("expires_in", "3600");
            if (authentication.getState() != null) {
                uriBuilder.addParameter("state", authentication.getState());
            }

            String redirectUrl = uriBuilder.build().toString();
            logger.info("Redirecting user agent to: {}", redirectUrl);
            resp.sendRedirect(redirectUrl);
        } catch (IOException | URISyntaxException e) {
            logger.error("Error redirecting user agent", e);
            throw new RedirectException("Error redirecting user agent", e);
        }

        logger.info("Finished redirectUserAgent method");
    }

}
