package com.kinteg.frogrammer.controller.aspect.helper.impl;

import com.kinteg.frogrammer.controller.aspect.helper.AspectStringHelper;
import org.aspectj.lang.JoinPoint;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AspectStringHelperImpl<T> implements AspectStringHelper<T> {
    @Override
    public String getBefore(JoinPoint joinPoint) {
        String args = Arrays.stream(joinPoint.getArgs())
                .map(v -> (Objects.nonNull(v) ? v.getClass() : "null") + " = " + Objects.toString(v, "null"))
                .collect(Collectors.joining(",\n        "));

        return "before " + joinPoint.toString() + ",\n  args = [\n       " + args + "\n   ]";
    }

    @Override
    public String responseToString(JoinPoint joinPoint, ResponseEntity<T> object) {
        return "after returning " + joinPoint.toString() + getReturn(object);
    }

    private String getReturn(ResponseEntity<T> object) {
        if (object != null) {
            return ",\n  return = {\n" +
                    getSimpleBody(object) +
                    getStatus(object.getStatusCode()) +
                    "   }";
        } else {
            return "return is null";
        }
    }

    private String getSimpleBody(ResponseEntity<T> object) {
        return "        body = " + Objects.toString(object.getBody(), "") + ",\n";
    }

    @Override
    public String pageResponseToString(JoinPoint joinPoint, ResponseEntity<Page<T>> objects) {
        return "after returning " + joinPoint.toString() + getReturnPage(objects);
    }

    private String getReturnPage(ResponseEntity<Page<T>> objects) {
        if (objects != null) {
            return ",\n  return = {\n" +
                    getPageBody(objects) +
                    getStatus(objects.getStatusCode()) +
                    "   }";
        } else {
            return "return is null";
        }
    }

    private String getStatus(HttpStatus status) {
        return "        status = " + Objects.toString(status, "") + "\n";
    }

    private String getPageBody(ResponseEntity<Page<T>> object) {
        return "        body = {\n" +
                (object.hasBody() ? getPage(Objects.requireNonNull(object.getBody())) : "null") +
                "\n        },\n";
    }

    private String getPage(Page<T> page) {
        return "           total elements = " + page.getTotalElements() + "\n" +
                "           total pages = " + page.getTotalPages() + "\n" +
                "           pageable = " + page.getPageable() + "\n" +
                "           content = {\n               " +
                page.getContent().stream().map(Object::toString).collect(Collectors.joining(",\n               ")) +
                "\n           }";
    }

}
