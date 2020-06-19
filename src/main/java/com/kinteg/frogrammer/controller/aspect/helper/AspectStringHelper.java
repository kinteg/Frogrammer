package com.kinteg.frogrammer.controller.aspect.helper;

import org.aspectj.lang.JoinPoint;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AspectStringHelper {

    String getBefore(JoinPoint joinPoint);

    String responseToString(JoinPoint joinPoint, ResponseEntity<?> object);

    String pageResponseToString(JoinPoint joinPoint, Page<?> object);

}
