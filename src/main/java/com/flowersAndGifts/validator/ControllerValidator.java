package com.flowersAndGifts.validator;

import com.flowersAndGifts.exception.ControllerException;

public class ControllerValidator {
    public static String isValidString(final String field, final String name) throws ControllerException {
        if (field.isEmpty() || field == null) {
            throw new ControllerException(name + " is null or empty");
        }
        return field;
    }
}
