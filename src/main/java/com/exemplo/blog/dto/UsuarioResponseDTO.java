// dto/UsuarioResponseDTO.java 
package com.exemplo.blog.dto; 
  
import lombok.*; 
import java.time.LocalDateTime; 
  
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class UsuarioResponseDTO { 
    private Long id; 
    private String nome; 
    private String email; 
    private String bio; 
    private String avatarUrl; 
    private String role; 
    private LocalDateTime criadoEm; 
}