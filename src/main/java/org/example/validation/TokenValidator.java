package org.example.validation;

import org.example.exception.ValidationException;

import javax.servlet.http.HttpServletRequest;

public class TokenValidator implements Validator<HttpServletRequest> {


    @Override
    public void validate(HttpServletRequest request) throws ValidationException {
        validateGrantType(request);
        validateCode(request);
        validateRedirectUri(request);
        validateClientId(request);
        validateClientSecret(request);
    }

    private void validateGrantType(HttpServletRequest request) throws ValidationException {
        if (request.getParameter("grant_type") == null) {
            throw new ValidationException("grant_type is missing");
        }
    }

    private void validateCode(HttpServletRequest request) throws ValidationException {
        if (request.getParameter("code") == null) {
            throw new ValidationException("code is missing");
        }
    }

    private void validateRedirectUri(HttpServletRequest request) throws ValidationException {
        if (request.getParameter("redirect_uri") == null) {
            throw new ValidationException("redirect_uri is missing");
        }
    }

    private void validateClientId(HttpServletRequest request) throws ValidationException {
        if (request.getParameter("client_id") == null) {
            throw new ValidationException("client_id is missing");
        }
    }

    private void validateClientSecret(HttpServletRequest request) throws ValidationException {
        if (request.getParameter("client_secret") == null) {
            throw new ValidationException("client_secret is missing");
        }
    }
}
