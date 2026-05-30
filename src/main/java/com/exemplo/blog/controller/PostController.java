package com.exemplo.blog.controller; 
  
import com.exemplo.blog.dto.*; 
import com.exemplo.blog.service.PostService; 
import jakarta.validation.Valid; 
import lombok.RequiredArgsConstructor; 
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*; 
import org.springframework.security.core.Authentication; 
import org.springframework.web.bind.annotation.*; 

  
@RestController 
@RequestMapping("/api/posts") 
@RequiredArgsConstructor 
public class PostController { 
  
    private final PostService postService; 
  
    @GetMapping 
    public Page<PostResponseDTO> listar( 
            @RequestParam(required = false) String busca, 
            @RequestParam(required = false) String tag, 
            @PageableDefault(size = 10, sort = "criadoEm", 
                             direction = Sort.Direction.DESC) Pageable pageable) { 
        if (busca != null && !busca.isBlank()) { 
            return postService.buscar(busca, pageable); 
        } 
        if (tag != null && !tag.isBlank()) { 
            return postService.listarPorTag(tag, pageable); 
        } 
        return postService.listarPublicados(pageable); 
    } 
  
    @GetMapping("/{slug}") 
    public PostResponseDTO buscar(@PathVariable String slug) { 
        return postService.buscarPorSlug(slug); 
    } 
  
    @PostMapping 
    public ResponseEntity<PostResponseDTO> criar( 
            @Valid @RequestBody PostRequestDTO dto, 
            Authentication auth) { 
        PostResponseDTO criado = postService.criar(dto, auth.getName()); 
        return ResponseEntity.status(HttpStatus.CREATED).body(criado); 
    } 
  
    @PutMapping("/{id}") 
    public PostResponseDTO atualizar(@PathVariable Long id, 
                                     @Valid @RequestBody PostRequestDTO dto, 
                                     Authentication auth) { 
        return postService.atualizar(id, dto, auth.getName()); 
    } 
  
    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> excluir(@PathVariable Long id, Authentication auth) { 
        postService.excluir(id, auth.getName()); 
        return ResponseEntity.noContent().build(); 
    } 
} 