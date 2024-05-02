package org.example.authorization;

import org.apache.logging.log4j.Logger;
import org.example.client.ClientRepository;
import org.example.utils.OAuthUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ClientAuthenticationStrategy  extends AuthenticationStrategy{
    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ClientAuthenticationStrategy.class);
    public ClientAuthenticationStrategy(ClientRepository repository, AuthorizationRepository authorizationCodeRepository) {
        super(repository, authorizationCodeRepository);
    }

    @Override
    public void redirectUserAgent( Authentication authentication, HttpServletResponse resp) {
        try {
            String redirectUri = authentication.getRedirectUri();
            String state = authentication.getState();

            StringBuilder sb = new StringBuilder(redirectUri);
            sb.append("/callback");
            sb.append("?access_token=").append(OAuthUtils.generateAccessToken());

            if (state != null) {
                sb.append("&state=").append(URLEncoder.encode(state, StandardCharsets.UTF_8));
            }
            logger.info("Redirecting user agent to: {}", sb.toString());
            resp.sendRedirect(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException("Error redirecting user agent", e);
        }
    }

}
