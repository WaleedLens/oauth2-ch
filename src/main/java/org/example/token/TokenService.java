// TokenService.java
package org.example.token;

import com.google.inject.Inject;
import org.example.authorization.AuthorizationRepository;
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
        try {
            generateTokens(response, dto);
        } catch (IOException e) {
            log.error("Error generating access token ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates access tokens and saves them in the database.
     *
     * @param response
     */
    private void generateTokens(HttpServletResponse response, TokenDTO dto) throws IOException {
        log.info("Generating access token");
        try {
            Token token = new Token();
            token.setExpiresIn(3600);
            token.setAccessToken(OAuthUtils.generateAccessToken());
            token.setRefreshToken(OAuthUtils.generateRefreshToken());
            token.setClientId(Integer.parseInt(dto.getClientId()));
            log.debug("Generated access token: {}", token.toString());

            tokenRepository.save(token);
            String jsonResponse = JsonHandler.toJson(token);

            response.getWriter().write(jsonResponse);
        } catch (IOException e) {
            log.error("Error generating access token ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to generate access token");
            throw new RuntimeException(e);
        }
    }




}