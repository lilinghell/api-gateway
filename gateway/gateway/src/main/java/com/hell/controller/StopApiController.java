package com.hell.controller;

import com.hell.config.response.ResponseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StopApiController {
    @RequestMapping(path = "/api/stop")
    public ResponseResult error() {

        return new ResponseResult("EX0003", "api stop", "");
    }
}
