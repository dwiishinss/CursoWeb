package com.lemonsoftware.lemonmoney.lemonmoney_api.api.event;

import org.springframework.context.ApplicationEvent;

import jakarta.servlet.http.HttpServletResponse;

public class RecursoCriadoEvent extends ApplicationEvent{

    private HttpServletResponse response;
    private Long id;

    public RecursoCriadoEvent(Object source, HttpServletResponse response, Long id) {
        super(source);
        this.response = response;
        this.id = id;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public Long getId() {
        return id;
    }
    
}
