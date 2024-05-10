// TokenService.java
package org.example.token;

import com.google.inject.Inject;
import org.example.authorization.AuthorizationRepository;
import org.example.exception.InvalidTokenException;
import org.example.utils.JsonHandler;
import org.example.utils.OAuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenService {
    private final TokenRepository tokenRepository;
    private final AuthorizationRepository authorizationRepository;
    private Logger log = LoggerFactory.getLogger(TokenService.class);

    @Inject
    public TokenService(TokenRepository tokenRepository, AuthorizationRepository authorizationRepository) {
        this.tokenRepository = tokenRepository;
        this.authorizationRepository = authorizationRepository;
    }

    /**
     * Initiates the token generation process.
     *
     * @param response --> To send the response back
     * @param dto      --> From request body
     */
    public void initiateTokenGeneration(HttpServletResponse response, TokenDTO dto) {
        generateAccessTokens(response);
    }

    /**
     * Generates access tokens and saves them in the database.
     *
     * @param response
     */
    private void generateAccessTokens(HttpServletResponse response) {
        log.info("Generating access token");
        try {
            Token accessToken = new Token();
            accessToken.setExpiresIn(3600);
            accessToken.setAccessToken(OAuthUtils.generateAccessToken());
            accessToken.setRefreshToken(generateRefreshToken());
            log.debug("Generated access token: {}", accessToken.toString());

            tokenRepository.save(accessToken);
            String jsonResponse = JsonHandler.toJson(accessToken);

            response.getWriter().write(jsonResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String generateRefreshToken() {
        log.info("Generating refresh token");
        return OAuthUtils.generateRefreshToken();
    }




}