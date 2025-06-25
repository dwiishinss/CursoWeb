package com.lemonsoftware.teste.teste_api.domain.exception;

public class NegocioException extends RuntimeException {
    
    public NegocioException(String message){
        super(message);
    }

}
