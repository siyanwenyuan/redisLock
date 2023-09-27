package com.chen.redislockgithub.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudyController {


    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/study")
    public void study(){


        return ;

    }
}
