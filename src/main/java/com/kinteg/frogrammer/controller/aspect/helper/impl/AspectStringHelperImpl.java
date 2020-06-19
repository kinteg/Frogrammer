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
public class AspectStringHelperImpl implements AspectStringHelper {

    @Override
    public String getBefore(JoinPoint joinPoint) {
        String args = Arrays.stream(joinPoint.getArgs())
                .map(v -> (Objects.nonNull(v) ? v.getClass() : "null") + " = " + Objects.toString(v, "null"))
                .collect(Collectors.joining(",\n        "));

        return "before " + joinPoint.toString() + ",\n  args = [\n       " + args + "\n   ]";
    }

    @Override
    public String responseToString(JoinPoint joinPoint, ResponseEntity<?> object) {
        return "after returning " + joinPoint.toString() + getReturn(object);
    }

    private String getReturn(ResponseEntity<?> object) {
        if (object != null) {
            return ",\n  return = {\n" +
                    getSimpleBody(object) +
                    getStatus(object.getStatusCode()) +
                    "   }";
        } else {
            return "return is null";
        }
    }

    private String getSimpleBody(ResponseEntity<?> object) {
        return "        body = " + Objects.toString(object.getBody(), "") + ",\n";
    }

    @Override
    public String pageResponseToString(JoinPoint joinPoint, Page<?> objects) {
        return "after returning " + joinPoint.toString() + getReturnPage(objects);
    }

    private String getReturnPage(Page<?> objects) {
        if (objects != null) {
            return ",\n  return = {\n" +
                    getPageBody(objects) +
                    "   }";
        } else {
            return "return is null";
        }
    }

    private String getStatus(HttpStatus status) {
        return "        status = " + Objects.toString(status, "") + "\n";
    }

    private String getPageBody(Page<?> object) {
        return "        body = {\n" +
                getPage(Objects.requireNonNull(object)) +
                "\n        },\n";
    }

    private String getPage(Page<?> page) {
        return "           total elements = " + page.getTotalElements() + "\n" +
                "           total pages = " + page.getTotalPages() + "\n" +
                "           pageable = " + page.getPageable() + "\n" +
                "           content = {\n               " +
                page.getContent().stream().map(Object::toString).collect(Collectors.joining(",\n               ")) +
                "\n           }";
    }

}
