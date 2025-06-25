package com.lemonsoftware.teste.teste_api.api.exceptionhandler;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.lemonsoftware.teste.teste_api.domain.exception.EntityNotFoundException;
import com.lemonsoftware.teste.teste_api.domain.exception.NegocioException;
import com.lemonsoftware.teste.teste_api.domain.exception.StatusConflictException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler{
    
    private final MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle("Um ou mais campos estão invalidos");
        problemDetail.setType(URI.create("https://lemontransito.com/erros/campos-invalidos"));
        
        Map<String, String> fields = ex.getBindingResult().getAllErrors()
            .stream()
            .collect(Collectors.toMap(objectError -> ((FieldError)objectError).getField(), 
                                    objectError -> messageSource.getMessage(objectError, LocaleContextHolder.getLocale())));

        problemDetail.setProperty("fields", fields);

        return handleExceptionInternal(ex, problemDetail, headers, status, request);
    }

    @ExceptionHandler(NegocioException.class)
    public ProblemDetail handleNegocio(NegocioException e){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle(e.getMessage());
        problemDetail.setType(URI.create("https://lemontransito.com/erros/regra-de-negocio"));

        return problemDetail;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFound(EntityNotFoundException e){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle(e.getMessage());
        problemDetail.setType(URI.create("https://lemontransito.com/erros/nao-encontrado"));

        return problemDetail;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrity(DataIntegrityViolationException e){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle("Recurso está em uso");
        problemDetail.setType(URI.create("https://lemontransito.com/erros/recurso-em-uso"));

        return problemDetail;
    }

    @ExceptionHandler(StatusConflictException.class)
    public ProblemDetail handleStatusConflict(StatusConflictException e){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle(e.getMessage());
        problemDetail.setType(URI.create("https://lemontransito.com/erros/conflito-de-status"));

        return problemDetail;
    }

}
