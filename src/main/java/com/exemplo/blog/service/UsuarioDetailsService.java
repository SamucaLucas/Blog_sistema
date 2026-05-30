package com.exemplo.blog.service; 
  
import com.exemplo.blog.entity.Usuario; 
import com.exemplo.blog.repository.UsuarioRepository; 
import lombok.RequiredArgsConstructor; 
import org.springframework.security.core.authority.SimpleGrantedAuthority; 
import org.springframework.security.core.userdetails.*; 
import org.springframework.stereotype.Service; 
  
import java.util.List; 
  
@Service 
@RequiredArgsConstructor 
public class UsuarioDetailsService implements UserDetailsService { 
  
    private final UsuarioRepository usuarioRepository; 
  
    @Override 
    public UserDetails loadUserByUsername(String email) throws 
UsernameNotFoundException { 
        Usuario u = usuarioRepository.findByEmail(email) 
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado")); 
  
        return new User( 
            u.getEmail(), 
            u.getSenha(), 
            List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().name())) 
        ); 
    } 
} 