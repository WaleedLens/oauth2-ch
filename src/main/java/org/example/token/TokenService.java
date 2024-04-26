package org.example.token;

import com.google.inject.Inject;

public class TokenService {
    private final TokenRepository tokenRepository;

    @Inject
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }
}
