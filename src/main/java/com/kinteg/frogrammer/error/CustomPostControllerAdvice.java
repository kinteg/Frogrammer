package com.kinteg.frogrammer.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

import static com.kinteg.frogrammer.error.ErrorBodyCreator.createBody;

@ControllerAdvice(annotations = RestController.class)
public class CustomPostControllerAdvice {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> httpClientException(HttpClientErrorException ex, WebRequest request) {
        Map<String, Object> body = createBody(ex.getStatusCode(), ex.getMessage(), request);
        return new ResponseEntity<>(body, new HttpHeaders(), ex.getStatusCode());
    }

}
