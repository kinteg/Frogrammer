package com.kinteg.frogrammer.controller.aspect;

import com.kinteg.frogrammer.controller.aspect.helper.AspectStringHelper;
import com.kinteg.frogrammer.db.domain.Post;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
@Slf4j
public class PostControllerAspect {

    private final AspectStringHelper<Post> aspectStringHelper;

    public PostControllerAspect(AspectStringHelper<Post> aspectStringHelper) {
        this.aspectStringHelper = aspectStringHelper;
    }

    @Pointcut("execution(* com.kinteg.frogrammer.controller.PostController.create(..))")
    public void callCreatePost() {
    }

    @Pointcut("execution(* com.kinteg.frogrammer.controller.PostController.getPost(..))")
    public void callGetPost() {
    }

    @Pointcut("execution(* com.kinteg.frogrammer.controller.PostController.getAll(..))")
    public void callGetAll() {
    }

    @Pointcut("execution(* com.kinteg.frogrammer.controller.PostController.deleteById(..))")
    public void callDeleteById() {
    }

    @Pointcut("execution(* com.kinteg.frogrammer.controller.PostController.update(..))")
    public void callUpdate() {
    }

    @Before("callCreatePost()")
    public void beforeCallCreatePost(JoinPoint joinPoint) {
        log.info(aspectStringHelper.getBefore(joinPoint));
    }

    @AfterReturning(value = "callCreatePost()", returning = "post")
    public void afterCallCreatePost(JoinPoint joinPoint, ResponseEntity<Post> post) {
        log.info(aspectStringHelper.responseToString(joinPoint, post));
    }

    @AfterThrowing(value = "callCreatePost()", throwing = "e")
    public void afterThrowCallCreatePost(JoinPoint joinPoint, Exception e) {
        log.info("after throwing " + joinPoint.toString() + ", exception :\n" + e.getMessage());
    }

    @Before("callGetPost()")
    public void beforeCallGetPost(JoinPoint joinPoint) {
        log.info(aspectStringHelper.getBefore(joinPoint));
    }

    @AfterReturning(value = "callGetPost()", returning = "post")
    public void afterCallGetPost(JoinPoint joinPoint, ResponseEntity<Post> post) {
        log.info(aspectStringHelper.responseToString(joinPoint, post));
    }

    @AfterThrowing(value = "callGetPost()", throwing = "e")
    public void afterThrowCallGetPost(JoinPoint joinPoint, Exception e) {
        log.info("after throwing " + joinPoint.toString() + ", exception :\n" + e.getMessage());
    }

    @Before("callGetAll()")
    public void beforeCallGetAll(JoinPoint joinPoint) {
        log.info(aspectStringHelper.getBefore(joinPoint));
    }

    @AfterReturning(value = "callGetAll()", returning = "posts")
    public void afterCallGetAll(JoinPoint joinPoint, ResponseEntity<Page<Post>> posts) {
        log.info(aspectStringHelper.pageResponseToString(joinPoint, posts));
    }

    @AfterThrowing(value = "callGetAll()", throwing = "e")
    public void afterThrowCallGetAll(JoinPoint joinPoint, Exception e) {
        log.info("after throwing " + joinPoint.toString() + ", exception :\n" + e.getMessage());
    }

    @Before("callDeleteById()")
    public void beforeCallDeleteById(JoinPoint joinPoint) {
        log.info(aspectStringHelper.getBefore(joinPoint));
    }

    @AfterReturning(value = "callDeleteById()", returning = "post")
    public void afterCallDeleteById(JoinPoint joinPoint, ResponseEntity<Post> post) {
        log.info(aspectStringHelper.responseToString(joinPoint, post));
    }

    @AfterThrowing(value = "callDeleteById()", throwing = "e")
    public void afterThrowCallDeleteById(JoinPoint joinPoint, Exception e) {
        log.info("after throwing " + joinPoint.toString() + ", exception :\n" + e.getMessage());
    }

    @Before("callUpdate()")
    public void beforeCallUpdate(JoinPoint joinPoint) {
        log.info(aspectStringHelper.getBefore(joinPoint));
    }

    @AfterReturning(value = "callUpdate()", returning = "post")
    public void afterCallUpdate(JoinPoint joinPoint, ResponseEntity<Post> post) {
        log.info(aspectStringHelper.responseToString(joinPoint, post));
    }

    @AfterThrowing(value = "callUpdate()", throwing = "e")
    public void afterThrowCallUpdate(JoinPoint joinPoint, Exception e) {
        log.info("after throwing " + joinPoint.toString() + ", exception :\n" + e.getMessage());
    }

}
