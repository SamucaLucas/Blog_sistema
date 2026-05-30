package com.exemplo.blog.dto;

import lombok.*; 
import java.time.LocalDateTime;
import java.util.Set;

// dto/PostResponseDTO.java 
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class PostResponseDTO { 
    private Long id; 
    private String titulo; 
    private String slug; 
    private String resumo; 
    private String conteudo; 
    private UsuarioResponseDTO autor; 
    private Set<String> tags; 
    private Integer totalComentarios; 
    private Boolean publicado; 
    private LocalDateTime criadoEm; 
    private LocalDateTime atualizadoEm; 
}
