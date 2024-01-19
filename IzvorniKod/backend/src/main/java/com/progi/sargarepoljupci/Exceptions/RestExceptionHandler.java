package com.progi.sargarepoljupci.Exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(UserNotApprovedException.class)
    protected ResponseEntity<?> notApproved(Exception e, WebRequest request) {
        Map<String, String> ghhg = new HashMap<>();
        ghhg.put("message", e.getMessage());
        ghhg.put("status", "401");
        ghhg.put("error", "Voditelj nije potvrden");
        return new ResponseEntity<>(ghhg, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<?> handleIllegalArgument(Exception e, WebRequest request) {
        Map<String, String> ghhg = new HashMap<>();
        ghhg.put("message", e.getMessage());
        ghhg.put("status", "404");
        ghhg.put("error", "Bad Request");
        return new ResponseEntity<>(ghhg, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RequestDeniedException.class)
    protected ResponseEntity<?> badRequest(RequestDeniedException e, WebRequest request) {
        Map<String, String> ghhg = new HashMap<>();
        ghhg.put("message", e.getMessage());
        ghhg.put("status", "400");
        ghhg.put("error", "Bad Request");
        return new ResponseEntity<>(ghhg, HttpStatus.BAD_REQUEST);
    }




}
