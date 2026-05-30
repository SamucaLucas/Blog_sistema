package com.exemplo.blog.service; 
  
import com.exemplo.blog.dto.*; 
import com.exemplo.blog.entity.*; 
import com.exemplo.blog.exception.*; 
import com.exemplo.blog.repository.*; 
import lombok.RequiredArgsConstructor; 
import org.springframework.data.domain.*; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional; 
  
import java.text.Normalizer; 
import java.util.HashSet; 
import java.util.Set; 
import java.util.stream.Collectors; 
  
@Service 
@RequiredArgsConstructor 
@Transactional 
public class PostService { 
  
    private final PostRepository postRepository; 
    private final TagRepository tagRepository; 
    private final UsuarioRepository usuarioRepository; 
  
    public PostResponseDTO criar(PostRequestDTO dto, String emailAutor) { 
        Usuario autor = usuarioRepository.findByEmail(emailAutor) 
            .orElseThrow(() -> new RecursoNaoEncontradoException("Autor não encontrado")); 
  
        Post post = Post.builder() 
            .titulo(dto.getTitulo()) 
            .slug(gerarSlugUnico(dto.getTitulo())) 
            .conteudo(dto.getConteudo()) 
            .resumo(dto.getResumo() != null ? dto.getResumo() : 
gerarResumo(dto.getConteudo())) 
            .autor(autor) 
            .publicado(dto.getPublicado()) 
            .tags(processarTags(dto.getTags())) 
            .build(); 
  
        Post salvo = postRepository.save(post); 
        return paraDTO(salvo); 
    } 
  
    public PostResponseDTO atualizar(Long id, PostRequestDTO dto, String emailUsuario) 
{ 
        Post post = postRepository.findById(id) 
            .orElseThrow(() -> new RecursoNaoEncontradoException("Post não encontrado")); 
  
        if (!post.getAutor().getEmail().equals(emailUsuario)) { 
            throw new AcessoNegadoException("Você não tem permissão para editar este post"); 
        } 
  
        post.setTitulo(dto.getTitulo()); 
        post.setConteudo(dto.getConteudo()); 
        if (dto.getResumo() != null) post.setResumo(dto.getResumo()); 
        if (dto.getPublicado() != null) post.setPublicado(dto.getPublicado()); 
        if (dto.getTags() != null) post.setTags(processarTags(dto.getTags())); 
  
        return paraDTO(postRepository.save(post)); 
    } 
  
    public void excluir(Long id, String emailUsuario) { 
        Post post = postRepository.findById(id) 
            .orElseThrow(() -> new RecursoNaoEncontradoException("Post não encontrado")); 
        if (!post.getAutor().getEmail().equals(emailUsuario)) { 
            throw new AcessoNegadoException("Sem permissão"); 
        } 
        postRepository.delete(post); 
    }

     @Transactional(readOnly = true) 
    public Page<PostResponseDTO> listarPublicados(Pageable pageable) { 
        return postRepository.findByPublicadoTrue(pageable).map(this::paraDTO); 
    } 
  
    @Transactional(readOnly = true) 
    public PostResponseDTO buscarPorSlug(String slug) { 
        Post p = postRepository.findBySlug(slug) 
            .orElseThrow(() -> new RecursoNaoEncontradoException("Post não encontrado")); 
        return paraDTO(p); 
    }
      @Transactional(readOnly = true) 
    public Page<PostResponseDTO> buscar(String termo, Pageable pageable) { 
        return postRepository.buscarPorTermo(termo, pageable).map(this::paraDTO); 
    } 
  
    @Transactional(readOnly = true) 
    public Page<PostResponseDTO> listarPorTag(String tag, Pageable pageable) { 
        return postRepository.findByTagNome(tag, pageable).map(this::paraDTO); 
    } 
  
    // ===== AUXILIARES ===== 
  
    private String gerarSlugUnico(String titulo) { 
        String base = gerarSlug(titulo); 
        String slug = base; 
        int contador = 1; 
        while (postRepository.findBySlug(slug).isPresent()) { 
            slug = base + "-" + contador++; 
        } 
        return slug; 
    } 
  
    private String gerarSlug(String texto) { 
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD) 
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", ""); 
        return normalizado.toLowerCase() 
            .replaceAll("[^a-z0-9\\s-]", "") 
            .replaceAll("\\s+", "-") 
            .replaceAll("-+", "-"); 
    } 
  
    private String gerarResumo(String conteudo) { 
        if (conteudo.length() <= 200) return conteudo; 
        return conteudo.substring(0, 197) + "..."; 
    } 
  
    private Set<Tag> processarTags(Set<String> nomes) { 
        if (nomes == null || nomes.isEmpty()) return new HashSet<>(); 
        return nomes.stream() 
            .map(n -> n.toLowerCase().trim()) 
            .filter(n -> !n.isEmpty()) 
            .map(n -> tagRepository.findByNome(n) 
                .orElseGet(() -> tagRepository.save(Tag.builder().nome(n).build()))) 
            .collect(Collectors.toSet()); 
    } 
  
    private PostResponseDTO paraDTO(Post p) { 
        return PostResponseDTO.builder() 
            .id(p.getId()) 
            .titulo(p.getTitulo()) 
            .slug(p.getSlug()) 
            .resumo(p.getResumo()) 
            .conteudo(p.getConteudo()) 
            .autor(new UsuarioResponseDTO( 
                p.getAutor().getId(), p.getAutor().getNome(), 
                p.getAutor().getEmail(), p.getAutor().getBio(), 
                p.getAutor().getAvatarUrl(), p.getAutor().getRole().name(), 
                p.getAutor().getCriadoEm())) 
            .tags(p.getTags().stream().map(Tag::getNome).collect(Collectors.toSet())) 
            .totalComentarios(p.getComentarios().size()) 
            .publicado(p.getPublicado()) 
            .criadoEm(p.getCriadoEm()) 
            .atualizadoEm(p.getAtualizadoEm()) 
            .build(); 
    } 
} 