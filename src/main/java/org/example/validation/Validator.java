package org.example.validation;

import org.example.exception.ValidationException;

public interface Validator<T> {
    void validate(T request) throws ValidationException;
}
