// dto/PostRequestDTO.java 
package com.exemplo.blog.dto; 
  
import jakarta.validation.constraints.*; 
import lombok.Data; 
import java.util.Set; 
  
@Data 
public class PostRequestDTO { 
  
    @NotBlank @Size(max = 200) 
    private String titulo; 
  
    @NotBlank 
    private String conteudo; 
  
    @Size(max = 500) 
    private String resumo; 
  
    private Boolean publicado = false; 
  
    private Set<String> tags; 
} 
  
