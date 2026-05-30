// dto/UsuarioRegistroDTO.java 
package com.exemplo.blog.dto; 
  
import jakarta.validation.constraints.*; 
import lombok.Data; 
  
@Data 
public class UsuarioRegistroDTO { 
  
    @NotBlank(message = "Nome é obrigatório") 
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres") 
    private String nome; 
  
    @NotBlank(message = "Email é obrigatório") 
    @Email(message = "Email inválido") 
    @Size(max = 150) 
    private String email; 
  
    @NotBlank(message = "Senha é obrigatória") 
    @Size(min = 6, max = 100, message = "Senha deve ter entre 6 e 100 caracteres") 
    private String senha; 
}