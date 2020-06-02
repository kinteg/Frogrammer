package com.kinteg.frogrammer.controller.aspect.helper;

import org.aspectj.lang.JoinPoint;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AspectStringHelper<T> {

    String getBefore(JoinPoint joinPoint);

    String responseToString(JoinPoint joinPoint, ResponseEntity<T> object);

    String pageResponseToString(JoinPoint joinPoint, ResponseEntity<Page<T>> object);

}
