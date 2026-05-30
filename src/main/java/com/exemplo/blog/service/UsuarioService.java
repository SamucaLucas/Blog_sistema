package com.exemplo.blog.service; 
  
import com.exemplo.blog.dto.*; 
import com.exemplo.blog.entity.Usuario; 
import com.exemplo.blog.exception.RecursoExistenteException; 
import com.exemplo.blog.exception.RecursoNaoEncontradoException; 
import com.exemplo.blog.repository.UsuarioRepository; 
import lombok.RequiredArgsConstructor; 
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional; 
  
@Service 
@RequiredArgsConstructor 
@Transactional 
public class UsuarioService { 
  
    private final UsuarioRepository usuarioRepository; 
    private final PasswordEncoder passwordEncoder; 
  
    public UsuarioResponseDTO registrar(UsuarioRegistroDTO dto) { 
        if (usuarioRepository.existsByEmail(dto.getEmail())) { 
            throw new RecursoExistenteException( 
                "Email já cadastrado: " + dto.getEmail() 
            ); 
        } 
  
        Usuario usuario = Usuario.builder() 
            .nome(dto.getNome()) 
            .email(dto.getEmail()) 
            .senha(passwordEncoder.encode(dto.getSenha())) 
            .role(Usuario.Role.AUTOR)   // todos novos usuários podem postar 
            .build(); 
  
        Usuario salvo = usuarioRepository.save(usuario); 
        return paraDTO(salvo); 
    } 
  
    @Transactional(readOnly = true) 
    public UsuarioResponseDTO buscarPorId(Long id) { 
        Usuario u = usuarioRepository.findById(id) 
            .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado")); 
        return paraDTO(u); 
    } 
  
    @Transactional(readOnly = true) 
    public UsuarioResponseDTO buscarPorEmail(String email) { 
        Usuario u = usuarioRepository.findByEmail(email) 
            .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado")); 
        return paraDTO(u); 
    } 
  
    private UsuarioResponseDTO paraDTO(Usuario u) { 
        return new UsuarioResponseDTO( 
            u.getId(), u.getNome(), u.getEmail(), 
            u.getBio(), u.getAvatarUrl(), u.getRole().name(), 
            u.getCriadoEm() 
        ); 
    } 
}