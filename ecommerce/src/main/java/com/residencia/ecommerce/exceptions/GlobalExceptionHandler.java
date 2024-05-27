package com.residencia.ecommerce.exceptions;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	
	@ExceptionHandler(NoSuchElementException.class)
    ProblemDetail handleNoSuchElementException(NoSuchElementException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        
        problemDetail.setTitle("Recurso Não Encontrado");
        problemDetail.setType(URI.create("https://api.biblioteca.com/errors/not-found"));
        return problemDetail;
    }
	
	@ExceptionHandler(PSQLException.class)
    ProblemDetail handlePSQLException(PSQLException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        
        problemDetail.setTitle("Possui campos únicos que não podem ser repetidos.");
        problemDetail.setType(URI.create("https://api.biblioteca.com/errors/not-found"));
        return problemDetail;
    }
	
	@ExceptionHandler(PropertyValueException.class)
    ProblemDetail handlePropertyValueException(PropertyValueException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        
        problemDetail.setTitle("Possui campos que não podem ser nulos");
        problemDetail.setType(URI.create("https://api.biblioteca.com/errors/not-found"));
        return problemDetail;
    }
	
	@Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, 
    		HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        ResponseEntity<Object> response = super.handleExceptionInternal(ex, body, headers, statusCode, request);

        if (response.getBody() instanceof ProblemDetail problemDetailBody) {
            problemDetailBody.setProperty("message", ex.getMessage());
            if (ex instanceof MethodArgumentNotValidException subEx) {
                BindingResult result = subEx.getBindingResult();
                problemDetailBody.setType(URI.create("http://api.biblioteca.com/erros/argument-not-valid"));;
                problemDetailBody.setTitle("Erro na requisição");
                problemDetailBody.setDetail("Ocorreu um erro ao processar a Requisição");
                problemDetailBody.setProperty(
                		"message", "Falha na Validação do Objeto '" + result.getObjectName() + 
                		"'. " + "Quantidade de Erros: " + result.getErrorCount()
                		);
                List<FieldError> fldErros = result.getFieldErrors();
                List<String> erros = new ArrayList<>();
                
                for(FieldError obj : fldErros) {
                	erros.add("Campo: " + obj.getField() + " - Erro: " + obj.getDefaultMessage());
                }
                problemDetailBody.setProperty("Erros Encontrados", erros.toString());
            }
        }
        return response;
    }

}
