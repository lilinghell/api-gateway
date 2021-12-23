package com.hell.controller;

import com.hell.dto.MockRequest;
import org.bouncycastle.util.encoders.UrlBase64;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockController {
    @RequestMapping(path = "/api/mock")
    public ResponseEntity<String> mock(MockRequest request) {
        ResponseEntity<String> resp = ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new String(UrlBase64.decode(request.getMockInfo())));

        return resp;
    }
}
