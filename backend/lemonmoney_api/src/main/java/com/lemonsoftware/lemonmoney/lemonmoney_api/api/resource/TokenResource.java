package com.lemonsoftware.lemonmoney.lemonmoney_api.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/tokens")
public class TokenResource {
    
    @DeleteMapping("/revoke")
    public void revoke(HttpServletRequest req, HttpServletResponse resp){
        resp.reset();
        resp.setStatus(HttpStatus.NO_CONTENT.value());
    }
}
