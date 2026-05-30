package com.exemplo.blog.controller; 
  
import com.exemplo.blog.dto.*; 
import com.exemplo.blog.service.UsuarioService; 
import jakarta.servlet.http.*; 
import jakarta.validation.Valid; 
import lombok.RequiredArgsConstructor; 
import org.springframework.http.*; 
import org.springframework.security.authentication.*; 
import org.springframework.security.core.context.*; 
import org.springframework.security.web.context.*; 
import org.springframework.web.bind.annotation.*; 
  
@RestController 
@RequestMapping("/api/auth") 
@RequiredArgsConstructor 
public class AuthController { 
  
    private final UsuarioService usuarioService; 
    private final AuthenticationManager authManager; 
    private final SecurityContextRepository securityContextRepo = 
        new HttpSessionSecurityContextRepository(); 
  
    @PostMapping("/registrar") 
    public ResponseEntity<UsuarioResponseDTO> registrar( 
            @Valid @RequestBody UsuarioRegistroDTO dto) { 
        UsuarioResponseDTO criado = usuarioService.registrar(dto); 
        return ResponseEntity.status(HttpStatus.CREATED).body(criado); 
    } 
  
    @PostMapping("/login") 
    public ResponseEntity<UsuarioResponseDTO> login( 
            @Valid @RequestBody LoginDTO dto, 
            HttpServletRequest request, 
            HttpServletResponse response) { 
  
        var token = new UsernamePasswordAuthenticationToken( 
            dto.getEmail(), dto.getSenha() 
        ); 
        var auth = authManager.authenticate(token); 
  
        var context = SecurityContextHolder.createEmptyContext(); 
        context.setAuthentication(auth); 
        SecurityContextHolder.setContext(context); 
        securityContextRepo.saveContext(context, request, response); 
  
        return ResponseEntity.ok(usuarioService.buscarPorEmail(dto.getEmail())); 
    } 
  
    @PostMapping("/logout") 
    public ResponseEntity<Void> logout(HttpServletRequest request) { 
        var session = request.getSession(false); 
        if (session != null) session.invalidate(); 
        SecurityContextHolder.clearContext(); 
        return ResponseEntity.noContent().build(); 
    } 
  
    @GetMapping("/me") 
    public ResponseEntity<UsuarioResponseDTO> me() { 
        var auth = SecurityContextHolder.getContext().getAuthentication(); 
        if (auth == null || !auth.isAuthenticated()) { 
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); 
        } 
        return ResponseEntity.ok(usuarioService.buscarPorEmail(auth.getName())); 
    } 
} 