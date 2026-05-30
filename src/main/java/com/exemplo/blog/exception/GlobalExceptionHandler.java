package com.exemplo.blog.exception; 
  
import org.springframework.http.*; 
import org.springframework.web.bind.MethodArgumentNotValidException; 
import org.springframework.web.bind.annotation.*; 
  
import java.time.LocalDateTime; 
import java.util.*; 
  
@RestControllerAdvice 
public class GlobalExceptionHandler { 
  
    @ExceptionHandler(RecursoNaoEncontradoException.class) 
    public ResponseEntity<Map<String, Object>> 
naoEncontrado(RecursoNaoEncontradoException e) { 
        return resposta(HttpStatus.NOT_FOUND, e.getMessage()); 
    } 
  
    @ExceptionHandler(RecursoExistenteException.class) 
    public ResponseEntity<Map<String, Object>> existente(RecursoExistenteException e) { 
        return resposta(HttpStatus.CONFLICT, e.getMessage()); 
    } 
  
    @ExceptionHandler(AcessoNegadoException.class) 
    public ResponseEntity<Map<String, Object>> acesso(AcessoNegadoException e) { 
        return resposta(HttpStatus.FORBIDDEN, e.getMessage()); 
    } 
  
    @ExceptionHandler(MethodArgumentNotValidException.class) 
    public ResponseEntity<Map<String, Object>> 
validacao(MethodArgumentNotValidException e) { 
        Map<String, String> erros = new HashMap<>(); 
        e.getBindingResult().getFieldErrors().forEach(fe -> 
            erros.put(fe.getField(), fe.getDefaultMessage()) 
        ); 
        Map<String, Object> body = new HashMap<>(); 
        body.put("timestamp", LocalDateTime.now()); 
        body.put("status", 400); 
        body.put("erros", erros); 
        return ResponseEntity.badRequest().body(body); 
    } 
  
    private ResponseEntity<Map<String, Object>> resposta(HttpStatus status, String msg) 
{ 
        Map<String, Object> body = new HashMap<>(); 
        body.put("timestamp", LocalDateTime.now()); 
        body.put("status", status.value()); 
        body.put("mensagem", msg); 
        return ResponseEntity.status(status).body(body); 
    } 
} 