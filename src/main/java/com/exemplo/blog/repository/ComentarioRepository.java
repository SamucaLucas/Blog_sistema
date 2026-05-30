// repository/ComentarioRepository.java 
package com.exemplo.blog.repository; 


import org.springframework.data.jpa.repository.JpaRepository; 

import org.springframework.stereotype.Repository; 
  
import java.util.List; 


import com.exemplo.blog.entity.Comentario;
@Repository 
public interface ComentarioRepository extends JpaRepository<Comentario, Long> { 
    List<Comentario> findByPostIdOrderByCriadoEmDesc(Long postId); 
}