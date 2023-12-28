package com.sericulture.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestRestController {

    @GetMapping("/hello")
    public String hello(){
        log.debug("I am debug");
        log.error("I am error");
        log.info("I am info");
        log.warn("I am warn");
        return "Hi";
    }


}
