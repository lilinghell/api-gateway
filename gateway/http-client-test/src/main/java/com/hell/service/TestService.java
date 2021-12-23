package com.hell.service;


import com.hell.utils.HttpClientUtils;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    public String qyrService() throws Exception {
        String re = HttpClientUtils.doGet("/api/common/token", null);
        return re;
    }
}
