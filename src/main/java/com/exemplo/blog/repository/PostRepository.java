package com.exemplo.blog.repository; 
  
import com.exemplo.blog.entity.Post; 
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.data.jpa.repository.Query; 
import org.springframework.data.repository.query.Param; 
import org.springframework.stereotype.Repository; 
  
import java.util.Optional; 
  
@Repository 
public interface PostRepository extends JpaRepository<Post, Long> { 
  
    Optional<Post> findBySlug(String slug); 
  
    Page<Post> findByPublicadoTrue(Pageable pageable); 
  
    Page<Post> findByAutorIdAndPublicadoTrue(Long autorId, Pageable pageable); 
  
    @Query("SELECT p FROM Post p WHERE p.publicado = true " + 
           "AND (LOWER(p.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) " + 
           "     OR LOWER(p.conteudo) LIKE LOWER(CONCAT('%', :termo, '%')))") 
    Page<Post> buscarPorTermo(@Param("termo") String termo, Pageable pageable); 
  
    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t.nome = :tagNome " + 
           "AND p.publicado = true") 
    Page<Post> findByTagNome(@Param("tagNome") String tagNome, Pageable pageable); 
}