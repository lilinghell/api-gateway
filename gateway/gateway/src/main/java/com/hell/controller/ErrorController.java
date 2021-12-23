package com.hell.controller;

import com.hell.config.response.ResponseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController {

    @RequestMapping(path = "/error/fallback")
    public ResponseResult error() {

        return new ResponseResult("EX0001", "fallback", "");
    }
    @RequestMapping(path = "/error/429fallback")
    public ResponseResult error429() {

        return new ResponseResult("EX0002", "Too Many Requests", "");
    }
}
