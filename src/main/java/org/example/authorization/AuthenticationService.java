package org.example.authorization;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.example.client.Client;
import org.example.client.ClientRepository;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

public class AuthenticationService {
    private final ClientRepository repository;
    private final AuthorizationRepository authorizationCodeRepository;
    private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(AuthenticationService.class);

    @Inject
    public AuthenticationService(ClientRepository repository, AuthorizationRepository authorizationCodeRepository) {
        this.repository = repository;
        this.authorizationCodeRepository = authorizationCodeRepository;
    }

    public void processAuthentication(Authentication authentication, HttpServletResponse resp) {
        // Validate the response type
        if (!ResponseType.CODE.toString().equals(authentication.getResponseType())) {
            throw new IllegalArgumentException("Invalid response type");
        }

        // Validate the authentication
        if (validateAuthentication(authentication)) {
            // Generate an authorization code
            String authorizationCode = generateAuthorizationCode();

            // Store the authorization code
            storeAuthorizationCode(authorizationCode, authentication);

            // Redirect the user agent
            redirectUserAgent(authorizationCode, authentication, resp);

            logger.info("Processing authentication for client: {}", authentication.getClientId());
        }
    }

    private String generateAuthorizationCode() {
        // Generate a secure random authorization code
        SecureRandom secureRandom = new SecureRandom();
        byte[] code = new byte[20];
        secureRandom.nextBytes(code);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(code);
    }

    public void storeAuthorizationCode(String authorizationCode, Authentication authentication) {
        try {

            // Convert the encrypted byte array to a Base64 encoded string
            logger.info("Storing authorization code: {}", authorizationCode);

            // Create an AuthorizationCode object
            AuthorizationCode code = new AuthorizationCode();
            code.setCode(authorizationCode);
            code.setClientId(authentication.getClientId());
            code.setRedirectUri(authentication.getRedirectUri());
            code.setScope(authentication.getScope());
            code.setCreatedDate(new Date());
            code.setExpirationDate(new Date(System.currentTimeMillis() + 10 * 60 * 1000)); // Expires in 10 minutes
            authorizationCodeRepository.save(code);


        } catch (Exception e) {
            throw new RuntimeException("Error storing authorization code", e);
        }
    }

    public void redirectUserAgent(String authorizationCode, Authentication authentication, HttpServletResponse resp) {
        try {
            String redirectUri = authentication.getRedirectUri();
            String state = authentication.getState();

            StringBuilder sb = new StringBuilder(redirectUri);
            sb.append("?code=").append(URLEncoder.encode(authorizationCode, StandardCharsets.UTF_8));

            if (state != null) {
                sb.append("&state=").append(URLEncoder.encode(state, StandardCharsets.UTF_8));
            }
            logger.info("Redirecting user agent to: {}", sb.toString());
            resp.sendRedirect(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException("Error redirecting user agent", e);
        }
    }

    private boolean validateAuthentication(Authentication authentication) {
        Client client = repository.find(authentication.getClientId());
        if (client == null) {
            throw new IllegalArgumentException("Invalid client id");
        }

        // Validate the redirect URI
        if (!client.getRedirectUri().equals(authentication.getRedirectUri())) {
            throw new IllegalArgumentException("Invalid redirect URI");
        }

        return true;
    }


}
