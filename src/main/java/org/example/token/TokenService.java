package org.example.token;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.example.authorization.AuthorizationCode;
import org.example.authorization.AuthorizationRepository;
import org.example.utils.JsonHandler;
import org.example.utils.OAuthUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenService {
    private final TokenRepository tokenRepository;
    private final AuthorizationRepository authorizationRepository;
    Logger log = org.apache.logging.log4j.LogManager.getLogger(TokenService.class);

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
        if (validateRequestParams(dto)) {
            generateAccessTokens(response);
        } else {
            log.info("Invalid request parameters");
        }
    }

    public boolean validateRequestParams(TokenDTO tokenDTO) {

        AuthorizationCode authorizationCode = authorizationRepository.findByField("client_id", Long.valueOf(tokenDTO.getClientId()));
        log.info("Validating request parameters ", tokenDTO.toString(), " -- ", authorizationCode.toString());
        return authorizationCode.equals(tokenDTO);

    }

    /**
     * Generates access tokens and saves them in the database.
     * @param response
     */
    private void generateAccessTokens(HttpServletResponse response) {
        log.info("Generating access tokens");

        try {
            Token accessToken = new Token();
            accessToken.setExpiresIn(3600);
            accessToken.setAccessToken(OAuthUtils.generateAccessToken());
            log.info("Generated access token: ", accessToken.toString());
            tokenRepository.save(accessToken);
            String jsonResponse = JsonHandler.toJson(accessToken);
            response.getWriter().write(jsonResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
