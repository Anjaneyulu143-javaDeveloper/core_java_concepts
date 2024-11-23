package com.java.spring_boot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class JavaController {

    private static final Logger logger = LoggerFactory.getLogger(JavaController.class);

    @GetMapping("/hello")
    public Map<String,String> helloPage(@RequestParam(defaultValue = "world")String start) {
        logger.trace("Name: {}",start);
        return Map.of("greeting","Hello "+start);
    }
}
