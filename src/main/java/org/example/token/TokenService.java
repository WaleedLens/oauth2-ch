package org.example.token;

import com.google.inject.Inject;

import javax.servlet.http.HttpServletResponse;

public class TokenService {
    private final TokenRepository tokenRepository;

    @Inject
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void initiateTokenGeneration(HttpServletResponse response, TokenDTO dto) {
        if (validateRequestParams(dto)) {
            generateAccessTokens(response);
        }
    }

    public boolean validateRequestParams(TokenDTO tokenDTO) {

        return false;

    }

    protected void generateAccessTokens(HttpServletResponse response) {

    }


}
