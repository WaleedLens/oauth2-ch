package org.example.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import org.example.exception.InvalidTokenException;
import org.example.exception.ValidationException;
import org.example.token.Token;
import org.example.token.TokenRepository;
import org.example.utils.OAuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenValidator implements Validator<JsonNode> {
    @Inject
    private TokenRepository tokenRepository;

    private final static Logger log = LoggerFactory.getLogger(TokenValidator.class);

    @Override
    public void validate(JsonNode jsonNode) throws ValidationException {

        validateGrantType(jsonNode);
        validateCode(jsonNode);
        validateRedirectUri(jsonNode);
        validateClientId(jsonNode);
        validateClientSecret(jsonNode);
    }

    public boolean validateRefreshToken(String refreshToken) throws InvalidTokenException {
        Token token = tokenRepository.findByField("refresh_token", refreshToken);
        log.debug("Token found: {}", token);
        if (OAuthUtils.isExpired(token.getExpiresIn(), token.getCreatedAt())) {
            log.error("Refresh token expired: {}", token.getRefreshToken());
            throw new InvalidTokenException("Refresh token expired");
        }

        return true;
    }

    private void validateGrantType(JsonNode node) throws ValidationException {
        if (node.get("grant_type") == null) {
            throw new ValidationException("grant_type is missing");
        }
    }

    private void validateCode(JsonNode node) throws ValidationException {
        if (node.get("code") == null) {
            throw new ValidationException("code is missing");
        }
    }

    private void validateRedirectUri(JsonNode node) throws ValidationException {
        if (node.get("redirect_uri") == null) {
            throw new ValidationException("redirect_uri is missing");
        }
    }

    private void validateClientId(JsonNode node) throws ValidationException {
        if (node.get("client_id") == null) {
            throw new ValidationException("client_id is missing");
        }
    }

    private void validateClientSecret(JsonNode node) throws ValidationException {
        if (node.get("client_secret") == null) {
            throw new ValidationException("client_secret is missing");
        }
    }
}
