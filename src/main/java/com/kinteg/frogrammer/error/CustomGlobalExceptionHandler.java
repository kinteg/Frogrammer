package com.kinteg.frogrammer.error;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status,
            WebRequest request) {


        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        Map<String, Object> body = createBody(status, errors);

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {

        Map<Object, Object> errors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap((ConstraintViolation::getPropertyPath),
                                ConstraintViolation::getMessage));

        Map<String, Object> body = createBody(HttpStatus.BAD_REQUEST, errors);

        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> createBody(HttpStatus httpStatus, Object errors) {
        Map<String, Object> body = new LinkedHashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh-mm-ss");
        body.put("timestamp", LocalDateTime.now().format(formatter));
        body.put("status", httpStatus.value());

        body.put("errors", errors);

        return body;
    }

}
