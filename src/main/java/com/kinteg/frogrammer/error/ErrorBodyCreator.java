package com.kinteg.frogrammer.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class ErrorBodyCreator {

    public static Map<String, Object> createBody(HttpStatus httpStatus, Object errors, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh-mm-ss");
        body.put("timestamp", LocalDateTime.now().format(formatter));
        body.put("status", httpStatus.value());
        body.put("errors", errors);
        body.put("uri", request.getDescription(false).substring(4));

        return body;
    }

}
