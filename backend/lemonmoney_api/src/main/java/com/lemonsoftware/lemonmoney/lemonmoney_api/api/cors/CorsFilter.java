package com.lemonsoftware.lemonmoney.lemonmoney_api.api.cors;

import java.io.IOException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter{

    private String originPermitida = "http://localhost:8000";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        res.setHeader("Access-Control-Allow-Origin", originPermitida);
        res.setHeader("Access-Control-Allow-Credentials", "true");

        if("OPTIONS".equals(req.getMethod()) && originPermitida.equals(req.getHeader("Origin"))){

            res.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
            res.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
            res.setHeader("Access-Control-Max-Age", "3600");
  
            res.setStatus(HttpServletResponse.SC_OK);
        }else{
            chain.doFilter(req, res);
        }
    }
    
}
