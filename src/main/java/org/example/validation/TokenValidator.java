package org.example.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exception.ValidationException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;

public class TokenValidator implements Validator<JsonNode> {


    @Override
    public void validate(JsonNode jsonNode) throws ValidationException {

        validateGrantType(jsonNode);
        validateCode(jsonNode);
        validateRedirectUri(jsonNode);
        validateClientId(jsonNode);
        validateClientSecret(jsonNode);
    }

    /**
     * Verifying that the refresh token exists in your database.
     * Checking that it has not been revoked or expired.
     * Ensuring that the client_id associated with the request matches the client_id that was used to obtain the refresh token.
     */
    public void validateRefreshToken(JsonNode node){

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
