package org.example.authorization;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.example.client.ClientRepository;
import org.example.exception.RedirectException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ServerAuthenticationStrategy extends AuthenticationStrategy {
    private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(ServerAuthenticationStrategy.class);


    @Inject
    public ServerAuthenticationStrategy(ClientRepository repository, AuthorizationRepository authorizationCodeRepository) {
        super(repository, authorizationCodeRepository);
    }

    /**
     * Redirects the user agent to the provided redirect URI with the authorization code and state as query parameters.
     * The redirect URI, authorization code, and state are URL encoded to ensure they are safe to include in a URL.
     * If an IOException occurs during the redirection, it is logged and a RedirectException is thrown.
     *
     * @param authentication The Authentication object containing the redirect URI and state.
     * @param resp           The HttpServletResponse to which the user agent should be redirected.
     * @throws RedirectException If an IOException occurs during the redirection.
     */
    @Override
    public void redirectUserAgent(Authentication authentication, HttpServletResponse resp) {
        try {
            String redirectUri = URLEncoder.encode(authentication.getRedirectUri(), StandardCharsets.UTF_8);
            String state = authentication.getState();

            StringBuilder sb = new StringBuilder(redirectUri)
                    .append("?code=").append(URLEncoder.encode(authorizationCode, StandardCharsets.UTF_8));

            if (state != null) {
                sb.append("&state=").append(URLEncoder.encode(state, StandardCharsets.UTF_8));
            }
            logger.info("Redirecting user agent to: {}", sb.toString());

            resp.sendRedirect(sb.toString());
        } catch (IOException e) {
            logger.error("Error redirecting user agent", e);
            throw new RedirectException("Error redirecting user agent to: " + e.getMessage(), e);
        }
    }
}
