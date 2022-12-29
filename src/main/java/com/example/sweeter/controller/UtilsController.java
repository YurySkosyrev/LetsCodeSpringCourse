package com.example.sweeter.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Здесь хранятся различные полезные утилиты
 */

public class UtilsController {
    static Map<String, String> getErrors(BindingResult bindingResult) { // static т.к. нужно только внутри пакета
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage
        );
        return bindingResult.getFieldErrors().stream().collect(collector);
    }
}
