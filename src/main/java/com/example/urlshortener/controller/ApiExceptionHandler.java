package com.example.urlshortener.controller;
import com.example.urlshortener.exception.*; import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*; import java.time.Instant; import java.util.*;
@RestControllerAdvice
public class ApiExceptionHandler {
 @ExceptionHandler(NotFoundException.class) ResponseEntity<?> notFound(NotFoundException e){return error(HttpStatus.NOT_FOUND,e.getMessage());}
 @ExceptionHandler(ExpiredUrlException.class) ResponseEntity<?> expired(ExpiredUrlException e){return error(HttpStatus.GONE,e.getMessage());}
 @ExceptionHandler({IllegalArgumentException.class,SecurityException.class}) ResponseEntity<?> bad(RuntimeException e){return error(HttpStatus.BAD_REQUEST,e.getMessage());}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<?> validation(MethodArgumentNotValidException e){String m=e.getBindingResult().getFieldErrors().stream().findFirst().map(x->x.getField()+": "+x.getDefaultMessage()).orElse("Invalid request");return error(HttpStatus.BAD_REQUEST,m);}
 private ResponseEntity<Map<String,Object>> error(HttpStatus s,String m){return ResponseEntity.status(s).body(Map.of("timestamp",Instant.now(),"status",s.value(),"error",m));}
}