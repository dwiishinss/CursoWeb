package com.lemonsoftware.lemonmoney.lemonmoney_api.api.event.listener;

import java.net.URI;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lemonsoftware.lemonmoney.lemonmoney_api.api.event.RecursoCriadoEvent;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class RecursoCriadoListener implements ApplicationListener<RecursoCriadoEvent>{

    @Override
    public void onApplicationEvent(RecursoCriadoEvent event) {
        HttpServletResponse response = event.getResponse();
        Long id = event.getId();

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
        .buildAndExpand(id).toUri();
        response.setHeader("location", uri.toASCIIString());
    }
    
}
