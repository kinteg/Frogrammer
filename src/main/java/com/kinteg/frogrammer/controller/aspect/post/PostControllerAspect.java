package com.kinteg.frogrammer.controller.aspect.post;

import com.kinteg.frogrammer.controller.aspect.helper.AspectStringHelper;
import com.kinteg.frogrammer.db.domain.Post;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PostControllerAspect {

    private final AspectStringHelper aspectStringHelper;

    public PostControllerAspect(AspectStringHelper aspectStringHelper) {
        this.aspectStringHelper = aspectStringHelper;
    }

    @Pointcut("execution(* com.kinteg.frogrammer.controller.post.PostController.getPost(..))")
    public void callGetPost() {
    }

    @Pointcut("execution(* com.kinteg.frogrammer.controller.post.PostController.getAll(..))")
    public void callGetAll() {
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

//    @AfterReturning(value = "callGetAll()", returning = "posts")
//    public void afterCallGetAll(JoinPoint joinPoint, Page<Post> posts) {
//        log.info(aspectStringHelper.pageResponseToString(joinPoint, posts));
//    }

    @AfterThrowing(value = "callGetAll()", throwing = "e")
    public void afterThrowCallGetAll(JoinPoint joinPoint, Exception e) {
        log.info("after throwing " + joinPoint.toString() + ", exception :\n" + e.getMessage());
    }

}
