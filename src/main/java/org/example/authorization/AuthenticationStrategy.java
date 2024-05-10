package org.example.authorization;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.example.client.Client;
import org.example.client.ClientRepository;
import org.example.utils.OAuthUtils;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;

public abstract class AuthenticationStrategy {
    private final ClientRepository repository;
    private final AuthorizationRepository authorizationCodeRepository;
    private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(AuthenticationStrategy.class);

    protected String authorizationCode;

    @Inject
    public AuthenticationStrategy(ClientRepository repository, AuthorizationRepository authorizationCodeRepository) {
        this.repository = repository;
        this.authorizationCodeRepository = authorizationCodeRepository;
    }

    public void processAuthentication(Authentication authentication, HttpServletResponse resp) {

        // Generate an authorization code
        this.authorizationCode = OAuthUtils.generateAuthorizationCode();
        // Store the authorization code
        storeAuthorizationCode(authentication);

        // Redirect the user agent
        redirectUserAgent(authentication, resp);

        logger.info("Processing authentication for client: {}", authentication.getClientId());

    }


    public void storeAuthorizationCode(Authentication authentication) {
        try {

            // Convert the encrypted byte array to a Base64 encoded string
            logger.info("Storing authorization code: {}", authorizationCode);

            // Create an AuthorizationCode object
            AuthorizationCode code = new AuthorizationCode();
            code.setCode(authorizationCode);
            code.setClientId(authentication.getClientId());
            code.setRedirectUri(authentication.getRedirectUri());
            code.setScope(authentication.getScope());
            code.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            code.setExpirationDate(new Timestamp(System.currentTimeMillis() + 10 * 60 * 1000)); // Expires in 10 minutes
            authorizationCodeRepository.save(code);


        } catch (Exception e) {
            throw new RuntimeException("Error storing authorization code", e);
        }
    }


    public abstract void redirectUserAgent(Authentication authentication, HttpServletResponse resp);
}
