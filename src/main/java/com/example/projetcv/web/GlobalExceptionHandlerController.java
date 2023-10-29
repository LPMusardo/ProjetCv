package com.example.projetcv.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import com.example.projetcv.exception.MyJwtException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.validation.FieldError;


@RestControllerAdvice
public class GlobalExceptionHandlerController {

//  @Bean
//  public ErrorAttributes errorAttributes() {
//    // Hide exception field in the return object
//    return new DefaultErrorAttributes() {
//      @Override
//      public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
//        return super.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults().excluding(ErrorAttributeOptions.Include.EXCEPTION));
//      }
//    };
//  }

/*  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleHttpMessageNotReadableException(HttpServletResponse res, HttpMessageNotReadableException ex) throws IOException {
    return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }*/


  @ExceptionHandler(HttpMessageNotReadableException.class)
  public void handleHttpMessageNotReadableException(HttpServletResponse res, HttpMessageNotReadableException ex) throws IOException {
    res.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public void handleAccessDeniedException(HttpServletResponse res, AccessDeniedException ex) throws IOException {
    res.sendError(HttpStatus.FORBIDDEN.value(), ex.getMessage());
  }

  @ExceptionHandler(MyJwtException.class)
  public void handleMyJwtException(HttpServletResponse res, MyJwtException ex) throws IOException {
    SecurityContextHolder.clearContext();
    res.sendError(ex.getHttpStatus().value(), ex.getMessage());
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public void handleUsernameNotFoundException(HttpServletResponse res, UsernameNotFoundException ex) throws IOException {
    res.sendError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
  }

  @ExceptionHandler(AuthenticationException.class)
  public void handleAuthenticationException(HttpServletResponse res, AuthenticationException ex) throws IOException {
    res.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
  }

  @ExceptionHandler(NumberFormatException .class)
  public void handleNumberFormatException(HttpServletResponse res, NumberFormatException ex) throws IOException {
    res.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public void handleValidationErrors(HttpServletResponse res, MethodArgumentNotValidException ex) throws IOException {
    //List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
    res.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "ex.getMessage()");
    //return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

//  private Map<String, List<String>> getErrorsMap(List<String> errors) {
//    Map<String, List<String>> errorResponse = new HashMap<>();
//    errorResponse.put("errors", errors);
//    return errorResponse;
//  }


  @ExceptionHandler(Exception.class)
  public void handleException(HttpServletResponse res) throws IOException {
    res.sendError(HttpStatus.BAD_REQUEST.value(), "Something went wrong");
  }


}
