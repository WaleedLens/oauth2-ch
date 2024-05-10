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

    /**
     * Validates the refresh token from the provided JsonNode.
     * It extracts the refresh token and client id from the JsonNode, finds the corresponding token,
     * and validates the token's expiration and ownership.
     *
     * @param node The JsonNode containing the refresh token and client id.
     * @throws InvalidTokenException If the token is expired or the client id does not match the token's client id.
     */
    public void validateRefreshToken(JsonNode node) throws InvalidTokenException {
        String refreshToken = node.get("refresh_token").asText();
        int clientId = node.get("client_id").asInt();
        Token token = findToken(refreshToken);
        validateTokenExpiration(token);
        validateTokenOwner(token, clientId, refreshToken);
    }

    /**
     * Finds the token associated with the provided refresh token.
     *
     * @param refreshToken The refresh token associated with the token.
     * @return The found token.
     * @throws InvalidTokenException If no token is found for the provided refresh token.
     */
    private Token findToken(String refreshToken) throws InvalidTokenException {
        log.debug("Validating refresh token: {}", refreshToken);
        Token token = tokenRepository.findByField("refresh_token", refreshToken);
        log.debug("Token found: {}", token);
        return token;
    }

    /**
     * Validates that the provided token is not expired.
     *
     * @param token The token to validate.
     * @throws InvalidTokenException If the token is expired.
     */
    private void validateTokenExpiration(Token token) throws InvalidTokenException {
        if (OAuthUtils.isExpired(token.getExpiresIn(), token.getCreatedAt())) {
            log.error("Refresh token expired: {}", token.getRefreshToken());
            throw new InvalidTokenException("Refresh token expired");
        }
    }

    /**
     * Validates that the provided token is owned by the client with the provided id.
     *
     * @param token        The token to validate.
     * @param clientId     The id of the client that should own the token.
     * @param refreshToken The refresh token associated with the token.
     * @throws InvalidTokenException If the client id does not match the token's client id or the refresh token does not match the token's refresh token.
     */
    private void validateTokenOwner(Token token, int clientId, String refreshToken) throws InvalidTokenException {
        if (token.getClientId() != clientId || !token.getRefreshToken().equals(refreshToken)) {
            log.error("Either client id mismatch or refresh token mismatch");
            log.debug("Client id: {} Token client id: {}", clientId, token.getClientId());
            log.debug("Refresh token: {} Token refresh token: {}", refreshToken, token.getRefreshToken());
            throw new InvalidTokenException("Either client id mismatch or refresh token mismatch");
        }
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
